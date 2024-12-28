package es.uma.foodcalendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MealListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_list)

        val mealListView = findViewById<ListView>(R.id.mealListView)
        val fabAddMeal = findViewById<FloatingActionButton>(R.id.fab_add_meal)

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_MEALS}", null)

        val meals = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                val mealName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
                meals.add(mealName)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, meals)
        mealListView.adapter = adapter

        mealListView.setOnItemClickListener { _, _, position, _ ->
            val selectedMeal = meals[position]
            val mealType = intent.getStringExtra("mealType")
            val resultIntent = Intent()
            resultIntent.putExtra("selectedMeal", selectedMeal)
            resultIntent.putExtra("mealType", mealType)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        fabAddMeal.setOnClickListener {
            val intent = Intent(this, AddMealActivity::class.java)
            startActivity(intent)
        }
    }
}