package org.example.data.datasource.local.csv.manager

import org.example.data.datasource.local.bases.CsvFileManager
import org.example.data.datasource.local.bases.Parser
import org.example.domain.entity.Project

class ProjectsCsvFileManager(
    filePath: String,
    parser: Parser<Project>
) : CsvFileManager<Project>(filePath, parser) {
    override fun getItemId(item: Project) = item.id
}