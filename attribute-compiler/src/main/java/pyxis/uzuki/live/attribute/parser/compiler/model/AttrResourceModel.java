package pyxis.uzuki.live.attribute.parser.compiler.model;

import javax.lang.model.element.VariableElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttrResource;

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class AttrResourceModel extends BaseAttrModel {
    private String mSource;
    private int mDefValue;

    public AttrResourceModel(VariableElement element) {
        super(element);

        AttrResource annotation = element.getAnnotation(AttrResource.class);

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