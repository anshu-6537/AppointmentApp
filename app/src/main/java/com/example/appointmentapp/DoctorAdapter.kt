package com.example.appointmentapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DoctorAdapter(
    private val doctors: List<Doctor>,
    private val userLatitude: Double,
    private val userLongitude: Double,
    private val onItemClick: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    class DoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textView_doctor_name)
        val distanceTextView: TextView = view.findViewById(R.id.textView_doctor_distance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.nameTextView.text = doctor.name
        holder.distanceTextView.text = "Distance: %.2f km".format(doctor.distanceTo(userLatitude, userLongitude) / 1000)
        holder.itemView.setOnClickListener { onItemClick(doctor) }
    }

    override fun getItemCount() = doctors.size
}
