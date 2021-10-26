package com.easy.demon

import android.app.Application
import com.easy.skin.SkinManager

class DemonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
    }
}