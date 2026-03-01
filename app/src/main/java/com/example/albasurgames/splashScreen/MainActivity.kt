package com.example.albasurgames.splashScreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.albasurgames.sesionApartado.UserLoginActivity

import androidx.lifecycle.lifecycleScope
import com.example.albasurgames.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.view.ViewTreeObserver


class MainActivity : ComponentActivity() {

    private lateinit var logoImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        initComponents()

        logoImage.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    logoImage.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // Forzar punto de escala
                    logoImage.pivotX = logoImage.width / 2f
                    logoImage.pivotY = logoImage.height / 2f

                    // Estado inicial REAL
                    logoImage.scaleX = 0.5f
                    logoImage.scaleY = 0.5f

                    val scaleUpX = ObjectAnimator.ofFloat(logoImage, View.SCALE_X, 0.5f, 1.5f)
                    val scaleUpY = ObjectAnimator.ofFloat(logoImage, View.SCALE_Y, 0.5f, 1.5f)

                    val scaleDownX = ObjectAnimator.ofFloat(logoImage, View.SCALE_X, 1.5f, 1f)
                    val scaleDownY = ObjectAnimator.ofFloat(logoImage, View.SCALE_Y, 1.5f, 1f)

                    val up = AnimatorSet().apply {
                        playTogether(scaleUpX, scaleUpY)
                        duration = 1000
                        interpolator = OvershootInterpolator()
                    }

                    val down = AnimatorSet().apply {
                        playTogether(scaleDownX, scaleDownY)
                        duration = 500
                    }

                    AnimatorSet().apply {
                        playSequentially(up, down)
                        start()
                    }
                }
            }
        )


        // Espera X segundos y navega a la vista de inicio de sesion/registro de usuario
        lifecycleScope.launch {
            delay(2000)
            val intent = Intent(this@MainActivity, UserLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initComponents()
    {
        logoImage = findViewById(R.id.logoImage)
        // ESTADO INICIAL
        logoImage.scaleX = 0.5f
        logoImage.scaleY = 0.5f
    }
}
