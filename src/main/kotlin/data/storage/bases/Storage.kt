package data.storage

interface Storage<T> {
    fun read(): List<T>
    fun append(item: T)
}