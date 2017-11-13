package com.example.anwarshahriar.openup.persistance

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(@ColumnInfo(name = "ip_port") var ipPort: String,
                @ColumnInfo(name = "device_name") var name: String) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}