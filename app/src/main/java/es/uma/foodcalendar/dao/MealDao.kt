package es.uma.foodcalendar.dao

import androidx.room.*
import es.uma.foodcalendar.model.Meal

@Dao
interface MealDao {
    @Query("SELECT * FROM meal WHERE date = :date")
    suspend fun getMealsByDate(date: Long): List<Meal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)
}