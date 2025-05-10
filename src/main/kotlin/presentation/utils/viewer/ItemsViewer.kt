package org.example.presentation.utils.viewer

interface ItemsViewer<T> {
    fun view(items: List<T>)
}