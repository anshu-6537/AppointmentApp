package com.example.appointmentapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationRequestCode = 1000
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardio: ImageButton=findViewById(R.id.cardio)
        val neuro: ImageButton=findViewById(R.id.neuro)
        val pedia: ImageButton=findViewById(R.id.Pediatrician)
        val ortho: ImageButton=findViewById(R.id.Ortho)
        val dental : ImageButton=findViewById(R.id.Dento)
        var docCat=""
        cardio.setOnClickListener {
            docCat="Cardiologist"
        }
        neuro.setOnClickListener {
            docCat="Neurologist"
        }
        pedia.setOnClickListener {
            docCat="Pediatrician"
        }
        ortho.setOnClickListener {
            docCat="Orthopedist"
        }
        dental.setOnClickListener {
            docCat="Dentist"
        }
//        val spinnerSpecialty: Spinner = findViewById(R.id.spinner_specialty)
        val buttonFindDoctors: Button = findViewById(R.id.button_find_doctors)

        // Populate specialties
        val specialties = listOf("Cardiologist", "Dermatologist", "Pediatrician", "General Practitioner", "Orthopedist")
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, specialties)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerSpecialty.adapter = adapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationRequestCode)
        } else {
            getCurrentLocation()
        }

        buttonFindDoctors.setOnClickListener {

            findDoctorsBySpecialty(docCat)
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener(OnCompleteListener<Location> { task ->
            if (task.isSuccessful && task.result != null) {
                val location: Location? = task.result
                location?.let {
                    userLatitude = it.latitude
                    userLongitude = it.longitude
                    Toast.makeText(this, "Your Current Coordinates: $userLatitude, $userLongitude", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun findDoctorsBySpecialty(specialty: String) {
        val intent = Intent(this, DoctorListActivity::class.java)
        intent.putExtra("SPECIALTY", specialty)
        intent.putExtra("LATITUDE", userLatitude)
        intent.putExtra("LONGITUDE", userLongitude)
        startActivity(intent)
    }
}
