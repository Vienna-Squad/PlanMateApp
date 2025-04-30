package org.example.presentation.controller

import org.example.domain.PlanMateAppException
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemViewer

interface UiController {
    fun execute()
    fun tryAndShowError(exceptionViewer: ItemViewer<PlanMateAppException> = ExceptionViewer(), bloc: () -> Unit) {
        try {
            bloc()
        } catch (exception: PlanMateAppException) {
            exceptionViewer.view(exception)
        }
    }
}