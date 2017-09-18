package com.xilin.enjoyread.base

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.widget.Toolbar
import com.xilin.enjoyread.view.CustomDialog

/**
 * 模板Activity
 * Created by Administrator on 2017/9/17.
 */

abstract class BaseActivity : AppCompatActivity() {
    var mCommonToolbar: Toolbar? = null
    protected var mContext: Context? = null
    private var mNowMode: Boolean = false
    private var dialog: CustomDialog? = null

}
