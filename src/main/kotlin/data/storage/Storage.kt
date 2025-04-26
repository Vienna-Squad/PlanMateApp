package data.storage

interface Storage<T> {
    fun write(list: List<T>)
    fun read(): List<T>
    fun append(item: T)
}