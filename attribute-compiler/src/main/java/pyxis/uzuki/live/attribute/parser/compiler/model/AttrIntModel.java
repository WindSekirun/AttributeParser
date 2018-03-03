package pyxis.uzuki.live.attribute.parser.compiler.model;

import javax.lang.model.element.VariableElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttrInt;

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class AttrIntModel extends BaseAttrModel {
    private String mSource;
    private int mDefValue;

    public AttrIntModel(VariableElement element) {
        super(element);

        AttrInt annotation = element.getAnnotation(AttrInt.class);

        mSource = annotation.value();
        mDefValue = annotation.defValue();

        findEnclosingClass();
    }

    public String getSource() {
        return mSource;
    }

    public int getDefValue() {
        return mDefValue;
    }
}