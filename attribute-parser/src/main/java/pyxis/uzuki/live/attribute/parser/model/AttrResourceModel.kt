package pyxis.uzuki.live.attribute.parser.model

import pyxis.uzuki.live.attribute.parser.annotation.AttrResource
import javax.lang.model.element.VariableElement

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

class AttrResourceModel(element: VariableElement) : BaseAttrModel<AttrResource>(element, AttrResource::class.java) {
    init {
        source = annotation.value
        defValue = annotation.def
    }
}