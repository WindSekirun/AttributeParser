package pyxis.uzuki.live.attribute.parser.compiler.model;

import javax.lang.model.element.VariableElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttrString;

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class AttrStringModel extends BaseAttrModel {
    private String mSource;

    public AttrStringModel(VariableElement element) {
        super(element);

        AttrString annotation = element.getAnnotation(AttrString.class);

        mSource = annotation.value();

        findEnclosingClass();
    }

    public String getSource() {
        return mSource;
    }
}