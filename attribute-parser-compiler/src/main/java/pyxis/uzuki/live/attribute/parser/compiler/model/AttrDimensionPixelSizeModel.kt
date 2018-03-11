package pyxis.uzuki.live.attribute.parser.compiler.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrDimension
import javax.lang.model.element.VariableElement

import pyxis.uzuki.live.attribute.parser.annotation.AttrDimensionPixelSize

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrDimensionPixelSizeModel(element: VariableElement) : BaseAttrModel(element) {
    val defValue: Int

    init {
        val annotation = element.getAnnotation<AttrDimensionPixelSize>(AttrDimensionPixelSize::class.java)
        source = annotation.value
        defValue = annotation.def
    }
}