package org.example.data.datasource.local.csv.manager

import org.example.data.utils.Parser
import org.example.data.datasource.local.csv.manager.base.UnEditableCsvFileManager
import org.example.domain.entity.log.Log

class LogsCsvFileManager(
    filePath: String,
    parser: Parser<String, Log>
) : UnEditableCsvFileManager<Log>(filePath, parser)