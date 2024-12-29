package es.uma.foodcalendar

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class StatisticsActivity : AppCompatActivity() {
    private lateinit var repository: FoodCalendarRepository
    private lateinit var calendarView: CalendarView
    private lateinit var selectedDateLabel: TextView
    private lateinit var mealsListView: ListView
    private lateinit var totalCalories: TextView

    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        repository = FoodCalendarRepository(this)

        // Inicializar vistas
        calendarView = findViewById(R.id.calendarView)
        selectedDateLabel = findViewById(R.id.selectedDateLabel)
        mealsListView = findViewById(R.id.mealsListView)
        totalCalories = findViewById(R.id.totalCalories)

        // Configurar fecha inicial
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        selectedDateLabel.text = getString(R.string.selected_date, selectedDate)

        // Mostrar comidas para la fecha inicial
        loadMealsForDate(selectedDate)

        // Cambiar la fecha al seleccionar en el calendario
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            selectedDateLabel.text = getString(R.string.selected_date, selectedDate)
            loadMealsForDate(selectedDate)
        }
    }

    private fun loadMealsForDate(date: String) {
        // Obtener las comidas del día
        val meals = repository.getMealsByDate(date)

        if (meals.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_meals_for_date), Toast.LENGTH_SHORT).show()
            mealsListView.adapter = null
            totalCalories.text = getString(R.string.total_calories, 0)
            return
        }

        // Agrupar por franja horaria y mostrar
        val groupedMeals = meals.groupBy { it.timeOfDay }
        val mealDescriptions = groupedMeals.map { (timeOfDay, meals) ->
            val totalCaloriesForTime = meals.sumOf { it.calories }
            val mealDetails = meals.joinToString("\n") { meal ->
                "- ${meal.name}: ${meal.calories} kcal (${meal.quantity}x)"
            }
            "${timeOfDay.capitalize()}:\n$totalCaloriesForTime kcal\n$mealDetails"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mealDescriptions)
        mealsListView.adapter = adapter

        // Calcular el total de calorías del día
        val totalCaloriesForDay = meals.sumOf { it.calories }
        totalCalories.text = getString(R.string.total_calories, totalCaloriesForDay)
    }
}
