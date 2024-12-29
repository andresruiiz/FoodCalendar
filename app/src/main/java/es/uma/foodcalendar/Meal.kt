package es.uma.foodcalendar

data class Meal(
    val id: Long,           // ID de la comida en la tabla 'meals'
    val name: String,       // Nombre del alimento (de la tabla 'foods')
    val calories: Int,      // Calorías totales (calculadas como calorías * cantidad)
    val quantity: Int,      // Cantidad consumida
    val timeOfDay: String,  // Franja horaria (ej.: desayuno, almuerzo)
    val date: String        // Fecha de la comida
)
