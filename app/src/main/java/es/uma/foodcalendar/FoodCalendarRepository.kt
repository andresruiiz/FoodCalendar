package es.uma.foodcalendar

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class FoodCalendarRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addFood(name: String, calories: Int, protein: Int, fat: Int, carbs: Int): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FoodCalendarContract.Foods.COLUMN_NAME, name)
            put(FoodCalendarContract.Foods.COLUMN_CALORIES, calories)
            put(FoodCalendarContract.Foods.COLUMN_PROTEIN, protein)
            put(FoodCalendarContract.Foods.COLUMN_FAT, fat)
            put(FoodCalendarContract.Foods.COLUMN_CARBS, carbs)
        }
        return db.insert(FoodCalendarContract.Foods.TABLE_NAME, null, values)
    }

    fun addMeal(foodId: Long, date: String, timeOfDay: String, quantity: Int): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FoodCalendarContract.Meals.COLUMN_FOOD_ID, foodId)
            put(FoodCalendarContract.Meals.COLUMN_DATE, date)
            put(FoodCalendarContract.Meals.COLUMN_TIME_OF_DAY, timeOfDay)
            put(FoodCalendarContract.Meals.COLUMN_QUANTITY, quantity)
        }
        return db.insert(FoodCalendarContract.Meals.TABLE_NAME, null, values)
    }

    fun getMealsByDate(date: String): List<String> {
        val db = dbHelper.readableDatabase
        val query = """
            SELECT f.${FoodCalendarContract.Foods.COLUMN_NAME}, 
                   f.${FoodCalendarContract.Foods.COLUMN_CALORIES}, 
                   m.${FoodCalendarContract.Meals.COLUMN_QUANTITY}
            FROM ${FoodCalendarContract.Meals.TABLE_NAME} m
            JOIN ${FoodCalendarContract.Foods.TABLE_NAME} f
            ON m.${FoodCalendarContract.Meals.COLUMN_FOOD_ID} = f.${BaseColumns._ID}
            WHERE m.${FoodCalendarContract.Meals.COLUMN_DATE} = ?
        """
        val cursor = db.rawQuery(query, arrayOf(date))
        val meals = mutableListOf<String>()

        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val calories = cursor.getInt(1)
            val quantity = cursor.getInt(2)
            meals.add("$name: ${calories * quantity} kcal")
        }
        cursor.close()
        return meals
    }
}
