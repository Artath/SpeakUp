package com.example.artem.speakup.WelcomeScreens

import android.animation.ArgbEvaluator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.artem.speakup.R
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.example.artem.speakup.Auth.AuthActivity
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    var color1 = 0
    var color2 = 0
    var color3 = 0
    var evaluator = ArgbEvaluator()
    lateinit var colorList: IntArray

    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var dots: Array<TextView?>? = null
    lateinit var layouts: IntArray
    private var prefManager: PreferenceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        color1 = ContextCompat.getColor(this, R.color.bg_slider_screen1)
        color2 = ContextCompat.getColor(this, R.color.bg_slider_screen2)
        color3 = ContextCompat.getColor(this, R.color.bg_slider_screen3)
        colorList = intArrayOf(color1, color2, color3)

        // Checking for first time launch - before calling setContentView()
        prefManager = PreferenceManager(this)
        if (!prefManager!!.isFirstTimeLaunch) {
            launchHomeScreen()
            finish()
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(R.layout.activity_welcome)

        // layouts of all welcome sliders
        layouts = intArrayOf(R.layout.slide_screen1, R.layout.slide_screen2, R.layout.slide_screen3)

        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        changeStatusBarColor()

        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager.adapter = myViewPagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        skipBtn.setOnClickListener { launchHomeScreen() }

        nextBt.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val current = getItem(+1)
            if (current < layouts.size) {
                // move to next screen
                viewPager.currentItem = current
            } else {
                launchHomeScreen()
            }
        }
    }

    //  viewpager change listener
    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            when (position) {
                0 -> viewPager.setBackgroundColor(color1)
                1 -> viewPager.setBackgroundColor(color2)
                2 -> viewPager.setBackgroundColor(color3)
            }

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.size - 1) {
                // last page. make button text to GOT IT
                nextBt.text = getString(R.string.start)
                skipBtn.visibility = View.GONE
            } else {
                // still pages are left
                nextBt.text = getString(R.string.next)
                skipBtn.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            val colorUpdate = evaluator.evaluate(arg1, colorList[arg0], colorList[if (arg0 === 2) arg0 else arg0 + 1]) as Int
            viewPager!!.setBackgroundColor(colorUpdate)

        }

        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        layoutDots.removeAllViews()
        for (i in dots!!.indices) {
            dots!![i] = TextView(this)
            dots!![i]!!.text = Html.fromHtml("&#8226;")
            dots!![i]!!.textSize = 40f
            dots!![i]!!.setTextColor(colorsInactive[currentPage])
            layoutDots.addView(dots!![i])
        }

        if (dots!!.isNotEmpty())
            dots!![currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    private fun getItem(i: Int): Int {
        return viewPager.currentItem + i
    }

    private fun launchHomeScreen() {
        prefManager!!.isFirstTimeLaunch = false
        startActivity(Intent(this@WelcomeActivity, AuthActivity::class.java))
        finish()
    }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(layouts[position], container, false)
            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}
