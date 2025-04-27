package org.example.presentation

import org.example.presentation.utils.menus.AdminMenuItem

class AdminApp {
    fun run() {
        AdminMenuItem.entries.forEach { println(it.title) }
        val optionIndex = readln().toInt()
        val option = getAdminMenuItemByIndex(optionIndex)
        if (option == AdminMenuItem.EXIT) return
        option.execute()
        run()
    }

    private fun getAdminMenuItemByIndex(input: Int) = AdminMenuItem.entries.getOrNull(input - 1) ?: AdminMenuItem.EXIT
}