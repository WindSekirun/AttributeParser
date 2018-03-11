package pyxis.uzuki.live.attribute.parser.compiler.holder

import com.squareup.javapoet.ClassName

class CustomViewHolder(val className: ClassName) {
    val simpleName: String = className.simpleName()
}
