package es.uma.foodcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Enable the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtener datos del usuario y mostrarlos
        val userName = findViewById<TextView>(R.id.user_name)
        val userWeight = findViewById<TextView>(R.id.user_weight)
        val userHeight = findViewById<TextView>(R.id.user_height)
        val userGoal = findViewById<EditText>(R.id.user_goal)

        // Aquí deberías obtener los datos del usuario desde tu base de datos o SharedPreferences
        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        userName.text = sharedPreferences.getString("name", "Nombre del Usuario")
        userWeight.text = "Peso: ${sharedPreferences.getInt("weight", 70)}kg"
        userHeight.text = "Altura: ${sharedPreferences.getInt("height", 175)}cm"
        userGoal.setText(sharedPreferences.getInt("calorieGoal", 2000).toString())

        // Guardar el nuevo objetivo de calorías
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            val newGoal = userGoal.text.toString().toInt()
            val editor = sharedPreferences.edit()
            editor.putInt("calorieGoal", newGoal)
            editor.apply()

            Toast.makeText(this, "Objetivo de calorías actualizado", Toast.LENGTH_SHORT).show()
            finish() // Return to the previous activity
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button action
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}