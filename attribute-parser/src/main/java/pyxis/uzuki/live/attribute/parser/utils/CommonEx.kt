@file:JvmName("CommonEx")
@file:JvmMultifileClass

package pyxis.uzuki.live.attribute.parser.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName

/**
 * AttributesParser
 * Class: TypeNameUtil
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

/**
 * guess [TypeName] including Primitive type;
 *
 * @param elementName
 * @return
 */

val ANY = ClassName("kotlin", "Any")
val ARRAY = ClassName("kotlin", "Array")
val UNIT = Unit::class.asClassName()
val BOOLEAN = ClassName("kotlin", "Boolean")
val BYTE = ClassName("kotlin", "Byte")
val SHORT = ClassName("kotlin", "Short")
val INT = ClassName("kotlin", "Int")
val LONG = ClassName("kotlin", "Long")
val CHAR = ClassName("kotlin", "Char")
val FLOAT = ClassName("kotlin", "Float")
val DOUBLE = ClassName("kotlin", "Double")

fun String.bestGuess(): TypeName = when (this) {
    "int" -> INT
    "byte" -> BYTE
    "short" -> SHORT
    "long" -> LONG
    "char" -> CHAR
    "float" -> FLOAT
    "double" -> DOUBLE
    "boolean" -> BOOLEAN
    else -> ClassName.bestGuess(this)
}

fun multiply(delimiter: CharSequence, count: Int): String {
    val str = StringBuilder()
    for (i in 0 until count) {
        str.append(delimiter)
    }

    return str.toString()
}