package com.prueba.tinyshopapp.utils

import java.util.regex.Pattern

fun validateZipcode(zipcode: String): Boolean {
    val regex = "^[0-9]{5}(?:-[0-9]{4})?$"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(zipcode)
    return matcher.matches()
}

fun String.hasDigits(): Boolean {
    val regex = "[0-9]".toRegex()
    return this.contains(regex)
}
