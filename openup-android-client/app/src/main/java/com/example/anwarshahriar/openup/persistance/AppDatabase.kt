package com.example.anwarshahriar.openup.persistance

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(Device::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao
}