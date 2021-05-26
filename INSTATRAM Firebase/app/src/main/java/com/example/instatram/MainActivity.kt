package com.example.instatram

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.example.instatram.ui.main.SectionsPagerAdapter
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        tabs.setupWithViewPager(viewPager)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu1,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.themes -> showTheme()
            R.id.language -> Translate()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun Translate() {
        val listitems = arrayOf("español", "English")

        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle(R.string.mode2)
        mBuilder.setSingleChoiceItems(listitems, -1){dialog, which->
            if (which==0){
                setLocate("es")
                recreate()
            }
            else if (which == 1){
                setLocate("eng")
                recreate()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun setLocate(Lang: String){
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang",Lang)
        editor.apply()
    }
    private fun showTheme() {
        var dark: String = getString(R.string.dark)
        var light: String = getString(R.string.light)
        val listitems = arrayOf(dark,light)


        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle(R.string.mode1)
        mBuilder.setSingleChoiceItems(listitems, -1){dialog, which->
            if (which==0){
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES)
                recreate()
            }
            else if (which == 1){
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO)
                recreate()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

}