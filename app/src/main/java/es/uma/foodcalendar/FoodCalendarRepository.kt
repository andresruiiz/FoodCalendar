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

    fun getAllFoods(): List<Food> {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${FoodCalendarContract.Foods.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)
        val foods = mutableListOf<Food>()

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_NAME))
            val calories = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_CALORIES))
            val protein = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_PROTEIN))
            val fat = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_FAT))
            val carbs = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_CARBS))

            foods.add(Food(id, name, calories, protein, fat, carbs))
        }
        cursor.close()
        return foods
    }

    fun getFoodById(id: Long): Food? {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${FoodCalendarContract.Foods.TABLE_NAME} WHERE ${BaseColumns._ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        var food: Food? = null
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_NAME))
            val calories = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_CALORIES))
            val protein = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_PROTEIN))
            val fat = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_FAT))
            val carbs = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_CARBS))

            food = Food(id, name, calories, protein, fat, carbs)
        }
        cursor.close()
        return food
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


    fun getMealsByDate(date: String): List<Meal> {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT m.${BaseColumns._ID}, f.${FoodCalendarContract.Foods.COLUMN_NAME}, 
               f.${FoodCalendarContract.Foods.COLUMN_CALORIES},
                f.${FoodCalendarContract.Foods.COLUMN_PROTEIN},
                f.${FoodCalendarContract.Foods.COLUMN_FAT},
                f.${FoodCalendarContract.Foods.COLUMN_CARBS},
               m.${FoodCalendarContract.Meals.COLUMN_QUANTITY}, 
               m.${FoodCalendarContract.Meals.COLUMN_TIME_OF_DAY}, 
               m.${FoodCalendarContract.Meals.COLUMN_DATE}
        FROM ${FoodCalendarContract.Meals.TABLE_NAME} m
        JOIN ${FoodCalendarContract.Foods.TABLE_NAME} f
        ON m.${FoodCalendarContract.Meals.COLUMN_FOOD_ID} = f.${BaseColumns._ID}
        WHERE m.${FoodCalendarContract.Meals.COLUMN_DATE} = ?
    """
        val cursor = db.rawQuery(query, arrayOf(date))
        val meals = mutableListOf<Meal>()

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_NAME))
            val calories = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_CALORIES))
            val proteins = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_PROTEIN))
            val fats = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_FAT))
            val carbs = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_CARBS))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Meals.COLUMN_QUANTITY))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Meals.COLUMN_TIME_OF_DAY))
            val mealDate = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Meals.COLUMN_DATE))

            meals.add(Meal(id, name, (calories * quantity) / 100, (proteins * quantity) / 100, (fats * quantity) / 100, (carbs * quantity) / 100, quantity, time, mealDate))
        }
        cursor.close()
        return meals
    }



    fun getMealsByDateAndTime(date: String, timeOfDay: String): List<Meal> {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT m.${BaseColumns._ID}, f.${FoodCalendarContract.Foods.COLUMN_NAME}, 
               f.${FoodCalendarContract.Foods.COLUMN_CALORIES},
               f.${FoodCalendarContract.Foods.COLUMN_PROTEIN},
                f.${FoodCalendarContract.Foods.COLUMN_FAT},
                f.${FoodCalendarContract.Foods.COLUMN_CARBS},
               m.${FoodCalendarContract.Meals.COLUMN_QUANTITY}, 
               m.${FoodCalendarContract.Meals.COLUMN_TIME_OF_DAY}, 
               m.${FoodCalendarContract.Meals.COLUMN_DATE}
        FROM ${FoodCalendarContract.Meals.TABLE_NAME} m
        JOIN ${FoodCalendarContract.Foods.TABLE_NAME} f
        ON m.${FoodCalendarContract.Meals.COLUMN_FOOD_ID} = f.${BaseColumns._ID}
        WHERE m.${FoodCalendarContract.Meals.COLUMN_DATE} = ? 
        AND m.${FoodCalendarContract.Meals.COLUMN_TIME_OF_DAY} = ?
    """
        val cursor = db.rawQuery(query, arrayOf(date, timeOfDay))
        val meals = mutableListOf<Meal>()

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_NAME))
            val calories = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_CALORIES))
            val proteins = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_PROTEIN))
            val fats = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_FAT))
            val carbs = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_CARBS))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Meals.COLUMN_QUANTITY))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Meals.COLUMN_TIME_OF_DAY))
            val mealDate = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Meals.COLUMN_DATE))

            meals.add(Meal(id, name, (calories * quantity) / 100, (proteins * quantity) / 100, (fats * quantity) / 100, (carbs * quantity) / 100, quantity, time, mealDate))
        }
        cursor.close()
        return meals
    }

    fun getMealByFoodIdAndTime(foodId: Long, date: String, timeOfDay: String): Meal? {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT m.${BaseColumns._ID}, f.${FoodCalendarContract.Foods.COLUMN_NAME}, 
               f.${FoodCalendarContract.Foods.COLUMN_CALORIES}, 
               f.${FoodCalendarContract.Foods.COLUMN_PROTEIN},
                f.${FoodCalendarContract.Foods.COLUMN_FAT},
                f.${FoodCalendarContract.Foods.COLUMN_CARBS},
               m.${FoodCalendarContract.Meals.COLUMN_QUANTITY}, 
               m.${FoodCalendarContract.Meals.COLUMN_TIME_OF_DAY}, 
               m.${FoodCalendarContract.Meals.COLUMN_DATE}
        FROM ${FoodCalendarContract.Meals.TABLE_NAME} m
        JOIN ${FoodCalendarContract.Foods.TABLE_NAME} f
        ON m.${FoodCalendarContract.Meals.COLUMN_FOOD_ID} = f.${BaseColumns._ID}
        WHERE m.${FoodCalendarContract.Meals.COLUMN_FOOD_ID} = ? 
        AND m.${FoodCalendarContract.Meals.COLUMN_DATE} = ? 
        AND m.${FoodCalendarContract.Meals.COLUMN_TIME_OF_DAY} = ?
    """
        val cursor = db.rawQuery(query, arrayOf(foodId.toString(), date, timeOfDay))
        var meal: Meal? = null

        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_NAME))
            val calories = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_CALORIES))
            val proteins = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_PROTEIN))
            val fats = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_FAT))
            val carbs = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Foods.COLUMN_CARBS))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(FoodCalendarContract.Meals.COLUMN_QUANTITY))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Meals.COLUMN_TIME_OF_DAY))
            val mealDate = cursor.getString(cursor.getColumnIndexOrThrow(FoodCalendarContract.Meals.COLUMN_DATE))

            meal = Meal(id, name, (calories * quantity) / 100, (proteins * quantity) / 100, (fats * quantity) / 100, (carbs * quantity) / 100, quantity, time, mealDate)
        }

        cursor.close()
        return meal
    }


    fun updateMealQuantity(mealId: Long, newQuantity: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FoodCalendarContract.Meals.COLUMN_QUANTITY, newQuantity)
        }
        db.update(
            FoodCalendarContract.Meals.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(mealId.toString())
        )
    }

}
