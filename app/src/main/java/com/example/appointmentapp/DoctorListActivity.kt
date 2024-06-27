package com.example.appointmentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DoctorListActivity : AppCompatActivity() {

    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list)

        val specialty = intent.getStringExtra("SPECIALTY")
        userLatitude = intent.getDoubleExtra("LATITUDE", 0.0)
        userLongitude = intent.getDoubleExtra("LONGITUDE", 0.0)

        if (specialty != null) {
            val doctors = findDoctorsBySpecialtyAndLocation(specialty)
            val recyclerViewDoctors: RecyclerView = findViewById(R.id.recyclerView_doctors)
            recyclerViewDoctors.layoutManager = LinearLayoutManager(this)
            recyclerViewDoctors.adapter = DoctorAdapter(doctors, userLatitude, userLongitude) { selectedDoctor ->
                val intent = Intent(this, AppointmentConfirmationActivity::class.java)
                intent.putExtra("DOCTOR_NAME", selectedDoctor.name)
                intent.putExtra("DOCTOR_LATITUDE", selectedDoctor.latitude)
                intent.putExtra("DOCTOR_LONGITUDE", selectedDoctor.longitude)
                intent.putExtra("USER_LATITUDE", userLatitude)
                intent.putExtra("USER_LONGITUDE", userLongitude)
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "Specialty not provided", Toast.LENGTH_SHORT).show()
        }
    }

    private fun findDoctorsBySpecialtyAndLocation(specialty: String): List<Doctor> {
        // Dummy data for doctors with locations
        val allDoctors = listOf(
            Doctor("Dr Abad khan - Cardiologist", 28.682589947763596, 77.41050115260498),
            Doctor("Dr.Navmeet - Cardiologist", 28.63022689805857, 77.43594837330197),
            Doctor("Dr. Ananya Singh - Cardiologist", 28.63070433094375, 77.43721972778118),
            Doctor("Dr. Anurag Vyas - Cardiologist", 28.661144019544754, 77.48446940204533),
            Doctor("Dr. Abhishek Singh - Cardiologist", 28.65665958427229, 77.47090786058985),
            Doctor("Dr. Puneet Malik - Neurologist ", 28.67322108874965, 77.4486667067486),
            Doctor("Dr.Rakesh's - Neurologist", 28.647192428275826, 77.41415974207607),
            Doctor("Dr. Shishir Pandey  - Neurologist", 28.608267296044268, 77.42531474972213),
            Doctor("Dr. DP Singh - Neurologist", 28.67372742947313, 77.42083832040032),
            Doctor("Dr. Iram Siraj - Pediatrician ", 28.69722593674082, 77.45204044408844),
            Doctor("Dr. Vandana Anand - Pediatrician, ", 28.678719555487508, 77.44872081601201),
            Doctor("Dr Mohit Ghai - Pediatrician, ", 28.627318579356032, 77.4375167525738),
            Doctor("Dr Manmeet sharma - Pediatrician, ", 28.626417816592788, 77.43366439589478),
            Doctor("Dr. Nidhi - Dentist",28.63423212635396, 77.43496897335214),
            Doctor("Dr Neeraj Kumar - Dentist",28.627820129328153, 77.43522162768103),
            Doctor("Dr.Chawla - Dentist",28.631174750350876, 77.43436305060997),
            Doctor("Dr. Arti Singh - Dentist",28.647830168114954, 77.51273092255767),
        )

        // Filter doctors by specialty and sort by distance from user location
        return allDoctors.filter { it.name.contains(specialty, ignoreCase = true) }
            .sortedBy { it.distanceTo(userLatitude, userLongitude) }
    }
}

data class Doctor(val name: String, val latitude: Double, val longitude: Double) {
    fun distanceTo(userLatitude: Double, userLongitude: Double): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(latitude, longitude, userLatitude, userLongitude, results)
        return results[0]
    }
}
