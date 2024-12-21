package es.uma.foodcalendar.data

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

data class FoodResponse(val products: List<FoodItem>)

data class FoodItem(
    val product_name: String,
    val nutriments: Nutriments
)

data class Nutriments(
    val energy_kcal: Float
)

interface OpenFoodFactsApiService {
    @GET("cgi/search.pl?search_simple=1&json=1")
    fun searchFood(@Query("search_terms") query: String): Call<FoodResponse>
}