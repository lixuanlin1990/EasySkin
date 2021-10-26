package com.easy.skin

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.easy.skin.interfaces.SkinViewSupport
import com.easy.skin.utils.SkinResources
import com.easy.skin.utils.SkinThemeUtils

/**
 * 属性
 * @author 李轩林
 */
internal class SkinAttribute {
    companion object {
        val mAttributes: List<String> = arrayListOf(
            "background",
            "src",
            "textColor",
            "drawableLeft",
            "drawableStart",
            "drawableTop",
            "drawableRight",
            "drawableEnd",
            "drawableBottom"
        )
    }

    var skinViews: ArrayList<SkinView> = arrayListOf()

    fun look(view: View, attrs: AttributeSet) {
        val skinPairs: ArrayList<SkinPair> = arrayListOf()
        for (i in 0 until attrs.attributeCount) {
            //获得属性名  textColor/background
            val attributeName = attrs.getAttributeName(i)
            if (mAttributes.contains(attributeName)) {
                // 获取属性值
                val attributeValue = attrs.getAttributeValue(i)
                if (attributeValue.startsWith("#"))
                    continue
                val resId = if (attributeValue.startsWith("?")) {
                    // 以 ？开头的表示使用 属性
                    val attrId = Integer.parseInt(attributeValue.substring(1))
                    SkinThemeUtils.getResId(view.context, intArrayOf(attrId))[0]
                } else {
                    // 正常以 @ 开头
                    Integer.parseInt(attributeValue.substring(1))
                }
                val skinPair = SkinPair(attributeName, resId)
                skinPairs.add(skinPair)
            }
        }
        if (skinPairs.isNotEmpty() || view is SkinViewSupport) {
            val skinView = SkinView(view, skinPairs)
            skinView.applySkin()
            skinViews.add(skinView)
        }
    }

    fun applySkin() {
        for (skinView in skinViews) {
            skinView.applySkin()
        }
    }
}

internal class SkinPair(val attributeName: String, val resId: Int)

internal class SkinView(private val view: View, private val skinPairs: List<SkinPair>) {

    fun applySkin() {
        applySkinSupport()
        for (skinPair in skinPairs) {
            var left: Drawable? = null
            var start: Drawable? = null
            var top: Drawable? = null
            var right: Drawable? = null
            var end: Drawable? = null
            var bottom: Drawable? = null
            when (skinPair.attributeName) {
                "background" -> {
                    val background = SkinResources.instance?.getBackground(skinPair.resId)
                    if (background is Int) {
                        view.setBackgroundColor(background)
                    } else if (background is Drawable) {
                        ViewCompat.setBackground(view, background)
                    }
                }
                "src" -> {
                    val background = SkinResources.instance?.getBackground(skinPair.resId)
                    if (background is Int) {
                        (view as? ImageView)?.setImageDrawable(ColorDrawable(background))
                    } else if (background is Drawable) {
                        (view as? ImageView)?.setImageDrawable(background)
                    }
                }
                "textColor" -> {
                    (view as? TextView)?.setTextColor(
                        SkinResources.instance?.getColorStateList(
                            skinPair.resId
                        )
                    )
                }
                "drawableLeft" -> left = SkinResources.instance?.getDrawable(skinPair.resId)
                "drawableStart" -> start = SkinResources.instance?.getDrawable(skinPair.resId)
                "drawableTop" -> top = SkinResources.instance?.getDrawable(skinPair.resId)
                "drawableRight" -> right = SkinResources.instance?.getDrawable(skinPair.resId)
                "drawableEnd" -> end = SkinResources.instance?.getDrawable(skinPair.resId)
                "drawableBottom" -> bottom = SkinResources.instance?.getDrawable(skinPair.resId)
            }
            if (null != left || null != right || null != top || null != bottom) {
                (view as? TextView)?.setCompoundDrawablesWithIntrinsicBounds(
                    left,
                    top,
                    right,
                    bottom
                )
            }
            if (null != start || null != end || null != top || null != bottom) {
                (view as? TextView)?.setCompoundDrawablesWithIntrinsicBounds(
                    start,
                    top,
                    end,
                    bottom
                )
            }
        }
    }

    private fun applySkinSupport() {
        if (view is SkinViewSupport) {
            view.applySkin()
        }
    }
}