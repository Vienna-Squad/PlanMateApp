package org.example.presentation

import org.example.presentation.utils.menus.AuthMenuItems

class AuthApp {
    fun run() {
        AuthMenuItems.entries.forEachIndexed { index, option -> println("${index + 1}. ${option.title}") }
        val optionIndex = readln().toInt()
        val option = getAuthMenuItemByIndex(optionIndex)
        if (option == AuthMenuItems.EXIT) return
        option.execute()
        run()
    }

    private fun getAuthMenuItemByIndex(input: Int) = AuthMenuItems.entries.getOrNull(input - 1) ?: AuthMenuItems.EXIT
}