package es.uma.foodcalendar

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddMealActivity : AppCompatActivity() {

    private lateinit var repository: FoodCalendarRepository
    private lateinit var foodListView: ListView
    private lateinit var etFoodQuantity: EditText
    private lateinit var btnAddMeal: Button
    private lateinit var btnAddNewFood: Button
    private lateinit var btnSearchFood: Button
    private var selectedFood: Food? = null
    private var foods: List<Food> = listOf()
    private lateinit var adapter: FoodListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meal)

        repository = FoodCalendarRepository(this)
        foodListView = findViewById(R.id.foodListView)
        etFoodQuantity = findViewById(R.id.etFoodQuantity)
        btnAddMeal = findViewById(R.id.btnAddMeal)
        btnAddNewFood = findViewById(R.id.btnAddNewFood)
        btnSearchFood = findViewById(R.id.btnSearchFood)

        val date = intent.getStringExtra("date") ?: return
        val timeOfDay = intent.getStringExtra("timeOfDay") ?: return

        // Cargar alimentos desde la base de datos
        loadFoods()

        // Seleccionar un alimento de la lista
        foodListView.setOnItemClickListener { _, _, position, _ ->
            selectedFood = foods[position]
            adapter.setSelectedPosition(position) // Resaltar el elemento seleccionado
        }

        // Añadir una comida con la cantidad especificada
        btnAddMeal.setOnClickListener {
    val food = selectedFood
    if (food == null) {
        Toast.makeText(this, getString(R.string.please_select_food), Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }

    val quantity = etFoodQuantity.text.toString().toIntOrNull()
    if (quantity == null || quantity <= 0) {
        Toast.makeText(this, getString(R.string.please_enter_valid_quantity), Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }

    AlertDialog.Builder(this)
        .setTitle(R.string.confirmation)
        .setMessage(R.string.confirm_save_meal)
        .setPositiveButton(R.string.yes) { dialog, which ->
            // Calcular calorías según la cantidad en gramos
            val totalCalories = food.calories * quantity / 100

            repository.addMeal(
                foodId = food.id,
                date = date,
                timeOfDay = timeOfDay,
                quantity = quantity
            )
            Toast.makeText(this, getString(R.string.added_food, quantity, food.name, totalCalories), Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()
        }
        .setNegativeButton(R.string.no, null)
        .show()
}

        // Botón para añadir un nuevo alimento
        btnAddNewFood.setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            startActivityForResult(intent, ADD_FOOD_REQUEST_CODE)
        }

        // Botón para buscar un alimento en la API
        btnSearchFood.setOnClickListener {
            val intent = Intent(this, SearchFoodActivity::class.java)
            startActivityForResult(intent, SEARCH_FOOD_REQUEST_CODE)
        }
    }

    private fun loadFoods() {
        foods = repository.getAllFoods()
        adapter = FoodListAdapter(this, foods)
        foodListView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == ADD_FOOD_REQUEST_CODE || requestCode == SEARCH_FOOD_REQUEST_CODE) && resultCode == Activity.RESULT_OK) {
            loadFoods()
        }
    }

    companion object {
        const val ADD_FOOD_REQUEST_CODE = 1
        const val SEARCH_FOOD_REQUEST_CODE = 2
    }
}