package com.zeitform.wasserapp

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import android.widget.Button
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.viewpager.widget.PagerAdapter
import android.view.WindowManager
import android.os.Build
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.text.Html
import android.util.TypedValue
import android.view.View
import com.zeitform.wasserapp.prefmanagers.PrefManager

private const val PERMISSION_REQUEST = 10

class IntroActivity : AppCompatActivity() {

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var viewPager: ViewPager? = null
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var dotsLayout: LinearLayout? = null
    private var dots: Array<TextView?>? = null
    private var layouts: IntArray? = null
    private var btnNext:Button? = null
    private var prefManager: PrefManager? = null
    private var zustimmen: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Checking for first time launch - before calling setContentView()
        prefManager = PrefManager(this)
        if (!prefManager!!.isFirstTimeLaunch) {
            launchHomeScreen()
            finish()
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(R.layout.activity_intro)

        viewPager = findViewById(R.id.view_pager)
        dotsLayout = findViewById(R.id.layoutDots)
        btnNext = findViewById(R.id.btn_next)

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = intArrayOf(
            R.layout.intro_slide1,
            R.layout.intro_slide2,
            R.layout.intro_slide3,
            R.layout.intro_slide4,
            R.layout.intro_slide5
        )

        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        changeStatusBarColor()

        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager!!.adapter = myViewPagerAdapter
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)

        btnNext!!.setOnClickListener {
            if(viewPager!!.currentItem == 0 && !checkPermission(permissions)){
                //Standort freigeben
                requestLocationPermission()
                viewPager!!.currentItem = 0
            } else {
                // checking for last page
                // if last page home screen will be launched
                val current = getItem(+1)
                if (current < layouts!!.size) {
                    if(current == 0 && !checkPermission(permissions)){
                        //Standort freigeben
                        requestLocationPermission()
                    }
                    // move to next screen
                    viewPager!!.currentItem = current
                } else {
                    launchHomeScreen()
                }
            }
        }
    }
    private fun requestLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    btnNext!!.text = getString(R.string.btn_weiter)
                } else {
                    //denied
                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }
    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls<TextView?>(layouts!!.size)

        val colorActive = ContextCompat.getColor(this@IntroActivity, R.color.dot_active)
        val colorInactive = ContextCompat.getColor(this@IntroActivity, R.color.dot_inactive)

        dotsLayout?.removeAllViews()
        for (i in 0 until dots!!.size) {
            dots?.set(i, TextView(this))
            dots!![i]?.text = Html.fromHtml("&#8226;")
            dots!![i]?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35F)
            dots!![i]?.setTextColor(colorInactive)
            dotsLayout?.addView(dots!![i])
        }
        if (dots!!.isNotEmpty())
            dots!![currentPage]?.setTextColor(colorActive)
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    private fun launchHomeScreen() {
        prefManager!!.isFirstTimeLaunch = false
        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
        finish()
    }
    //  viewpager change listener
    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == 0 && !checkPermission(permissions)) {
                btnNext!!.text = getString(R.string.btn_standort)
            } else  if(position == 0 && checkPermission(permissions)) {
                btnNext!!.text = getString(R.string.btn_weiter)
            }else if (position == layouts!!.size - 1) {
                // last page. make button text to GOT IT
                btnNext!!.visibility = View.INVISIBLE
            } else {
                // still pages are left
                btnNext!!.text = getString(R.string.btn_weiter)
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

        }

        override fun onPageScrollStateChanged(arg0: Int) {

        }
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

            var view: View?
            if(position == 4){
                view = layoutInflater!!.inflate(layouts!![position], container, false)
                zustimmen = view.findViewById<Button>(R.id.zustimmen)
                zustimmen!!.setOnClickListener {
                    launchHomeScreen()
                }
                container.addView(view)
            } else {
                view = layoutInflater!!.inflate(layouts!![position], container, false)
                container.addView(view)
            }

            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
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
