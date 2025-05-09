package org.example.presentation.utils.viewer

class ExceptionViewer : ItemViewer<Throwable> {
    override fun view(item: Throwable) {
        println("\u001B[31m${item.message}\u001B[0m")
    }
}