package com.example.codeeditor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.codeeditor.database.CodeReviewSuggestionDao

class CodeReviewSuggestionDetailViewModelFactory(
    private val suggestionKey: String,
    private val dataSource: CodeReviewSuggestionDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodeReviewSuggestionDetailViewModel::class.java)) {
            return CodeReviewSuggestionDetailViewModel(suggestionKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}