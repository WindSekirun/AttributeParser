@file:JvmName("AttrModelUtils")
@file:JvmMultifileClass

package pyxis.uzuki.live.attribute.parser.compiler.utils

import com.squareup.javapoet.MethodSpec
import pyxis.uzuki.live.attribute.parser.annotation.*
import pyxis.uzuki.live.attribute.parser.model.*
import java.util.*
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.VariableElement

// constants
val attrIntPair = AttrInt::class.java to AttrIntModel::class.java
val attrStringPair = AttrString::class.java to AttrStringModel::class.java
val attrBooleanPair = AttrBoolean::class.java to AttrBooleanModel::class.java
val attrColorPair = AttrColor::class.java to AttrColorModel::class.java
val attrDimensionPair = AttrDimension::class.java to AttrDimensionModel::class.java
val attrDimensionPixelSizePair = AttrDimensionPixelSize::class.java to AttrDimensionPixelSizeModel::class.java
val attrDrawablePair = AttrDrawable::class.java to AttrDrawableModel::class.java
val attrFloatPair = AttrFloat::class.java to AttrFloatModel::class.java
val attrResourcePair = AttrResource::class.java to AttrResourceModel::class.java

/**
 * parse annotation list for finding usages within [RoundEnvironment]
 */
fun <T : BaseAttrModel<*>, A : Annotation> parseModelIntoMap(env: RoundEnvironment, map: MutableMap<String, List<T>>, pair: Pair<Class<A>, Class<T>>) {
    env.getElementsAnnotatedWith(pair.first)
            .map { pair.second.getConstructor(Class.forName(VariableElement::class.java.name)).newInstance(it as VariableElement) }
            .filter { it.isValid }
            .forEach { updateAttrMapList(map, it) }
}

/**
 * adding code statement into [MethodSpec.Builder] using [BaseAttrModel]
 */
fun MethodSpec.Builder.addCode(model: BaseAttrModel<*>, className: String) {
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

/**
 * getting [BaseAttrModel] list for generate fields
 */
fun <T : BaseAttrModel<*>> getModelList(className: String, vararg targetMaps: Map<String, List<T>>) = targetMaps.map { getTargetList(it, className) }.flatMap { it }

private fun <T : BaseAttrModel<*>> getTargetList(map: Map<String, List<T>>, className: String): List<T> {
    var models: List<T> = ArrayList()

    for ((key, value) in map) {
        if (key.contains(className)) {
            models = value
            break
        }
    }

    return models
}

private fun <T : BaseAttrModel<*>> updateAttrMapList(map: MutableMap<String, List<T>>, model: T) {
    var elementsClasses = map[model.enclosingClass]?.toMutableList()
    if (elementsClasses == null || elementsClasses.isEmpty()) {
        elementsClasses = ArrayList()
    }

    elementsClasses.add(model)
    map[model.enclosingClass] = elementsClasses
}

private fun createIntCode(model: AttrIntModel, className: String) =
        createCode(model, "%s = array.getInt(%s, %s);\n", className, model.defValue as Int)

private fun createBooleanCode(model: AttrBooleanModel, className: String) =
        createCode(model, "%s = array.getBoolean(%s, %s);\n", className, model.defValue as Boolean)

private fun createColorCode(model: AttrColorModel, className: String) =
        createCode(model, "%s = array.getColor(%s, %s);\n", className, model.defValue as Int)

private fun createDimensionCode(model: AttrDimensionModel, className: String) =
        createCode(model, "%s = array.getDimension(%s, %sf);\n", className, model.defValue as Float)

private fun createDimensionPixelCode(model: AttrDimensionPixelSizeModel, className: String) =
        createCode(model, "%s = array.getDimensionPixelSize(%s, %s);\n", className, model.defValue as Int)

private fun createDrawableCode(model: AttrDrawableModel, className: String) =
        createCode(model, "%s = array.getDrawable(%s);\n", className)

private fun createFloatCode(model: AttrFloatModel, className: String) =
        createCode(model, "%s = array.getFloat(%s, %sf);\n", className, model.defValue as Float)

private fun createResourceCode(model: AttrResourceModel, className: String) =
        createCode(model, "%s = array.getResourceId(%s, %s);\n", className, model.defValue as Int)

private fun createStringCode(model: AttrStringModel, className: String) =
        createCode(model, "%s = array.getString(%s);\n", className)

private fun createCode(model: BaseAttrModel<*>, format: String, className: String, defValue: Any? = null): String {
    val variableName = model.annotatedElementName
    val source = reviseSource(className, variableName, model.source)

    return if (defValue != null) {
        format.format(variableName, source, defValue)
    } else {
        format.format(variableName, source)
    }
}


private fun reviseSource(className: String, variableName: String, source: String): String? {
    var newSource: String? = source

    if (newSource == null || newSource.isEmpty() || newSource.trim { it <= ' ' }.isEmpty()) {
        newSource = String.format("%s_%s", className, variableName)
    }

    newSource = String.format("R.styleable.%s", newSource)

    return newSource
}
