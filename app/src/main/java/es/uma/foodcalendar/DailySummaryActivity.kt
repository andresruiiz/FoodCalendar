package es.uma.foodcalendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class DailySummaryActivity : AppCompatActivity() {
    private lateinit var repository: FoodCalendarRepository
    private lateinit var currentDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_summary)

        repository = FoodCalendarRepository(this)

        // Obtener la fecha actual
        currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Inicializar vistas
        val tvCaloriesConsumed = findViewById<TextView>(R.id.tvCaloriesConsumed)
        val tvCaloriesRemaining = findViewById<TextView>(R.id.tvCaloriesRemaining)

        val tvSelectedBreakfast = findViewById<TextView>(R.id.tvSelectedBreakfast)
        val tvSelectedLunch = findViewById<TextView>(R.id.tvSelectedLunch)
        val tvSelectedSnack = findViewById<TextView>(R.id.tvSelectedSnack)
        val tvSelectedDinner = findViewById<TextView>(R.id.tvSelectedDinner)

        val btnAddBreakfast = findViewById<Button>(R.id.btnAddBreakfast)
        val btnAddLunch = findViewById<Button>(R.id.btnAddLunch)
        val btnAddSnack = findViewById<Button>(R.id.btnAddSnack)
        val btnAddDinner = findViewById<Button>(R.id.btnAddDinner)

        // Mostrar resumen de calorías
        updateCalorieSummary(tvCaloriesConsumed, tvCaloriesRemaining)

        // Cargar comidas por franja horaria
        updateSelectedMeals("breakfast", tvSelectedBreakfast)
        updateSelectedMeals("lunch", tvSelectedLunch)
        updateSelectedMeals("snack", tvSelectedSnack)
        updateSelectedMeals("dinner", tvSelectedDinner)

        // Botones para añadir comidas
        btnAddBreakfast.setOnClickListener { addMeal("breakfast") }
        btnAddLunch.setOnClickListener { addMeal("lunch") }
        btnAddSnack.setOnClickListener { addMeal("snack") }
        btnAddDinner.setOnClickListener { addMeal("dinner") }
    }

    private fun updateCalorieSummary(tvConsumed: TextView, tvRemaining: TextView) {
        val meals = repository.getMealsByDate(currentDate)
        var totalCaloriesConsumed = 0

        for (meal in meals) {
            totalCaloriesConsumed += meal.calories
        }

        val calorieGoal = getSharedPreferences("UserData", MODE_PRIVATE)
            .getInt("calorieGoal", 2000) // Default to 2000 kcal

        tvConsumed.text = getString(R.string.calories_consumed, totalCaloriesConsumed)
        tvRemaining.text = getString(R.string.calories_remaining, calorieGoal - totalCaloriesConsumed)
    }

    private fun updateSelectedMeals(timeOfDay: String, textView: TextView) {
        val meals = repository.getMealsByDateAndTime(currentDate, timeOfDay)
        if (meals.isEmpty()) {
            textView.text = getString(R.string.no_meals)
        } else {
            val mealDescriptions = meals.joinToString("\n") { meal ->
                "${meal.name}: ${meal.calories} kcal"
            }
            textView.text = mealDescriptions
        }
    }

    private fun addMeal(timeOfDay: String) {
        // Lanza una nueva actividad para seleccionar un alimento
        val intent = Intent(this, AddMealActivity::class.java)
        intent.putExtra("timeOfDay", timeOfDay)
        intent.putExtra("date", currentDate)
        startActivity(intent)
    }
}
