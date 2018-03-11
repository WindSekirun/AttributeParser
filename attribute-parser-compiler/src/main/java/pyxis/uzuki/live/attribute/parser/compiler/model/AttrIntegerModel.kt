package pyxis.uzuki.live.attribute.parser.compiler.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrInteger
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrIntegerModel(element: VariableElement) : BaseAttrModel(element) {
    init {
        val annotation = element.getAnnotation<AttrInteger>(AttrInteger::class.java)
        source = annotation.value
    }
}