package org.example.presentation.utils.interactor

class StringInputReader : InputReader<String> {
    override fun getInput(): String {
        return readln()
    }
}