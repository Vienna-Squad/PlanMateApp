package org.example.presentation.controller

import org.example.domain.*
import org.example.presentation.utils.viewer.ExceptionViewer
import org.example.presentation.utils.viewer.ItemViewer
import kotlin.io.AccessDeniedException

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