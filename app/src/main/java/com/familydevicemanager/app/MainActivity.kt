package com.familydevicemanager.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.familydevicemanager.app.data.DeviceRepository
import com.familydevicemanager.app.data.DeviceStatus
import com.familydevicemanager.app.data.DeviceStatusUpdate
import com.familydevicemanager.app.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repository = DeviceRepository()
    private val db = Firebase.firestore

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            registerDevice()
        }

        binding.ringButton.setOnClickListener {
            sendCommand("ring")
        }

        binding.locationButton.setOnClickListener {
            sendCommand("location_request")
        }

        binding.statusButton.setOnClickListener {
            updateStatus()
        }

        binding.toggleNotificationButton.setOnClickListener {
            toggleSmsNotification()
        }

        requestNotificationPermissionIfNeeded()
        loadDemoStatus()
    }

    private fun loadDemoStatus() {
        binding.deviceNameText.text = "Demo Device"
        binding.statusText.text = "Ready for consent-based control"
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun registerDevice() {
        val deviceName = binding.deviceNameInput.text?.toString()?.trim().ifEmpty { "My Device" }
        val deviceId = "demo-device-001"
        val deviceStatus = DeviceStatus(
            deviceId = deviceId,
            deviceName = deviceName,
            ownerUid = "demo-user",
            platform = "android",
            battery = 87,
            status = "online",
            online = true,
            location = mapOf("lat" to 23.8103, "lng" to 90.4125),
            lastSeen = System.currentTimeMillis().toString(),
            smsNotificationsEnabled = true
        )

        repository.registerDevice(deviceStatus)
        binding.statusText.text = "Device registered: $deviceName"
        Toast.makeText(this, "Device registered", Toast.LENGTH_SHORT).show()
    }

    private fun sendCommand(type: String) {
        val commandId = "cmd_${System.currentTimeMillis()}"
        val command = DeviceStatusUpdate(
            commandId = commandId,
            deviceId = "demo-device-001",
            type = type,
            status = "pending",
            createdBy = "demo-user",
            createdAt = System.currentTimeMillis().toString()
        )

        repository.sendCommand(command)
        binding.statusText.text = "Command sent: $type"
        Toast.makeText(this, "Command queued", Toast.LENGTH_SHORT).show()
    }

    private fun updateStatus() {
        val status = DeviceStatus(
            deviceId = "demo-device-001",
            deviceName = binding.deviceNameInput.text?.toString()?.trim().ifEmpty { "My Device" },
            ownerUid = "demo-user",
            platform = "android",
            battery = 91,
            status = "online",
            online = true,
            location = mapOf("lat" to 23.8103, "lng" to 90.4125),
            lastSeen = System.currentTimeMillis().toString(),
            smsNotificationsEnabled = true
        )

        repository.updateDeviceStatus(status)
        binding.statusText.text = "Device status refreshed"
        Toast.makeText(this, "Status updated", Toast.LENGTH_SHORT).show()
    }

    private fun toggleSmsNotification() {
        val currentValue = binding.toggleNotificationButton.text.toString().contains("Disable")
        val newValue = !currentValue
        val status = DeviceStatus(
            deviceId = "demo-device-001",
            deviceName = binding.deviceNameInput.text?.toString()?.trim().ifEmpty { "My Device" },
            ownerUid = "demo-user",
            platform = "android",
            battery = 87,
            status = "online",
            online = true,
            location = mapOf("lat" to 23.8103, "lng" to 90.4125),
            lastSeen = System.currentTimeMillis().toString(),
            smsNotificationsEnabled = newValue
        )

        repository.updateDeviceStatus(status)
        binding.toggleNotificationButton.text = if (newValue) "Disable SMS Notifications" else "Enable SMS Notifications"
        binding.statusText.text = if (newValue) "SMS notifications enabled" else "SMS notifications disabled"
    }
}
