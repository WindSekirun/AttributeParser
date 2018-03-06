package pyxis.uzuki.live.attribute.parser.compiler.utils

import pyxis.uzuki.live.attribute.parser.model.*

/**
 * AttributesParser
 * Class: AttrModelUtils
 * Created by Pyxis on 3/4/18.
 *
 *
 * Description:
 */

object AttrModelUtils {

    fun createIntCode(model: AttrIntModel, className: String): String {
        val variableName = model.annotatedElementName
        val source = reviseSource(className, variableName, model.source)
        val defValue = model.defValue

        return String.format("%s = array.getInt(%s, %s);\n", variableName, source, defValue)
    }

    fun createBooleanCode(model: AttrBooleanModel, className: String): String {
        val variableName = model.annotatedElementName
        val source = reviseSource(className, variableName, model.source)
        val defValue = model.defValue

        return String.format("%s = array.getBoolean(%s, %s);\n", variableName, source, defValue)
    }

    fun createColorCode(model: AttrColorModel, className: String): String {
        val variableName = model.annotatedElementName
        val source = reviseSource(className, variableName, model.source)
        val defValue = model.defValue

        return String.format("%s = array.getColor(%s, %s);\n", variableName, source, defValue)
    }

    fun createDimensionCode(model: AttrDimensionModel, className: String): String {
        val variableName = model.annotatedElementName
        val source = reviseSource(className, variableName, model.source)
        val defValue = model.defValue

        return String.format("%s = array.getDimension(%s, %sf);\n", variableName, source, defValue)
    }

    fun createDimensionPixelCode(model: AttrDimensionPixelSizeModel, className: String): String {
        val variableName = model.annotatedElementName
        val source = reviseSource(className, variableName, model.source)
        val defValue = model.defValue

        return String.format("%s = array.getDimensionPixelSize(%s, %s);\n", variableName, source, defValue)
    }

    fun createDrawableCode(model: AttrDrawableModel, className: String): String {
        val variableName = model.annotatedElementName
        val source = reviseSource(className, variableName, model.source)

        return String.format("%s = array.getDrawable(%s);\n", variableName, source)
    }

    fun createFloatCode(model: AttrFloatModel, className: String): String {
        val variableName = model.annotatedElementName
        val source = reviseSource(className, variableName, model.source)
        val defValue = model.defValue

        return String.format("%s = array.getFloat(%s, %sf);\n", variableName, source, defValue)
    }

    fun createResourceCode(model: AttrResourceModel, className: String): String {
        val variableName = model.annotatedElementName
        val source = reviseSource(className, variableName, model.source)
        val defValue = model.defValue

        return String.format("%s = array.getResourceId(%s, %s);\n", variableName, source, defValue)
    }

    fun createStringCode(model: AttrStringModel, className: String): String {
        val variableName = model.annotatedElementName
        val source = reviseSource(className, variableName, model.source)

        return String.format("%s = array.getString(%s);\n", variableName, source)
    }

    private fun reviseSource(className: String, variableName: String, source: String): String? {
        var newSource: String? = source

        if (newSource == null || newSource.isEmpty() || newSource.trim { it <= ' ' }.isEmpty()) {
            newSource = String.format("%s_%s", className, variableName)
        }

        newSource = String.format("R.styleable.%s", newSource)

        return newSource
    }
}
