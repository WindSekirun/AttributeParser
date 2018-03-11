package pyxis.uzuki.live.attribute.parser.compiler.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrFloat
import javax.lang.model.element.VariableElement

import pyxis.uzuki.live.attribute.parser.annotation.AttrInt

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrIntModel(element: VariableElement) : BaseAttrModel(element) {
    val defValue: Int

    init {
        val annotation = element.getAnnotation<AttrInt>(AttrInt::class.java)
        source = annotation.value
        defValue = annotation.defValue
    }
}