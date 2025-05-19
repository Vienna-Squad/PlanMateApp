package org.example.data.datasource.remote.mongo.manager

import org.bson.Document
import org.example.data.datasource.remote.mongo.manager.base.MongoManager
import org.example.data.utils.Parser
import org.example.domain.entity.User

class UsersMongoManager(
    collectionPath: String,
    parser: Parser<Document, User>
) : MongoManager<User>(collectionPath, parser) {
    override fun getItemId(item: User) = item.id
}