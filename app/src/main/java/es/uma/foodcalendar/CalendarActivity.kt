package es.uma.foodcalendar

import android.os.Bundle
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate)
        val tvCaloriesSummary = findViewById<TextView>(R.id.tvCaloriesSummary)
        val lvMeals = findViewById<ListView>(R.id.lvMeals)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            tvSelectedDate.text = "Selected Date: $selectedDate"

            // Update the calories summary and meals list for the selected date
            updateCaloriesSummary(selectedDate, tvCaloriesSummary)
            updateMealsList(selectedDate, lvMeals)
        }
    }

    private fun updateCaloriesSummary(date: String, textView: TextView) {
        // Implement logic to update calories summary for the selected date
        textView.text = "Calories Summary: 0" // Placeholder
    }

    private fun updateMealsList(date: String, listView: ListView) {
        // Implement logic to update meals list for the selected date
        // Placeholder: listView.adapter = ...
    }
}