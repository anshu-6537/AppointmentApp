package com.example.appointmentapp

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RemoteViews
import android.widget.TextView

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

class AppointmentConfirmationActivity : AppCompatActivity() {

    private var doctorLatitude: Double = 0.0
    private var doctorLongitude: Double = 0.0
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0
    lateinit var selectedDateTimeTextView: TextView
    lateinit var selectDateTimeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_confirmation)
        selectedDateTimeTextView=findViewById(R.id.textView_selectedDateTime)
        selectDateTimeButton= findViewById(R.id.button_selectDateTime)

        val doctorName = intent.getStringExtra("DOCTOR_NAME")
        doctorLatitude = intent.getDoubleExtra("DOCTOR_LATITUDE", 0.0)
        doctorLongitude = intent.getDoubleExtra("DOCTOR_LONGITUDE", 0.0)
        userLatitude = intent.getDoubleExtra("USER_LATITUDE", 0.0)
        userLongitude = intent.getDoubleExtra("USER_LONGITUDE", 0.0)

//        val textViewDoctorName: TextView = findViewById(R.id.textView_doctor_name)
        val buttonViewMap: Button = findViewById(R.id.button_view_map)

//        textViewDoctorName.text = doctorName

        buttonViewMap.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=$doctorLatitude,$doctorLongitude(Doctor's+Location)")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

            // Check if there is an application that can handle the intent
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                // Optionally, provide feedback to the user if Google Maps is not installed
                Toast.makeText(this, "Google Maps not installed", Toast.LENGTH_SHORT).show()
            }
        }



        selectDateTimeButton.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                showTimePicker(selectedDate.timeInMillis)
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // Limit date range to within a week (7 days)
        calendar.add(Calendar.DAY_OF_MONTH, 7)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun showTimePicker(selectedDateInMillis: Long) {
        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.timeInMillis = selectedDateInMillis
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)

                val appointmentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(Date(selectedTime.timeInMillis))

                selectedDateTimeTextView.text = appointmentDateTime
                scheduleAppointment(selectedTime.timeInMillis)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false // 24-hour format
        )

        timePickerDialog.show()
    }
    private fun scheduleAppointment(appointmentTimeInMillis: Long) {
        // Implement logic to schedule appointment (e.g., save to database or send to server)
        val appointmentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(Date(appointmentTimeInMillis))

        Toast.makeText(this, "Appointment scheduled for $appointmentDateTime", Toast.LENGTH_SHORT)
            .show()

        //  !!!!    NOTIFICATION   !!!!

    }
}

