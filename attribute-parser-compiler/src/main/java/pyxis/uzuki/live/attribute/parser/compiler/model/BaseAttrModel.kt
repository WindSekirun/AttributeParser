package pyxis.uzuki.live.attribute.parser.compiler.model

import javax.lang.model.element.*

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

open class BaseAttrModel(private var mElement: VariableElement) {
    var enclosingClass: String = ""
        protected set
    var defValue: Any? = null
    var source = ""

    val annotatedElementName: String
        get() = mElement.simpleName.toString()

    val annotatedElementClass: String
        get() = mElement.asType().toString()

    val isValid: Boolean
        get() = mElement.modifiers.contains(Modifier.PUBLIC)

    init {
        findEnclosingClass()
    }

    private fun findEnclosingClass() {
        var element: Element? = mElement

        while (element != null && element.kind != ElementKind.CLASS) {
            element = element.enclosingElement
        }

        if (element != null) {
            enclosingClass = (element as TypeElement).qualifiedName.toString()
        }
    }
}