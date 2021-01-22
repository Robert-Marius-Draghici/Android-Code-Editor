package com.example.codeeditor.viewmodels

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.codeeditor.database.CodeReviewSuggestionDao
import com.example.codeeditor.domain.CodeReviewAnalysis
import com.example.codeeditor.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class CodeEditorViewModel(
    dataSource: CodeReviewSuggestionDao,
    application: Application
): ViewModel() {

    val database = dataSource

    val code = MutableLiveData<String>()
    val input = MutableLiveData<String>()

    private val _output = MutableLiveData<String>()
    val output: LiveData<String>
        get() = _output

    private val _selectedProgrammingLanguage = MutableLiveData<String>()
    val selectedProgrammingLanguage: LiveData<String>
        get() = _selectedProgrammingLanguage

    private val _sessionToken = MutableLiveData<String>()
    val sessionToken: LiveData<String>
        get() = _sessionToken

    private val _loginUrl = MutableLiveData<String>()
    val loginUrl: LiveData<String>
        get() = _loginUrl

    private val _codeReviewAnalysis = MutableLiveData<CodeReviewAnalysis>()
    val codeReviewAnalysis: LiveData<CodeReviewAnalysis>
        get() = _codeReviewAnalysis

    private val _eventLoginCallFinish = MutableLiveData<Boolean>()
    val eventLoginCallFinish: LiveData<Boolean>
        get() = _eventLoginCallFinish

    private val _eventCodeReviewFinish = MutableLiveData<Boolean>()
    val eventCodeReviewFinish: LiveData<Boolean>
        get() = _eventCodeReviewFinish

    val outputVisible = Transformations.map(output) {
        if (it != "") {
            return@map View.VISIBLE
        } else {
            return@map View.GONE
        }
    }

    private val languages = mapOf(
        "C" to "c",
        "Java" to "java",
        "Kotlin" to "kt",
        "Python" to "py"
    )

    init {
        input.value = ""
        code.value = ""
        _output.value = ""
    }

    fun selectProgrammingLanguage(programmingLanguage: String) {
        _selectedProgrammingLanguage.value = programmingLanguage
    }

    fun compile() {
        viewModelScope.launch {
            try {
                _output.value = CompilerApi.retrofitService.compile(
                    CompilerServiceRequest(
                        code.value!!,
                        languages[selectedProgrammingLanguage.value]!!,
                        input.value!!
                    )
                )!!.output
            } catch (exception: Exception) {
                Log.e("CodeEditorViewModel", exception.message, exception)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            try {
            val response: LoginDeepCodeResponse = CodeReviewApi.retrofitService.loginToDeepCode()
            _sessionToken.value = response.sessionToken
            _loginUrl.value = response.loginURL
            println(response)
            _eventLoginCallFinish.value = true

            } catch (exception: Exception) {
                Log.e("CodeEditorViewModel", exception.message, exception)
            }
        }
    }

    fun completedBrowserLogin() {
        _eventLoginCallFinish.value = false
    }

    fun review() {
        viewModelScope.launch {
            try {
                CodeReviewApi.retrofitService.checkDeepCodeSession(_sessionToken.value!!)
                val response = CodeReviewApi.retrofitService.createBundle(_sessionToken.value!!,
                 CreateBundleRequest(listOf(FileItem("/.filename." + languages[selectedProgrammingLanguage.value]!!, code.value!!)))
                    )
                Log.i("[CodeEditorViewModel]", response.toString())
                var analysis: GetAnalysisResponse
                do {
                    analysis = CodeReviewApi.retrofitService.getAnalysis(_sessionToken.value!!, response.bundleId)
                    if (analysis.status == AnalysisStatus.DONE) {
                        break
                    }
                } while (true)
                Log.i("[CodeEditorViewModel]", analysis.toString())
                _codeReviewAnalysis.value = analysis.asDomainModel(code.value!!)
                withContext(Dispatchers.IO) {
                    database.insertAll(analysis.asDatabaseModel(code.value!!))
                }

                _eventCodeReviewFinish.value = true
            } catch (exception: Exception) {
                Log.e("[CodeEditorViewModel]", exception.message, exception)
            }
        }
    }

    fun navigate() {
        _eventCodeReviewFinish.value = false
    }

    fun clear() {
        input.value = ""
        code.value = ""
        _output.value = ""
    }
}