package com.example.instatram.ui.main.Intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.instatram.MainActivity
import com.example.instatram.R

class scratch : AppCompatActivity() {
    val SPLASH_SCREEN = 5000
    private lateinit var topAnimation : Animation
    private lateinit var bottomAnimation : Animation

    private lateinit var imageView: ImageView
    private lateinit var title_txt: TextView
    private lateinit var description_txt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_scratch)

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        imageView = findViewById(R.id.imageView)
        title_txt = findViewById(R.id.textView)
        description_txt = findViewById(R.id.textView2)

        imageView.animation = topAnimation
        title_txt.animation = bottomAnimation
        description_txt.animation = bottomAnimation

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN.toLong())
    }

}