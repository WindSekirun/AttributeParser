@file:JvmName("Utils")
@file:JvmMultifileClass

package pyxis.uzuki.live.attribute.parser.compiler.utils

fun multiply(delimiter: CharSequence, count: Int): String {
    val str = StringBuilder()
    for (i in 0 until count) {
        str.append(delimiter)
    }

    return str.toString()
}