package pyxis.uzuki.live.attribute.parser.compiler.utils

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

object TypeNameUtils {

    /**
     * guess [TypeName] including Primitive type;
     *
     * @param elementName
     * @return
     */
    fun bestGuess(elementName: String): TypeName = when (elementName) {
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
