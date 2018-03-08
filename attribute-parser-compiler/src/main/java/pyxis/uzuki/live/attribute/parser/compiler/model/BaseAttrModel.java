package pyxis.uzuki.live.attribute.parser.compiler.model;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttrInt;

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class BaseAttrModel {
    protected VariableElement mElement;
    protected String mEnclosingClass;

    public BaseAttrModel(VariableElement element) {
        this.mElement = element;
    }

    protected void findEnclosingClass() {
        Element element = mElement;

        while (element != null && element.getKind() != ElementKind.CLASS) {
            element = element.getEnclosingElement();
        }

        if (element != null) {
            mEnclosingClass = ((TypeElement) element).getQualifiedName().toString();
        }
    }

    public String getAnnotatedElementName() {
        return mElement.getSimpleName().toString();
    }

    public String getAnnotatedElementClass() {
        return mElement.asType().toString();
    }

    public boolean isValid() {
        return !mElement.getModifiers().contains(Modifier.PROTECTED) &&
                !mElement.getModifiers().contains(Modifier.PRIVATE) &&
                !mElement.getModifiers().contains(Modifier.DEFAULT);
    }

    public String getEnclosingClass() {
        return mEnclosingClass;
    }
}