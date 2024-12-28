package es.uma.foodcalendar

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "foodcalendar.db"
        const val DATABASE_VERSION = 1

        const val TABLE_MEALS = "meals"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_CALORIES = "calories"
        const val COLUMN_PROTEIN = "protein"
        const val COLUMN_FAT = "fat"
        const val COLUMN_CARBS = "carbs"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_MEALS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_CALORIES INTEGER,
                $COLUMN_PROTEIN INTEGER,
                $COLUMN_FAT INTEGER,
                $COLUMN_CARBS INTEGER,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_DATE TEXT
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEALS")
        onCreate(db)
    }
}