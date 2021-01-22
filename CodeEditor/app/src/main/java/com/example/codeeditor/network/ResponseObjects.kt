package com.example.codeeditor.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompilationResponse(
    val sourceCode: String,
    val status: Int,
    val errorCode: Int,
    val output: String,
    val date: String,
    val language: String,
    val input: String,
    val id: Int
)

@JsonClass(generateAdapter = true)
data class LoginDeepCodeResponse(
    val sessionToken: String,
    val loginURL: String
)

@JsonClass(generateAdapter = true)
data class CreateBundleResponse(
    val bundleId: String,
    val uploadURL: String,
    val missingFiles: List<String>
)


@JsonClass(generateAdapter = true)
data class GetAnalysisResponse(
    val status: AnalysisStatus,
    val progress: Int,
    val analysisURL: String = "",
    val analysisResults: AnalysisResults? = null,
    val timing: Timing? = null,
    val coverage: List<Coverage> = emptyList()
)

data class Timing(
    val fetchingCode: Int,
    val analysis: Int,
    val queue: Int
)

data class Coverage(
    val lang: String,
    val isSupported: Boolean,
    val files: Int
)

data class AnalysisResults(
    val suggestions: Map<Int, Suggestion>,
    val files: Map<String, Map<Int, List<SuggestionPosition>>>
)

data class Suggestion(
    val id: String,
    val rule: String,
    val message: String,
    val severity: Int,
    val leadURL: String,
    val categories: List<String>,
    val tags: List<String>,
    val repoDatasetSize: Int,
    val exampleCommitDescriptions: List<String>,
    val exampleCommitFixes: List<CommitFix>
)

data class CommitFix(
    val commitURL: String,
    val lines: List<Line>
)

data class Line(
    val line: String,
    val lineNumber: Int,
    val lineChange: LineChange
)

data class SuggestionPosition(
    val cols: List<Int>,
    val rows: List<Int>,
    val markers: List<Marker>
)

data class Marker(
    val msg: List<Int>,
    val pos: List<Position>
)

data class Position(
    val cols: List<Int>,
    val rows: List<Int>
)

enum class AnalysisStatus {
    WAITING, FETCHING, ANALYZING, DC_DONE, DONE, FAILED
}

enum class LineChange {
    removed, added, none
}