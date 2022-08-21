package com.example.rolldice

import org.junit.Test

import org.junit.Assert.*


class ExampleUnitTest {
    @Test
    fun generates_number() {
        val dice = Dice(6)
        val rollResult = dice.roll()
        assertTrue("The value of rollResult was not between 1 and 6", rollResult in 1..6)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


}