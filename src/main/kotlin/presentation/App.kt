//
//package org.example.presentation
//
//import org.example.presentation.controller.*
//import org.koin.core.component.KoinComponent
//import org.koin.core.component.get
//
//abstract class App : KoinComponent {
//    protected abstract fun getMenuItems(): List<MenuItem>
//
//    fun run() {
//        val menuItems = getMenuItems()
//        menuItems.forEachIndexed { index, item ->
//            println("${index + 1}. ${item.title}")
//        }
//
//        print("enter your selection: ")
//        val selection = readln().toIntOrNull() ?: 0
//
//        if (selection > 0 && selection <= menuItems.size) {
//            try {
//                val selectedItem = menuItems[selection - 1]
//                selectedItem.action()
//            } catch (e: Exception) {
//                println("Error: ${e.message}")
//            }
//            run()
//        }
//    }
//
//    data class MenuItem(
//        val title: String,
//        val action: () -> Unit
//    )
//
//    // Helper method to get controllers from Koin when needed
//    protected inline fun <reified T : UiController> getController(): T {
//        return get()
//    }
//}
//
//// Auth App Implementation
//class AuthApp : App() {
//    override fun getMenuItems(): List<MenuItem> {
//        return listOf(
//            MenuItem("Log In") {
//                getController<LoginUiController>().execute()
//            },
//            MenuItem("Sign Up (Register New Account)") {
//                getController<RegisterUiController>().execute()
//            },
//            MenuItem("Exit Application") {
//                getController<ExitUiController>().execute()
//            }
//        )
//    }
//}
//
//class AdminApp : App() {
//    override fun getMenuItems(): List<MenuItem> {
//        return listOf(
//            MenuItem("Create New Project") {
//                getController<CreateProjectUiController>().execute()
//            },
//            MenuItem("Edit Project Name") {
//                getController<SoonUiController>().execute()
//            },
//            MenuItem("Add New State to Project") {
//                getController<AddStateToProjectUiController>().execute()
//            },
//            MenuItem("Remove State from Project") {
//                getController<SoonUiController>().execute()
//            },
//            MenuItem("Add Mate User to Project") {
//                getController<AddMateToProjectUiController>().execute()
//            },
//            MenuItem("Remove Mate User from Project") {
//                getController<SoonUiController>().execute()
//            },
//            MenuItem("Delete Project") {
//                getController<SoonUiController>().execute()
//            },
//            MenuItem("View All Tasks in Project") {
//                getController<GetAllTasksOfProjectController>().execute()
//            },
//            MenuItem("View Project Change History") {
//                getController<GetProjectHistoryUiController>().execute()
//            },
//            MenuItem("Create New Task") {
//                getController<CreateTaskUiController>().execute()
//            },
//            MenuItem("Edit Task Title") {
//                getController<EditTaskTitleUiController>().execute()
//            },
//            MenuItem("View Task Details") {
//                getController<GetTaskUiController>().execute()
//            },
//            MenuItem("View Task Change History") {
//                getController<GetTaskHistoryUIController>().execute()
//            },
//            MenuItem("Log Out") {
//                getController<LogoutUiController>().execute()
//            }
//        )
//    }
//}
//
//class MateApp : App() {
//    override fun getMenuItems(): List<MenuItem> {
//        return listOf(
//            MenuItem("View All Tasks in Project") {
//                getController<GetAllTasksOfProjectController>().execute()
//            },
//            MenuItem("View Project Change History") {
//                getController<GetProjectHistoryUiController>().execute()
//            },
//            MenuItem("Create New Task") {
//                getController<CreateTaskUiController>().execute()
//            },
//            MenuItem("Edit Task Title") {
//                getController<EditTaskTitleUiController>().execute()
//            },
//            MenuItem("View Task Details") {
//                getController<GetTaskUiController>().execute()
//            },
//            MenuItem("View Task Change History") {
//                getController<GetTaskHistoryUIController>().execute()
//            },
//            MenuItem("Log Out") {
//                getController<LogoutUiController>().execute()
//            }
//        )
//    }
//}
package org.example.presentation

import org.example.presentation.controller.*

abstract class App(val menuItems: List<MenuItem>) {
    fun run() {
        menuItems.forEachIndexed { index, option -> println("${index + 1}. ${option.title}") }
        print("enter your selection: ")
        getMenuItemByIndex(readln().toIntOrNull() ?: -1)?.let { option ->
            option.uiController.execute()
            run()
        } ?: return
    }

    private fun getMenuItemByIndex(input: Int) =
        if (input != menuItems.size) menuItems.getOrNull(input - 1) else null

    data class MenuItem(val title: String, val uiController: UiController = SoonUiController())
}

class AdminApp : App(
    menuItems = listOf(
        MenuItem("Create New Project",CreateProjectUiController()),
        MenuItem("Edit Project Name"),
        MenuItem("Add New State to Project",AddStateToProjectUiController()),
        MenuItem("Remove State from Project"),
        MenuItem("Add Mate User to Project", AddMateToProjectUiController()),
        MenuItem("Remove Mate User from Project"),
        MenuItem("Delete Project"),
        MenuItem("View All Tasks in Project", GetAllTasksOfProjectController()),
        MenuItem("View Project Change History"),
        MenuItem("Create New Task", CreateTaskUiController()),
        MenuItem("View Project Change History",GetProjectHistoryUiController()),
        MenuItem("Create New Task"),
        MenuItem("Delete Task"),
        MenuItem("Edit Task Details"),
        MenuItem("Edit Task Title ", EditTaskTitleUiController()),
        MenuItem("Edit Task Details", GetTaskUiController()),
        MenuItem("View Task Details"),
        MenuItem("View Task Change History",GetTaskHistoryUIController()),
        MenuItem("Log Out", LogoutUiController())

)
)
class AuthApp : App(
    menuItems = listOf(
        MenuItem("Log In", LoginUiController()),
        MenuItem("Sign Up (Register New Account),", RegisterUiController()),
        MenuItem("Exit Application",)
    )
)

class MateApp : App(
    menuItems = listOf(
        MenuItem("View All Tasks in Project", GetAllTasksOfProjectController()),
        MenuItem("View Project Change History"),
        MenuItem("Create New Task"),
        MenuItem("Delete Task"),
        MenuItem("Edit Task Details"),
        MenuItem("Edit Task Title ", EditTaskTitleUiController()),
        MenuItem("Edit Task Details",GetTaskUiController()),
        MenuItem("View Task Details"),
        MenuItem("View Task Change History"),
        MenuItem("Log Out", LogoutUiController())
    )
)
