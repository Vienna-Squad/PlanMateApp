package org.example.data.datasource.local.csv.manager

import org.example.data.utils.Parser
import org.example.data.datasource.local.csv.manager.base.CsvFileManager
import org.example.domain.entity.Project

class ProjectsCsvFileManager(
    filePath: String,
    parser: Parser<String, Project>
) : CsvFileManager<Project>(filePath, parser) {
    override fun getItemId(item: Project) = item.id
}