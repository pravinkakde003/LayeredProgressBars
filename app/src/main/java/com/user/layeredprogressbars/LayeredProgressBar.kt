package com.user.layeredprogressbars

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.user.layeredprogressbars.Utils.sp2px


class LayeredProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {
    // Default values variables
    private val defaultInnerStrokeWidth: Float = sp2px(resources, 5f)
    private val defaultInnerStrokeUnfinishedWidth: Float = sp2px(resources, 5f)
    private val defaultOuterStrokeWidth: Float = sp2px(resources, 5f)
    private val defaultOuterStrokeUnfinishedWidth: Float = sp2px(resources, 5f)
    private val defaultProgressBarUnfinishedColor: Int = Color.GRAY
    private var defaultProgressBarFilledColor: Int
    private val defaultProgressBarOverallProgress: Float
    private val defaultProgressBarInnerThirdProgress: Float
    private var innerStrokeWidth = 0f
    private var innerStrokeWidthUnfinished = 0f
    private var outerStrokeWidth = 0f
    private var outerStrokeWidthUnfinished = 0f
    private var progressBarOuterColor = 0
    private var progressBarInnerColor = 0
    private var progressBarUnfinishedColor = 0
    private var progressBarOverallProgress = 0f
    private var progressBarSpeedProgress = 0f
    private val emptyArcAngle = 360f
    private var progressBarPaint: Paint? = null
    private var progressBarOuter: RectF? = null
    private var progressBarInner: RectF? = null
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        progressBarOuter!![outerStrokeWidth / 2f, outerStrokeWidth / 2f, MeasureSpec.getSize(widthMeasureSpec) - outerStrokeWidth / 2f] = MeasureSpec.getSize(heightMeasureSpec) - outerStrokeWidth / 2f
        progressBarInner!![progressBarOuter!!.left + (outerStrokeWidth + innerStrokeWidth) / 0.7f, progressBarOuter!!.top + (outerStrokeWidth + innerStrokeWidth) / 0.7f, progressBarOuter!!.right - (outerStrokeWidth + innerStrokeWidth) / 0.7f] = progressBarOuter!!.bottom - (outerStrokeWidth + innerStrokeWidth) / 0.7f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw empty progressBar
        progressBarPaint!!.color = progressBarUnfinishedColor
        progressBarPaint!!.strokeWidth = outerStrokeWidthUnfinished
        val startAngle = 270f
        canvas.drawArc(progressBarOuter!!, startAngle, emptyArcAngle, false, progressBarPaint!!)
        progressBarPaint!!.strokeWidth = innerStrokeWidthUnfinished
        canvas.drawArc(progressBarInner!!, startAngle, emptyArcAngle, false, progressBarPaint!!)
        val highlighted = false
        if (!highlighted) { // Draw filled progressBar
            progressBarPaint!!.strokeWidth = outerStrokeWidth
            progressBarPaint!!.color = progressBarOuterColor
            canvas.drawArc(progressBarOuter!!, startAngle, progressBarOverallProgress, false, progressBarPaint!!)
            progressBarPaint!!.strokeWidth = innerStrokeWidth
            progressBarPaint!!.color = progressBarInnerColor
            canvas.drawArc(progressBarInner!!, startAngle, progressBarSpeedProgress, false, progressBarPaint!!)
        } else {
            val highlightedProgressBar: Short = -1
            when (highlightedProgressBar) {
                OUTER_PROGRESS_BAR -> {
                    // Draw filled progressBar
                    progressBarPaint!!.strokeWidth = outerStrokeWidth
                    progressBarPaint!!.color = progressBarOuterColor
                    canvas.drawArc(progressBarOuter!!, startAngle, progressBarOverallProgress, false, progressBarPaint!!)
                    progressBarPaint!!.strokeWidth = innerStrokeWidth
                    progressBarPaint!!.color = defaultProgressBarFilledColor
                    canvas.drawArc(progressBarInner!!, startAngle, progressBarSpeedProgress, false, progressBarPaint!!)
                }
                INNER_PROGRESS_BAR -> {
                    // Draw filled progressBar
                    progressBarPaint!!.strokeWidth = innerStrokeWidth
                    progressBarPaint!!.color = progressBarInnerColor
                    canvas.drawArc(progressBarOuter!!, startAngle, progressBarSpeedProgress, false, progressBarPaint!!)
                    progressBarPaint!!.color = defaultProgressBarFilledColor
                    progressBarPaint!!.strokeWidth = outerStrokeWidth
                    canvas.drawArc(progressBarOuter!!, startAngle, progressBarOverallProgress, false, progressBarPaint!!)
                }
                else -> throw IllegalArgumentException("Use one of the constants provided to " +
                        "highlight a progressBar: OUTER_PROGRESS_BAR OR INNER_PROGRESS_BAR")
            }
        }
    }

    private fun initByAttributes(attributes: TypedArray) {
        innerStrokeWidth = attributes.getDimension(R.styleable.customProgressBars_progress_bar_inner_stroke_width, defaultInnerStrokeWidth)
        innerStrokeWidthUnfinished = attributes.getDimension(R.styleable.customProgressBars_progress_bar_inner_stroke_width_unfinished, defaultInnerStrokeUnfinishedWidth)
        outerStrokeWidth = attributes.getDimension(R.styleable.customProgressBars_progress_bar_outer_stroke_width, defaultOuterStrokeWidth)
        outerStrokeWidthUnfinished = attributes.getDimension(R.styleable.customProgressBars_progress_bar_outer_stroke_width_unfinished, defaultOuterStrokeUnfinishedWidth)
        progressBarUnfinishedColor = attributes.getColor(R.styleable.customProgressBars_progress_bar_unfinished_color, defaultProgressBarUnfinishedColor)
        defaultProgressBarFilledColor = attributes.getColor(R.styleable.customProgressBars_progress_bar_default_filled_color, defaultProgressBarFilledColor)
        progressBarOuterColor = attributes.getColor(R.styleable.customProgressBars_progress_bar_outer_color, defaultProgressBarFilledColor)
        progressBarInnerColor = attributes.getColor(R.styleable.customProgressBars_progress_bar_inner_color, defaultProgressBarFilledColor)
        setOuterProgressBarProgress(attributes.getFloat(R.styleable.customProgressBars_progress_bar_outer_progress, defaultProgressBarOverallProgress), false)
        setInnerProgressBarProgress(attributes.getFloat(R.styleable.customProgressBars_progress_bar_inner_progress, defaultProgressBarInnerThirdProgress), false)
    }

    private fun initPainters() { // progressBar Rectangle objects
        progressBarOuter = RectF()
        progressBarInner = RectF()
        // progressBar Paint
        progressBarPaint = Paint()
        progressBarPaint!!.isAntiAlias = true
        progressBarPaint!!.style = Paint.Style.STROKE
        progressBarPaint!!.strokeCap = Paint.Cap.ROUND
    }

    fun setOuterProgressBarProgress(outerProgress: Float, invalidate: Boolean) {
        progressBarOverallProgress = emptyArcAngle / 100f * outerProgress
        if (invalidate) invalidate()
    }

    fun setInnerProgressBarProgress(innerProgress: Float, invalidate: Boolean) {
        progressBarSpeedProgress = emptyArcAngle / 100f * innerProgress
        if (invalidate) invalidate()
    }

    fun setOuterProgressBarColor(colorInHexFormat: String, invalidate: Boolean) {
        progressBarOuterColor = Color.parseColor(colorInHexFormat)
        if (invalidate) invalidate()
    }

    fun setInnerProgressBarColor(colorInHexFormat: String, invalidate: Boolean) {
        progressBarInnerColor = Color.parseColor(colorInHexFormat)
        if (invalidate) invalidate()
    }

    fun setProgressBarUnfinishedColor(colorInHexFormat: String, invalidate: Boolean) {
        progressBarUnfinishedColor = Color.parseColor(colorInHexFormat)
        if (invalidate) invalidate()
    }


    fun setProgressBarDefaultFilledColor(colorInHexFormat: String, invalidate: Boolean) {
        defaultProgressBarFilledColor = Color.parseColor(colorInHexFormat)
        if (invalidate) invalidate()
    }


    companion object {
        const val OUTER_PROGRESS_BAR: Short = 1
        const val INNER_PROGRESS_BAR: Short = 2
    }

    init {
        // Default values initialization
        defaultProgressBarFilledColor = Color.parseColor("#E6E6E6")
        defaultProgressBarOverallProgress = 0f
        defaultProgressBarInnerThirdProgress = 0f
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.customProgressBars, defStyle, 0)
        initByAttributes(attributes)
        attributes.recycle()
        initPainters()
    }
}