package pyxis.uzuki.live.attribute.parser.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrString
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrStringModel(element: VariableElement) : BaseAttrModel<AttrString>(element, AttrString::class.java) {
    init {
        source = annotation.value
    }
}