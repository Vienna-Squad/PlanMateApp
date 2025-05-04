package org.example.presentation.controller

import org.example.presentation.utils.viewer.ExceptionViewerDemo
import org.example.presentation.utils.viewer.ItemViewer

interface UiController {
    fun execute()
    fun tryAndShowError(
        exceptionViewer: ItemViewer<Throwable> = ExceptionViewerDemo(),
        bloc: () -> Unit,
    ) {
        try {
            bloc()
        } catch (e: Throwable) {
            exceptionViewer.view(e)
        }
    }
}