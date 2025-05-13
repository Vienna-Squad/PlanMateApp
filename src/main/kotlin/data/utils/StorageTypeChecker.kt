package org.example.data.utils

import data.datasource.preferences.Preferences
import org.example.common.StorageType

fun isRemote(preferences: Preferences = Preferences) = preferences.getDataSourceType() == StorageType.REMOTE