package es.uma.foodcalendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.uma.foodcalendar.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aquí puedes añadir la lógica para mostrar las estadísticas (implementaremos esto más adelante)
    }
}