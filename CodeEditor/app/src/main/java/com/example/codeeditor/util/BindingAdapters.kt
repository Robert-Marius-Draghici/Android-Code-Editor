package com.example.codeeditor.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.codeeditor.R
import com.example.codeeditor.database.CodeReviewSuggestion
import com.example.codeeditor.domain.CodeReviewSuggestionModel

@SuppressLint("SetTextI18n")
@BindingAdapter("lineCode")
fun TextView.setLineCode(item: CodeReviewSuggestionModel) {
    text = "Code: " + item.code;
}

@SuppressLint("SetTextI18n")
@BindingAdapter("suggestionMessage")
fun TextView.setSuggestionMessage(item: CodeReviewSuggestionModel) {
    text = "Suggestion: " + item.suggestionMessage
}

@BindingAdapter("severityImage")
fun ImageView.setSeverityImage(item: CodeReviewSuggestionModel) {
    setImageResource(when (item.severity) {
        1 -> R.drawable.green
        2 -> R.drawable.yellow
        3 -> R.drawable.red
        else -> R.drawable.gray
    })
}

@SuppressLint("SetTextI18n")
@BindingAdapter("severityText")
fun TextView.setSeverityText(item: CodeReviewSuggestion?) {
    item?.let {
        text = when (item.severity) {
            1 -> "1 (INFO)"
            2 -> "2 (WARNING)"
            3 -> "3 (CRITICAL)"
            else -> "UNDEFINED"
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("exampleCommitFixes")
fun TextView.setExampleCommitFixes(item: CodeReviewSuggestion?) {
    item?.let {
        setTextIsSelectable(true)
        text = formatExampleCommitFixes(item.exampleCommitFixes)
    }
}
