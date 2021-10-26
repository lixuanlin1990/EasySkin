package com.easy.demon

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.easy.skin.SkinManager
import com.easy.skin.SkinPreference
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!File("${cacheDir}/app-debug.apk").exists()) {
            val inputStream = assets.open("app-debug.apk")
            val bufferedInputStream = BufferedInputStream(inputStream)
            val outputStream = FileOutputStream(File("${cacheDir}/app-debug.apk"))
            val bufferedOutputStream = BufferedOutputStream(outputStream)
            val buffer = ByteArray(1024)
            var byteCount: Int
            while (bufferedInputStream.read(buffer).also { byteCount = it } != -1) {
                bufferedOutputStream.write(buffer, 0, byteCount)
            }
            bufferedOutputStream.flush()
            bufferedOutputStream.close()
            bufferedInputStream.close()
        }
    }


    fun skinApply(view: View) {
        if (!File("${cacheDir}/app-debug.apk").exists()) {
            return
        }
        if (SkinPreference.instance?.skin.isNullOrBlank())
            SkinManager.instance?.loadSkin("${cacheDir}/app-debug.apk")
        else
            SkinManager.instance?.loadSkin(null)
    }
}