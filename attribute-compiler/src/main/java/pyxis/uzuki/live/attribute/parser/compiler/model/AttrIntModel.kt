package pyxis.uzuki.live.attribute.parser.compiler.model

import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

import pyxis.uzuki.live.attribute.parser.annotation.AttrInt

/**
 * AttributesParser
 * Class: AttrIntModel
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

abstract class AttrIntModel(private val mElement: VariableElement) {
    val source: String
    val defValue: Int
    var enclosingClass: String? = null
        private set

    /**
     * Annotation 의 변수 이름을 찾음
     * `@AttrInt int hintText`
     *
     *
     * 에서 hintText 를 리턴함
     */
    val annotatedElementName: String
        get() = mElement.simpleName.toString()

    /**
     * Annotation 의 타입을 찾음
     *
     *
     * `@AttrInt int hintText`
     * 에서 int 를 리턴함
     */
    val annotatedElementClass: String
        get() = mElement.asType().toString()

    /**
     * public 또는 package default 만이 Annotation Processor 에서 처리할 수 있음
     * 나머지는 false
     */
    val isValid: Boolean
        get() = !mElement.modifiers.contains(Modifier.PROTECTED) && !mElement.modifiers.contains(Modifier.PRIVATE)

    init {
        val annotation = mElement.getAnnotation(AttrInt::class.java)

        source = annotation.value
        defValue = annotation.def

        findEnclosingClass()
    }

    /**
     * [VariableElement] 를 둘러싸고 있는 최상위 레벨 식별자를 찾을 떄 까지 elements hierarchy(요소 계층)
     * 을 통과시킴
     */
    protected fun findEnclosingClass() {
        var element: Element? = mElement

        while (element != null && element.kind != ElementKind.CLASS) {
            element = element.enclosingElement
        }

        if (element != null) {
            enclosingClass = (element as TypeElement).qualifiedName.toString()
        }
    }
}