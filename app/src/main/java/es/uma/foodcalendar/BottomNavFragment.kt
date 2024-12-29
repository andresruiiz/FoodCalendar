package es.uma.foodcalendar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("bottom_nav_prefs", Context.MODE_PRIVATE)
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set the selected item from SharedPreferences
        val selectedItemId = sharedPreferences.getInt("selected_item_id", R.id.navigation_home)
        bottomNavigationView.selectedItemId = selectedItemId

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            // Save the selected item ID to SharedPreferences
            sharedPreferences.edit().putInt("selected_item_id", item.itemId).apply()

            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(activity, DailySummaryActivity::class.java))
                    true
                }
                R.id.navigation_statistics -> {
                    startActivity(Intent(activity, StatisticsActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(activity, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}