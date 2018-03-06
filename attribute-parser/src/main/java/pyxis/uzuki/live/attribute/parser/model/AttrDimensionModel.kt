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

class AttrDimensionModel(element: VariableElement) : BaseAttrModel(element) {
    val source: String
    val defValue: Float

    init {

        val annotation = element.getAnnotation(AttrDimension::class.java)

        source = annotation.value
        defValue = annotation.def

        findEnclosingClass()
    }
}