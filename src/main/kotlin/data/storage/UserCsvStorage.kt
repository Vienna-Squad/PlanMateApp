package data.storage

import org.example.domain.entity.User
import org.example.domain.entity.UserType
import java.time.LocalDateTime

class UserCsvStorage(filePath: String) : CsvStorage<User>(filePath) {

    override fun writeHeader() {
        writeToFile("id,username,password,type,createdAt\n")
    }

    override fun serialize(item: User): String {
        return "${item.id},${item.username},${item.password},${item.type},${item.cratedAt}"
    }

    override fun deserialize(line: String): User {
        val parts = line.split(",", limit = 5)
        require(parts.size == 5) { "Invalid user data format: $line" }

        val user = User(
            username = parts[1],
            password = parts[2],
            type = UserType.valueOf(parts[3])
        )

        // We need to set the ID and createdAt fields since they are generated in constructor
        setPrivateField(user, "id", parts[0])
        setPrivateField(user, "cratedAt", LocalDateTime.parse(parts[4]))

        return user
    }

    // todo : i make it because i need it in deserialize because the field are private
    //  (i want to remove it but when the user model updated and the field become accessible)
    private fun setPrivateField(obj: Any, fieldName: String, value: Any) {
        val field = obj.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(obj, value)
        field.isAccessible = false
    }
}