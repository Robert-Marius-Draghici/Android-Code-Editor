package com.example.codeeditor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.codeeditor.database.CodeReviewSuggestion
import com.example.codeeditor.database.CodeReviewSuggestionDao

class CodeReviewSuggestionDetailViewModel(
    suggestionKey: String,
    dataSource: CodeReviewSuggestionDao
): ViewModel() {

    val database = dataSource
    private val suggestion: LiveData<CodeReviewSuggestion>

    fun getSuggestion() = suggestion

    init {
        suggestion = database.get(suggestionKey)
    }
}