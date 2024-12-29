package es.uma.foodcalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso

class FoodSearchAdapter(
    private val context: Context,
    private val foods: List<FoodSearchResult>
) : android.widget.BaseAdapter() {

    override fun getCount(): Int = foods.size

    override fun getItem(position: Int): FoodSearchResult = foods[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_food_search, parent, false)

        val food = getItem(position)

        val ivFoodImage = view.findViewById<ImageView>(R.id.ivFoodImage)
        val tvFoodName = view.findViewById<TextView>(R.id.tvFoodName)
        val tvCalories = view.findViewById<TextView>(R.id.tvCalories)

        tvFoodName.text = food.name
        tvCalories.text = "Calories: ${food.calories} kcal"

        // Cargar la imagen si está disponible, de lo contrario usar un placeholder
        if (food.imageUrl.isNotEmpty()) {
            Picasso.get()
                .load(food.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(ivFoodImage)
        } else {
            ivFoodImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.placeholder_image))
        }

        return view
    }
}
