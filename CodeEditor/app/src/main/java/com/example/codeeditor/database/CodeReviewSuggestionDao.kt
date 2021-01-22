package com.example.codeeditor.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CodeReviewSuggestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(codeReviewSuggestion: CodeReviewSuggestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( codeReviewSuggestions: List<CodeReviewSuggestion>) : List<Long>

    @Update
    suspend fun update(codeReviewSuggestion: CodeReviewSuggestion)

    @Query("SELECT * from code_review_suggestion WHERE id = :key")
    fun get(key: String): LiveData<CodeReviewSuggestion>

    @Query("SELECT * FROM code_review_suggestion")
    fun getAllSuggestions(): List<CodeReviewSuggestion>
}