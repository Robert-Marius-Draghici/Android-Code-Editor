package com.example.codeeditor.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.codeeditor.R
import com.example.codeeditor.database.CodeReviewSuggestionDatabase
import com.example.codeeditor.databinding.FragmentCodeEditorBinding
import com.example.codeeditor.viewmodels.CodeEditorViewModel
import com.example.codeeditor.viewmodels.CodeEditorViewModelFactory
import java.io.*


/**
 * A simple [Fragment] subclass.
 * Use the [CodeEditorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

const val WRITE_REQUEST_CODE = 1
const val READ_REQUEST_CODE = 2

class CodeEditorFragment : Fragment() {

    private lateinit var binding: FragmentCodeEditorBinding

    private val viewModel: CodeEditorViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val dataSource = CodeReviewSuggestionDatabase.getInstance(application).codeReviewSuggestionDao
        val viewModelFactory = CodeEditorViewModelFactory(dataSource, application)
        ViewModelProvider(this, viewModelFactory).get(CodeEditorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_code_editor, container, false
        )

        binding.lifecycleOwner = this
        binding.codeEditorViewModel = viewModel
        binding.codeReviewButton.setOnClickListener { view: View ->
            review(view)
        }

        val spinner = binding.spinner

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.available_programming_languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val spinnerAction = SpinnerAction()
        spinner.onItemSelectedListener = spinnerAction

        setHasOptionsMenu(true)
        return binding.root
    }

    fun review(view: View) {
        if (viewModel.sessionToken.value == null) {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage(R.string.deepcode_login_message)
                .setPositiveButton(R.string.deepcode_login_button_message) { _, _ ->
                    viewModel.login()
                    viewModel.eventLoginCallFinish.observe(
                        viewLifecycleOwner,
                        { hasFinished ->
                            if (hasFinished) {
                                val webpage: Uri = Uri.parse(viewModel.loginUrl.value)
                                val intent = Intent(Intent.ACTION_VIEW, webpage)
                                startActivity(intent)
                                viewModel.completedBrowserLogin()
                            }
                        })
                }
                .setNegativeButton(R.string.deepcode_cancel_button) { _, _ ->
                }
            builder.create()
            builder.show()
        }
        viewModel.review()
        viewModel.eventCodeReviewFinish.observe(
            viewLifecycleOwner,
            { hasFinished ->
                if (hasFinished) {
                    println(viewModel.codeReviewAnalysis.value)
                    view.findNavController()
                        .navigate(
                            CodeEditorFragmentDirections.actionCodeEditorFragmentToCodeReviewFragment(
                                viewModel.codeReviewAnalysis.value!!
                            ))
                    viewModel.navigate()
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveOption -> saveFile()
            R.id.openOption -> openFile()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, WRITE_REQUEST_CODE)
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            WRITE_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> if (data != null && data.data != null) {
                        writeInFile(data.data!!, viewModel.code.value!!)
                    }
                    Activity.RESULT_CANCELED -> {
                    }
                }
            }
            READ_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> if (data != null && data.data != null) {
                        readFromFile(data.data!!)
                    }
                    Activity.RESULT_CANCELED -> {
                    }
                }
            }
        }
    }

    private fun writeInFile(uri: Uri, text: String) {
        val outputStream: OutputStream
        try {
            outputStream = requireContext().contentResolver.openOutputStream(uri)!!
            val bw = BufferedWriter(OutputStreamWriter(outputStream))
            bw.write(text)
            bw.flush()
            bw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readFromFile(uri: Uri) {
        val inputStream: InputStream
        try {
            inputStream = requireContext().contentResolver.openInputStream(uri)!!
            val br = BufferedReader(InputStreamReader(inputStream))
            viewModel.code.value = br.readText()
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    inner class SpinnerAction : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
            // On selecting a spinner item
            val item = parent!!.getItemAtPosition(pos).toString()

            viewModel.selectProgrammingLanguage(item)
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }
}