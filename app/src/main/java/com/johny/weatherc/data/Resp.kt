package com.johny.weatherc.data

import java.lang.Exception

sealed class Resp<out R> {

    data class Ok<out T>(val data: T): Resp<T>()

    data class Error(val e: Exception): Resp<Nothing>()

    object Loading: Resp<Nothing>()

    override fun toString(): String {
        return when(this) {
            is Ok<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$e]"
            is Loading -> "Loading..."
        }
    }

}

val Resp<*>.succeeded
    get() = this is Resp.Ok && data != null