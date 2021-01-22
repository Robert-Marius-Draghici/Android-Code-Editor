package com.example.codeeditor.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.codeeditor.database.CodeReviewSuggestionDao

class CodeReviewViewModel(
    dataSource: CodeReviewSuggestionDao,
    application: Application
): ViewModel() {

    val database = dataSource

    private val _navigateToCodeReviewSuggestionDetail = MutableLiveData<String>()
    val navigateToCodeReviewSuggestionDetail
        get() = _navigateToCodeReviewSuggestionDetail

    fun onCodeReviewSuggestionClicked(id: String) {
        _navigateToCodeReviewSuggestionDetail.value = id
    }

    fun onCodeReviewSuggestionNavigated() {
        _navigateToCodeReviewSuggestionDetail.value = null
    }

}