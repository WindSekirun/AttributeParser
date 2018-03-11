package pyxis.uzuki.live.attribute.parser.compiler.model

import javax.lang.model.element.VariableElement

import pyxis.uzuki.live.attribute.parser.annotation.AttrResource

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrResourceModel(element: VariableElement) : BaseAttrModel(element) {
    val defValue: Int

    init {
        val annotation = element.getAnnotation(AttrResource::class.java)
        source = annotation.value
        defValue = annotation.def
    }
}