package es.uma.foodcalendar

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class ProfileActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
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

        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        userName.text = sharedPreferences.getString("name", getString(R.string.user_name))
        userHeight.text = getString(R.string.user_height, sharedPreferences.getInt("height", 175))
        userWeight.setText(sharedPreferences.getInt("weight", 70).toString())
        userGoal.setText(sharedPreferences.getInt("calorieGoal", 2000).toString())

        // Guardar el nuevo objetivo de calorías
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            val newWeight = userWeight.text.toString().toInt()
            val newGoal = userGoal.text.toString().toInt()

            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.confirmation)
            builder.setMessage(R.string.confirm_save_data)
            builder.setPositiveButton(R.string.yes) { dialog, which ->
                val editor = sharedPreferences.edit()
                editor.putInt("weight", newWeight)
                editor.putInt("calorieGoal", newGoal)
                editor.apply()

                Toast.makeText(this, R.string.calorie_goal_updated, Toast.LENGTH_SHORT).show()
                recreate() // Recargar la actividad actual
            }
            builder.setNegativeButton(R.string.no) { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
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