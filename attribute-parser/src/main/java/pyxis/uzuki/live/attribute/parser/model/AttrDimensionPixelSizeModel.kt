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

class AttrDimensionPixelSizeModel(element: VariableElement) : BaseAttrModel(element) {
    val source: String
    val defValue: Int

    init {

        val annotation = element.getAnnotation(AttrDimensionPixelSize::class.java)

        source = annotation.value
        defValue = annotation.def

        findEnclosingClass()
    }
}