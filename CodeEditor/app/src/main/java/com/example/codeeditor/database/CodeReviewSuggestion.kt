package com.example.codeeditor.database

import androidx.room.*
import com.example.codeeditor.domain.CodeReviewSuggestionModel
import com.example.codeeditor.network.CommitFix
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


@Entity(tableName = "code_review_suggestion")
data class CodeReviewSuggestion (
    @PrimaryKey(autoGenerate = false)
    val id: String = "",

    @ColumnInfo(name = "suggestion_id")
    val suggestionId: String = "",

    @ColumnInfo(name = "code")
    val code: String = "",

    @ColumnInfo(name = "rule")
    val rule: String = "",

    @ColumnInfo(name = "suggestion_message")
    val suggestionMessage: String = "",

    @ColumnInfo(name = "severity")
    val severity: Int = -1,

    @ColumnInfo(name = "categories")
    val categories: List<String> = emptyList(),

    @ColumnInfo(name = "example_commit_fixes")
    val exampleCommitFixes: List<CommitFix> = emptyList()
)

class CategoriesConverter {
    @TypeConverter
    fun stringsToList(value: String): List<String> {
        return value.split("\\s*,\\s*")
    }

    @TypeConverter
    fun listToString(categories: List<String>): String {
        var value = ""
        for (category in categories)
            value += "$category,"
        return value
    }
}

class CommitFixesConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToCommitFixes(data: String?): List<CommitFix?>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType: Type = object : TypeToken<List<CommitFix?>?>() {}.type
        return gson.fromJson<List<CommitFix?>>(data, listType)
    }

    @TypeConverter
    fun commitFixesToString(commitFixes: List<CommitFix?>?): String? {
        return gson.toJson(commitFixes)
    }
}

/**
 * Map CodeReviewSuggestion to domain entity.
 */
fun List<CodeReviewSuggestion>.asDomainModel(): List<CodeReviewSuggestionModel> {
    return map {
        CodeReviewSuggestionModel(
            code = it.code,
            suggestionId = it.suggestionId,
            rule = it.rule,
            suggestionMessage = it.suggestionMessage,
            severity = it.severity,
            categories = it.categories,
            exampleCommitFixes = it.exampleCommitFixes
        )
    }
}
