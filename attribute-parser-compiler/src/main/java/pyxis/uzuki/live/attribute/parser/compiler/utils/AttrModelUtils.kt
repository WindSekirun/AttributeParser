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
        AttrColor::class, AttrDimension::class, AttrDimensionPixelSize::class, AttrDrawable::class,
        AttrFloat::class, AttrResource::class, AttrString::class, CustomView::class, AttributeParser::class)

val attrIntPair = AttrInt::class to AttrIntModel::class
val attrStringPair = AttrString::class to AttrStringModel::class
val attrBooleanPair = AttrBoolean::class to AttrBooleanModel::class
val attrColorPair = AttrColor::class to AttrColorModel::class
val attrDimensionPair = AttrDimension::class to AttrDimensionModel::class
val attrDimensionPixelSizePair = AttrDimensionPixelSize::class to AttrDimensionPixelSizeModel::class
val attrDrawablePair = AttrDrawable::class to AttrDrawableModel::class
val attrFloatPair = AttrFloat::class to AttrFloatModel::class
val attrResourcePair = AttrResource::class to AttrResourceModel::class

fun MethodSpec.Builder.addCode(model: BaseAttrModel, className: String) {
    when (model) {
        is AttrIntModel -> this.addCode(createIntCode(model, className))
        is AttrStringModel -> this.addCode(createStringCode(model, className))
        is AttrBooleanModel -> this.addCode(createBooleanCode(model, className))
        is AttrColorModel -> this.addCode(createColorCode(model, className))
        is AttrDimensionModel -> this.addCode(createDimensionCode(model, className))
        is AttrDimensionPixelSizeModel -> this.addCode(createDimensionPixelCode(model, className))
        is AttrDrawableModel -> this.addCode(createDrawableCode(model, className))
        is AttrFloatModel -> this.addCode(createFloatCode(model, className))
        is AttrResourceModel -> this.addCode(createResourceCode(model, className))
    }
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
        createModelCode(model, "%s = array.getInt(%s, %s);\n", className, model.defValue)

private fun createBooleanCode(model: AttrBooleanModel, className: String) =
        createModelCode(model, "%s = array.getBoolean(%s, %s);\n", className, model.defValue)

private fun createColorCode(model: AttrColorModel, className: String) =
        createModelCode(model, "%s = array.getColor(%s, %s);\n", className, model.defValue)

private fun createDimensionCode(model: AttrDimensionModel, className: String) =
        createModelCode(model, "%s = array.getDimension(%s, %sf);\n", className, model.defValue)

private fun createDimensionPixelCode(model: AttrDimensionPixelSizeModel, className: String) =
        createModelCode(model, "%s = array.getDimensionPixelSize(%s, %s);\n", className, model.defValue)

private fun createDrawableCode(model: AttrDrawableModel, className: String) =
        createModelCode(model, "%s = array.getString(%s);\n", className, null)

private fun createFloatCode(model: AttrFloatModel, className: String) =
        createModelCode(model, "%s = array.getFloat(%s, %sf);\n", className, model.defValue)

private fun createResourceCode(model: AttrResourceModel, className: String) =
        createModelCode(model, "%s = array.getResourceId(%s, %s);\n", className, model.defValue)

private fun createStringCode(model: AttrStringModel, className: String) =
        createModelCode(model, "%s = array.getString(%s);\n", className, null)