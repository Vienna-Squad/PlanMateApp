package data.storage.bases

interface Storage<T> {
    fun read(): List<T>
    fun append(item: T)
}