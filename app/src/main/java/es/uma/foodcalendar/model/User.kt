package es.uma.foodcalendar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int = 0,
    val age: Int,
    val weight: Float,
    val height: Float,
    val goal: Int
)