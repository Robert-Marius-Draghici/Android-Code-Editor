package com.example.codeeditor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.codeeditor.R
import com.example.codeeditor.database.CodeReviewSuggestion
import com.example.codeeditor.database.CodeReviewSuggestionDatabase
import com.example.codeeditor.databinding.FragmentCodeReviewDetailBinding
import com.example.codeeditor.viewmodels.CodeReviewSuggestionDetailViewModel
import com.example.codeeditor.viewmodels.CodeReviewSuggestionDetailViewModelFactory
import com.example.codeeditor.viewmodels.CodeReviewViewModel
import com.example.codeeditor.viewmodels.CodeReviewViewModelFactory

class CodeReviewSuggestionDetailFragment: Fragment() {

    private lateinit var binding: FragmentCodeReviewDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_code_review_detail, container, false
        )

        val application = requireNotNull(this.activity).application
        val arguments = CodeReviewSuggestionDetailFragmentArgs.fromBundle(requireArguments())

        // Create an instance of the ViewModel Factory.
        val dataSource = CodeReviewSuggestionDatabase.getInstance(application).codeReviewSuggestionDao
        val viewModelFactory = CodeReviewSuggestionDetailViewModelFactory(arguments.suggestionKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val viewModel =
            ViewModelProvider(
                this, viewModelFactory).get(CodeReviewSuggestionDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }
}