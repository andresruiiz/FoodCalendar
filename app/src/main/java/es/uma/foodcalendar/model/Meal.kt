package es.uma.foodcalendar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal")
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val calories: Int,
    val type: String, // breakfast, lunch, dinner, snack
    val date: Long // timestamp
)