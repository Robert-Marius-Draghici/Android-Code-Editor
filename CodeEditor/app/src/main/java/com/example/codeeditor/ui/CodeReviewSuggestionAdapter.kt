package com.example.codeeditor.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.codeeditor.databinding.ListItemCodeReviewBinding
import com.example.codeeditor.domain.CodeReviewSuggestionModel

class CodeReviewSuggestionAdapter(val clickListener: CodeReviewSuggestionListener) : ListAdapter<CodeReviewSuggestionModel, CodeReviewSuggestionAdapter.ViewHolder>(CodeReviewSuggestionModelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemCodeReviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CodeReviewSuggestionModel, clickListener: CodeReviewSuggestionListener) {
            binding.suggestion = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCodeReviewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CodeReviewSuggestionModelDiffCallback: DiffUtil.ItemCallback<CodeReviewSuggestionModel>() {
    override fun areItemsTheSame(oldItem: CodeReviewSuggestionModel, newItem: CodeReviewSuggestionModel): Boolean {
        return oldItem.suggestionId.plus(oldItem.code) == newItem.suggestionId.plus(newItem.code)
    }

    override fun areContentsTheSame(oldItem: CodeReviewSuggestionModel, newItem: CodeReviewSuggestionModel): Boolean {
        return oldItem == newItem
    }
}

class CodeReviewSuggestionListener(val clickListener: (codeReviewSuggestionId: String) -> Unit) {
    fun onClick(codeReviewSuggestionModel: CodeReviewSuggestionModel) = clickListener(
        codeReviewSuggestionModel.suggestionId.plus(codeReviewSuggestionModel.code))
}