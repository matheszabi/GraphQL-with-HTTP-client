package com.matheszabi.vokalo

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MakeHttpPost {

    companion object {

        suspend fun doPost(urlString: String, postData: String) : String{// NetworkOnMainThreadException! add suspend too!

            val url = URL(urlString)

            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json") // need to send json to GraphQL
            conn.setRequestProperty("Content-Length", postData.length.toString())
            conn.useCaches = false

            val dos = DataOutputStream(conn.outputStream)
            try {
                dos.writeBytes(postData)
                dos.flush()
                dos.close()
                println("body is written!")
            }catch (e: Exception){
                println("Error while write to body")
                e.printStackTrace()
            }


            println("conn.responseCode: ${conn.responseCode}")// 500

            var result = ""

            when(conn.responseCode){
                200->{
                    BufferedReader(InputStreamReader(conn.inputStream)).use { br ->
                        var line: String?
                        while (br.readLine().also { line = it } != null) {
                            println(line)
                            result += line

                        }
                    }
                }
                else ->{
                    println("The error stream: (message from server)")
                    BufferedReader(InputStreamReader(conn.errorStream)).use { br ->
                        var line: String?
                        while (br.readLine().also { line = it } != null) {
                            println(line)// POST body missing. Did you forget use body-parser middleware?
                            result += line
                        }
                    }
                    // print a message to the user it was a network error.
                }
            }
            return result
        }
    }
}

