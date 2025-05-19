package org.example.data.datasource.local.csv.manager

import org.example.data.datasource.local.bases.Parser
import org.example.data.datasource.local.bases.UnEditableCsvFileManager
import org.example.domain.entity.log.Log

class LogsCsvFileManager(
    filePath: String,
    parser: Parser<Log>
) : UnEditableCsvFileManager<Log>(filePath, parser)