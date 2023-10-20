package com.penguinmagic.test

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.penguinmagic.cardsgenerator.R
import com.penguinmagic.cardsgeneratorlib.cardsgeneratortech.CanvasCardGen
import com.penguinmagic.cardsgeneratorlib.model.cards.Card
import com.penguinmagic.cardsgeneratorlib.model.cards.CardsSuitEnum

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageView = findViewById<ImageView>(R.id.ivMain)

        Log.d("Generation","Started")
        val bitmap = CanvasCardGen(this).Builder()
            .setBackgroundPos(0)
            .setPreLoadedBackground(arrayListOf(R.mipmap.background2))
            .setCustomCardsScale(80f)
            .setCardsRotation(0f)
            .getCardsImageBitmap(listOf(Card(CardsSuitEnum.CLUB_8, 1), Card(CardsSuitEnum.CLUB_ACE, 25), Card(CardsSuitEnum.CLUB_7, 52))) {
                Log.d("Generation","Ended")
                Glide.with(this).load(it).into(imageView)
            }
    }
}