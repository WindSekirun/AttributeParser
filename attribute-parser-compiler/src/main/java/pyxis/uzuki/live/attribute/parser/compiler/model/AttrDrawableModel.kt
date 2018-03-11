package pyxis.uzuki.live.attribute.parser.compiler.model

import javax.lang.model.element.VariableElement

import pyxis.uzuki.live.attribute.parser.annotation.AttrColor
import pyxis.uzuki.live.attribute.parser.annotation.AttrDimension
import pyxis.uzuki.live.attribute.parser.annotation.AttrDrawable

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrDrawableModel(element: VariableElement) : BaseAttrModel(element) {
    init {
        val annotation = element.getAnnotation<AttrDrawable>(AttrDrawable::class.java)
        source = annotation.value
    }
}