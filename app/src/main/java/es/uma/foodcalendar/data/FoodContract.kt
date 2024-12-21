package es.uma.foodcalendar.data

import android.provider.BaseColumns

object FoodContract {
    object FoodEntry : BaseColumns {
        const val TABLE_NAME = "food"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_CALORIES = "calories"
    }
}