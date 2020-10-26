package com.example.emptyapp

//import android.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.example.emptyapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var counter : Int = 0

        val viewCounter: TextView = findViewById(R.id.editTextNumber)

        val button: Button = findViewById(R.id.increment_button)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                println(counter++)
                viewCounter.setText(counter.toString())
            }
        })

    }
}


