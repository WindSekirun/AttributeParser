package pyxis.uzuki.live.attribute.parser.compiler

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

internal object Constants {
    const val PACKAGE_NAME = "pyxis.uzuki.live.attribute.parser"

    const val ATTRIBUTES = "Attributes"
    const val R_FILE = ".R"
    const val R_VARIABLE = "r"
    const val APPLY = "apply"
    const val ARRAY = "array"
    const val BIND_ATTRIBUTES_INVOKE = "bindAttributes(array);\n\n"
    const val STATEMENT_APPLY = "%s.%s = %s;\n"
    const val PRINT_VARIABLES = "printVariables"
    const val BIND_ATTRIBUTES = "bindAttributes"
    const val SET = "set"
    const val EQUALS = "="
    const val STATEMENT_BINDATTRIBUTES = "if (array == null) return;\n\n"
    const val STATEMENT_RECYCLE = "\narray.recycle();\n"
    const val STATEMENT_OBTAIN_APPLY = "apply(%s, %s.getContext().obtainStyledAttributes(set, R.styleable.%s));\n"
    const val STATEMENT_LOG = "android.util.Log.d(\"%s\", %s);\n"
    const val STATEMENT_LOG_FIRST_LINE = "\"%s %s %s\" + \n"
    const val STATEMENT_LOG_LAST_LINE = "\"\\n%s\""


    @JvmField
    val TYPED_ARRAY_CLASS_NAME: TypeName = ClassName.bestGuess("android.content.res.TypedArray").asNullable()
    @JvmField
    val ATTRIBUTE_SET_CLASS_NAME: TypeName = ClassName.bestGuess("android.util.AttributeSet").asNullable()
    @JvmField
    val LOG_CLASS = ClassName.bestGuess("android.util.Log")
}
