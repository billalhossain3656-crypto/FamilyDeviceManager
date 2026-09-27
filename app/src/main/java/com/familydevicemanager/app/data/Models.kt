package com.familydevicemanager.app.data

import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class DeviceStatus(
    val deviceId: String = "",
    val deviceName: String = "",
    val ownerUid: String = "",
    val platform: String = "android",
    val battery: Int = 0,
    val status: String = "offline",
    val online: Boolean = false,
    val location: Map<String, Any> = emptyMap(),
    val lastSeen: String = "",
    val smsNotificationsEnabled: Boolean = true
)

data class DeviceStatusUpdate(
    val commandId: String = "",
    val deviceId: String = "",
    val type: String = "",
    val status: String = "pending",
    val createdBy: String = "",
    val createdAt: String = ""
)

class DeviceRepository {
    private val db = Firebase.firestore

    fun registerDevice(device: DeviceStatus) {
        db.collection("devices")
            .document(device.deviceId)
            .set(device, SetOptions.merge())
    }

    fun updateDeviceStatus(device: DeviceStatus) {
        db.collection("devices")
            .document(device.deviceId)
            .set(device, SetOptions.merge())
    }

    fun sendCommand(command: DeviceStatusUpdate) {
        db.collection("commands")
            .document(command.commandId)
            .set(command)
    }
}
