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

class AttrFloatModel(element: VariableElement) : BaseAttrModel(element) {
    val source: String
    val defValue: Float

    init {

        val annotation = element.getAnnotation(AttrFloat::class.java)

        source = annotation.value
        defValue = annotation.def

        findEnclosingClass()
    }
}