import org.example.domain.entity.Project
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import java.util.UUID

val dummyProjects = listOf(
    Project(
        name = "E-Commerce Platform",
        states = listOf("Backlog", "In Progress", "Testing", "Completed"),
        createdBy = UUID.fromString("admin1"),
        matesIds = listOf("mate1", "mate2", "mate3").map { UUID.fromString(it) }
    ),
    Project(
        name = "Social Media App",
        states = listOf("Idea", "Prototype", "Development", "Live"),
        createdBy = UUID.fromString("admin2"),
        matesIds = listOf("mate4", "mate5").map { UUID.fromString(it) }
    ),
    Project(
        name = "Travel Booking System",
        states = listOf("Planned", "Building", "QA", "Release"),
        createdBy = UUID.fromString("admin2"),
        matesIds = listOf("mate1", "mate6").map { UUID.fromString(it) }
    ),
    Project(
        name = "Food Delivery App",
        states = listOf("Todo", "In Progress", "Review", "Delivered"),
        createdBy = UUID.fromString("admin3"),
        matesIds = listOf("mate7", "mate8").map { UUID.fromString(it) }
    ),
    Project(
        name = "Online Education Platform",
        states = listOf("Draft", "Content Ready", "Published"),
        createdBy = UUID.fromString("admin2"),
        matesIds = listOf("mate2", "mate9").map { UUID.fromString(it) }
    ),
    Project(
        name = "Banking Mobile App",
        states = listOf("Requirements", "Design", "Development", "Testing", "Deployment"),
        createdBy = UUID.fromString("admin4"),
        matesIds = listOf("mate10", "mate3").map { UUID.fromString(it) }
    ),
    Project(
        name = "Fitness Tracking App",
        states = listOf("Planned", "In Progress", "Completed"),
        createdBy = UUID.fromString("admin1"),
        matesIds = listOf("mate5", "mate7").map { UUID.fromString(it) }
    ),
    Project(
        name = "Event Management System",
        states = listOf("Initiated", "Planning", "Execution", "Closure"),
        createdBy = UUID.fromString("admin5"),
        matesIds = listOf("mate8", "mate9").map { UUID.fromString(it) }
    ),
    Project(
        name = "Online Grocery Store",
        states = listOf("Todo", "Picking", "Dispatch", "Delivered"),
        createdBy = UUID.fromString("admin3"),
        matesIds = listOf("mate1", "mate4").map { UUID.fromString(it) }
    ),
    Project(
        name = "Real Estate Listing Site",
        states = listOf("Listing", "Viewing", "Negotiation", "Sold"),
        createdBy = UUID.fromString("admin4"),
        matesIds = listOf("mate6", "mate10").map { UUID.fromString(it) }
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