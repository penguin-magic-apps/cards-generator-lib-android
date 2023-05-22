package com.penguinmagic.cardsgeneratorlib.db.backgrounds

import com.penguinmagic.cardsgeneratorlib.R

object BackgroundsRepository {

    private val backgrounds = listOf(
        R.mipmap.background1,
        R.mipmap.background2,
        R.mipmap.background3,
        R.mipmap.background4,
        R.mipmap.background5,
        R.mipmap.background6,
        R.mipmap.background7,
        R.mipmap.background8,
        R.mipmap.background9
    )

    fun getBackground(index: Int) = backgrounds[index]

    fun getRandomBackground() = backgrounds.random()
}