package org.example.data.datasource.remote.mongo.manager

import org.bson.Document
import org.example.data.datasource.remote.mongo.manager.base.UnEditableMongoManager
import org.example.data.utils.Parser
import org.example.domain.entity.log.Log

class LogsMongoManager(
    collectionPath: String,
    parser: Parser<Document, Log>
) : UnEditableMongoManager<Log>(collectionPath, parser)