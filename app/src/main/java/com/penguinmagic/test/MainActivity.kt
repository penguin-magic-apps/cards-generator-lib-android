package com.penguinmagic.test

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.penguinmagic.cardsgenerator.R
import com.penguinmagic.cardsgeneratorlib.cardsgeneratortech.CardsGenerator
import com.penguinmagic.cardsgeneratorlib.model.cards.Card
import com.penguinmagic.cardsgeneratorlib.model.cards.CardsSuitEnum

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageView = findViewById<ImageView>(R.id.ivMain)

        val bitmap = CardsGenerator(this).Builder()
            .setBackground(R.mipmap.background1)
            .getCardsImageBitmap(listOf(Card(CardsSuitEnum.CLUB_8, 6), Card(CardsSuitEnum.CLUB_ACE, 25), Card(CardsSuitEnum.CLUB_7, 44)))

        Glide.with(this).load(bitmap).into(imageView)
    }
}