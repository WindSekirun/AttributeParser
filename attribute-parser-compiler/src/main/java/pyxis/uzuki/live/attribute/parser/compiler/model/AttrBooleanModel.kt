package pyxis.uzuki.live.attribute.parser.compiler.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrBoolean
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrBooleanModel(element: VariableElement) : BaseAttrModel(element) {
    init {
        val annotation = element.getAnnotation<AttrBoolean>(AttrBoolean::class.java)
        source = annotation.value
        defValue = element
    }
}