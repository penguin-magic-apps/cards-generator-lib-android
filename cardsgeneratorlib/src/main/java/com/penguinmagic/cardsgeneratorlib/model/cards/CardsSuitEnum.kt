package com.penguinmagic.cardsgeneratorlib.model.cards

import com.penguinmagic.cardsgeneratorlib.R

enum class CardsSuitEnum {
    CLUB_KING, CLUB_QUEEN, CLUB_JACK, CLUB_ACE, CLUB_10, CLUB_9,
    CLUB_8, CLUB_7, CLUB_6, CLUB_5, CLUB_4, CLUB_3, CLUB_2,

    DIAMOND_KING, DIAMOND_QUEEN, DIAMOND_JACK, DIAMOND_ACE,
    DIAMOND_10, DIAMOND_9, DIAMOND_8, DIAMOND_7, DIAMOND_6,
    DIAMOND_5, DIAMOND_4, DIAMOND_3, DIAMOND_2,

    HEART_KING, HEART_QUEEN, HEART_JACK, HEART_ACE, HEART_10,
    HEART_9, HEART_8, HEART_7, HEART_6, HEART_5, HEART_4, HEART_3, HEART_2,

    SPADE_KING, SPADE_QUEEN, SPADE_JACK, SPADE_ACE, SPADE_10, SPADE_9,
    SPADE_8, SPADE_7, SPADE_6, SPADE_5, SPADE_4, SPADE_3, SPADE_2;

    val card: Card
        get() = when (this)  {
            CLUB_KING       -> Card("K♣", R.mipmap.c49)
            CLUB_QUEEN      -> Card("Q♣", R.mipmap.c45)
            CLUB_JACK       -> Card("J♣", R.mipmap.c41)
            CLUB_ACE        -> Card("A♣", R.mipmap.c1)
            CLUB_10         -> Card("10♣", R.mipmap.c37)
            CLUB_9          -> Card("9♣", R.mipmap.c33)
            CLUB_8          -> Card("8♣", R.mipmap.c29)
            CLUB_7          -> Card("7♣", R.mipmap.c25)
            CLUB_6          -> Card("6♣", R.mipmap.c21)
            CLUB_5          -> Card("5♣", R.mipmap.c17)
            CLUB_4          -> Card("4♣", R.mipmap.c13)
            CLUB_3          -> Card("3♣", R.mipmap.c9)
            CLUB_2          -> Card("2♣", R.mipmap.c5)
            DIAMOND_KING    -> Card("K♦", R.mipmap.c51)
            DIAMOND_QUEEN   -> Card("Q♦", R.mipmap.c48)
            DIAMOND_JACK    -> Card("J♦", R.mipmap.c44)
            DIAMOND_ACE     -> Card("A♦", R.mipmap.c4)
            DIAMOND_10      -> Card("10♦", R.mipmap.c40)
            DIAMOND_9       -> Card("9♦", R.mipmap.c36)
            DIAMOND_8       -> Card("8♦", R.mipmap.c32)
            DIAMOND_7       -> Card("7♦", R.mipmap.c28)
            DIAMOND_6       -> Card("6♦", R.mipmap.c24)
            DIAMOND_5       -> Card("5♦", R.mipmap.c20)
            DIAMOND_4       -> Card("4♦", R.mipmap.c16)
            DIAMOND_3       -> Card("3♦", R.mipmap.c12)
            DIAMOND_2       -> Card("2♦", R.mipmap.c8)
            HEART_KING      -> Card("K♥", R.mipmap.c50)
            HEART_QUEEN     -> Card("Q♥", R.mipmap.c46)
            HEART_JACK      -> Card("J♥", R.mipmap.c42)
            HEART_ACE       -> Card("A♥", R.mipmap.c2)
            HEART_10        -> Card("10♥", R.mipmap.c38)
            HEART_9         -> Card("9♥", R.mipmap.c34)
            HEART_8         -> Card("8♥", R.mipmap.c30)
            HEART_7         -> Card("7♥", R.mipmap.c26)
            HEART_6         -> Card("6♥", R.mipmap.c22)
            HEART_5         -> Card("5♥", R.mipmap.c18)
            HEART_4         -> Card("4♥", R.mipmap.c14)
            HEART_3         -> Card("3♥", R.mipmap.c10)
            HEART_2         -> Card("2♥", R.mipmap.c6)
            SPADE_KING      -> Card("K♠", R.mipmap.c52)
            SPADE_QUEEN     -> Card("Q♠", R.mipmap.c47)
            SPADE_JACK      -> Card("J♠", R.mipmap.c43)
            SPADE_ACE       -> Card("A♠", R.mipmap.c3)
            SPADE_10        -> Card("10♠", R.mipmap.c39)
            SPADE_9         -> Card("9♠", R.mipmap.c35)
            SPADE_8         -> Card("8♠", R.mipmap.c31)
            SPADE_7         -> Card("7♠", R.mipmap.c27)
            SPADE_6         -> Card("6♠", R.mipmap.c23)
            SPADE_5         -> Card("5♠", R.mipmap.c19)
            SPADE_4         -> Card("4♠", R.mipmap.c15)
            SPADE_3         -> Card("3♠", R.mipmap.c11)
            SPADE_2         -> Card("2♠", R.mipmap.c7)
        }

    companion object {
        operator fun get(key: String) = values().firstOrNull { enumCard -> enumCard.card.key == key }?.card
    }

}