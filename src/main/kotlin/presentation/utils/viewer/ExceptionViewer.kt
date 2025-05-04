package org.example.presentation.utils.viewer

import org.example.domain.PlanMateAppException

class ExceptionViewer : ItemViewer<PlanMateAppException> {
    override fun view(item: PlanMateAppException) {
        println("\u001B[31m${item.message}\u001B[0m")
    }
}

class ExceptionViewerDemo : ItemViewer<Throwable> {
    override fun view(item: Throwable) {
        println("\u001B[31m${item.message}\u001B[0m")
    }
}