package pyxis.uzuki.live.attribute.parser.compiler.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrInt
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrIntModel(element: VariableElement) : BaseAttrModel(element) {
    init {
        val annotation = element.getAnnotation<AttrInt>(AttrInt::class.java)
        source = annotation.value
        findDefaultValue(0)
    }
}