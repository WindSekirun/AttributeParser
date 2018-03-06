package pyxis.uzuki.live.attribute.parser.compiler.utils

/**
 * AttributesParser
 * Class: StringUtils
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

object StringUtils {

    fun multiply(delimiter: CharSequence, count: Int): String {
        val str = StringBuilder()
        for (i in 0 until count) {
            str.append(delimiter)
        }

        return str.toString()
    }
}
