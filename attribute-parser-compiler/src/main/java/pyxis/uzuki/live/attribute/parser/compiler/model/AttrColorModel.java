package pyxis.uzuki.live.attribute.parser.compiler.model;

import javax.lang.model.element.VariableElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttrColor;
import pyxis.uzuki.live.attribute.parser.annotation.AttrInt;

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class AttrColorModel extends BaseAttrModel {
    private String mSource;
    private int mDefValue;

    public AttrColorModel(VariableElement element) {
        super(element);

        AttrColor annotation = element.getAnnotation(AttrColor.class);

        mSource = annotation.value();
        mDefValue = annotation.def();

        findEnclosingClass();
    }

    public String getSource() {
        return mSource;
    }

    public int getDefValue() {
        return mDefValue;
    }
}