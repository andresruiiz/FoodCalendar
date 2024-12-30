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
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        val food = getItem(position)
        textView.text = food.name

        // Cambiar el color de fondo si está seleccionado
        if (position == selectedPosition) {
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_200)) // Color seleccionado
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent)) // Color por defecto
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }

        return view
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }
}
