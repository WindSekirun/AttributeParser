package pyxis.uzuki.live.attribute.parser.compiler.model;

import javax.lang.model.element.VariableElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttrColor;
import pyxis.uzuki.live.attribute.parser.annotation.AttrDimension;

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class AttrDimensionModel extends BaseAttrModel {
    private String mSource;
    private float mDefValue;

    public AttrDimensionModel(VariableElement element) {
        super(element);

        AttrDimension annotation = element.getAnnotation(AttrDimension.class);

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