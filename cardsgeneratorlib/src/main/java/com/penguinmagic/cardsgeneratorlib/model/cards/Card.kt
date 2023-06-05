package com.penguinmagic.cardsgeneratorlib.model.cards

import com.penguinmagic.cardsgeneratorlib.R
import java.io.Serializable

class Card(private val suit: CardsSuitEnum, val position: Int) : Serializable {

    val picture: Int = when (suit) {
        CardsSuitEnum.CLUB_KING -> R.mipmap.c49
        CardsSuitEnum.CLUB_QUEEN -> R.mipmap.c45
        CardsSuitEnum.CLUB_JACK -> R.mipmap.c41
        CardsSuitEnum.CLUB_ACE -> R.mipmap.c1
        CardsSuitEnum.CLUB_10 -> R.mipmap.c37
        CardsSuitEnum.CLUB_9 -> R.mipmap.c33
        CardsSuitEnum.CLUB_8 -> R.mipmap.c29
        CardsSuitEnum.CLUB_7 -> R.mipmap.c25
        CardsSuitEnum.CLUB_6 -> R.mipmap.c21
        CardsSuitEnum.CLUB_5 -> R.mipmap.c17
        CardsSuitEnum.CLUB_4 -> R.mipmap.c13
        CardsSuitEnum.CLUB_3 -> R.mipmap.c9
        CardsSuitEnum.CLUB_2 -> R.mipmap.c5
        CardsSuitEnum.DIAMOND_KING -> R.mipmap.c51
        CardsSuitEnum.DIAMOND_QUEEN -> R.mipmap.c48
        CardsSuitEnum.DIAMOND_JACK -> R.mipmap.c44
        CardsSuitEnum.DIAMOND_ACE -> R.mipmap.c4
        CardsSuitEnum.DIAMOND_10 -> R.mipmap.c40
        CardsSuitEnum.DIAMOND_9 -> R.mipmap.c36
        CardsSuitEnum.DIAMOND_8 -> R.mipmap.c32
        CardsSuitEnum.DIAMOND_7 -> R.mipmap.c28
        CardsSuitEnum.DIAMOND_6 -> R.mipmap.c24
        CardsSuitEnum.DIAMOND_5 -> R.mipmap.c20
        CardsSuitEnum.DIAMOND_4 -> R.mipmap.c16
        CardsSuitEnum.DIAMOND_3 -> R.mipmap.c12
        CardsSuitEnum.DIAMOND_2 -> R.mipmap.c8
        CardsSuitEnum.HEART_KING -> R.mipmap.c50
        CardsSuitEnum.HEART_QUEEN -> R.mipmap.c46
        CardsSuitEnum.HEART_JACK -> R.mipmap.c42
        CardsSuitEnum.HEART_ACE -> R.mipmap.c2
        CardsSuitEnum.HEART_10 -> R.mipmap.c38
        CardsSuitEnum.HEART_9 -> R.mipmap.c34
        CardsSuitEnum.HEART_8 -> R.mipmap.c30
        CardsSuitEnum.HEART_7 -> R.mipmap.c26
        CardsSuitEnum.HEART_6 -> R.mipmap.c22
        CardsSuitEnum.HEART_5 -> R.mipmap.c18
        CardsSuitEnum.HEART_4 -> R.mipmap.c14
        CardsSuitEnum.HEART_3 -> R.mipmap.c10
        CardsSuitEnum.HEART_2 -> R.mipmap.c6
        CardsSuitEnum.SPADE_KING -> R.mipmap.c52
        CardsSuitEnum.SPADE_QUEEN -> R.mipmap.c47
        CardsSuitEnum.SPADE_JACK -> R.mipmap.c43
        CardsSuitEnum.SPADE_ACE -> R.mipmap.c3
        CardsSuitEnum.SPADE_10 -> R.mipmap.c39
        CardsSuitEnum.SPADE_9 -> R.mipmap.c35
        CardsSuitEnum.SPADE_8 -> R.mipmap.c31
        CardsSuitEnum.SPADE_7 -> R.mipmap.c27
        CardsSuitEnum.SPADE_6 -> R.mipmap.c23
        CardsSuitEnum.SPADE_5 -> R.mipmap.c19
        CardsSuitEnum.SPADE_4 -> R.mipmap.c15
        CardsSuitEnum.SPADE_3 -> R.mipmap.c11
        CardsSuitEnum.SPADE_2 -> R.mipmap.c7
    }

}