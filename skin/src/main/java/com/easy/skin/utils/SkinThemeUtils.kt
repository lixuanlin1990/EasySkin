package com.easy.skin.utils

import android.content.Context

/**
 * 工具类
 * @author 李轩林
 */
internal object SkinThemeUtils {
    fun getResId(context: Context, attrs: IntArray): IntArray {
        val resIds = IntArray(attrs.size)
        val ta = context.obtainStyledAttributes(attrs)
        for (i in attrs.indices) {
            resIds[i] = ta.getResourceId(i, 0)
        }
        ta.recycle()
        return resIds
    }
}