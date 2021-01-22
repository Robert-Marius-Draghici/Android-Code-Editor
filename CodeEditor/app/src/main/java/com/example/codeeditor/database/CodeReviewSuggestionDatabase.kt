package com.example.codeeditor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CodeReviewSuggestion::class], version = 1, exportSchema = false)
@TypeConverters(CategoriesConverter::class, CommitFixesConverter::class)
abstract class CodeReviewSuggestionDatabase : RoomDatabase() {

    abstract val codeReviewSuggestionDao: CodeReviewSuggestionDao

    companion object {

        @Volatile
        private var INSTANCE: CodeReviewSuggestionDatabase? = null

        fun getInstance(context: Context): CodeReviewSuggestionDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CodeReviewSuggestionDatabase::class.java,
                        "code_review_history_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}