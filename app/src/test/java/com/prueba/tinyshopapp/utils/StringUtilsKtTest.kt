package com.prueba.tinyshopapp.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class StringUtilsKtTest {
    @Test
    fun `invalid zipcode returns false`() {
        val result1 = validateZipcode("25635-25")
        val result2 = validateZipcode("2563")

        assertThat(result1).isFalse()
        assertThat(result2).isFalse()
    }

    @Test
    fun `a valid zipcode returns true`() {
        val result1 = validateZipcode("33178-1855")
        val result2 = validateZipcode("33178")

        assertThat(result1).isTrue()
        assertThat(result2).isTrue()
    }

    @Test
    fun `returns true if string contains numbers`() {
        val result = "Jose25".hasDigits()
        assertThat(result).isTrue()
    }

    @Test
    fun `returns false if string does not contain numbers`() {
        val result = "Jose".hasDigits()
        assertThat(result).isFalse()
    }

}