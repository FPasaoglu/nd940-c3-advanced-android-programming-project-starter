package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.BackgroundColorSpan
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var loadText: String = context.getString(R.string.button_loading)
    private var baseText: String = context.getString(R.string.button_download)
    private var loadingColor: Int = context.getColor(R.color.colorPrimaryDark)
    private var baseColor: Int = context.getColor(R.color.colorPrimary)
    private var loadCircleColor: Int = context.getColor(R.color.colorAccent)
    private var textColor: Int = context.getColor(R.color.white)

    private var widthSize = 0
    private var heightSize = 0

    private var loadingWidth = 0

     private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                ValueAnimator.ofInt(0, measuredWidth).apply {
                    duration = 2000
                    addUpdateListener {
                        loadingWidth = it.animatedValue as Int
                        // buttonState = ButtonState.Completed
                        this@LoadingButton.invalidate()
                    }
                    doOnEnd {
                        buttonState = ButtonState.Completed
                        this@LoadingButton.invalidate()
                    }
                    start()
                }
            }

        }
    }


    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            loadText = getString(R.styleable.LoadingButton_loadText) ?: loadText
            baseText = getString(R.styleable.LoadingButton_baseText) ?: baseText
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, loadingColor)
            baseColor = getColor(R.styleable.LoadingButton_baseColor, baseColor)
            loadCircleColor = getColor(R.styleable.LoadingButton_loadingCircleColor, loadCircleColor)
            textColor = getColor(R.styleable.LoadingButton_textColor, textColor)

        }
    }

    val paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLUE
        textSize = 45f
        textAlign = Paint.Align.CENTER
    }


    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)
        paint.color = baseColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.color = loadingColor
        canvas.drawRect(0f, 0f, loadingWidth.toFloat(), height.toFloat(), paint)

        paint.color = textColor
        if (buttonState == ButtonState.Completed) {
            canvas.drawText(baseText, width / 2f, (height + 30) / 2f, paint)
        } else {
            canvas.drawText(loadText, width / 2f, (height + 30) / 2f, paint)
            paint.color = loadCircleColor
            canvas.drawArc(
                width / 1.2f,
                heightSize / 4f,
                (width / 1.2f + 80f),
                (heightSize / 4f) + 80f,
                0f,
                normalize(loadingWidth),
                true,
                paint
            )
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


    fun normalize(
        input: Int,
        minValue: Float = 0f,
        maxValue: Float = measuredWidth.toFloat()
    ): Float {
        return ((input - minValue) / (maxValue - minValue) * 360)
    }

    fun setState(buttonState: ButtonState) {
        this.buttonState = buttonState
    }
}