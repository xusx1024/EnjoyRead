package com.xilin.enjoyread.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Fun:
 * Created by sxx.xu on 9/18/2017.
 */
class LoadingView @JvmOverloads constructor
(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int = 0) :
        View(context, attributeSet, defStyleAttr) {

    companion object {
        private val CIRCLE_DIAMETER = 56//直径默认值
        private val CENTER_RADIUS = 15f//半径默认值
        private val STROKE_WIDTH = 3.5f//边框宽度默认值
        private val MAX_PROGRESS_ARC


    }

}