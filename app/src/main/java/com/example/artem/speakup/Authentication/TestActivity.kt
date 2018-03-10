package com.example.artem.speakup.Authentication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        saveSessionBtn.setOnClickListener({
            var fdb = Fdb()
            var part1 = Session.Part("teeest paaaart1")
            var part2 = Session.Part("teeest paaaart2")
            var ses = Session(listOf(part1, part2))
            fdb.saveSession(ses)
        })

    }
}
