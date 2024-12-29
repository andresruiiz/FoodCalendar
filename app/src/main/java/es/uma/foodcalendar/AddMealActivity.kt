package es.uma.foodcalendar

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddMealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meal)

        val foodListView = findViewById<ListView>(R.id.foodListView)
        val repository = FoodCalendarRepository(this)

        // Obtener la fecha y franja horaria desde el intent
        val date = intent.getStringExtra("date") ?: return
        val timeOfDay = intent.getStringExtra("timeOfDay") ?: return

        // Cargar alimentos
        val foods = repository.getAllFoods()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foods.map { it.name })
        foodListView.adapter = adapter

        foodListView.setOnItemClickListener { _, _, position, _ ->
            val selectedFood = foods[position]

            repository.addMeal(
                foodId = selectedFood.id,
                date = date,
                timeOfDay = timeOfDay,
                quantity = 1 // Por defecto, 1 porción
            )
            Toast.makeText(this, "Meal added successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}
