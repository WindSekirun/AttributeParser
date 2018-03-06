package pyxis.uzuki.live.attribute.parser.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrFloat
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrFloatModel(element: VariableElement) : BaseAttrModel<AttrFloat>(element, AttrFloat::class.java) {
    init {
        source = annotation.value
        defValue = annotation.def
    }
}