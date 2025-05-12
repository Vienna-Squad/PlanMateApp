package org.example.data.config

enum class StorageType {
    LOCAL,
    REMOTE
}

object StorageConfig {
    var currentStorageType: StorageType = StorageType.REMOTE
}