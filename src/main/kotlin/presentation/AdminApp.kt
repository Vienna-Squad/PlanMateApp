package org.example.presentation

import org.example.presentation.utils.menus.AdminMenuItem

class AdminApp {
    fun run() {
        AdminMenuItem.entries.forEachIndexed { index, option -> println("${index + 1}. ${option.title}") }
        val optionIndex = readln().toInt()
        val option = getAdminMenuItemByIndex(optionIndex)
        if (option == AdminMenuItem.LOG_OUT) return
        option.execute()
        run()
    }

    private fun getAdminMenuItemByIndex(input: Int) =
        AdminMenuItem.entries.getOrNull(input - 1) ?: AdminMenuItem.LOG_OUT
}