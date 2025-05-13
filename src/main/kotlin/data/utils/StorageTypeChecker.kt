package org.example.data.utils

import data.datasource.local.preferences.Preferences
import org.example.StorageType

fun isRemote(preferences: Preferences = Preferences) = preferences.getDataSourceType() == StorageType.REMOTE