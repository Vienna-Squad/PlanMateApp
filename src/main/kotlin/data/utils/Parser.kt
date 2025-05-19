package org.example.data.utils

interface Parser<Serialized, Deserialized> {
    fun serialize(item: Deserialized): Serialized
    fun deserialize(item: Serialized): Deserialized
}