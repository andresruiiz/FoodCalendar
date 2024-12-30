package es.uma.foodcalendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class DailySummaryActivity : AppCompatActivity() {
    private lateinit var repository: FoodCalendarRepository
    private lateinit var selectedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_summary)

        repository = FoodCalendarRepository(this)

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

        // Configurar la fecha actual
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Mostrar datos iniciales
        updateCalorieSummary(tvCaloriesConsumed, tvCaloriesRemaining)
        updateSelectedMeals("breakfast", tvSelectedBreakfast)
        updateSelectedMeals("lunch", tvSelectedLunch)
        updateSelectedMeals("snack", tvSelectedSnack)
        updateSelectedMeals("dinner", tvSelectedDinner)

        // Abrir AddMealActivity para cada franja horaria
        btnAddBreakfast.setOnClickListener { openAddMealActivity("breakfast") }
        btnAddLunch.setOnClickListener { openAddMealActivity("lunch") }
        btnAddSnack.setOnClickListener { openAddMealActivity("snack") }
        btnAddDinner.setOnClickListener { openAddMealActivity("dinner") }
    }

    // Abrir AddMealActivity
    private fun openAddMealActivity(timeOfDay: String) {
        val intent = Intent(this, AddMealActivity::class.java)
        intent.putExtra("date", selectedDate)
        intent.putExtra("timeOfDay", timeOfDay)
        startActivityForResult(intent, ADD_MEAL_REQUEST_CODE)
    }

    // Actualizar comidas y calorías al volver de AddMealActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_MEAL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val tvCaloriesConsumed = findViewById<TextView>(R.id.tvCaloriesConsumed)
            val tvCaloriesRemaining = findViewById<TextView>(R.id.tvCaloriesRemaining)

            val tvSelectedBreakfast = findViewById<TextView>(R.id.tvSelectedBreakfast)
            val tvSelectedLunch = findViewById<TextView>(R.id.tvSelectedLunch)
            val tvSelectedSnack = findViewById<TextView>(R.id.tvSelectedSnack)
            val tvSelectedDinner = findViewById<TextView>(R.id.tvSelectedDinner)

            // Recargar los datos
            updateCalorieSummary(tvCaloriesConsumed, tvCaloriesRemaining)
            updateSelectedMeals("breakfast", tvSelectedBreakfast)
            updateSelectedMeals("lunch", tvSelectedLunch)
            updateSelectedMeals("snack", tvSelectedSnack)
            updateSelectedMeals("dinner", tvSelectedDinner)
        }
    }

    private fun updateCalorieSummary(tvConsumed: TextView, tvRemaining: TextView) {
        val meals = repository.getMealsByDate(selectedDate)
        val totalCalories = meals.sumOf { it.calories } // Tomamos calorías calculadas del repositorio

        val calorieGoal = getSharedPreferences("UserData", MODE_PRIVATE)
            .getInt("calorieGoal", 2000) // Por defecto, 2000 kcal

        tvConsumed.text = getString(R.string.calories_consumed, totalCalories)
        tvRemaining.text = getString(R.string.calories_remaining, calorieGoal - totalCalories)
    }

    private fun updateSelectedMeals(timeOfDay: String, textView: TextView) {
        val meals = repository.getMealsByDateAndTime(selectedDate, timeOfDay)

        if (meals.isEmpty()) {
            textView.text = getString(R.string.no_meals)
        } else {
            val mealDescriptions = meals.joinToString("\n") { meal ->
                "- ${meal.name}: ${meal.calories} kcal (${meal.quantity}g)"
            }
            textView.text = mealDescriptions
        }
    }





    companion object {
        const val ADD_MEAL_REQUEST_CODE = 1
    }
}
