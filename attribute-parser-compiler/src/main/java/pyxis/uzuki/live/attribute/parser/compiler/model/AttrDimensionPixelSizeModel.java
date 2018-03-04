package pyxis.uzuki.live.attribute.parser.compiler.model;

import javax.lang.model.element.VariableElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttrDimensionPixelSize;

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class AttrDimensionPixelSizeModel extends BaseAttrModel {
    private String mSource;
    private int mDefValue;

    public AttrDimensionPixelSizeModel(VariableElement element) {
        super(element);

        AttrDimensionPixelSize annotation = element.getAnnotation(AttrDimensionPixelSize.class);

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