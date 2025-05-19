package org.example.data.datasource.local.csv.manager

import org.example.data.utils.Parser
import org.example.data.datasource.local.csv.manager.base.CsvFileManager
import org.example.domain.entity.User

class UsersCsvFileManager(
    filePath: String,
    parser: Parser<String, User>
) : CsvFileManager<User>(filePath, parser) {
    override fun getItemId(item: User) = item.id
}