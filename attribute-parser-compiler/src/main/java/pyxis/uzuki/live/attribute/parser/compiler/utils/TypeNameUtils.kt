@file:JvmName("Utils")
@file:JvmMultifileClass

package pyxis.uzuki.live.attribute.parser.compiler.utils

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

/**
 * guess [TypeName] including Primitive type;
 *
 * @param elementName
 * @return
 */
fun bestGuess(elementName: String): TypeName {
    return when (elementName) {
        "int" -> TypeName.INT
        "byte" -> TypeName.BYTE
        "short" -> TypeName.SHORT
        "long" -> TypeName.LONG
        "char" -> TypeName.CHAR
        "float" -> TypeName.FLOAT
        "double" -> TypeName.DOUBLE
        "boolean" -> TypeName.BOOLEAN
        else -> ClassName.bestGuess(elementName)
    }
}