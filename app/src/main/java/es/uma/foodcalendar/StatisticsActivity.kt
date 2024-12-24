package es.uma.foodcalendar

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class StatisticsActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val selectedDateLabel = findViewById<TextView>(R.id.selectedDateLabel)
        val mealsListView = findViewById<ListView>(R.id.mealsListView)

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        // Formato de fecha
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Cambia la fecha seleccionada
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = dateFormat.format(
                GregorianCalendar(year, month, dayOfMonth).time
            )
            selectedDateLabel.text = getString(R.string.selected_date) + " " + selectedDate

            // Cargar las comidas del día seleccionado
            val query = """
                SELECT * FROM ${DatabaseHelper.TABLE_MEALS} 
                WHERE ${DatabaseHelper.COLUMN_DATE} = ?
            """
            val cursor: Cursor = db.rawQuery(query, arrayOf(selectedDate))
            val meals = mutableListOf<String>()

            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME))
                val calories = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CALORIES))
                val protein = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PROTEIN))
                val fat = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FAT))
                val carbs = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CARBS))
                meals.add("$name - $calories kcal, $protein g Protein, $fat g Fat, $carbs g Carbs")
            }
            cursor.close()

            // Mostrar las comidas en la lista
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, meals)
            mealsListView.adapter = adapter
        }

        // Inicializar con la fecha actual
        val todayDate = dateFormat.format(Date())
        calendarView.date = Date().time
        selectedDateLabel.text = getString(R.string.selected_date) + " " + todayDate

        // Cargar las comidas del día actual
        val initialQuery = """
            SELECT * FROM ${DatabaseHelper.TABLE_MEALS} 
            WHERE ${DatabaseHelper.COLUMN_DATE} = ?
        """
        val initialCursor: Cursor = db.rawQuery(initialQuery, arrayOf(todayDate))
        val initialMeals = mutableListOf<String>()

        while (initialCursor.moveToNext()) {
            val name = initialCursor.getString(initialCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME))
            val calories = initialCursor.getInt(initialCursor.getColumnIndex(DatabaseHelper.COLUMN_CALORIES))
            val protein = initialCursor.getInt(initialCursor.getColumnIndex(DatabaseHelper.COLUMN_PROTEIN))
            val fat = initialCursor.getInt(initialCursor.getColumnIndex(DatabaseHelper.COLUMN_FAT))
            val carbs = initialCursor.getInt(initialCursor.getColumnIndex(DatabaseHelper.COLUMN_CARBS))
            initialMeals.add("$name - $calories kcal, $protein g Protein, $fat g Fat, $carbs g Carbs")
        }
        initialCursor.close()

        // Mostrar las comidas del día actual
        val initialAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, initialMeals)
        mealsListView.adapter = initialAdapter
    }
}
