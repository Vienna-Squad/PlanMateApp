package org.example.data.datasource.local.csv.manager

import org.example.data.datasource.local.bases.CsvFileManager
import org.example.data.datasource.local.bases.Parser
import org.example.domain.entity.User

class UsersCsvFileManager(
    filePath: String,
    parser: Parser<User>
) : CsvFileManager<User>(filePath, parser) {
    override fun getItemId(item: User) = item.id
}