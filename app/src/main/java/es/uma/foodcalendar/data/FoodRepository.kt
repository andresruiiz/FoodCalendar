package es.uma.foodcalendar.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodRepository(context: Context) {
    private val dbHelper = FoodDbHelper(context)

    fun insertFood(name: String, calories: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FoodContract.FoodEntry.COLUMN_NAME_NAME, name)
            put(FoodContract.FoodEntry.COLUMN_NAME_CALORIES, calories)
        }
        db.insert(FoodContract.FoodEntry.TABLE_NAME, null, values)
    }

    fun getAllFoods(): List<Food> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID, FoodContract.FoodEntry.COLUMN_NAME_NAME, FoodContract.FoodEntry.COLUMN_NAME_CALORIES)
        val cursor: Cursor = db.query(
            FoodContract.FoodEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val foods = mutableListOf<Food>()
        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val itemName = getString(getColumnIndexOrThrow(FoodContract.FoodEntry.COLUMN_NAME_NAME))
                val itemCalories = getInt(getColumnIndexOrThrow(FoodContract.FoodEntry.COLUMN_NAME_CALORIES))
                foods.add(Food(itemId, itemName, itemCalories))
            }
        }
        cursor.close()
        return foods
    }

    fun searchFood(query: String, callback: (List<FoodItem>) -> Unit) {
        val call = ApiClient.apiService.searchFood(query)
        call.enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val foodItems = response.body()?.products ?: emptyList()
                    callback(foodItems)
                } else {
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                callback(emptyList())
            }
        })
    }
}