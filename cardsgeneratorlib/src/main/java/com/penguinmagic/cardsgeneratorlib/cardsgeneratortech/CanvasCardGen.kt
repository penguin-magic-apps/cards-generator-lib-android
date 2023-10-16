package com.penguinmagic.cardsgeneratorlib.cardsgeneratortech

import android.app.ActionBar
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.scale
import com.google.android.material.card.MaterialCardView
import com.penguinmagic.cardsgeneratorlib.R
import com.penguinmagic.cardsgeneratorlib.db.backgrounds.BackgroundsRepository
import com.penguinmagic.cardsgeneratorlib.model.cards.Card
import com.penguinmagic.cardsgeneratorlib.utils.ViewUtils
import com.penguinmagic.cardsgeneratorlib.utils.ViewUtils.rotate
import kotlinx.android.synthetic.main.photo_layout.view.clCards
import kotlinx.android.synthetic.main.photo_layout.view.ivBackground
import kotlinx.android.synthetic.main.photo_layout.view.rlRoot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.Serializable
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

class CanvasCardGen(val context: Context) {


    companion object {
        private var CARD_BITMAP_WIDHT = 70
        private var CARD_BITMAP_HEIGTH = 100
    }

    private val maxRotation = 50f
    private val minRotation = 0f

    private val angleBetweenCards = Math.PI / 52
    private var radius = 200
    private val photoLayout: View by lazy {
        LayoutInflater.from(this.context).inflate(R.layout.photo_layout, null, false)
    }


    private fun addCardsOnLayout(
        cards: List<Card>,
        cardElevation: Float,
        cardScale: Float,
        translationX: Float,
        translationY: Float,
        cardsOnDeck: Boolean,
        rotation: Float,
        background: Int
    ): Bitmap {


        val clampedRotation = rotation.coerceIn(minRotation, maxRotation)
        val angleOffset = clampedRotation * angleBetweenCards
         radius +=   getPercent(radius.toFloat(), cardScale).toInt()
         CARD_BITMAP_WIDHT += getPercent(CARD_BITMAP_WIDHT.toFloat(),cardScale).toInt()
         CARD_BITMAP_HEIGTH += getPercent(CARD_BITMAP_HEIGTH.toFloat(),cardScale).toInt()

        val backgroundBitmap = BitmapFactory.decodeResource(context.resources, background)
            .copy(Bitmap.Config.ARGB_8888, true).scale(context.resources.displayMetrics.widthPixels,context.resources.displayMetrics.heightPixels)
        val paint = Paint()
        val canvas = Canvas(backgroundBitmap)
        var isActualCard: Boolean

        val centerX = backgroundBitmap.width / 2
        val centerY = backgroundBitmap.height / 2


        for (i in 1..52) {
            val cardRotation = randomRotation(i) + clampedRotation
            val shadowBitmap =
                BitmapFactory.decodeResource(context.resources, R.mipmap.card_shadow).scale(
                    CARD_BITMAP_WIDHT, CARD_BITMAP_HEIGTH
                )
                    .rotate(cardRotation)
            val cardBitmap = if (cards.any { it.position == i }) {
                isActualCard = true
                val id = cards.first { it.position == i }.picture
                BitmapFactory.decodeResource(context.resources, id)
                    .scale(CARD_BITMAP_WIDHT, CARD_BITMAP_HEIGTH)
                    .rotate(cardRotation)
            } else {
                isActualCard = false
                BitmapFactory.decodeResource(context.resources, R.mipmap.card_back)
                    .scale(CARD_BITMAP_WIDHT, CARD_BITMAP_HEIGTH)
                    .rotate(cardRotation)
            }



            val angle = if (clampedRotation < 0) {
                angleOffset + (-(i * angleBetweenCards))
            } else {
               -angleOffset + (-(i * angleBetweenCards))
            }
            val x = (centerX - radius * cos(angle)).toFloat()
            val y = (centerY + radius * sin(angle)).toFloat()

            var left = x - CARD_BITMAP_WIDHT / 2
            var top = y - CARD_BITMAP_HEIGTH / 2


            if (isActualCard) {
                if (i <= 25) {
                    left -= (CARD_BITMAP_WIDHT / 2)
                } else {
                    left += (CARD_BITMAP_WIDHT / 2)
                }

                left += clampedRotation
                top -= (CARD_BITMAP_HEIGTH / 2)
            }

            canvas.drawBitmap(cardBitmap, left, top, paint)

        }

        return backgroundBitmap
    }



    private fun getPercent(value: Float, scale: Float) = (value / 100f) * scale;




    private fun randomRotation(index: Int): Float {
        var rotationDegrees = index.toDouble() * 1.2
        rotationDegrees -= 45.0
        rotationDegrees += Random.nextDouble(1.0, 2.2)

        return rotationDegrees.toFloat()
    }


    inner class Builder {
        private val params = Params()

        fun setCardsElevation(elevation: Float) = this.apply {
            this.params.cardElevation = elevation
        }

        fun setCardsOnDeck(boolean: Boolean) = this.apply {
            this.params.cardsOnDeck = boolean
        }

        fun setCustomCardsScale(cardScale: Float) = this.apply {
            this.params.cardScale = cardScale
        }

        fun setTranslation(translationX: Float, translationY: Float) = this.apply {
            this.params.translationX = translationX
            this.params.translationY = translationY
        }

        fun setCardsRotation(rotation: Float) = this.apply {
            this.params.rotation = rotation
        }

        fun setBackground(background: Int?) = this.apply {
            if (background != null) {
                params.background = background
                photoLayout.ivBackground.setImageResource(background)
            } else {
                val randomBackground = BackgroundsRepository.getRandomBackground()
                params.background = randomBackground
                photoLayout.ivBackground.setImageResource(randomBackground)
            }
        }

        fun getCardsImageBitmap(cards: List<Card>, onBitmapCreated: (bitmap: Bitmap) -> Unit) {
            executeAsyncAwaitCodeWithEx {

                val bitmap = addCardsOnLayout(
                    cards,
                    this@Builder.params.cardElevation,
                    this@Builder.params.cardScale,
                    this@Builder.params.translationX,
                    this@Builder.params.translationY,
                    this@Builder.params.cardsOnDeck,
                    this@Builder.params.rotation,
                    background = params.background
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
        var cardsOnDeck: Boolean = false
        var cardElevation: Float = 0F
        var cardScale: Float = 1F
        var translationX: Float = -70F
        var translationY: Float = 350F
        var rotation: Float = 0F
        var background: Int = R.mipmap.background1
    }
}