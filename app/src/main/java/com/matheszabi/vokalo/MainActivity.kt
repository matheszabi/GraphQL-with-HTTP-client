package com.matheszabi.vokalo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock.sleep
import android.view.View
import android.widget.EditText
import com.matheszabi.vokalo.MakeHttpPost.Companion.doPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() , CoroutineScope by MainScope() {

     private lateinit var editTextTextMultiLine :EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTextMultiLine = findViewById<EditText>(R.id.editTextTextMultiLine)
    }

    fun buttonClicked(view: View) {
        // in production: check the connection first, connection state, now skip it

        val urlString = "https://api.graphql.jobs/"

        // get the schema, generate artifacts ( classes) and use the functions, just like in hybernate
        // those will generate a JSON, exactly how the hybernate is generation an SQL
        // here is used the saw JSON :)
        val queryParams = "{\n" +
                "  \"query\": \"{jobs{title, createdAt}}\"\n" +
                "}"

        // fire and forget:
        launch(Dispatchers.IO) {

            // this is a suspend function, and is slowing down because it write to the console, remove that!
            val responseErrorOrBody = doPost(urlString, queryParams)
            // handle if is an error or a body, but for now display to UI
            // inn order to do that we have to change the thread context from IO to Main:
            launch(Dispatchers.Main){

                editTextTextMultiLine.setText(responseErrorOrBody)

            }

        }
        //println("after job lunch called - from button event(this should be seen first, proof as async call!)")
    }
}