package pyxis.uzuki.live.attribute.parser.compiler.model

import javax.lang.model.element.VariableElement

import pyxis.uzuki.live.attribute.parser.annotation.AttrDimension
import pyxis.uzuki.live.attribute.parser.annotation.AttrDrawable
import pyxis.uzuki.live.attribute.parser.annotation.AttrFloat

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrFloatModel(element: VariableElement) : BaseAttrModel(element) {
    val defValue: Float

    init {
        val annotation = element.getAnnotation<AttrFloat>(AttrFloat::class.java)
        source = annotation.value
        defValue = annotation.def
    }
}