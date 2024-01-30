package com.nadhifhayazee.locationsaver

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private fun isPalindrom(s: String): Boolean {
        var palindrom = StringBuilder()
        for (i in s.lastIndex downTo 0) {
            palindrom.append(s[i])
        }
        return s == s.reversed()
    }

    @Test
    fun addition_isCorrect() {

        assertEquals(isPalindrom("katak"), true)
    }
}