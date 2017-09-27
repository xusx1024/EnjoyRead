package com.xilin.enjoyread.view

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.View.MeasureSpec.AT_MOST
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.xilin.enjoyread.R

/**
 * Fun:
 * Created by sxx.xu on 9/18/2017.
 */
class LoadingView @JvmOverloads constructor
(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(context, attributeSet, defStyleAttr) {


    private var bounds: Rect? = null
    private var mRing: Ring? = null

    private var animator: Animator? = null
    private var animatorSet: AnimatorSet? = null
    private var mIsAnimatorCancel: Boolean = false

    private var interpolator: Interpolator? = null
    private val mTempBounds: RectF = RectF()

    private var mPaint: Paint? = null
    private val DEFAULT_COLOR = 0xFF3B99DF
    private var mAnimatorStarted = false

    private var mRotation = 0F

    internal var animatorListener: AnimatorListener = object : AnimatorListener {
        /**
         *
         * Notifies the repetition of the animation.
         *
         * @param animation The animation which was repeated.
         */
        override fun onAnimationRepeat(animation: Animator?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        /**
         *
         * Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.
         *
         * @param animation The animation which reached its end.
         */
        override fun onAnimationEnd(animation: Animator?) {
            if (mIsAnimatorCancel) return
            if (animation is ValueAnimator) {
                mRing!!.sweeping = mRing!!.sweep
            } else if (animation is AnimatorSet) {
                mRing!!.restore()
                animatorSet!!.start()
            }
        }

        /**
         *
         * Notifies the cancellation of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.
         *
         * @param animation The animation which was canceled.
         */
        override fun onAnimationCancel(animation: Animator?) {
        }

        /**
         *
         * Notifies the start of the animation.
         *
         * @param animation The started animation.
         */
        override fun onAnimationStart(animation: Animator?) {
        }
    }

    init {
        mRing = Ring()
        bounds = Rect()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = mRing!!.strokeWidth

        if (attributeSet != null) {
            var a = context.obtainStyledAttributes(attributeSet, R.styleable.LoadingView, 0, 0)
            setColor(a.getInt(R.styleable.LoadingView_loadding_color, DEFAULT_COLOR.toInt()))
            setRingStyle(a.getInt(R.styleable.LoadingView_ring_style, RING_STYLE_SQUARE))
            setProgressStyle(a.getInt(R.styleable.LoadingView_progress_style, PROGRESS_STYLE_MATERIAL))
            setStrokeWidth(a.getDimension(R.styleable.LoadingView_ring_width, dp2px(STROKE_WIDTH)))
            setCenterRadius(a.getDimension(R.styleable.LoadingView_ring_radius, dp2px(CENTER_RADIUS)))
        }

    }

    fun setCenterRadius(dimension: Float) {
        mRing!!.ringCenterRadius = dimension
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    fun setStrokeWidth(dimension: Float) {
        mRing!!.strokeWidth = dimension
        mPaint!!.strokeWidth = dimension
    }


    fun setProgressStyle(style: Int) {
        when (style) {
            PROGRESS_STYLE_MATERIAL -> interpolator = FastOutSlowInInterpolator()
            PROGRESS_STYLE_LINEAR -> interpolator = LinearInterpolator()
        }
    }

    fun setRingStyle(style: Int) {
        when (style) {
            RING_STYLE_SQUARE -> mPaint!!.strokeCap = Paint.Cap.SQUARE
            RING_STYLE_ROUND -> mPaint!!.strokeCap = Paint.Cap.ROUND
        }
    }


    fun setColor(color: Int) {
        mRing!!.color = color
        mPaint!!.color = color
    }

    fun getColor(): Int {
        return mRing!!.color
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = dp2px(CIRCLE_DIAMETER.toFloat()).toInt()
        var height = dp2px(CIRCLE_DIAMETER.toFloat()).toInt()

        if (widthSpecMode == AT_MOST && heightSpecMode == AT_MOST) {
            setMeasuredDimension(width, height)
        } else if (widthSpecMode == AT_MOST) {
            setMeasuredDimension(width, heightSpecSize)
        } else if (heightSpecMode == AT_MOST) {
            setMeasuredDimension(widthSpecSize, height)
        }
    }

    /**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     *
     * @param w Current width of this view.
     * @param h Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val ring = Ring()
        ring.setInsets(w, h)
        bounds!!.set(0, 0, w, h)
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!mIsAnimatorCancel) {
            val bounds = getBounds()
            val saveCount = canvas!!.save()
            canvas.rotate(mRotation * 360, bounds.exactCenterX(), bounds.exactCenterY())
            drawRing(canvas, bounds)
            canvas.restoreToCount(saveCount)
        } else {
            canvas.restore()
        }
    }

    /**
     * This is called when the view is attached to a window.  At this point it
     * has a Surface and will start drawing.  Note that this function is
     * guaranteed to be called before [.onDraw],
     * however it may be called any time before the first onDraw -- including
     * before or after [.onMeasure].
     *
     * @see .onDetachedFromWindow
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    /**
     * This is called when the view is detached from a window.  At this point it
     * no longer has a surface for drawing.
     *
     * @see .onAttachedToWindow
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

    /**
     * Called when the visibility of the view or an ancestor of the view has
     * changed.
     *
     * @param changedView The view whose visibility changed. May be
     * `this` or an ancestor view.
     * @param visibility The new visibility, one of [.VISIBLE],
     * [.INVISIBLE] or [.GONE].
     */
    override fun onVisibilityChanged(changedView: View?, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE)
            start()
        else
            stop()
    }

    /**
     * Hook allowing a view to generate a representation of its internal state
     * that can later be used to create a new instance with that same state.
     * This state should only contain information that is not persistent or can
     * not be reconstructed later. For example, you will never store your
     * current position on screen because that will be computed again when a
     * new instance of the view is placed in its view hierarchy.
     *
     *
     * Some examples of things you may store here: the current cursor position
     * in a text view (but usually not the text itself since that is stored in a
     * content provider or other persistent storage), the currently selected
     * item in a list view.
     *
     * @return Returns a Parcelable object containing the view's current dynamic
     * state, or null if there is nothing interesting to save. The
     * default implementation returns null.
     * @see .onRestoreInstanceState
     * @see .saveHierarchyState
     * @see .dispatchSaveInstanceState
     * @see .setSaveEnabled
     */
    override fun onSaveInstanceState(): Parcelable {
        var parcelable = super.onSaveInstanceState()
        var state = SavedState(parcelable)
        state.ring = mRing
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var savedState = state as SavedState
        super.onRestoreInstanceState(state)
        mRing = savedState.ring
    }

    fun stop() {
        mIsAnimatorCancel = true
        if (animator != null) {
            animator!!.end()
            animator!!.cancel()
        }
        if (animatorSet != null) {
            animatorSet!!.end()
            animatorSet!!.cancel()
        }
        animator = null
        animatorSet = null

        mAnimatorStarted = false
        mRing!!.reset()
        mRotation = 0F
        invalidate()
    }

    fun start() {
        if (mAnimatorStarted) return
        if (animator == null || animatorSet == null) {
            mRing!!.reset()
            buildAnimator()
        }
        animator!!.start()
        animatorSet!!.start()
        mAnimatorStarted = true
        mIsAnimatorCancel = false
    }

    fun buildAnimator() {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(ANIMATOR_DURATION)
        valueAnimator.repeatCount = -1
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener {
            mRotation = valueAnimator.getAnimatedValue() as Float
            invalidate()
        }
        animator = valueAnimator
        animatorSet = buildFlexibleAnimation()
        animatorSet!!.addListener(animatorListener)

    }

    private fun buildFlexibleAnimation(): AnimatorSet {
        val ring = mRing
        var set = AnimatorSet()
        var increment = ValueAnimator.ofFloat(0f, MAX_PROGRESS_ARC - MIN_PROGRESS_ARC).setDuration(ANIMATOR_DURATION / 2)
        increment.interpolator = LinearInterpolator()
        increment.addUpdateListener { animation ->
            var sweeping = ring!!.sweeping
            val value = animation.animatedValue as Float
            ring!!.sweep = sweeping.plus(value)
            invalidate()
        }
        increment.addListener(animatorListener)
        var reduce = ValueAnimator.ofFloat(0f, MAX_PROGRESS_ARC - MIN_PROGRESS_ARC).setDuration(ANIMATOR_DURATION / 2)
        reduce.interpolator = interpolator
        reduce.addUpdateListener { animation ->

            var sweeping = ring!!.sweeping
            var starting = ring!!.starting
            var value = animation.animatedValue as Float
            ring.sweep = sweeping.plus(value)
            ring.start = starting.plus(value)
        }
        set.play(reduce).after(increment)
        return set
    }


    private fun drawRing(canvas: Canvas, bounds: Rect) {
        val arcBounds = mTempBounds
        val ring = mRing
        arcBounds.set(bounds)
        arcBounds.inset(ring!!.strokeInset, ring.strokeInset)
        canvas.drawArc(arcBounds, ring!!.start, ring.sweep, false, mPaint)
    }

    fun getBounds(): Rect {
        return bounds!!
    }


    internal class Ring() : Parcelable {

        var strokeInset = 0f
        var strokeWidth = 0f
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
                insets = Math.ceil((strokeWidth / 2.0f).toDouble()).toFloat()
            } else {

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

    internal class SavedState : BaseSavedState {
        var ring: Ring? = null

        constructor(superState: Parcelable) : super(superState) {}
        constructor(parcel: Parcel) : super(parcel) {
            ring = parcel.readParcelable(Ring::class.java.classLoader)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeParcelable(ring, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }

    }

    companion object {
        private val CIRCLE_DIAMETER = 56//直径默认值
        private val CENTER_RADIUS = 15f//半径默认值
        private val STROKE_WIDTH = 3.5f//边框宽度默认值
        private val MAX_PROGRESS_ARC = 300f
        private val MIN_PROGRESS_ARC = 20f
        private val ANIMATOR_DURATION = 1332L
        val RING_STYLE_SQUARE = 0
        val RING_STYLE_ROUND = 1
        val PROGRESS_STYLE_MATERIAL = 0
        val PROGRESS_STYLE_LINEAR = 1
    }
}