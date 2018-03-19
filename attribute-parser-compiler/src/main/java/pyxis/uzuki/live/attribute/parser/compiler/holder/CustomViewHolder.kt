package pyxis.uzuki.live.attribute.parser.compiler.holder

import com.squareup.kotlinpoet.ClassName

class CustomViewHolder(val className: ClassName, val packageName: String) {
    val simpleName: String = className.simpleName()
}
