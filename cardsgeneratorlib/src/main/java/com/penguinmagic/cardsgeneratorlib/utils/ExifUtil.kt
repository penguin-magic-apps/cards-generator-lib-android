package com.penguinmagic.cardsgeneratorlib.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.core.net.toUri
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException

object ExifUtil {

    fun rotateBitmap(src: File, bitmap: Bitmap): Bitmap {
        try {
            val orientation = getExifOrientation(src)
            if (orientation == 1) {
                return bitmap
            }
            val matrix = Matrix()
            when (orientation) {
                2 -> matrix.setScale(-1F, 1F)
                3 -> matrix.setRotate(180F)
                4 -> {
                    matrix.setRotate(180F)
                    matrix.postScale(-1F, 1F)
                }
                5 -> {
                    matrix.setRotate(90F)
                    matrix.postScale(-1F, 1F)
                }
                6 -> matrix.setRotate(90F)
                7 -> {
                    matrix.setRotate(-90F)
                    matrix.postScale(-1F, 1F)
                }
                8 -> matrix.setRotate(-90F)
                else -> return bitmap
            }
            return try {
                val oriented =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                bitmap.recycle()
                oriented
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    @Throws(IOException::class)
    private fun getExifOrientation(src: File): Int {
        var orientation = 1
        try {
                val exifInterface = ExifInterface(src.path)
                orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return orientation
    }
}