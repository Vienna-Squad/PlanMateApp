package presentation.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.domain.UnknownException
import org.example.domain.usecase.project.EditProjectStatesUseCase
import org.example.domain.usecase.task.EditTaskStateUseCase
import org.example.presentation.controller.EditProjectStatesController
import org.example.presentation.controller.EditTaskStateController
import org.example.presentation.utils.interactor.Interactor
import org.example.presentation.utils.viewer.ExceptionViewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EditTaskStateControllerTest {
    private lateinit var editTaskStateUseCase: EditTaskStateUseCase
    private lateinit var interactor: Interactor<String>
    private lateinit var controller: EditTaskStateController
    private val exceptionViewer: ExceptionViewer = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        editTaskStateUseCase = mockk(relaxed = true)
        interactor = mockk(relaxed = true)
        controller = EditTaskStateController(
            editTaskStateUseCase, interactor,
            exceptionViewer,
        )
    }

   @Test
    fun `should execute use case with correct inputs`() {
        // given
        val taskId = "456"
        val newState = "completed"

        every { interactor.getInput() } returnsMany listOf(taskId, newState)

        // when
        controller.execute()

        // then
        verify { editTaskStateUseCase(taskId, newState) }
    }

    @Test
    fun `should handle exception and show error`() {
        // given
        val taskId = "456"
        val newState = "completed"
        val exception = UnknownException("Test Failed")

        every { interactor.getInput() } returnsMany listOf(taskId, newState)
        every { editTaskStateUseCase(any(), any()) } throws exception

        // when
        controller.execute()

        // then
        verify { exceptionViewer.view(exception) }
    }
}