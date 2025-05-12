package org.example.data.datasource.csv.manager

import org.example.common.FilesPaths.PROJECTS_FILE_PATH
import org.example.common.bases.CsvFileManager
import org.example.common.bases.Parser
import org.example.data.datasource.csv.parser.ProjectParser
import org.example.domain.entity.Project

class ProjectsCsvFileManager(
    filePath: String = PROJECTS_FILE_PATH,
    parser: Parser<Project> = ProjectParser()
) : CsvFileManager<Project>(filePath,parser) {
    override fun getItemId(item: Project) = item.id
}