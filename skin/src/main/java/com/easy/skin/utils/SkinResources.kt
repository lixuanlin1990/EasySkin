package com.easy.skin.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.AnyRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

internal class SkinResources private constructor(appContext: Context) {
    //app原始resources
    private val mAppResources: Resources = appContext.resources

    //皮肤包resources
    private var mSkinResources: Resources? = null

    //皮肤包包名
    private var mSkinPkgName: String? = null
    private var isDefaultSkin = true
    fun applySkin(resources: Resources?, pkgName: String?) {
        mSkinResources = resources
        mSkinPkgName = pkgName
        isDefaultSkin = pkgName.isNullOrBlank() || resources == null
    }

    fun reset() {
        mSkinResources = null
        mSkinPkgName = ""
        isDefaultSkin = true
    }

    private fun getIdentifier(@AnyRes resId: Int): Int {
        if (isDefaultSkin)
            return resId
        val resName = mAppResources.getResourceEntryName(resId)
        val resType = mAppResources.getResourceTypeName(resId)
        return mSkinResources?.getIdentifier(resName, resType, mSkinPkgName) ?: resId
    }

    private fun getColor(@ColorRes resId: Int): Int {
        if (isDefaultSkin)
            return mAppResources.getColor(resId)
        val skinId = getIdentifier(resId)
        return if (skinId == 0) mAppResources.getColor(resId)
        else mSkinResources?.getColor(resId) ?: mAppResources.getColor(resId)
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    fun getColorStateList(resId: Int): ColorStateList {
        if (isDefaultSkin)
            return mAppResources.getColorStateList(resId)
        val skinId = getIdentifier(resId)
        return if (skinId == 0) mAppResources.getColorStateList(resId)
        else mSkinResources?.getColorStateList(resId) ?: mAppResources.getColorStateList(resId)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawable(@DrawableRes resId: Int): Drawable {
        if (isDefaultSkin)
            return mAppResources.getDrawable(resId)

        val skinId = getIdentifier(resId)
        return if (skinId == 0) mAppResources.getDrawable(resId)
        else mSkinResources?.getDrawable(resId) ?: mAppResources.getDrawable(resId)
    }

    fun getBackground(resId: Int): Any? {
        return when (mAppResources.getResourceTypeName(resId)) {
            "color" -> getColor(resId)
            "drawable", "mipmap" -> getDrawable(resId)
            else -> null
        }
    }

    companion object {
        //单例
        @Volatile
        var instance: SkinResources? = null
            private set

        fun init(appContext: Context) {
            if (instance == null) {
                synchronized(SkinResources::class.java) {
                    if (instance == null) {
                        instance = SkinResources(appContext)
                    }
                }
            }
        }
    }
}