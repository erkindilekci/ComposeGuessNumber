package com.erkindilekci.composeguessnumber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.erkindilekci.composeguessnumber.ui.theme.ComposeGuessNumberTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNavigation()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeGuessNumberTheme {

    }
}

@Composable
fun MainScreen(navController: NavController){
    Surface(modifier = Modifier.fillMaxSize(), color = colorResource(id = R.color.myBackground)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Guess The Number",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )

            Image(painter = painterResource(id = R.drawable.die),
                contentDescription = null)

            OutlinedButton(onClick = { navController.navigate("gamescreen") },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.myBackground)
                ),
                border = BorderStroke(3.dp, Color.DarkGray),
                shape = RoundedCornerShape(50)
            )
            {
                Text(text = "Start Game", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun GameScreen(navController: NavController){
    val tfValue = remember { mutableStateOf("") }
    val right = remember { mutableStateOf(2) }
    val randomNumber = remember { mutableStateOf(0) }
    val upOrDown = remember { mutableStateOf("") }
    Surface(modifier = Modifier.fillMaxSize(), color = colorResource(id = R.color.myBackground)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchedEffect(key1 = true){
                randomNumber.value = Random.nextInt(1, 7)
            }

            Text(
                text = "Remaining: ${right.value}",
                fontSize = 36.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Text(text = "Hint: ${upOrDown.value}",
                fontSize = 24.sp, color = Color.DarkGray,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center)

            OutlinedTextField(value = tfValue.value,
                onValueChange = { tfValue.value = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colorResource(id = R.color.myBackground),
                    textColor = Color.DarkGray
                ),
                label = { Text(text = "Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(30)
            )

            OutlinedButton(
                onClick = {
                    right.value-=1
                    val guessedNumber = tfValue.value.toInt()

                    if (guessedNumber == randomNumber.value){
                        navController.navigate("happyscreen"){
                            popUpTo("gamescreen") { inclusive = true }
                        }
                        return@OutlinedButton
                    }
                    if (guessedNumber > randomNumber.value){
                        upOrDown.value = "Reduce the number"
                    }
                    if (guessedNumber < randomNumber.value){
                        upOrDown.value = "Increase the number"
                    }
                    if (right.value == 0){
                        navController.navigate("sadscreen"){
                            popUpTo("gamescreen") { inclusive = true }
                        }
                        upOrDown.value = ""
                    }
                    tfValue.value = ""
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.myBackground)
                ),
                border = BorderStroke(3.dp, Color.DarkGray),
                shape = RoundedCornerShape(50)
            )
            {
                Text(text = "Guess", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun HappyResultScreen(){
    Surface(modifier = Modifier.fillMaxSize(), color = colorResource(id = R.color.myBackground)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "You Have Won!", fontSize = 33.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Image(painter = painterResource(id = R.drawable.happy),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )
        }
    }
}

@Composable
fun SadResultScreen(){
    Surface(modifier = Modifier.fillMaxSize(), color = colorResource(id = R.color.myBackground)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "You Have Lost", fontSize = 33.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Image(painter = painterResource(id = R.drawable.sad),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )
        }
    }
}

@Composable
fun ComposeNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "mainscreen"){
        composable("mainscreen"){
            MainScreen(navController = navController)
        }
        composable("gamescreen"){
            GameScreen(navController = navController)
        }
        composable("sadscreen"){
            SadResultScreen()
        }
        composable("happyscreen"){
            HappyResultScreen()
        }
    }
}
