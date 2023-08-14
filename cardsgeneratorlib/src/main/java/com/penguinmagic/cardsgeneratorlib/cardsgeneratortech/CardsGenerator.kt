package com.penguinmagic.cardsgeneratorlib.cardsgeneratortech

import android.content.Context
import android.graphics.Bitmap
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.card.MaterialCardView
import com.penguinmagic.cardsgeneratorlib.R
import com.penguinmagic.cardsgeneratorlib.db.backgrounds.BackgroundsRepository
import com.penguinmagic.cardsgeneratorlib.model.cards.Card
import com.penguinmagic.cardsgeneratorlib.utils.ViewUtils
import kotlinx.android.synthetic.main.photo_layout.view.*
import java.io.Serializable
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class CardsGenerator(private val context: Context) {

    private val photoLayout: View by lazy { LayoutInflater.from(this.context).inflate(R.layout.photo_layout, null, false) }

    private fun addCardsOnLayout(
        cards: List<Card>,
        cardElevation: Float,
        cardScale: Float,
        translationX: Float,
        translationY: Float,
        cardsOnDeck: Boolean,
        rotation: Float
    ) {
        for (i in 1..52) {
            val mcv = MaterialCardView(context)
            setupCardView(mcv, i)
            applyCardScale(mcv, cardScale)

            mcv.cardElevation = cardElevation
            mcv.rotation = randomRotation(i)

            setRandomPosition(mcv, i, cardScale)

            val imageView = AppCompatImageView(ContextThemeWrapper(context, R.style.CardBack))
            mcv.addView(imageView)

            for (card in cards) {
                if (card.position == i) {
                    calculateCardTranslationAndTranslate(mcv, i, cardScale)
                    imageView.setImageResource(card.picture)
                    setCardsElevation(cardsOnDeck, mcv)
                }
            }

            applyCardsTranslation(translationX, translationY)
            applyCardsRotation(rotation)

            photoLayout.clCards.addView(mcv)
        }
    }

    private fun setupCardView(mcv: MaterialCardView, index: Int) {
        mcv.id = index
        mcv.layoutParams = ConstraintLayout.LayoutParams(178, 250).apply {
            this.topToTop = photoLayout.clCards.id
            this.startToStart = photoLayout.clCards.id
            this.endToEnd = photoLayout.clCards.id
            this.bottomToBottom = photoLayout.clCards.id
        }
    }

    private fun applyCardScale(mcv: MaterialCardView, cardScale: Float) {
        mcv.scaleX = cardScale
        mcv.scaleY = cardScale
    }

    private fun setCardsElevation(cardsOnDeck: Boolean, mcv: MaterialCardView) {
        if (cardsOnDeck) {
            mcv.elevation = 2F
        }
    }

    private fun applyCardsTranslation(translationX: Float, translationY: Float) {
        photoLayout.clCards.translationX = translationX
        photoLayout.clCards.translationY = translationY
    }

    private fun applyCardsRotation(rotation: Float) {
        photoLayout.clCards.rotation = rotation
    }

    private fun setRandomPosition(cardView: MaterialCardView, index: Int, cardScale: Float) {
        val pi = 3.14F
        val rotationCard = cardView.rotation
        val radian = rotationCard * pi/180
        var x = (-170 + index * 2) * cos(radian)
        var y = (150 - index) * sin(radian)

        x += Random.nextDouble(1.0, 5.0).toFloat()
        y += Random.nextDouble(1.0, 8.0).toFloat()

        translateCard(x, y, cardView, cardScale)
    }

    private fun randomRotation(index: Int): Float {
        var rotationDegrees = index.toDouble() * 5.60
        rotationDegrees -= 15.0
        rotationDegrees += Random.nextDouble(1.0, 2.2)

        return rotationDegrees.toFloat()
    }

    private fun calculateCardTranslationAndTranslate(cardView: MaterialCardView, index: Int, cardScale: Float) {
        val pi = 3.14F
        val rotationCard = cardView.rotation
        val radian = rotationCard * pi/180
        val x = (-220 + index * 2) * cos(radian)
        val y = (200 - index) * sin(radian)

        translateCard(x, y, cardView, cardScale)
    }

    private fun translateCard(x: Float, y: Float, view: View, scale: Float = 1.0F) {
        view.translationY = ViewUtils.dpToPx(x, context) * scale
        view.translationX = ViewUtils.dpToPx(y, context) * scale
    }

    inner class Builder {
        private val params = Params()

        fun setCardsElevation(elevation: Float) {
            this.params.cardElevation = elevation
        }

        fun setCardsOnDeck(boolean: Boolean) {
            this.params.cardsOnDeck = boolean
        }

        fun setCustomCardsScale(cardScale: Float) {
            this.params.cardScale = cardScale
        }

        fun setTranslation(translationX: Float, translationY: Float) {
            this.params.translationX = translationX
            this.params.translationY = translationY
        }

        fun setCardsRotation(rotation: Float) {
            this.params.rotation = rotation
        }

        fun setBackground(background: Int?) {
            if (background != null) {
                photoLayout.ivBackground.setImageResource(background)
            } else {
                photoLayout.ivBackground.setImageResource(BackgroundsRepository.getRandomBackground())
            }
        }

        fun getCardsImageBitmap(cards: List<Card>): Bitmap {
            photoLayout.rlRoot.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

            addCardsOnLayout(
                cards,
                this.params.cardElevation,
                this.params.cardScale,
                this.params.translationX,
                this.params.translationY,
                this.params.cardsOnDeck,
                this.params.rotation
            )

            photoLayout.requestLayout()

            val rlRoot = photoLayout.findViewById<RelativeLayout>(R.id.rlRoot)
            rlRoot.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            rlRoot.layout(0, 0, rlRoot.measuredWidth, rlRoot.measuredHeight)

            val bitmapWidth = rlRoot.measuredWidth
            val bitmapHeight = rlRoot.measuredHeight

            return ViewUtils.createDrawableFromView(context, photoLayout, bitmapWidth, bitmapHeight)
        }

    }

    private class Params : Serializable {
        var cardsOnDeck: Boolean = false
        var cardElevation: Float = 0F
        var cardScale: Float = 1F
        var translationX: Float = -70F
        var translationY: Float = 350F
        var rotation: Float = 0F
    }

}