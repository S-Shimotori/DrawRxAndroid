package net.terminal_end.drawrxandroid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.jakewharton.rxbinding.view.RxView

/**
 * Created by S-Shimotori on 5/15/16.
 */

class DrawingView(context: Context, attrs: AttributeSet?): View(context, attrs) {
    private val paint = Paint()
    private val path = Path()

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeWidth = 10.0f

        RxView.touches(this).subscribe {
            val x = it.x
            val y = it.y

            when(it.action) {
                MotionEvent.ACTION_DOWN -> path.moveTo(x, y)
                MotionEvent.ACTION_MOVE -> path.lineTo(x, y)
                MotionEvent.ACTION_UP -> path.lineTo(x, y)
            }

            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    fun delete() {
        path.reset()
        invalidate()
    }
}