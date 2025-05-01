package data

import java.io.File

object TestUtils {
    fun createTempFile(prefix: String, suffix: String): String {
        val tempFile = File.createTempFile(prefix, suffix)
        tempFile.deleteOnExit()
        return tempFile.absolutePath
    }

    fun cleanupFile(file: File) {
        if (file.exists()) {
            file.delete()
        }
    }
}