package pyxis.uzuki.live.attribute.parser.compiler.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrString
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrStringModel(element: VariableElement) : BaseAttrModel(element) {

    init {
        val annotation = element.getAnnotation(AttrString::class.java)
        source = annotation.value
        defValue = annotation.defValue
    }
}