import org.example.domain.entity.AddedLog
import org.example.domain.entity.ChangedLog
import org.example.domain.entity.CreatedLog
import org.example.domain.entity.DeletedLog
import org.example.domain.entity.Log
import org.example.domain.entity.Project
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import java.util.UUID

val dummyProjects = listOf(
    Project(
        name = "E-Commerce Platform",
        states = listOf("Backlog", "In Progress", "Testing", "Completed"),
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Social Media App",
        states = listOf("Idea", "Prototype", "Development", "Live"),
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Travel Booking System",
        states = listOf("Planned", "Building", "QA", "Release"),
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Food Delivery App",
        states = listOf("Todo", "In Progress", "Review", "Delivered"),
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Online Education Platform",
        states = listOf("Draft", "Content Ready", "Published"),
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Banking Mobile App",
        states = listOf("Requirements", "Design", "Development", "Testing", "Deployment"),
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Fitness Tracking App",
        states = listOf("Planned", "In Progress", "Completed"),
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Event Management System",
        states = listOf("Initiated", "Planning", "Execution", "Closure"),
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Online Grocery Store",
        states = listOf("Todo", "Picking", "Dispatch", "Delivered"),
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Real Estate Listing Site",
        states = listOf("Listing", "Viewing", "Negotiation", "Sold"),
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    )
)
val dummyProject = dummyProjects[5]
val dummyAdmin = User(
    username = "admin1",
    hashedPassword = "adminPass123",
    role = UserRole.ADMIN
)
val dummyMate = User(
    username = "mate1",
    hashedPassword = "matePass456",
    role = UserRole.MATE
)
val dummyTasks = listOf(
    Task(
        title = "Implement user authentication",
        state = "In Progress",
        assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Design database schema",
        state = "Done",
        assignedTo = listOf(UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Create API endpoints",
        state = "To Do",
        assignedTo = emptyList(),
        createdBy = dummyAdmin.id,
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Fix login bug",
        state = "Done",
        assignedTo = listOf(UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Optimize database queries",
        state = "To Do",
        assignedTo = emptyList(),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Deploy to staging",
        state = "In Progress",
        assignedTo = listOf(UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Update documentation",
        state = "To Do",
        assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Refactor legacy code",
        state = "In Progress",
        assignedTo = listOf(UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Add error logging",
        state = "Done",
        assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    )
)
val dummyLogs = listOf(
    CreatedLog(
        username = "admin1",
        affectedId = UUID.randomUUID().toString(),
        affectedType = Log.AffectedType.PROJECT
    ),
    AddedLog(
        username = "admin2",
        affectedId = UUID.randomUUID().toString(),
        affectedType = Log.AffectedType.MATE,
        addedTo = "project-${UUID.randomUUID()}"
    ),
    ChangedLog(
        username = "mate1",
        affectedId = UUID.randomUUID().toString(),
        affectedType = Log.AffectedType.TASK,
        changedFrom = "ToDo",
        changedTo = "In Progress"
    ),
    DeletedLog(
        username = "admin3",
        affectedId = UUID.randomUUID().toString(),
        affectedType = Log.AffectedType.STATE,
        deletedFrom = "project-${UUID.randomUUID()}"
    ),
    CreatedLog(
        username = "admin2",
        affectedId = UUID.randomUUID().toString(),
        affectedType = Log.AffectedType.TASK
    ),
    AddedLog(
        username = "admin1",
        affectedId = UUID.randomUUID().toString(),
        affectedType = Log.AffectedType.STATE,
        addedTo = "project-${UUID.randomUUID()}"
    ),
    ChangedLog(
        username = "mate2",
        affectedId = UUID.randomUUID().toString(),
        affectedType = Log.AffectedType.STATE,
        changedFrom = "New",
        changedTo = "ToDo"
    ),
    DeletedLog(
        username = "admin1",
        affectedId = UUID.randomUUID().toString(),
        affectedType = Log.AffectedType.MATE
    ),
    CreatedLog(
        username = "admin4",
        affectedId = UUID.randomUUID().toString(),
        affectedType = Log.AffectedType.STATE
    ),
    ChangedLog(
        username = "mate3",
        affectedId = UUID.randomUUID().toString(),
        affectedType = Log.AffectedType.TASK,
        changedFrom = "In Review",
        changedTo = "Done"
    )
)
