package es.uma.foodcalendar.repository

import es.uma.foodcalendar.dao.MealDao
import es.uma.foodcalendar.model.Meal

class MealRepository(private val mealDao: MealDao) {
    suspend fun getMealsByDate(date: Long): List<Meal> = mealDao.getMealsByDate(date)
    suspend fun insertMeal(meal: Meal) = mealDao.insertMeal(meal)
    suspend fun updateMeal(meal: Meal) = mealDao.updateMeal(meal)
    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)
}