package es.uma.foodcalendar

import android.provider.BaseColumns

object FoodCalendarContract {

    object Foods : BaseColumns {
        const val TABLE_NAME = "foods"
        const val COLUMN_NAME = "name"
        const val COLUMN_CALORIES = "calories"
        const val COLUMN_PROTEIN = "protein"
        const val COLUMN_FAT = "fat"
        const val COLUMN_CARBS = "carbs"
    }

    object Meals : BaseColumns {
        const val TABLE_NAME = "meals"
        const val COLUMN_FOOD_ID = "food_id"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME_OF_DAY = "time_of_day"
        const val COLUMN_QUANTITY = "quantity"
    }
}
