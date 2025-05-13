package org.example.presentation.utils.viewer

class ExceptionViewer : ItemViewer<Throwable> {
    override fun view(item: String) {
        println("\u001B[31m${item}\u001B[0m")
    }
}