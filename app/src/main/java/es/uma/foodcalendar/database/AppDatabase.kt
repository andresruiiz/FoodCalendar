package es.uma.foodcalendar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.uma.foodcalendar.dao.MealDao
import es.uma.foodcalendar.dao.UserDao
import es.uma.foodcalendar.model.Meal
import es.uma.foodcalendar.model.User

@Database(entities = [User::class, Meal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food_calendar_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}