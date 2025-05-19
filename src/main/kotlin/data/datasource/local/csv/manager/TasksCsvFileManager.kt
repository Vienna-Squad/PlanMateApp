package org.example.data.datasource.local.csv.manager

import org.example.data.datasource.local.bases.CsvFileManager
import org.example.data.datasource.local.bases.Parser
import org.example.domain.entity.Task

class TasksCsvFileManager(
    filePath: String,
    parser: Parser<Task>
) : CsvFileManager<Task>(filePath, parser) {
    override fun getItemId(item: Task) = item.id
}