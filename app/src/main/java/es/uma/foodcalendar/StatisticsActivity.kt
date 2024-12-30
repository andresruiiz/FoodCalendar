package es.uma.foodcalendar

import android.annotation.SuppressLint
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
    private lateinit var totalProteins: TextView
    private lateinit var totalFats: TextView
    private lateinit var totalCarbs: TextView

    private var selectedDate: String = ""

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        repository = FoodCalendarRepository(this)

        // Inicializar vistas
        calendarView = findViewById(R.id.calendarView)
        selectedDateLabel = findViewById(R.id.selectedDateLabel)
        mealsListView = findViewById(R.id.mealsListView)
        totalCalories = findViewById(R.id.totalCalories)
        totalProteins = findViewById(R.id.totalProteins)
        totalFats = findViewById(R.id.totalFats)
        totalCarbs = findViewById(R.id.totalCarbs)

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
            totalProteins.text = getString(R.string.total_proteins, 0)
            totalFats.text = getString(R.string.total_fats, 0)
            totalCarbs.text = getString(R.string.total_carbs, 0)
            return
        }

        // Agrupar por franja horaria y mostrar
        val groupedMeals = meals.groupBy { it.timeOfDay }
        val mealDescriptions = groupedMeals.map { (timeOfDay, meals) ->
            val totalCaloriesForTime = meals.sumOf { it.calories }
            val mealDetails = meals.joinToString("\n") { meal ->
                "- ${meal.name}: ${meal.calories} kcal (${meal.quantity}g)"
            }
            "${translateTimeOfDay(timeOfDay)}:\n$totalCaloriesForTime kcal\n$mealDetails"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mealDescriptions)
        mealsListView.adapter = adapter

        // Calcular el total de calorías, proteínas, grasas y carbohidratos del día
        val totalCaloriesForDay = meals.sumOf { it.calories }
        val totalProteinsForDay = meals.sumOf { it.proteins }
        val totalFatsForDay = meals.sumOf { it.fats }
        val totalCarbsForDay = meals.sumOf { it.carbs }

        totalCalories.text = getString(R.string.total_calories, totalCaloriesForDay)
        totalProteins.text = getString(R.string.total_proteins, totalProteinsForDay)
        totalFats.text = getString(R.string.total_fats, totalFatsForDay)
        totalCarbs.text = getString(R.string.total_carbs, totalCarbsForDay)
    }

    private fun translateTimeOfDay(timeOfDay: String): String {
        return when (timeOfDay.toLowerCase(Locale.getDefault())) {
            "breakfast" -> getString(R.string.breakfast)
            "lunch" -> getString(R.string.lunch)
            "snack" -> getString(R.string.snack)
            "dinner" -> getString(R.string.dinner)
            else -> timeOfDay
        }
    }
}