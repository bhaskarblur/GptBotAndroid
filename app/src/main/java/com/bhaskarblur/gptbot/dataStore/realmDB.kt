package com.bhaskarblur.gptbot.dataStore

import com.bhaskarblur.gptbot.models.realmMessageModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class realmDB {

    companion object {
        @Volatile
        private var instance: Realm? = null;

        fun getInstance(): Realm {
            val config = RealmConfiguration.Builder(schema = setOf(realmMessageModel::class))
                .deleteRealmIfMigrationNeeded()
                .compactOnLaunch()
                .build()
            return instance ?: synchronized(this) {
                instance ?: Realm.also { instance = Realm.open(config) }
                return Realm.open(config);
            }
        }

        fun close() {
            instance?.close()
        }
    }
}