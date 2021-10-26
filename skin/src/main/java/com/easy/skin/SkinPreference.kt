package com.easy.skin

import android.content.Context
import android.content.SharedPreferences

class SkinPreference private constructor(appContext: Context) {
    private val mPref: SharedPreferences =
        appContext.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE)

    fun reset() = mPref.edit().remove(KEY_SKIN_PATH).apply()

    var skin: String?
        get() = mPref.getString(KEY_SKIN_PATH, null)
        set(skinPath) {
            mPref.edit().putString(KEY_SKIN_PATH, skinPath).apply()
        }

    companion object {
        private const val SKIN_SHARED = "skins"
        private const val KEY_SKIN_PATH = "skin-path"

        @Volatile
        var instance: SkinPreference? = null
            private set

        fun init(appContext: Context) {
            if (instance == null) {
                synchronized(SkinPreference::class.java) {
                    if (instance == null) {
                        instance = SkinPreference(appContext.applicationContext)
                    }
                }
            }
        }
    }
}