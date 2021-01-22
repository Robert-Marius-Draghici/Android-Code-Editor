package com.example.codeeditor.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.codeeditor.database.CodeReviewSuggestionDao

class CodeReviewViewModelFactory(
    private val dataSource: CodeReviewSuggestionDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodeReviewViewModel::class.java)) {
            return CodeReviewViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}