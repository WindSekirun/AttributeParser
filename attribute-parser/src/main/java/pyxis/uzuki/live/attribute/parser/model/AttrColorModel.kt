package pyxis.uzuki.live.attribute.parser.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrColor
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrColorModel(element: VariableElement) : BaseAttrModel<AttrColor>(element, AttrColor::class.java) {
    init {
        source = annotation.value
        defValue = annotation.def
    }
}