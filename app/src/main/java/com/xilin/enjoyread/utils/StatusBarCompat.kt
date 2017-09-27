package com.xilin.enjoyread.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.xilin.enjoyread.R

/**
 * Fun:
 * Created by sxx.xu on 9/27/2017.
 */
object StatusBarCompat {

    private val INVALID_VALUE = -1

    fun compat(activity: Activity, statusColor: Int): View? {

        var color = ContextCompat.getColor(activity, R.color.colorPrimaryDark)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VALUE) {
                color = statusColor
            }
            activity.window.statusBarColor = color
            return null
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            var contentView: ViewGroup = activity.findViewById(android.R.id.content) as ViewGroup
            if (statusColor != INVALID_VALUE) {
                color = statusColor
            }
            var statusBarView = contentView.getChildAt(0)
            if (statusBarView != null && statusBarView.measuredHeight == getStatusBarHeight(activity)) {
                statusBarView.setBackgroundColor(color)
                return statusBarView
            }
            statusBarView = View(activity)
            var lp = ViewGroup.LayoutParams(MATCH_PARENT, getStatusBarHeight(activity))
            statusBarView.setBackgroundColor(color)
            contentView.addView(statusBarView, lp)
            return statusBarView

        }
        return null
    }

    fun compat(activity: Activity) {
        compat(activity, INVALID_VALUE)
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        var resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}