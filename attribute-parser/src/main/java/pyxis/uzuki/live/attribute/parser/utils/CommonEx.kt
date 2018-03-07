@file:JvmName("CommonEx")
@file:JvmMultifileClass

package pyxis.uzuki.live.attribute.parser.utils

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

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
fun String.bestGuess(): TypeName = when (this) {
    "int" -> TypeName.INT
    "byte" -> TypeName.BYTE
    "short" -> TypeName.SHORT
    "long" -> TypeName.LONG
    "char" -> TypeName.CHAR
    "float" -> TypeName.FLOAT
    "double" -> TypeName.DOUBLE
    "boolean" -> TypeName.BOOLEAN
    else -> ClassName.bestGuess(this)
}

fun multiply(delimiter: CharSequence, count: Int): String {
    val str = StringBuilder()
    for (i in 0 until count) {
        str.append(delimiter)
    }

    return str.toString()
}