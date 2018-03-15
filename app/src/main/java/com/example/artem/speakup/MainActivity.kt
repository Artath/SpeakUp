package com.example.artem.speakup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity(),
        MainPresenter.MainPresenterInterface,
        TabRecords.Callback {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateTabs()
    }

    override fun onResume() {
        super.onResume()
        updateTabs()
    }

    override fun onRestart() {
        super.onRestart()
        updateTabs()
    }

    override fun updateTabs() {
        tabs.addOnTabSelectedListener(object: TabLayout.ViewPagerOnTabSelectedListener(content) {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                super.onTabSelected(tab)
                content.currentItem = tab!!.position

                tabs.getTabAt(0)?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.playlist_play)
                tabs.getTabAt(1)?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.message_bulleted)
                tabs.getTabAt(2)?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.approval)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        val adapter = TabsAdapter(applicationContext, supportFragmentManager)
        content.adapter = adapter
        tabs.setupWithViewPager(content)
    }

    override fun getAudioRecords(): ArrayList<AudioRecord>? {
        val files = File(externalCacheDir.absolutePath).listFiles()
        var data: ArrayList<AudioRecord> = arrayListOf()

        files.forEach {
            f -> data.add(
                AudioRecord(
                        f.nameWithoutExtension,
                        f.lastModified(),
                        f.path)) }

        data.reverse()
        return data
    }
}
