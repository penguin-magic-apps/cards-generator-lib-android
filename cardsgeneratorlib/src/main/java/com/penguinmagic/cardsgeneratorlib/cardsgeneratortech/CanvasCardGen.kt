package com.penguinmagic.cardsgeneratorlib.cardsgeneratortech

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import androidx.core.graphics.scale
import com.penguinmagic.cardsgeneratorlib.R

import com.penguinmagic.cardsgeneratorlib.model.cards.Card
import com.penguinmagic.cardsgeneratorlib.utils.ViewUtils.bitmap
import com.penguinmagic.cardsgeneratorlib.utils.ViewUtils.rotate
import kotlinx.android.synthetic.main.photo_layout.view.ivBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.File
import java.io.Serializable
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random


class CanvasCardGen(val context: Context) {


    companion object {
        private const val CARD_BITMAP_BASE_WIDTH = 70
        private const val  CARD_BITMAP_BASE_HEIGHT = 100
    }

    private val photoLayout: View by lazy {
        LayoutInflater.from(this.context).inflate(R.layout.photo_layout, null, false)
    }

    private var CARD_BITMAP_WIDTH = CARD_BITMAP_BASE_WIDTH
    private var CARD_BITMAP_HEIGHT = CARD_BITMAP_BASE_HEIGHT

    private val maxRotation = 50f
    private val minRotation = 0f

    private val baseRadius = 0.25



    private fun addCardsOnLayout(
        cards: List<Card>,
        cardScale: Float,
        rotation: Float,
        backgroundPos: Int,
        preLoadedBackgrounds : ArrayList<Int>,
        customBackgrounds: ArrayList<String>
    ): Bitmap {



        var isActualCard: Boolean
        val clampedRotation = rotation.coerceIn(minRotation, maxRotation)
        val radiusFactor = getRadiusFactor(cardScale)


        // apply scale
        CARD_BITMAP_WIDTH += getPercent(CARD_BITMAP_WIDTH.toFloat(), cardScale).toInt()
        CARD_BITMAP_HEIGHT += getPercent(CARD_BITMAP_HEIGHT.toFloat(), cardScale).toInt()


        val originalWidth = context.resources.displayMetrics.widthPixels.toFloat()
        val originalHeight = context.resources.displayMetrics.heightPixels.toFloat()

        // width adjustment based on cardScaling
        val widthAdjustment = getPercent(originalWidth, 50f) / (cardScale / 10f)
        val heightAdjustment = getPercent(originalHeight, 50f) / (cardScale / 10f)

        val cardBoundsWidth = originalWidth - widthAdjustment
        val cardsBoundHeight = originalHeight - heightAdjustment


        val backgroundBitmap = getBackgroundBitmap(backgroundPos,preLoadedBackgrounds,customBackgrounds)
            .copy(Bitmap.Config.ARGB_8888, true).scale(
                getBackgroundImageScale(originalWidth, cardBoundsWidth),
                getBackgroundImageScale(originalHeight, cardsBoundHeight)
            )


        val paint = Paint()
        val canvas = Canvas(backgroundBitmap)


        val screenWidth = getPercent(cardBoundsWidth, 80f)
        val arcCenterY = backgroundBitmap.height / 2  // Middle of the screen
        val paddingLeft = (originalWidth - getCoordinatesAt(
            52,
            isActualCard = false,
            screenWidth,
            arcCenterY,
            radiusFactor,
            clampedRotation
        ).x) / 2


        for (i in 1..52) {
            val cardRotation = randomRotation(i) + clampedRotation
            val cardBitmap = if (cards.any { it.position == i }) {
                isActualCard = true
                val id = cards.first { it.position == i }.picture
                BitmapFactory.decodeResource(context.resources, id)
                    .scale(CARD_BITMAP_WIDTH, CARD_BITMAP_HEIGHT)
                    .rotate(cardRotation)
            } else {
                isActualCard = false
                BitmapFactory.decodeResource(context.resources, R.mipmap.card_back)
                    .scale(CARD_BITMAP_WIDTH, CARD_BITMAP_HEIGHT)
                    .rotate(cardRotation)
            }


            val coordinates = getCoordinatesAt(
                i,
                isActualCard,
                screenWidth,
                arcCenterY,
                radiusFactor,
                clampedRotation,
                paddingLeft
            )


            canvas.drawBitmap(cardBitmap, coordinates.x, coordinates.y, paint)
        }

        return backgroundBitmap
    }


