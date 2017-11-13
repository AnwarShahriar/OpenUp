package com.example.anwarshahriar.openup.persistance

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
public interface DeviceDao {

    @Query("SELECT * FROM devices")
    fun getAllDevices(): List<Device>

    @Insert
    fun insertDevice(device: Device)
}