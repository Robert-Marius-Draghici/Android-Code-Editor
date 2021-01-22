package com.example.codeeditor.domain

import android.os.Parcelable
import com.example.codeeditor.network.CommitFix
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class CodeReviewSuggestionModel(
    val code: String,
    val suggestionId: String,
    val rule: String,
    val suggestionMessage: String,
    val severity: Int,
    val categories: List<String>,
    val exampleCommitFixes: @RawValue List<CommitFix>
) : Parcelable

@Parcelize
data class CodeReviewAnalysis(
    val analysisUrl: String,
    val suggestions: List<CodeReviewSuggestionModel>
) : Parcelable