    fun calculateArcLength(xValues: List<Double>, yValues: List<Double>): Double {
        var arcLength = 0.0
        for (i in 1 until xValues.size) {
            val dx = xValues[i] - xValues[i - 1]
            val dy = yValues[i] - yValues[i - 1]
            arcLength += sqrt(dx.pow(2) + dy.pow(2))
        }
        return arcLength
    }


    fun getBackgroundBitmap(backgroundPos: Int, preLoadedBackgrounds: ArrayList<Int>, customBackgrounds : ArrayList<String>): Bitmap{
        return if(backgroundPos < preLoadedBackgrounds.size ){
           BitmapFactory.decodeResource(context.resources, preLoadedBackgrounds[backgroundPos])
        }else{
            File(customBackgrounds[backgroundPos - preLoadedBackgrounds.size]).bitmap
        }
    }

    private fun getCoordinatesAt(
        index: Int,
        isActualCard: Boolean,
        screenWidth: Float,
        arcCenterY: Int,
        radiusFactor: Double,
        clampedRotation: Float,
        padding: Float? = null
    ): Coordinates {

        val angle = (index.toDouble() / 52.toDouble()) * PI  // Angle from 0 to PI
        var x = (screenWidth.toDouble() / (52 - 1)) * (index - 1)  // X value from 0 to screen width
        var y = arcCenterY - (arcCenterY * radiusFactor * sin(angle))  // Y value for the arc



        if (isActualCard) {
            if (index <= 25) {
                x -= (CARD_BITMAP_WIDTH / 2).toDouble()
            } else {
                x += (CARD_BITMAP_WIDTH / 2).toDouble()
            }

            x += clampedRotation.toDouble()
            y -= (CARD_BITMAP_HEIGHT / 2).toDouble()
        }

        padding?.let {
            x += it
        }

        return Coordinates(x.toFloat(), y.toFloat())
    }


    private fun getRadiusFactor(cardScale: Float): Double {
        var radius = baseRadius

        var decimal = -((cardScale / 10f) - 10)

        while (decimal > 0) {
            radius -= 0.01
            decimal -= 1
        }

        return radius
    }

    private fun getBackgroundImageScale(originalValue: Float, cardBoundValue: Float): Int {
        return (originalValue + getPercent(cardBoundValue, 10f)).toInt()
    }

    private fun getPercent(value: Float, scale: Float) = (value / 100f) * scale;


    private fun randomRotation(index: Int): Float {
        var rotationDegrees = index.toDouble() * 1.2
        rotationDegrees -= 45.0
        rotationDegrees += Random.nextDouble(1.0, 2.2)

        return rotationDegrees.toFloat()
    }


    data class Coordinates(var x: Float, val y: Float)

    inner class Builder {
        private val params = Params()


        fun setCustomCardsScale(cardScale: Float) = this.apply {
            this.params.cardScale = cardScale
        }


        fun setCardsRotation(rotation: Float) = this.apply {
            this.params.rotation = rotation
        }

        fun setBackgroundPos(background: Int ) = this.apply {
                params.backgroundPos = background
        }

        fun setPreLoadedBackground(preLoadedBackgrounds: ArrayList<Int>)= this.apply {
            params.preLoadedBackGround= preLoadedBackgrounds

        }

        fun setCustomBackground(customBackgrounds: ArrayList<String>)= this.apply {
            params.customBackgrounds = customBackgrounds
        }

        fun getCardsImageBitmap(cards: List<Card>, onBitmapCreated: (bitmap: Bitmap) -> Unit) {
            executeAsyncAwaitCodeWithEx {

                val bitmap = addCardsOnLayout(
                    cards,
                    cardScale = params.cardScale,
                    rotation= params.rotation,
                    backgroundPos = params.backgroundPos,
                    customBackgrounds = params.customBackgrounds,
                    preLoadedBackgrounds = params.preLoadedBackGround
                )

                GlobalScope.async(Dispatchers.Main) { onBitmapCreated(bitmap) }



            }
        }

        private fun executeAsyncAwaitCodeWithEx(handler: suspend (ex: Throwable?) -> Unit) {
            GlobalScope.async(Dispatchers.Default) {
                try {
                    handler(null)
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                    handler(ex)
                }
            }
        }

    }

    private class Params : Serializable {
        var cardElevation: Float = 0F
        var cardScale: Float = 1F
        var rotation: Float = 0F
        var backgroundPos: Int = 0
        var customBackgrounds = ArrayList<String>()
        var preLoadedBackGround = ArrayList<Int>()
    }
}