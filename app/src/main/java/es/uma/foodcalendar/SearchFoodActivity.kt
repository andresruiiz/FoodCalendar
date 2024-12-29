package es.uma.foodcalendar

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SearchFoodActivity : AppCompatActivity() {

    private lateinit var etSearchQuery: EditText
    private lateinit var btnSearch: Button
    private lateinit var lvSearchResults: ListView
    private lateinit var repository: FoodCalendarRepository

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_food)

        repository = FoodCalendarRepository(this)

        etSearchQuery = findViewById(R.id.etSearchQuery)
        btnSearch = findViewById(R.id.btnSearch)
        lvSearchResults = findViewById(R.id.lvSearchResults)

        btnSearch.setOnClickListener {
            val query = etSearchQuery.text.toString().trim()
            if (query.isNotEmpty()) {
                searchFood(query)
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }

        lvSearchResults.setOnItemClickListener { _, _, position, _ ->
            val selectedFood = lvSearchResults.adapter.getItem(position) as Food
            saveFoodToDatabase(selectedFood)
        }
    }

    private fun searchFood(query: String) {
        val url = "https://world.openfoodfacts.org/cgi/search.pl?search_terms=$query&search_simple=1&json=1"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SearchFoodActivity, "Failed to fetch results", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val json = JSONObject(responseBody.string())
                    val products = json.optJSONArray("products") ?: return

                    val foods = mutableListOf<Food>()
                    for (i in 0 until products.length()) {
                        val product = products.optJSONObject(i) ?: continue
                        val name = product.optString("product_name", "Unknown")
                        val calories = product.optJSONObject("nutriments")?.optInt("energy-kcal_100g", 0) ?: 0
                        val protein = product.optJSONObject("nutriments")?.optInt("proteins_100g", 0) ?: 0
                        val fat = product.optJSONObject("nutriments")?.optInt("fat_100g", 0) ?: 0
                        val carbs = product.optJSONObject("nutriments")?.optInt("carbohydrates_100g", 0) ?: 0

                        foods.add(Food(0, name, calories, protein, fat, carbs))
                    }

                    runOnUiThread {
                        val adapter = ArrayAdapter(this@SearchFoodActivity, android.R.layout.simple_list_item_1, foods.map { it.name })
                        lvSearchResults.adapter = adapter
                    }
                }
            }
        })
    }

    private fun saveFoodToDatabase(food: Food) {
        repository.addFood(food.name, food.calories, food.protein, food.fat, food.carbs)
        Toast.makeText(this, "Food added successfully!", Toast.LENGTH_SHORT).show()

        // Enviar resultado a AddMealActivity
        setResult(Activity.RESULT_OK)
        finish()
    }
}