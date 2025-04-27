package org.example.presentation

import org.example.presentation.utils.menus.MateMenuItem

class MateApp {
    fun run() {
        MateMenuItem.entries.forEach { println(it.title) }
        val optionIndex = readln().toInt()
        val option = getMateMenuItemByIndex(optionIndex)
        if (option == MateMenuItem.LOG_OUT) return
        option.execute()
        run()
    }

    private fun getMateMenuItemByIndex(input: Int) = MateMenuItem.entries.getOrNull(input - 1) ?: MateMenuItem.LOG_OUT
}