package org.example.presentation.utils.viewer

import org.example.domain.PlanMateAppException

class ExceptionViewerDemo : ItemDetailsViewer<PlanMateAppException> {
    override fun view(item: PlanMateAppException) {
        println("\u001B[31m$item\u001B[0m")
    }
}