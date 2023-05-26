package com.penguinmagic.cardsgeneratorlib.cardsgeneratortech

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import com.penguinmagic.cardsgeneratorlib.R
import com.penguinmagic.cardsgeneratorlib.db.backgrounds.BackgroundsRepository
import com.penguinmagic.cardsgeneratorlib.model.cards.CardsSuitEnum
import com.penguinmagic.cardsgeneratorlib.utils.ViewUtils
import kotlinx.android.synthetic.main.photo_layout.view.clCards
import kotlinx.android.synthetic.main.photo_layout.view.ivBackground
import kotlinx.android.synthetic.main.photo_layout.view.rlRoot
import java.lang.Exception
import kotlin.math.cos
import kotlin.math.sin

class CardsGenerator(private val context: Context) {

    private val photoLayout: View by lazy { LayoutInflater.from(this.context).inflate(R.layout.photo_layout, null, false) }

    private var cardsSuit: List<CardsSuitEnum>? = null
    private var cardsPositions: List<Int>? = null
    private var cardsOnDeck: Boolean? = null


    fun chooseCardSuit(cardsSuit: List<CardsSuitEnum>) {
        this.cardsSuit = cardsSuit
    }

    fun chooseCardsPositions(positions: List<Int>) {
        this.cardsPositions = positions
    }

    fun setCardsOnDeck(boolean: Boolean) {
        this.cardsOnDeck = boolean
    }

    fun setBackground(background: Int?) {
        if (background != null) {
            photoLayout.ivBackground.setImageResource(background)
        } else {
            photoLayout.ivBackground.setImageResource(BackgroundsRepository.getRandomBackground())
        }
    }

    fun getCardsPhotoBitmap(): Bitmap {
        setCardsPositionAndSuit()
        photoLayout.rlRoot.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        photoLayout.requestLayout()
        return ViewUtils.createDrawableFromView(context, photoLayout.rlRoot, 1134, 2016)
    }

    private fun setCardsPositionAndSuit() {
        this.cardsPositions?.let { positions ->
            positions.forEachIndexed { index, position ->
                if (position <= 52) {
                    val materialCard = photoLayout.clCards.getChildAt(position-1) as MaterialCardView
                    calculateCardTranslationAndTranslate(materialCard)
                    if (this.cardsOnDeck == true) {
                        materialCard.elevation = 5F
                    }
                    try {
                        setCardSuit(materialCard.getChildAt(0), this.cardsSuit?.get(index)?.card?.picture)
                    } catch (ex: Exception) {
                        Log.e("CardsGenerator", "$ex")
                    }
                }
            }
        }
    }

    private fun setCardSuit(cardView: View, suit: Int?) {
        suit?.let { (cardView as ImageView).setImageResource(it) }
    }

    private fun calculateCardTranslationAndTranslate(cardView: MaterialCardView) {
        val pi = 3.14F
        val rotationCard = cardView.rotation
        val radian = rotationCard * pi/180
        val x = -40 * cos(radian)
        val y = 60 * sin(radian)
        translateCard(x, y, cardView)
    }

    private fun translateCard(x: Float, y: Float, view: View) {
        view.translationY = ViewUtils.dpToPx(x, context)
        view.translationX = ViewUtils.dpToPx(y, context)
    }

}