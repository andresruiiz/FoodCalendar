package es.uma.foodcalendar

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.uma.foodcalendar.data.FoodItem
import es.uma.foodcalendar.data.FoodRepository
import es.uma.foodcalendar.ui.theme.FoodCalendarTheme

class RegisterMealActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodCalendarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterMealScreen(modifier = Modifier.padding(innerPadding), onSaveMeal = { name, calories ->
                        val repository = FoodRepository(this)
                        repository.insertFood(name, calories)
                    })
                }
            }
        }
    }
}

@Composable
fun RegisterMealScreen(modifier: Modifier = Modifier, onSaveMeal: (String, Int) -> Unit) {
    val mealName = remember { mutableStateOf("") }
    val calories = remember { mutableStateOf("") }
    val searchResults = remember { mutableStateOf(listOf<FoodItem>()) }
    val selectedFood = remember { mutableStateOf<FoodItem?>(null) }
    val context = LocalContext.current

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Register Meal")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = mealName.value,
            onValueChange = {
                mealName.value = it
                // Call API to search for food
                try {
                    searchFood(context, it) { results ->
                        searchResults.value = results
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            label = { Text("Meal Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = calories.value,
            onValueChange = { calories.value = it },
            label = { Text("Calories") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val name = selectedFood.value?.product_name ?: mealName.value
            val cal = selectedFood.value?.nutriments?.energy_kcal?.toIntOrNull() ?: calories.value.toIntOrNull() ?: 0
            onSaveMeal(name, cal)
        }) {
            Text(text = "Save Meal")
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Display search results
        searchResults.value.forEach { foodItem ->
            Button(onClick = { selectedFood.value = foodItem }) {
                Text(text = "${foodItem.product_name} - ${foodItem.nutriments.energy_kcal} kcal")
            }
        }
    }
}

private fun Float?.toIntOrNull(): Int? {
    return this?.toInt()
}

fun searchFood(context: Context, query: String, callback: (List<FoodItem>) -> Unit) {
    val repository = FoodRepository(context)
    repository.searchFood(query) { results ->
        callback(results)
    }
}