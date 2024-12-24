package es.uma.foodcalendar

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddMealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meal)

        val mealNameInput = findViewById<EditText>(R.id.mealNameInput)
        val calorieInput = findViewById<EditText>(R.id.calorieInput)
        val proteinInput = findViewById<EditText>(R.id.proteinInput)
        val fatInput = findViewById<EditText>(R.id.fatInput)
        val carbsInput = findViewById<EditText>(R.id.carbsInput)
        val mealCategorySpinner = findViewById<Spinner>(R.id.mealCategorySpinner)
        val saveMealButton = findViewById<Button>(R.id.saveMealButton)

        val dbHelper = DatabaseHelper(this)

        saveMealButton.setOnClickListener {
            val mealName = mealNameInput.text.toString()
            val calories = calorieInput.text.toString()
            val protein = proteinInput.text.toString()
            val fat = fatInput.text.toString()
            val carbs = carbsInput.text.toString()
            val category = mealCategorySpinner.selectedItem.toString()
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (mealName.isNotEmpty() && calories.isNotEmpty() && protein.isNotEmpty() &&
                fat.isNotEmpty() && carbs.isNotEmpty()) {
                val db = dbHelper.writableDatabase
                val query = """
                    INSERT INTO ${DatabaseHelper.TABLE_MEALS} 
                    (${DatabaseHelper.COLUMN_NAME}, ${DatabaseHelper.COLUMN_CALORIES}, ${DatabaseHelper.COLUMN_PROTEIN}, 
                    ${DatabaseHelper.COLUMN_FAT}, ${DatabaseHelper.COLUMN_CARBS}, ${DatabaseHelper.COLUMN_CATEGORY}, ${DatabaseHelper.COLUMN_DATE})
                    VALUES ('$mealName', $calories, $protein, $fat, $carbs, '$category', '$date')
                """
                db.execSQL(query)
                Toast.makeText(this, "Comida añadida correctamente", Toast.LENGTH_SHORT).show()
                finish() // Vuelve a la pantalla anterior
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
