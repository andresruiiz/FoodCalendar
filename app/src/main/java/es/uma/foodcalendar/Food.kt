package es.uma.foodcalendar

data class Food(
    val id: Long,       // ID del alimento en la tabla 'foods'
    val name: String,   // Nombre del alimento
    val calories: Int,  // Calorías por porción
    val protein: Int,   // Proteínas por porción (g)
    val fat: Int,       // Grasas por porción (g)
    val carbs: Int      // Carbohidratos por porción (g)
)
