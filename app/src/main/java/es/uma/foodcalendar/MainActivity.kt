package es.uma.foodcalendar

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.uma.foodcalendar.ui.theme.FoodCalendarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodCalendarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding), onRegisterMealClick = {
                        startActivity(Intent(this, RegisterMealActivity::class.java))
                    })
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, onRegisterMealClick: () -> Unit) {
    Column(modifier = modifier.padding(16.dp)) {
        Button(onClick = onRegisterMealClick) {
            Text(text = "Register Meal")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO: Navigate to Search Food screen */ }) {
            Text(text = "Search Food")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO: Navigate to Set Goals screen */ }) {
            Text(text = "Set Goals")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO: Navigate to View Recipes screen */ }) {
            Text(text = "View Recipes")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO: Navigate to View Stats screen */ }) {
            Text(text = "View Stats")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FoodCalendarTheme {
        MainScreen(onRegisterMealClick = {})
    }
}