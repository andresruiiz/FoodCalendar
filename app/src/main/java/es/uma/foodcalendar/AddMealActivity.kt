package es.uma.foodcalendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddMealActivity : AppCompatActivity() {

    private lateinit var repository: FoodCalendarRepository
    private lateinit var foodListView: ListView
    private lateinit var btnAddNewFood: Button
    private var foods: List<Food> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meal)

        repository = FoodCalendarRepository(this)
        foodListView = findViewById(R.id.foodListView)
        btnAddNewFood = findViewById(R.id.btnAddNewFood)

        // Obtener la fecha y franja horaria desde el intent
        val date = intent.getStringExtra("date") ?: return
        val timeOfDay = intent.getStringExtra("timeOfDay") ?: return

        // Cargar alimentos
        loadFoods()

        // Seleccionar un alimento y añadirlo como comida
        foodListView.setOnItemClickListener { _, _, position, _ ->
            val selectedFood = foods[position]

            // Verificar si ya existe una comida para la misma fecha y franja horaria
            val existingMeal = repository.getMealByFoodIdAndTime(
                foodId = selectedFood.id,
                date = date,
                timeOfDay = timeOfDay
            )

            if (existingMeal != null) {
                // Incrementar la cantidad si ya existe
                repository.updateMealQuantity(existingMeal.id, existingMeal.quantity + 1)
                Toast.makeText(this, R.string.meal_quantity_updated, Toast.LENGTH_SHORT).show()
            } else {
                // Añadir una nueva comida si no existe
                repository.addMeal(
                    foodId = selectedFood.id,
                    date = date,
                    timeOfDay = timeOfDay,
                    quantity = 1
                )
                Toast.makeText(this, R.string.meal_added_successfully, Toast.LENGTH_SHORT).show()
            }

            setResult(Activity.RESULT_OK)
            finish()
        }


        // Navegar a AddFoodActivity para añadir un nuevo alimento
        btnAddNewFood.setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            startActivityForResult(intent, ADD_FOOD_REQUEST_CODE)
        }
    }

    // Cargar alimentos desde la base de datos
    private fun loadFoods() {
        foods = repository.getAllFoods()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foods.map { it.name })
        foodListView.adapter = adapter
    }

    // Manejar el resultado al volver de AddFoodActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_FOOD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Recargar la lista de alimentos
            loadFoods()
        }
    }

    companion object {
        const val ADD_FOOD_REQUEST_CODE = 1
    }
}
