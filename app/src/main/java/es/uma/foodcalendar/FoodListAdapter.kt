package es.uma.foodcalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import es.uma.foodcalendar.R

class FoodListAdapter(
    private val context: Context,
    private val foods: List<Food>
) : android.widget.BaseAdapter() {

    private var selectedPosition: Int = -1 // Índice del elemento seleccionado

    override fun getCount(): Int = foods.size

    override fun getItem(position: Int): Food = foods[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_food_list, parent, false)

        val tvFoodName = view.findViewById<TextView>(R.id.tvFoodName)
        val tvFoodCalories = view.findViewById<TextView>(R.id.tvFoodCalories)
        val tvFoodProteins = view.findViewById<TextView>(R.id.tvFoodProteins)

        val food = getItem(position)

        tvFoodName.text = food.name
        tvFoodCalories.text = "Calories: ${food.calories} kcal (per 100g)"
        tvFoodProteins.text = "Proteins: ${food.protein}g"

        // Manejar el fondo y color del texto para el estado seleccionado
        if (position == selectedPosition) {
            tvFoodName.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_item_background))
            tvFoodName.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            tvFoodName.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            tvFoodName.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }

        return view
    }


    // Detectar si el dispositivo está en modo oscuro
    private fun isDarkTheme(): Boolean {
        val nightModeFlags = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }


    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }
}
