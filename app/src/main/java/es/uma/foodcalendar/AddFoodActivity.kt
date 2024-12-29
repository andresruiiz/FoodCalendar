package es.uma.foodcalendar

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddFoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        val foodNameInput = findViewById<EditText>(R.id.foodNameInput)
        val foodCaloriesInput = findViewById<EditText>(R.id.foodCaloriesInput)
        val foodProteinInput = findViewById<EditText>(R.id.foodProteinInput)
        val foodFatInput = findViewById<EditText>(R.id.foodFatInput)
        val foodCarbsInput = findViewById<EditText>(R.id.foodCarbsInput)
        val saveFoodButton = findViewById<Button>(R.id.saveFoodButton)

        val repository = FoodCalendarRepository(this)

        saveFoodButton.setOnClickListener {
            val name = foodNameInput.text.toString()
            val calories = foodCaloriesInput.text.toString().toIntOrNull() ?: 0
            val protein = foodProteinInput.text.toString().toIntOrNull() ?: 0
            val fat = foodFatInput.text.toString().toIntOrNull() ?: 0
            val carbs = foodCarbsInput.text.toString().toIntOrNull() ?: 0

            if (name.isNotEmpty() && calories > 0) {
                repository.addFood(name, calories, protein, fat, carbs)
                Toast.makeText(this, "Food added successfully!", Toast.LENGTH_SHORT).show()

                // Enviar un resultado exitoso a AddMealActivity
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
