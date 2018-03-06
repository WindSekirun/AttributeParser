package pyxis.uzuki.live.attribute.parser.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrDimension
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrDimensionModel(element: VariableElement) : BaseAttrModel<AttrDimension>(element, AttrDimension::class.java) {
    init {
        source = annotation.value
        defValue = annotation.def
    }
}