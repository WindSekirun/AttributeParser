package pyxis.uzuki.live.attribute.parser.compiler.model;

import javax.lang.model.element.VariableElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttrBoolean;

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class AttrBooleanModel extends BaseAttrModel {
    private String mSource;
    private boolean mDefValue;

    public AttrBooleanModel(VariableElement element) {
        super(element);

        AttrBoolean annotation = element.getAnnotation(AttrBoolean.class);

        mSource = annotation.value();
        mDefValue = annotation.def();

        findEnclosingClass();
    }

    public String getSource() {
        return mSource;
    }

    public boolean getDefValue() {
        return mDefValue;
    }
}