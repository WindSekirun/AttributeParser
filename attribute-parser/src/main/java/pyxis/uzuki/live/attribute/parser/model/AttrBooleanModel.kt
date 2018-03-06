package pyxis.uzuki.live.attribute.parser.model

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

class AttrBooleanModel(element: VariableElement) : BaseAttrModel<AttrBoolean>(element, AttrBoolean::class.java) {
    init {
        source = annotation.value
        defValue = annotation.def
    }
}