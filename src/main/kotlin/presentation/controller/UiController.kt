package org.example.presentation.controller

import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemViewer

interface UiController {
    fun execute()
    fun tryAndShowError(
        exceptionViewer: ItemViewer<Throwable> = ExceptionViewer(),
        bloc: () -> Unit,
    ) {
        try {
            bloc()
        } catch (e: Throwable) {
            exceptionViewer.view(e)
        }
    }
}