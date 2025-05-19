package org.example.data.datasource.local.csv.manager

import org.example.data.utils.Parser
import org.example.data.datasource.local.csv.manager.base.CsvFileManager
import org.example.domain.entity.Task

class TasksCsvFileManager(
    filePath: String,
    parser: Parser<String, Task>
) : CsvFileManager<Task>(filePath, parser) {
    override fun getItemId(item: Task) = item.id
}