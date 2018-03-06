package pyxis.uzuki.live.attribute.parser.model

import javax.lang.model.element.VariableElement

import pyxis.uzuki.live.attribute.parser.annotation.AttrBoolean

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrBooleanModel(element: VariableElement) : BaseAttrModel(element) {
    val source: String
    val defValue: Boolean

    init {

        val annotation = element.getAnnotation(AttrBoolean::class.java)

        source = annotation.value
        defValue = annotation.def

        findEnclosingClass()
    }
}