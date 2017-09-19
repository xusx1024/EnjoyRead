package com.xilin.enjoyread.view

import android.content.Context
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
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
        private val MAX_PROGRESS_ARC = 300f
        private val MIN_PROGRESS_ARC = 20f
        private val ANIMATOR_DURATION = 1332L

        class Ring() : Parcelable {

            var strokeInset = 0f
            var stokeWidth = 0f
            var ringCenterRadius = 0f
            var start = 0f
            var end = 0f
            var sweep = 0f
            var sweeping = MIN_PROGRESS_ARC

            var starting = 0f
            var ending = 0f
            var color = 0

            fun restore() {
                starting = start
                sweeping = sweep
                ending = end
            }

            fun reset() {
                end = 0f
                start = 0f
                sweeping = MIN_PROGRESS_ARC
                sweep = 0f
                starting = 0f
            }

            fun setInsets(width: Int, height: Int) {
                val minEdge = Math.min(width, height).toFloat()
                var insets = 0f
                if (ringCenterRadius <= 0 || minEdge < 0) {
                    insets = Math.ceil((stokeWidth / 2.0f).toDouble()).toFloat()
                }else{

                }
            }

            constructor(parcel: Parcel) : this() {
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {

            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<Ring> {
                override fun createFromParcel(parcel: Parcel): Ring {
                    return Ring(parcel)
                }

                override fun newArray(size: Int): Array<Ring?> {
                    return arrayOfNulls(size)
                }
            }

        }
    }

    private var bounds: Rect? = null
}