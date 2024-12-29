package es.uma.foodcalendar

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)

        // Check if user data already exists
        if (userExists(sharedPreferences)) {
            // Redirect to DailySummaryActivity if user is already registered
            val intent = Intent(this, DailySummaryActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val heightInput = findViewById<EditText>(R.id.heightInput)
        val calorieGoalInput = findViewById<EditText>(R.id.calorieGoalInput)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val fabProfile = findViewById<FloatingActionButton>(R.id.fab_profile)

        // Check if data is already saved.
        if (userExists(sharedPreferences)) {
            fabProfile.show()
        } else {
            fabProfile.hide()
        }

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val weight = weightInput.text.toString()
            val height = heightInput.text.toString()
            val calorieGoal = calorieGoalInput.text.toString()

            if (name.isNotEmpty() && weight.isNotEmpty() && height.isNotEmpty() && calorieGoal.isNotEmpty()) {
                val editor = sharedPreferences.edit()
                editor.putString("name", name)
                editor.putInt("weight", weight.toInt())
                editor.putInt("height", height.toInt())
                editor.putInt("calorieGoal", calorieGoal.toInt())
                editor.apply()

                Toast.makeText(this, R.string.user_data_saved, Toast.LENGTH_SHORT).show()

                // Show the floating action button
                fabProfile.show()

                // Navigate to DailySummaryActivity (next step)
                val intent = Intent(this, DailySummaryActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, R.string.complete_fields, Toast.LENGTH_SHORT).show()
            }
        }

        // Listener for the floating action button to navigate to ProfileActivity
        fabProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun userExists(sharedPreferences: SharedPreferences): Boolean {
        val name = sharedPreferences.getString("name", null)
        val weight = sharedPreferences.getInt("weight", -1)
        val height = sharedPreferences.getInt("height", -1)
        val calorieGoal = sharedPreferences.getInt("calorieGoal", -1)

        return !name.isNullOrEmpty() && weight != -1 && height != -1 && calorieGoal != -1
    }
}