package com.easy.skin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import androidx.collection.ArrayMap
import androidx.core.view.LayoutInflaterCompat
import java.lang.reflect.Field
import java.util.*


/**
 * 生命周期监听
 * @author 李轩林
 */
internal class ApplicationActivityLifecycle(private val observable: Observable) :
    Application.ActivityLifecycleCallbacks {
    private val mLayoutInflaterFactories: ArrayMap<Activity, SkinLayoutInflaterFactory> = ArrayMap()
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
//        /**
//         * 更新状态栏
//         */
//        SkinThemeUtils.updateStatusBarColor(activity)

        //获得Activity的布局加载器
        val layoutInflater = activity.layoutInflater

        try {
            //Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
            //如设置过抛出一次
            //设置 mFactorySet 标签为false
            val field: Field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            field.isAccessible = true
            field.setBoolean(layoutInflater, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //使用factory2 设置布局加载工程
        val skinLayoutInflaterFactory = SkinLayoutInflaterFactory(activity)
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory)
        mLayoutInflaterFactories[activity] = skinLayoutInflaterFactory

        observable.addObserver(skinLayoutInflaterFactory)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        val observer = mLayoutInflaterFactories.remove(activity)
        observable.deleteObserver(observer)
    }
}