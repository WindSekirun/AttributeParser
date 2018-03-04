package pyxis.uzuki.live.attribute.parser.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

class Constants {
    static final String PACKAGE_NAME = "pyxis.uzuki.live.attribute.parser";

    static final String ATTRIBUTES = "Attributes";
    static final String R_FILE = ".R";
    static final String R_VARIABLE = "r";
    static final String APPLY = "apply";
    static final String ARRAY = "array";
    static final String BIND_ATTRIBUTES_INVOKE = "bindAttributes(array);\n\n";
    static final String STATEMENT_APPLY = "%s.%s = %s;\n";
    static final String PRINT_VARIABLES = "printVariables";
    static final String BIND_ATTRIBUTES = "bindAttributes";
    static final String SET = "set";
    static final String OBTAIN_APPLY = "apply";
    static final String STATEMENT_BINDATTRIBUTES = "if (array == null) return;\n\n";
    static final String STATEMENT_RECYCLE = "\narray.recycle();\n";
    static final String STATEMENT_OBTAIN_APPLY = "apply(%s, %s.getContext().obtainStyledAttributes(set, R.styleable.%s));\n";
    static final String STATEMENT_LOG = "android.util.Log.d(\"%s\", %s);\n";

    static final TypeName TYPED_ARRAY_CLASS_NAME = ClassName.bestGuess("android.content.res.TypedArray");
    static final TypeName ATTRIBUTE_SET_CLASS_NAME = ClassName.bestGuess("android.util.AttributeSet");
}
