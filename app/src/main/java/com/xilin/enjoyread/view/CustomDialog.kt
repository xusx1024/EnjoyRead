package com.xilin.enjoyread.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.xilin.enjoyread.R

/**
 * Fun:自定义的加载对话框
 * Created by sxx.xu on 9/18/2017.
 */
class CustomDialog @JvmOverloads constructor(context: Context, themeResId: Int = 0) : Dialog(context, themeResId) {

    companion object {
        fun instance(activity: Activity): CustomDialog {
            val view: LoadingView = View.inflate(activity, R.layout.common_progress, null) as LoadingView
            view.setColor(ContextCompat.getColor(activity, R.color.reader_menu_bg_color))
            val dialog = CustomDialog(activity, R.style.loading_dialog)
            dialog.setContentView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            return dialog
        }
    }

}

