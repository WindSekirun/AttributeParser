@file:JvmName("Utils")
@file:JvmMultifileClass

package pyxis.uzuki.live.attribute.parser.compiler.utils

import com.squareup.javapoet.MethodSpec
import pyxis.uzuki.live.attribute.parser.annotation.*
import pyxis.uzuki.live.attribute.parser.compiler.model.*
import java.util.*
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.VariableElement
import kotlin.reflect.KClass

val supportedAnnotationSet = classNameSetOf(AttrInt::class, AttrBoolean::class,
        AttrColor::class, AttrDimension::class, AttrInteger::class, AttrFraction::class,
        AttrFloat::class, AttrReference::class, AttrString::class, CustomView::class, AttributeParser::class)

val attrIntPair = AttrInt::class to AttrIntModel::class
val attrStringPair = AttrString::class to AttrStringModel::class
val attrBooleanPair = AttrBoolean::class to AttrBooleanModel::class
val attrColorPair = AttrColor::class to AttrColorModel::class
val attrDimensionPair = AttrDimension::class to AttrDimensionModel::class
val attrFloatPair = AttrFloat::class to AttrFloatModel::class
val attrResourcePair = AttrReference::class to AttrReferenceModel::class
val attrIntegerPair = AttrInteger::class to AttrIntegerModel::class
val attrFractionPair = AttrFraction::class to AttrFractionModel::class

fun MethodSpec.Builder.addCode(model: BaseAttrModel, className: String) {
    when (model) {
        is AttrIntModel -> this.addCode(createIntCode(model, className))
        is AttrStringModel -> this.addCode(createStringCode(model, className))
        is AttrBooleanModel -> this.addCode(createBooleanCode(model, className))
        is AttrColorModel -> this.addCode(createColorCode(model, className))
        is AttrDimensionModel -> this.addCode(createDimensionCode(model, className))
        is AttrFloatModel -> this.addCode(createFloatCode(model, className))
        is AttrReferenceModel -> this.addCode(createResourceCode(model, className))
        is AttrFractionModel -> this.addCode(createFractionCode(model, className))
        is AttrIntegerModel -> this.addCode(createIntegerCode(model, className))
    }

    if (model is AttrStringModel) {
        addEmptyCondition(model)
    }
}

fun MethodSpec.Builder.addEmptyCondition(model: AttrStringModel) {
    val variableName = model.annotatedElementName
    val defValue = "\"${model.annotatedElementConstantName}\""

    this.addCode("\n")

    this.beginControlFlow("if ($variableName == null || $variableName.length() == 0)")
            .addCode(String.format("%s = %s;\n", variableName, defValue))
            .endControlFlow()
}

fun <T : BaseAttrModel> getModelList(className: String, vararg maps: Map<String, List<T>>) =
        maps.map { getModelList(it, className) }.flatMap { it }.toList()

fun <A : Annotation, T : BaseAttrModel> RoundEnvironment.findAnnotatedWith(pair: Pair<Class<A>, Class<T>>,
                                                                           map: MutableMap<String, List<T>>) {
    findAnnotatedWith(pair.first, pair.second, map)
}

fun <A : Annotation, T : BaseAttrModel> RoundEnvironment.findAnnotatedWith(cls: Class<A>, modelCls: Class<T>,
                                                                           map: MutableMap<String, List<T>>) {
    this.getElementsAnnotatedWith(cls)
            .map { modelCls.getConstructor(VariableElement::class.java).newInstance(it) }
            .filter { it.isValid }
            .forEach { value ->
                var elementsClasses: MutableList<T>? = map[value.enclosingClass]?.toMutableList()
                if (elementsClasses == null || elementsClasses.isEmpty()) {
                    elementsClasses = ArrayList()
                }

                elementsClasses.add(value)
                map[value.enclosingClass] = elementsClasses
            }
}

fun <T : KClass<*>> classNameSetOf(vararg elements: T) = elements.map { it.java.name }.toHashSet()

private fun <T : BaseAttrModel> getModelList(map: Map<String, List<T>>, className: String): List<T> {
    var models: List<T> = ArrayList()

    for ((key, value) in map) {
        if (key.contains(className)) {
            models = value
            break
        }
    }

    return models
}

private infix fun <A : Annotation, T : BaseAttrModel> KClass<A>.to(cls: KClass<T>) = Pair(this.java, cls.java)

private fun createModelCode(formatStr: String, variableName: String, source: String?, defValue: Any? = null) = if (defValue == null) {
    formatStr.format(variableName, source)
} else {
    formatStr.format(variableName, source, defValue)
}

private fun <T : BaseAttrModel> createModelCode(model: T, formatStr: String, className: String, defValue: Any?): String {
    val variableName = model.annotatedElementName
    val source = reviseSource(className, variableName, model.source)
    return createModelCode(formatStr, variableName, source, defValue)
}

private fun reviseSource(className: String, variableName: String, source: String): String? {
    var newSource: String? = source

    if (newSource == null || newSource.isEmpty() || newSource.trim { it <= ' ' }.isEmpty()) {
        newSource = String.format("%s_%s", className, variableName)
    }

    newSource = String.format("R.styleable.%s", newSource)

    return newSource
}

private fun createIntCode(model: AttrIntModel, className: String) =
        createModelCode(model, "%s = array.getInt(%s, %s);\n", className, model.annotatedElementConstantName)

private fun createBooleanCode(model: AttrBooleanModel, className: String) =
        createModelCode(model, "%s = array.getBoolean(%s, %s);\n", className, model.annotatedElementConstantName)

private fun createColorCode(model: AttrColorModel, className: String) =
        createModelCode(model, "%s = array.getColor(%s, %s);\n", className, model.annotatedElementConstantName)

private fun createDimensionCode(model: AttrDimensionModel, className: String) =
        createModelCode(model, "%s = array.getDimension(%s, %sf);\n", className, model.annotatedElementConstantName)

private fun createFloatCode(model: AttrFloatModel, className: String) =
        createModelCode(model, "%s = array.getFloat(%s, %sf);\n", className, model.annotatedElementConstantName)

private fun createResourceCode(model: AttrReferenceModel, className: String) =
        createModelCode(model, "%s = array.getResourceId(%s, %s);\n", className, model.annotatedElementConstantName)

private fun createStringCode(model: AttrStringModel, className: String) =
        createModelCode(model, "%s = array.getString(%s);\n", className, null)

private fun createIntegerCode(model: AttrIntegerModel, className: String) =
        createModelCode(model, "%s = array.getInteger(%s, %s);\n", className, model.annotatedElementConstantName)

private fun createFractionCode(model: AttrFractionModel, className: String): String {
    val variableName = model.annotatedElementName
    val source = reviseSource(className, variableName, model.source)
    return "%s = array.getFraction(%s, %s, %s, %s);\n".format(variableName, source, model.base, model.pbase,
            model.annotatedElementConstantName)
}