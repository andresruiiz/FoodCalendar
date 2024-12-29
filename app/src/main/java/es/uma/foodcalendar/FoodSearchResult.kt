package es.uma.foodcalendar

data class FoodSearchResult(
    val name: String,
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int,
    val imageUrl: String
)