# Android-Code-Editor

Android Code Editor is an Android application written in Kotlin which provides functionalities such as writing code, compiling and automatic code review. Currently it supports the following programming languages: C, Java, Python.

The first screen of the application allows you to select a programming language and write the code. Once the code is written you can compile and run it, clear it or perform automatic code review. You can also save the file on the phone or open an existing file. 

Note that for the automatic code review functionality, the implementation uses the REST API provided by https://www.deepcode.ai/ which needs you to login in order to use it. You will need to login for every session. After pressing the login button, you will be redirected to the browser and should see a Login Successful message. If you get a Login Failed message, you can either try again or wait a while (the API may not be available or you have logged in too many times) . For development purposes, you can also copy the login URL from the logs and paste it in the browser, if the redirect to browser generates a Login Failed message.

After login, pressing the Code Review button will redirect to a new page that shows the analysis URL and the suggestions in a concise form. For more details on a suggestion, you can click on a suggestion in the list and will be redirected to a page which presents more details about the clicked suggestion.
