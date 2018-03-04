package pyxis.uzuki.live.attribute.parser.compiler.model;

import javax.lang.model.element.VariableElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttrDimension;
import pyxis.uzuki.live.attribute.parser.annotation.AttrFloat;

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class AttrFloatModel extends BaseAttrModel {
    private String mSource;
    private float mDefValue;

    public AttrFloatModel(VariableElement element) {
        super(element);

        AttrFloat annotation = element.getAnnotation(AttrFloat.class);

        mSource = annotation.value();
        mDefValue = annotation.def();

        findEnclosingClass();
    }

    public String getSource() {
        return mSource;
    }

    public float getDefValue() {
        return mDefValue;
    }
}