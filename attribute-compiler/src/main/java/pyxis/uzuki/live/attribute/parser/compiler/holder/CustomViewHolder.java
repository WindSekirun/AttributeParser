package pyxis.uzuki.live.attribute.parser.compiler.holder;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Element;

public class CustomViewHolder {
    public Element element;
    public ClassName classNameComplete;
    public String className;

    public CustomViewHolder(Element element, ClassName classNameComplete, String className) {
        this.element = element;
        this.classNameComplete = classNameComplete;
        this.className = className;
    }

}
