package pyxis.uzuki.live.attribute.parser.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrDimensionPixelSize
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrDimensionPixelSizeModel(element: VariableElement) : BaseAttrModel<AttrDimensionPixelSize>(element, AttrDimensionPixelSize::class.java) {
    init {
        source = annotation.value
        defValue = annotation.def
    }
}