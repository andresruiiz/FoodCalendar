package es.uma.foodcalendar

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        const val DATABASE_NAME = "FoodCalendar.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla foods
        val createFoodsTable = """
            CREATE TABLE ${FoodCalendarContract.Foods.TABLE_NAME} (
                ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${FoodCalendarContract.Foods.COLUMN_NAME} TEXT,
                ${FoodCalendarContract.Foods.COLUMN_CALORIES} INTEGER,
                ${FoodCalendarContract.Foods.COLUMN_PROTEIN} INTEGER,
                ${FoodCalendarContract.Foods.COLUMN_FAT} INTEGER,
                ${FoodCalendarContract.Foods.COLUMN_CARBS} INTEGER
            )
        """
        db.execSQL(createFoodsTable)

        // Crear tabla meals
        val createMealsTable = """
            CREATE TABLE ${FoodCalendarContract.Meals.TABLE_NAME} (
                ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${FoodCalendarContract.Meals.COLUMN_FOOD_ID} INTEGER,
                ${FoodCalendarContract.Meals.COLUMN_DATE} TEXT,
                ${FoodCalendarContract.Meals.COLUMN_TIME_OF_DAY} TEXT,
                ${FoodCalendarContract.Meals.COLUMN_QUANTITY} INTEGER,
                FOREIGN KEY(${FoodCalendarContract.Meals.COLUMN_FOOD_ID}) 
                REFERENCES ${FoodCalendarContract.Foods.TABLE_NAME}(${BaseColumns._ID})
            )
        """
        db.execSQL(createMealsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${FoodCalendarContract.Meals.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${FoodCalendarContract.Foods.TABLE_NAME}")
        onCreate(db)
    }
}
