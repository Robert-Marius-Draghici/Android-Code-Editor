package com.example.codeeditor.network

import com.example.codeeditor.database.CodeReviewSuggestion
import com.example.codeeditor.domain.CodeReviewAnalysis
import com.example.codeeditor.domain.CodeReviewSuggestionModel

fun GetAnalysisResponse.asDomainModel(code: String): CodeReviewAnalysis {

    val codeReviewSuggestions = mutableListOf<CodeReviewSuggestionModel>()

    if (analysisResults!!.files.isNotEmpty()) {
        val filename = analysisResults.files.keys.first()
        val suggestionPositions = analysisResults.files[filename]
        val suggestions = analysisResults.suggestions


        for ((suggestionIndex, suggestionPosition) in suggestionPositions!!) {
            suggestionPosition.map {
                val startRow = it.rows[0]
                val endRow = it.rows[1]
                val lines = code.split("\n").subList(startRow - 1, endRow).toString()
                codeReviewSuggestions.add(
                    CodeReviewSuggestionModel(
                        code = lines,
                        suggestionId = suggestions[suggestionIndex]!!.id,
                        rule = suggestions[suggestionIndex]!!.rule,
                        suggestionMessage = suggestions[suggestionIndex]!!.message,
                        severity = suggestions[suggestionIndex]!!.severity,
                        categories = suggestions[suggestionIndex]!!.categories,
                        exampleCommitFixes = suggestions[suggestionIndex]!!.exampleCommitFixes
                    )
                )
            }
        }
    }

    return CodeReviewAnalysis(analysisURL, codeReviewSuggestions)
}

fun GetAnalysisResponse.asDatabaseModel(code: String): List<CodeReviewSuggestion> {

    val codeReviewSuggestions = mutableListOf<CodeReviewSuggestion>()

    if (analysisResults!!.files.isNotEmpty()) {
        val filename = analysisResults.files.keys.first()
        val suggestionPositions = analysisResults.files[filename]
        val suggestions = analysisResults.suggestions

        for ((suggestionIndex, suggestionPosition) in suggestionPositions!!) {
            suggestionPosition.map {
                val startRow = it.rows[0]
                val endRow = it.rows[1]
                val lines = code.split("\n").subList(startRow - 1, endRow).toString()
                codeReviewSuggestions.add(
                    CodeReviewSuggestion(
                        id = suggestions[suggestionIndex]!!.id.plus(lines),
                        suggestionId = suggestions[suggestionIndex]!!.id,
                        code = lines,
                        rule = suggestions[suggestionIndex]!!.rule,
                        suggestionMessage = suggestions[suggestionIndex]!!.message,
                        severity = suggestions[suggestionIndex]!!.severity,
                        categories = suggestions[suggestionIndex]!!.categories,
                        exampleCommitFixes = suggestions[suggestionIndex]!!.exampleCommitFixes
                    )
                )
            }
        }
    }

    return codeReviewSuggestions
}