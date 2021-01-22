package com.example.codeeditor.util

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.codeeditor.network.CommitFix
import com.example.codeeditor.network.LineChange

fun formatExampleCommitFixes(exampleCommitFixes: List<CommitFix>): Spanned {
    val sb = StringBuilder()
    sb.apply {
        exampleCommitFixes.forEach {
            append("Commit url: ")
            append("\t${it.commitURL}<br>")
            it.lines.forEach {
                if (it.lineChange != LineChange.none) {
                    append("Line: ")
                    append("\t${it.line}<br>")
                    append("Line Number: ")
                    append("\t${it.lineNumber}<br>")
                    append("Line Change: ")
                    append("\t${it.lineChange}<br>")
                }
            }
            append("<br>")
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}