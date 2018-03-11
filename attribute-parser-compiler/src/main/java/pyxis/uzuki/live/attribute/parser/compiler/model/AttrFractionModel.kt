package pyxis.uzuki.live.attribute.parser.compiler.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrFraction
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrFractionModel(element: VariableElement) : BaseAttrModel(element) {
    var base: Int = 0
    var pbase: Int = 0

    init {
        val annotation = element.getAnnotation<AttrFraction>(AttrFraction::class.java)
        source = annotation.value
        base = annotation.base
        pbase = annotation.pbase
        findDefaultValue(0)
    }
}