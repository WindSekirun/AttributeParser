package pyxis.uzuki.live.attribute.parser.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrDrawable
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrDrawableModel(element: VariableElement) : BaseAttrModel<AttrDrawable>(element, AttrDrawable::class.java) {
    init {
        source = annotation.value
    }
}