package org.example.data.datasource.csv.manager

import org.example.common.FilesPaths.TASKS_FILE_PATH
import org.example.common.bases.CsvFileManager
import org.example.common.bases.CsvParser
import org.example.data.datasource.csv.parser.TaskCsvParser
import org.example.domain.entity.Task

class TasksCsvFileManager(
    filePath: String = TASKS_FILE_PATH,
    parser: CsvParser<Task> = TaskCsvParser()
) : CsvFileManager<Task>(filePath,parser) {
    override fun getItemId(item: Task) = item.id
}