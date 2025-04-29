package data.storage

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

abstract class CsvStorage<T>(private val filePath: String) : Storage<T> {

    init {
        createFileIfNotExists()
    }

    private fun createFileIfNotExists() {
        val path = Paths.get(filePath)
        if (!Files.exists(path)) {
            path.parent?.let { Files.createDirectories(it) }
            Files.createFile(path)
            writeHeader()
        }
    }

    protected abstract fun writeHeader()

    protected abstract fun serialize(item: T): String
    protected abstract fun deserialize(line: String): T

    override fun write(list: List<T>) {
        val content = buildString {
            appendLine(getHeaderLine())
            list.forEach { appendLine(serialize(it)) }
        }

        File(filePath).writeText(content)
    }

    override fun read(): List<T> {
        val file = File(filePath)
        if (!file.exists() || file.length() == 0L) return emptyList()

        return file.readLines()
            .drop(1) // Skip header line
            .filter { it.isNotBlank() }
            .map { deserialize(it) }
    }

    override fun append(item: T) {
        File(filePath).appendText("${serialize(item)}\n")
    }

    protected fun writeToFile(content: String) {
        File(filePath).writeText(content)
    }

    private fun getHeaderLine(): String {
        val file = File(filePath)
        return if (file.exists() && file.length() > 0) {
            file.readLines().firstOrNull() ?: ""
        } else {
            // Generate a new header if file doesn't exist or is empty
            val tempWriter = StringBuffer()
            writeHeader()
            val lines = File(filePath).readLines()
            if (lines.isNotEmpty()) lines[0] else ""
        }
    }
}