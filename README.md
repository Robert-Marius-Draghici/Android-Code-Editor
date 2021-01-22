# Android-Code-Editor

Android Code Editor is an Android application written in Kotlin which provides functionalities such as writing code, compiling and automatic code review. Currently it supports the following programming languages: C, Java, Python.

The folder structure is the following:
* the CodeEditor folder holds the application code.
* the code snippets folder holds example code files to be used in the code editor for testing it.

The first screen of the application allows you to select a programming language and write the code. Once the code is written you can compile and run it, clear it or perform automatic code review. You can also save the file on the phone or open an existing file. Note that even if you open a file with the right extension, you still need to select the corresponding programming language in the dropdown (currently the code editor does not support automatic programming language inference from the extension). Also, please note that the code editor currently supports compiling code only from the core libraries of the supported programming languages.

Note that for the automatic code review functionality, the implementation uses the REST API provided by https://www.deepcode.ai/ which needs you to login in order to use it. You will need to login for every session. After pressing the login button, you will be redirected to the browser and should see a Login Successful message. If you get a Login Failed message, you can either try again or wait a while (the API may not be available or you have logged in too many times) . For development purposes, you can also copy the login URL from the logs and paste it in the browser, if the redirect to browser generates a Login Failed message.

After login, pressing the Code Review button will redirect (the code review analysis may take a while depending on the size of the file) to a new page that shows the analysis URL and the suggestions in a concise form. For more details on a suggestion, you can click on a suggestion in the list and will be redirected to a page which presents more details about the clicked suggestion.
