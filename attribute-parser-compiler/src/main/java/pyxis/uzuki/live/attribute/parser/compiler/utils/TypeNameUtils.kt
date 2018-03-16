@file:JvmName("Utils")
@file:JvmMultifileClass

package pyxis.uzuki.live.attribute.parser.compiler.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName

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
val STRING = ClassName("kotlin", "String")

fun String.bestGuess(): TypeName {
    return when (this) {
        "int" -> INT
        "byte" -> BYTE
        "short" -> SHORT
        "long" -> LONG
        "char" -> CHAR
        "float" -> FLOAT
        "double" -> DOUBLE
        "String" -> STRING
        "string" -> STRING
        "java.lang.String" -> STRING
        "boolean" -> BOOLEAN
        "Unit" -> UNIT
        "Any" -> ANY
        else -> ClassName.bestGuess(this)
    }
}

fun TypeName.initializer(): String {
    return when (this) {
        FLOAT -> "0f"
        BOOLEAN -> "false"
        STRING -> "\"\""
        else -> "0"
    }
}