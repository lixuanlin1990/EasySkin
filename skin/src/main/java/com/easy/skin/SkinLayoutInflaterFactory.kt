package com.easy.skin

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import java.lang.reflect.Constructor
import java.util.*


/**
 * 接管系统view的生产过程
 * @author 李轩林
 */
internal class SkinLayoutInflaterFactory(val activity: Activity) : LayoutInflater.Factory2,
    Observer {

    private var skinAttribute: SkinAttribute = SkinAttribute()

    companion object {
        val mClassPrefixList = arrayOf(
            "android.widget.",
            "android.webkit.",
            "android.app.",
            "android.view."
        )

        var mConstructorMap = HashMap<String, Constructor<out View?>>()
    }


    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        //换肤就是在需要时候替换 View的属性(src、background等)
        //所以这里创建 View,从而修改View属性
        var view = createSDKView(name, context, attrs)
        if (null == view) {
            view = createView(name, context, attrs)
        }
        //这就是我们加入的逻辑
        view?.run {
            //加载属性
            skinAttribute.look(this, attrs)
        }
        return view
    }

    private fun createSDKView(name: String, context: Context, attrs: AttributeSet): View? {
        //如果包含 . 则不是SDK中的view 可能是自定义view包括support库中的View
        if (-1 != name.indexOf('.')) {
            return null
        }
        //不包含就要在解析的 节点 name前，拼上： android.widget. 等尝试去反射
        for (element in mClassPrefixList) {
            val view = createView(element + name, context, attrs)
            if (view != null) {
                return view
            }
        }
        return null
    }

    private fun createView(name: String, context: Context, attrs: AttributeSet): View? {
        val constructor = findConstructor(context, name)
        try {
            return constructor!!.newInstance(context, attrs)
        } catch (e: Exception) {
        }
        return null
    }


    private fun findConstructor(context: Context, name: String): Constructor<out View>? {
        var constructor = mConstructorMap[name]
        if (constructor == null) {
            try {
                val clazz = context.classLoader.loadClass(name).asSubclass(
                    View::class.java
                )
                constructor = clazz.getConstructor(Context::class.java, AttributeSet::class.java)
                mConstructorMap[name] = constructor
            } catch (e: Exception) {
            }
        }
        return constructor
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }

    override fun update(o: Observable?, arg: Any?) {
//        SkinThemeUtils.updateStatusBarColor(activity);
        skinAttribute.applySkin()
    }
}