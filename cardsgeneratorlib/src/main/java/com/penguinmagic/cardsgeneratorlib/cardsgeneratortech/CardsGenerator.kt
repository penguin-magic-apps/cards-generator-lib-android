package com.penguinmagic.cardsgeneratorlib.cardsgeneratortech

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Bitmap
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.card.MaterialCardView
import com.penguinmagic.cardsgeneratorlib.R
import com.penguinmagic.cardsgeneratorlib.db.backgrounds.BackgroundsRepository
import com.penguinmagic.cardsgeneratorlib.model.cards.Card
import com.penguinmagic.cardsgeneratorlib.utils.ViewUtils
import kotlinx.android.synthetic.main.photo_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.Serializable
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

class CardsGenerator(private val context: Context) {

    companion object {
        private const val CARD_WIDTH = 356
        private const val CARD_OVERLAP = CARD_WIDTH * 0.15
    }

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
            applyCardScale(mcv)
            mcv.cardElevation = cardElevation
            val cardRotation = randomRotation(i)
            mcv.rotation = cardRotation

            val ivShadow = ImageView(context)
            ivShadow.rotation = cardRotation
            setupShadowImageView(ivShadow, i, mcv)

            val imageView = AppCompatImageView(ContextThemeWrapper(context, R.style.CardBack))
            mcv.addView(imageView)

            val x = -1350f + (i - 1) * CARD_OVERLAP.toInt()
            val y = ((-0.00045f * (52 * (26 - x) - (26 - x).pow(2))) + (Random.nextFloat() * 15)) - 500
            mcv.x = x
            mcv.y = y

            ivShadow.x = x
            ivShadow.y = y

            for (card in cards) {
                if (card.position == i) {
                    calculateCardTranslationAndTranslate(mcv)
                    calculateCardTranslationAndTranslate(ivShadow)
                    imageView.setImageResource(card.picture)
                    setCardsElevation(cardsOnDeck, mcv)
                }
            }

            photoLayout.clCards.addView(ivShadow)
            photoLayout.clCards.addView(mcv)
        }
        photoLayout.clCards.rotation = rotation
        photoLayout.clCards.scaleX = cardScale
        photoLayout.clCards.scaleY = cardScale
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

    private fun setupShadowImageView(imageView: ImageView, index: Int, mcv: MaterialCardView) {
        imageView.setImageResource(R.mipmap.card_shadow)
        imageView.id = index + 54
        imageView.scaleX = 1.1f
        imageView.scaleY = 1.1f
        imageView.layoutParams = ConstraintLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT).apply {
            this.startToStart = mcv.id
            this.topToTop = mcv.id
            this.endToEnd = mcv.id
            this.bottomToBottom = mcv.id
        }
    }

    private fun applyCardScale(mcv: MaterialCardView) {
        mcv.scaleX = 2f
        mcv.scaleY = 2f
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

    private fun randomRotation(index: Int): Float {
        var rotationDegrees = index.toDouble() * 1.2
        rotationDegrees -= 45.0
        rotationDegrees += Random.nextDouble(1.0, 2.2)

        return rotationDegrees.toFloat()
    }

    private fun calculateCardTranslationAndTranslate(view: View) {
        val pi = 3.14F
        val rotationCard = view.rotation
        val radian = rotationCard * pi/180
        val x = 250 * sin(radian)
        val y = -250 * cos(radian)

        view.x = view.x + x
        view.y = view.y + y
    }

    private fun translateCard(x: Float, y: Float, view: View) {
        view.translationY = ViewUtils.dpToPx(x, context)
        view.translationX = ViewUtils.dpToPx(y, context)
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
                photoLayout.ivBackground.setImageResource(background)
            } else {
                photoLayout.ivBackground.setImageResource(BackgroundsRepository.getRandomBackground())
            }
        }

        fun getCardsImageBitmap(cards: List<Card>, onBitmapCreated: (bitmap: Bitmap) -> Unit) {
            executeAsyncAwaitCodeWithEx {
                GlobalScope.async(Dispatchers.Main) {
                    photoLayout.rlRoot.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

                    addCardsOnLayout(
                        cards,
                        this@Builder.params.cardElevation,
                        this@Builder.params.cardScale,
                        this@Builder.params.translationX,
                        this@Builder.params.translationY,
                        this@Builder.params.cardsOnDeck,
                        this@Builder.params.rotation
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

                    onBitmapCreated(ViewUtils.createDrawableFromView(context, photoLayout, bitmapWidth, bitmapHeight))
                }
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
    }

}