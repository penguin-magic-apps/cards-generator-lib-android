package com.penguinmagic.cardsgeneratorlib.cardsgeneratortech

import android.content.Context
import android.graphics.Bitmap
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
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

    private fun addCardsOnLayout(cards: List<Card>, cardElevation: Float, width: Int?, height: Int?) {
        for (i in 1..52) {
            val mcv = MaterialCardView(context)
            mcv.id = i
            mcv.layoutParams = ConstraintLayout.LayoutParams(
                width ?: 178,
                height ?: 250
            ).apply {
                this.topToTop = photoLayout.clCards.id
                this.startToStart = photoLayout.clCards.id
                this.endToEnd = photoLayout.clCards.id
                this.bottomToBottom = photoLayout.clCards.id
            }
            mcv.cardElevation = cardElevation
            mcv.rotation = randomRotation(i)
            setRandomPosition(mcv, i)

            val imageView = AppCompatImageView(ContextThemeWrapper(context, R.style.CardBack))
            mcv.addView(imageView)

            for (card in cards) {
                if (card.position == i) {
                    calculateCardTranslationAndTranslate(mcv, i)
                    imageView.setImageResource(card.picture)
                }
            }

            photoLayout.clMainLayout.addView(mcv)
        }
    }

    private fun setRandomPosition(cardView: MaterialCardView, index: Int) {
        val pi = 3.14F
        val rotationCard = cardView.rotation
        val radian = rotationCard * pi/180
        var x = (-170 + index * 2) * cos(radian)
        var y = (150 - index) * sin(radian)

        x += Random.nextDouble(1.0, 5.0).toFloat()
        y += Random.nextDouble(1.0, 8.0).toFloat()

        translateCard(x, y, cardView)
    }

    private fun randomRotation(index: Int): Float {
        var rotationDegrees = index.toDouble() * 5.60
        rotationDegrees -= 15.0
        rotationDegrees += Random.nextDouble(1.0, 2.2)

        return rotationDegrees.toFloat()
    }

    private fun calculateCardTranslationAndTranslate(cardView: MaterialCardView, index: Int) {
        val pi = 3.14F
        val rotationCard = cardView.rotation
        val radian = rotationCard * pi/180
        val x = (-220 + index * 2) * cos(radian)
        val y = (200 - index) * sin(radian)

        translateCard(x, y, cardView)
    }

    private fun translateCard(x: Float, y: Float, view: View) {
        view.translationY = ViewUtils.dpToPx(x, context)
        view.translationX = ViewUtils.dpToPx(y, context)
    }

    inner class Builder {

        private val params = Params()

        fun setCardsElevation(elevation: Float) {
            this.params.cardElevation = elevation
        }

        fun setCardsOnDeck(boolean: Boolean) {
            this.params.cardsOnDeck = boolean
        }

        fun setCustomCardsScale(cardWight: Int, cardHeight: Int) {
            this.params.cardWight = cardWight
            this.params.cardHeight = cardHeight
        }

        fun setBackground(background: Int?) {
            if (background != null) {
                photoLayout.ivBackground.setImageResource(background)
            } else {
                photoLayout.ivBackground.setImageResource(BackgroundsRepository.getRandomBackground())
            }
        }

        fun getCardsPhotoBitmap(cards: List<Card>): Bitmap {
            photoLayout.rlRoot.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

            if (this.params.cardWight == null || this.params.cardHeight == null) {
                addCardsOnLayout(cards, this.params.cardElevation, null, null)
            } else {
                addCardsOnLayout(cards, this.params.cardElevation, this.params.cardWight, this.params.cardHeight)
            }

            photoLayout.requestLayout()

            return ViewUtils.createDrawableFromView(context, photoLayout.rlRoot, 1134, 2016)
        }

    }

    private class Params : Serializable {

        var cardsOnDeck: Boolean? = null
        var cardElevation: Float = 0F
        var cardWight: Int? = null
        var cardHeight: Int? = null


    }

}