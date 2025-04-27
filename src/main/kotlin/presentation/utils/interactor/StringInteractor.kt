package org.example.presentation.utils.interactor

class StringInteractor : Interactor<String> {
    override fun getInput(): String {
        return readln()
    }
}