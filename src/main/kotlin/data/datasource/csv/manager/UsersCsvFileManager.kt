package org.example.data.datasource.csv.manager

import org.example.common.FilesPaths.USERS_FILE_PATH
import org.example.common.bases.CsvFileManager
import org.example.common.bases.CsvParser
import org.example.data.datasource.csv.parser.UserCsvParser
import org.example.domain.entity.User

class UsersCsvFileManager(
    filePath: String = USERS_FILE_PATH,
    parser: CsvParser<User> = UserCsvParser()
) : CsvFileManager<User>(filePath,parser) {
    override fun getItemId(item: User) = item.id
}