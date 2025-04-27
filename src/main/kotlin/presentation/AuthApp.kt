package org.example.presentation

import org.example.presentation.utils.menus.AuthMenuItems

class AuthApp {
    fun run() {
        AuthMenuItems.entries.forEach { println(it.title) }
        val optionIndex = readln().toInt()
        val option = getAuthMenuItemByIndex(optionIndex)
        if (option == AuthMenuItems.EXIT) return
        option.execute()//LoginUiController
        run()
    }

    private fun getAuthMenuItemByIndex(input: Int) = AuthMenuItems.entries.getOrNull(input - 1) ?: AuthMenuItems.EXIT
}