package pyxis.uzuki.live.attribute.parser.compiler.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * AttributesParser
 * Class: TypeNameUtil
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class TypeNameUtils {

    /**
     * guess {@link TypeName} including Primitive type;
     *
     * @param elementName
     * @return
     */
    public static TypeName bestGuess(String elementName) {
        TypeName typeName;

        switch (elementName) {
            case "int":
                typeName = TypeName.INT;
                break;
            case "byte":
                typeName = TypeName.BYTE;
                break;
            case "short":
                typeName = TypeName.SHORT;
                break;
            case "long":
                typeName = TypeName.LONG;
                break;
            case "char":
                typeName = TypeName.CHAR;
                break;
            case "float":
                typeName = TypeName.FLOAT;
                break;
            case "double":
                typeName = TypeName.DOUBLE;
                break;
            case "boolean":
                typeName = TypeName.BOOLEAN;
                break;
            default:
                typeName = ClassName.bestGuess(elementName);
                break;
        }

        return typeName;
    }
}
