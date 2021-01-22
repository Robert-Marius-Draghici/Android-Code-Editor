package com.example.codeeditor.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompilerServiceRequest (
    val code: String,
    val language: String,
    val input: String
)

@JsonClass(generateAdapter = true)
data class CreateBundleRequest (
    val files: List<FileItem>
)

data class FileItem(
    val filePath: String,
    val fileContent: String
)