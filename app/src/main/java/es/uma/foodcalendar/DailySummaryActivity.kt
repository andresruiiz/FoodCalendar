package es.uma.foodcalendar

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DailySummaryActivity : AppCompatActivity() {
    private val REQUEST_CODE_SELECT_MEAL = 1
    private lateinit var tvSelectedBreakfast: TextView
    private lateinit var tvSelectedLunch: TextView
    private lateinit var tvSelectedDinner: TextView
    private lateinit var tvCaloriesConsumed: TextView
    private lateinit var tvCaloriesRemaining: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_summary)

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        dbHelper = DatabaseHelper(this)

        tvCaloriesConsumed = findViewById(R.id.tvCaloriesConsumed)
        tvCaloriesRemaining = findViewById(R.id.tvCaloriesRemaining)
        tvSelectedBreakfast = findViewById(R.id.tvSelectedBreakfast)
        tvSelectedLunch = findViewById(R.id.tvSelectedLunch)
        tvSelectedDinner = findViewById(R.id.tvSelectedDinner)

        updateCaloriesInfo()

        findViewById<Button>(R.id.btnAddBreakfast).setOnClickListener {
            val intent = Intent(this, MealListActivity::class.java)
            intent.putExtra("mealType", "Breakfast")
            startActivityForResult(intent, REQUEST_CODE_SELECT_MEAL)
        }

        findViewById<Button>(R.id.btnCreateBreakfast).setOnClickListener {
            val intent = Intent(this, AddMealActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnAddLunch).setOnClickListener {
            val intent = Intent(this, MealListActivity::class.java)
            intent.putExtra("mealType", "Lunch")
            startActivityForResult(intent, REQUEST_CODE_SELECT_MEAL)
        }

        findViewById<Button>(R.id.btnCreateLunch).setOnClickListener {
            val intent = Intent(this, AddMealActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnAddDinner).setOnClickListener {
            val intent = Intent(this, MealListActivity::class.java)
            intent.putExtra("mealType", "Dinner")
            startActivityForResult(intent, REQUEST_CODE_SELECT_MEAL)
        }

        findViewById<Button>(R.id.btnCreateDinner).setOnClickListener {
            val intent = Intent(this, AddMealActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_MEAL && resultCode == RESULT_OK) {
            val selectedMeal = data?.getStringExtra("selectedMeal")
            val mealType = data?.getStringExtra("mealType")
            when (mealType) {
                "Breakfast" -> {
                    tvSelectedBreakfast.text = "Selected Breakfast: $selectedMeal"
                    sharedPreferences.edit().putString("selectedBreakfast", selectedMeal).apply()
                }
                "Lunch" -> {
                    tvSelectedLunch.text = "Selected Lunch: $selectedMeal"
                    sharedPreferences.edit().putString("selectedLunch", selectedMeal).apply()
                }
                "Dinner" -> {
                    tvSelectedDinner.text = "Selected Dinner: $selectedMeal"
                    sharedPreferences.edit().putString("selectedDinner", selectedMeal).apply()
                }
            }
            updateCaloriesInfo()
        }
    }

    private fun updateCaloriesInfo() {
        val caloriesConsumed = getCaloriesConsumed()
        val calorieGoal = sharedPreferences.getInt("calorieGoal", 0)
        val caloriesRemaining = calorieGoal - caloriesConsumed

        tvCaloriesConsumed.text = "Calories Consumed: $caloriesConsumed"
        tvCaloriesRemaining.text = "Calories Remaining: $caloriesRemaining"
    }

    private fun getCaloriesConsumed(): Int {
        val db = dbHelper.readableDatabase
        var totalCalories = 0

        val selectedBreakfast = sharedPreferences.getString("selectedBreakfast", null)
        val selectedLunch = sharedPreferences.getString("selectedLunch", null)
        val selectedDinner = sharedPreferences.getString("selectedDinner", null)

        if (selectedBreakfast != null) {
            totalCalories += getMealCalories(db, selectedBreakfast)
        }
        if (selectedLunch != null) {
            totalCalories += getMealCalories(db, selectedLunch)
        }
        if (selectedDinner != null) {
            totalCalories += getMealCalories(db, selectedDinner)
        }

        return totalCalories
    }

    private fun getMealCalories(db: SQLiteDatabase, mealName: String): Int {
        val cursor = db.rawQuery("SELECT ${DatabaseHelper.COLUMN_CALORIES} FROM ${DatabaseHelper.TABLE_MEALS} WHERE ${DatabaseHelper.COLUMN_NAME} = ?", arrayOf(mealName))
        var calories = 0
        if (cursor.moveToFirst()) {
            calories = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CALORIES))
        }
        cursor.close()
        return calories
    }
}