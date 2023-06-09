package com.penguinmagic.cardsgeneratorlib.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.TypedValue
import android.view.View
import androidx.core.view.drawToBitmap

object ViewUtils {

    fun dpToPx(dp: Float, context: Context?): Float {
        return try {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context?.resources?.displayMetrics)
        } catch (e: Exception) {
            0F
        }
    }

    fun createDrawableFromView(context: Context, view: View, w: Int, h: Int) : Bitmap {
        view.layout(0, 0, w, h)

        val width = view.width
        val height = view.height

        val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        view.measure(measuredWidth, measuredHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        return view.drawToBitmap(Bitmap.Config.ARGB_8888)
    }

}