package com.xilin.enjoyread.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toolbar
import com.xilin.enjoyread.R
import com.xilin.enjoyread.utils.StatusBarCompat
import com.xilin.enjoyread.view.CustomDialog

/**
 * 模板Activity
 * Created by Administrator on 2017/9/17.
 */

abstract class BaseActivity : AppCompatActivity() {
    var mCommonToolbar: Toolbar? = null
    protected var mContext: Context? = null
    protected var statusBarColor = 0
    protected var statusBarView: View? = null
    private var mNowMode: Boolean = false
    private var dialog: CustomDialog? = null

    /**
     * Same as [.onCreate] but called for those activities created with
     * the attribute [android.R.attr.persistableMode] set to
     * `persistAcrossReboots`.
     *
     * @param savedInstanceState if the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in [.onSaveInstanceState].
     * ***Note: Otherwise it is null.***
     * @param persistentState if the activity is being re-initialized after
     * previously being shut down or powered off then this Bundle contains the data it most
     * recently supplied to outPersistentState in [.onSaveInstanceState].
     * ***Note: Otherwise it is null.***
     *
     * @see .onCreate
     * @see .onStart
     *
     * @see .onSaveInstanceState
     *
     * @see .onRestoreInstanceState
     *
     * @see .onPostCreate
     */
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(getLayoutId())
        if (statusBarColor == 0) {
            statusBarView = StatusBarCompat.compat(this,
                    ContextCompat.getColor(this, R.color.colorPrimaryDark))
        } else if (statusBarColor != -1) {
            statusBarView = StatusBarCompat.compat(this, statusBarColor)
        }
    }

    protected abstract fun getLayoutId(): Int
}
