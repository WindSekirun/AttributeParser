package pyxis.uzuki.live.attribute.parser.compiler.utils;

/**
 * AttributesParser
 * Class: StringUtils
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class StringUtils {

    public static String multiply(CharSequence delimiter, int count) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < count; i++) {
            str.append(delimiter);
        }

        return str.toString();
    }
}
