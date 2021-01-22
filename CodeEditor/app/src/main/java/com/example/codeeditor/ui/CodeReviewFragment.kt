package com.example.codeeditor.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.codeeditor.R
import com.example.codeeditor.database.CodeReviewSuggestionDatabase
import com.example.codeeditor.databinding.FragmentCodeEditorBinding
import com.example.codeeditor.databinding.FragmentCodeReviewBinding
import com.example.codeeditor.viewmodels.CodeEditorViewModel
import com.example.codeeditor.viewmodels.CodeEditorViewModelFactory
import com.example.codeeditor.viewmodels.CodeReviewViewModel
import com.example.codeeditor.viewmodels.CodeReviewViewModelFactory

class CodeReviewFragment: Fragment() {
    private lateinit var binding: FragmentCodeReviewBinding

    private val viewModel: CodeReviewViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val dataSource = CodeReviewSuggestionDatabase.getInstance(application).codeReviewSuggestionDao
        val viewModelFactory = CodeReviewViewModelFactory(dataSource, application)
        ViewModelProvider(this, viewModelFactory).get(CodeReviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_code_review, container, false
        )

        binding.lifecycleOwner = this
        binding.codeReviewViewModel = viewModel

        val args = CodeReviewFragmentArgs.fromBundle(requireArguments())

        val adapter = CodeReviewSuggestionAdapter(CodeReviewSuggestionListener { id ->
            viewModel.onCodeReviewSuggestionClicked(id)
        })

        binding.codeReviewList.adapter = adapter
        binding.codeReviewLink.text = args.codeReviewAnalysis.analysisUrl
        adapter.submitList(args.codeReviewAnalysis.suggestions)

        viewModel.navigateToCodeReviewSuggestionDetail.observe(viewLifecycleOwner, { suggestion ->
            suggestion?.let {
                this.findNavController().navigate(
                                    CodeReviewFragmentDirections
                                        .actionCodeReviewFragmentToCodeReviewSuggestionDetailFragment(suggestion))
                viewModel.onCodeReviewSuggestionNavigated()
            }
        })

        return binding.root
    }
}