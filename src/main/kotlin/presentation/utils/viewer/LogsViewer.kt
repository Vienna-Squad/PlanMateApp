package org.example.presentation.utils.viewer

import org.example.domain.entity.Log

class LogsViewer : ItemsViewer<Log> {
    override fun view(items: List<Log>) {
        items.forEach { println(it) }
    }
}