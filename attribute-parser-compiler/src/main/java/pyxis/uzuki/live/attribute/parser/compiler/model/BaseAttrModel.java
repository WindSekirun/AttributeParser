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

    /**
     * {@link VariableElement} 를 둘러싸고 있는 최상위 레벨 식별자를 찾을 떄 까지 elements hierarchy(요소 계층)
     * 을 통과시킴
     */
    protected void findEnclosingClass() {
        Element element = mElement;

        while (element != null && element.getKind() != ElementKind.CLASS) {
            element = element.getEnclosingElement();
        }

        if (element != null) {
            mEnclosingClass = ((TypeElement) element).getQualifiedName().toString();
        }
    }

    /**
     * Annotation 의 변수 이름을 찾음
     * <code>@AttrInt int hintText</code>
     * <p>
     * 에서 hintText 를 리턴함
     */
    public String getAnnotatedElementName() {
        return mElement.getSimpleName().toString();
    }

    /**
     * Annotation 의 타입을 찾음
     * <p>
     * <code>@AttrInt int hintText</code>
     * 에서 int 를 리턴함
     */
    public String getAnnotatedElementClass() {
        return mElement.asType().toString();
    }

    /**
     * public 만이 Annotation Processor 에서 처리할 수 있음
     * 나머지는 false
     */
    public boolean isValid() {
        return !mElement.getModifiers().contains(Modifier.PROTECTED) &&
                !mElement.getModifiers().contains(Modifier.PRIVATE) &&
                !mElement.getModifiers().contains(Modifier.DEFAULT);
    }

    public String getEnclosingClass() {
        return mEnclosingClass;
    }
}