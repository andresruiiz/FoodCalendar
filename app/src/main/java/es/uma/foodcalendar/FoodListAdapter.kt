package es.uma.foodcalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat

class FoodListAdapter(
    private val context: Context,
    private val foods: List<Food>
) : android.widget.BaseAdapter() {

    private var selectedPosition: Int = -1

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
        tvFoodCalories.text = context.getString(R.string.calories_per_100g, food.calories)
        tvFoodProteins.text = context.getString(R.string.proteins_per_100g, food.protein)

        // Resaltar todo el fondo si está seleccionado
        if (position == selectedPosition) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_item_background))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }

        return view
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }
}
