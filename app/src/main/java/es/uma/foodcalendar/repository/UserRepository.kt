package es.uma.foodcalendar.repository

import es.uma.foodcalendar.dao.UserDao
import es.uma.foodcalendar.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun getUser(): User? = userDao.getUser()
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
}