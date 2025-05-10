import org.example.domain.entity.log.Log
import org.example.domain.entity.Project
import org.example.domain.entity.State
import org.example.domain.entity.Task
import org.example.domain.entity.User
import org.example.domain.entity.log.AddedLog
import org.example.domain.entity.log.ChangedLog
import org.example.domain.entity.log.CreatedLog
import org.example.domain.entity.log.DeletedLog
import java.util.UUID

val dummyProjects = listOf(
    Project(
        name = "E-Commerce Platform",
        states = listOf("Backlog", "In Progress", "Testing", "Completed").map { State(name = it) },
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Social Media App",
        states = listOf("Idea", "Prototype", "Development", "Live").map { State(name = it) },
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Travel Booking System",
        states = listOf("Planned", "Building", "QA", "Release").map { State(name = it) },
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Food Delivery App",
        states = listOf("Todo", "In Progress", "Review", "Delivered").map { State(name = it) },
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Online Education Platform",
        states = listOf("Draft", "Content Ready", "Published").map { State(name = it) },
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Banking Mobile App",
        states = listOf("Requirements", "Design", "Development", "Testing", "Deployment").map { State(name = it) },
        createdBy = UUID.randomUUID(),
        matesIds = List(7) { UUID.randomUUID() }
    ),
    Project(
        name = "Fitness Tracking App",
        states = listOf("Planned", "In Progress", "Completed").map { State(name = it) },
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Event Management System",
        states = listOf("Initiated", "Planning", "Execution", "Closure").map { State(name = it) },
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Online Grocery Store",
        states = listOf("Todo", "Picking", "Dispatch", "Delivered").map { State(name = it) },
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    ),
    Project(
        name = "Real Estate Listing Site",
        states = listOf("Listing", "Viewing", "Negotiation", "Sold").map { State(name = it) },
        createdBy = UUID.randomUUID(),
        matesIds = List(3) { UUID.randomUUID() }
    )
)
val dummyProject = dummyProjects[5]
val dummyAdmin = User(
    username = "admin1",
    hashedPassword = "adminPass123",
    role = User.UserRole.ADMIN
)
val dummyMate = User(
    username = "mate1",
    hashedPassword = "matePass456",
    role = User.UserRole.MATE
)
val dummyTasks = listOf(
    Task(
        title = "Implement user authentication",
        state = State(name = "In Progress"),
        assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Design database schema",
        state = State(name = "Done"),
        assignedTo = listOf(UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Create API endpoints",
        state = State(name = "To Do"),
        assignedTo = emptyList(),
        createdBy = dummyAdmin.id,
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Fix login bug",
        state = State(name = "Done"),
        assignedTo = listOf(UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Optimize database queries",
        state = State(name = "To Do"),
        assignedTo = emptyList(),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Deploy to staging",
        state = State(name = "In Progress"),
        assignedTo = listOf(UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Update documentation",
        state = State(name = "To Do"),
        assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Refactor legacy code",
        state = State(name = "In Progress"),
        assignedTo = listOf(UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    ),
    Task(
        title = "Add error logging",
        state = State(name = "Done"),
        assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
        createdBy = UUID.randomUUID(),
        projectId = UUID.randomUUID()
    )
)
val dummyLogs = listOf(
    CreatedLog(
        username = "admin1",
        affectedId = UUID.randomUUID(),
        affectedName = "Project A",
        affectedType = Log.AffectedType.PROJECT
    ),
    AddedLog(
        username = "admin2",
        affectedId = UUID.randomUUID(),
        affectedName = "Mate X",
        affectedType = Log.AffectedType.MATE,
        addedTo = "Project A"
    ),
    ChangedLog(
        username = "mate1",
        affectedId = UUID.randomUUID(),
        affectedName = "Task T-123",
        affectedType = Log.AffectedType.TASK,
        changedFrom = "ToDo",
        changedTo = "In Progress"
    ),
    DeletedLog(
        username = "admin3",
        affectedId = UUID.randomUUID(),
        affectedName = "State S-001",
        affectedType = Log.AffectedType.STATE,
        deletedFrom = "Project B"
    ),
    CreatedLog(
        username = "admin2",
        affectedId = UUID.randomUUID(),
        affectedName = "Task T-555",
        affectedType = Log.AffectedType.TASK
    ),
    AddedLog(
        username = "admin1",
        affectedId = UUID.randomUUID(),
        affectedName = "State S-999",
        affectedType = Log.AffectedType.STATE,
        addedTo = "Project C"
    ),
    ChangedLog(
        username = "mate2",
        affectedId = UUID.randomUUID(),
        affectedName = "State S-123",
        affectedType = Log.AffectedType.STATE,
        changedFrom = "New",
        changedTo = "ToDo"
    ),
    DeletedLog(
        username = "admin1",
        affectedId = UUID.randomUUID(),
        affectedName = "Mate Z",
        affectedType = Log.AffectedType.MATE
    ),
    CreatedLog(
        username = "admin4",
        affectedId = UUID.randomUUID(),
        affectedName = "State S-789",
        affectedType = Log.AffectedType.STATE
    ),
    ChangedLog(
        username = "mate3",
        affectedId = UUID.randomUUID(),
        affectedName = "Task T-999",
        affectedType = Log.AffectedType.TASK,
        changedFrom = "In Review",
        changedTo = "Done"
    )
)

val dummyTask = Task(
    title = "Implement user authentication",
    state = State(name = "In Progress"),
    assignedTo = listOf(UUID.randomUUID(), UUID.randomUUID()),
    createdBy = UUID.randomUUID(),
    projectId = UUID.randomUUID()
)
val dummyProjectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
val dummyMateId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")
val dummyState = "done"


