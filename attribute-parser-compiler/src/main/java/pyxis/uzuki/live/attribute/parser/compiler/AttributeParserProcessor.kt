package pyxis.uzuki.live.attribute.parser.compiler

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import pyxis.uzuki.live.attribute.parser.annotation.CustomView
import pyxis.uzuki.live.attribute.parser.model.*
import pyxis.uzuki.live.attribute.parser.utils.*
import java.io.File
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

/**
 * AttributesParser
 * Class: AttributeParserProcessor
 * Created by Pyxis on 3/4/18.
 *
 * Description:
 */
@SupportedAnnotationTypes("pyxis.uzuki.live.attribute.parser.annotation.AttrInt",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrBoolean",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrColor",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrDimension",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrDimensionPixelSize",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrDrawable",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrFloat",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrResource",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrString",
        "pyxis.uzuki.live.attribute.parser.annotation.CustomView")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(javax.annotation.processing.Processor::class)
class AttributeParserProcessor : AbstractProcessor() {
    private val mCustomViewHolderMap = HashMap<ClassName, CustomViewHolder>()
    private val mAttrIntMap = HashMap<String, List<AttrIntModel>>()
    private val mAttrBooleanMap = HashMap<String, List<AttrBooleanModel>>()
    private val mAttrColorMap = HashMap<String, List<AttrColorModel>>()
    private val mAttrDimensionMap = HashMap<String, List<AttrDimensionModel>>()
    private val mAttrDimensionPixelSizeMap = HashMap<String, List<AttrDimensionPixelSizeModel>>()
    private val mAttrDrawableMap = HashMap<String, List<AttrDrawableModel>>()
    private val mAttrFloatMap = HashMap<String, List<AttrFloatModel>>()
    private val mAttrResourceMap = HashMap<String, List<AttrResourceModel>>()
    private val mAttrStringMap = HashMap<String, List<AttrStringModel>>()

//    override fun getSupportedAnnotationTypes(): MutableSet<String> {
//        return mutableSetOf(AttrInt::class.java.name, AttrBoolean::class.java.name,
//                AttrColor::class.java.name, AttrDimension::class.java.name,
//                AttrDimensionPixelSize::class.java.name, AttrDrawable::class.java.name,
//                AttrFloat::class.java.name, AttrResource::class.java.name,
//                AttrString::class.java.name, CustomView::class.java.name)
//    }

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        processAnnotation(roundEnvironment)
        for (customViewHolder in mCustomViewHolderMap.values) {
            writeAttributes(customViewHolder)
        }
        return true
    }

    private fun processAnnotation(env: RoundEnvironment) {
        for (element in env.getElementsAnnotatedWith(CustomView::class.java)) {
            val classFullName = (element as TypeElement).asClassName()
            val className = element.getSimpleName().toString()
            mCustomViewHolderMap[classFullName] = CustomViewHolder(element, classFullName, className)
        }

        parseModelIntoMap(env, mAttrIntMap, attrIntPair)
        parseModelIntoMap(env, mAttrStringMap, attrStringPair)
        parseModelIntoMap(env, mAttrBooleanMap, attrBooleanPair)
        parseModelIntoMap(env, mAttrColorMap, attrColorPair)
        parseModelIntoMap(env, mAttrDimensionMap, attrDimensionPair)
        parseModelIntoMap(env, mAttrDimensionPixelSizeMap, attrDimensionPixelSizePair)
        parseModelIntoMap(env, mAttrDrawableMap, attrDrawablePair)
        parseModelIntoMap(env, mAttrFloatMap, attrFloatPair)
        parseModelIntoMap(env, mAttrResourceMap, attrResourcePair)
    }

    private fun writeAttributes(customViewHolder: CustomViewHolder) {
        val fileName = customViewHolder.className + Constants.ATTRIBUTES
        val builder = TypeSpec.classBuilder(fileName).addModifiers(KModifier.PUBLIC)

        val models = getModelList<BaseAttrModel<*>>(customViewHolder.className,
                mAttrIntMap, mAttrStringMap, mAttrBooleanMap, mAttrColorMap, mAttrDimensionMap, mAttrDimensionPixelSizeMap,
                mAttrDrawableMap, mAttrFloatMap, mAttrResourceMap)

        for (model in models) {
            builder.addProperty(createAttrsFieldSpec(model))
        }

        builder.addProperty(createRFieldSpec(customViewHolder))
        builder.addFunction(createObtainApplyMethodSpec(customViewHolder))
        builder.addFunction(createApplyMethodSpec(customViewHolder, models))
        builder.addFunction(createPrintVariableMethodSpec(customViewHolder, models))
        builder.addFunction(createBindAttributesMethodSpec(customViewHolder, models))

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]

        val file = FileSpec.builder(Constants.PACKAGE_NAME, fileName)
                .addType(builder.build())
                .build()

        file.writeTo(File(kaptKotlinGeneratedDir, "$fileName.kt"))
    }

    private fun createRFieldSpec(customViewHolder: CustomViewHolder): PropertySpec {
        val typeName = ClassName.bestGuess(customViewHolder.classNameComplete.packageName() + Constants.R_FILE)
        return PropertySpec.builder(Constants.R_VARIABLE, typeName, KModifier.PRIVATE)
                .initializer("R()")
                .build()
    }

    private fun createAttrsFieldSpec(model: BaseAttrModel<*>): PropertySpec {
        val variableName = model.annotatedElementName
        val typeName = (model.annotatedElementClass).bestGuess()
        return PropertySpec.builder(variableName, typeName, KModifier.CONST).build()
    }

    private fun createPrintVariableMethodSpec(customViewHolder: CustomViewHolder, models: List<BaseAttrModel<*>>): FunSpec {
        val builder = FunSpec.builder(Constants.PRINT_VARIABLES)
                .addModifiers(KModifier.PUBLIC)

        val variablesBuilder = StringBuilder()
        var maximum = 0
        for (model in models) {
            val variableName = model.annotatedElementName
            val className = model.annotatedElementClass
            val line = "\"\\n$className $variableName = \" + $variableName +  \n"

            variablesBuilder.append(line)

            val count = line.length
            maximum = Math.max(maximum, count)
        }

        val stringBuilder = StringBuilder()
        val lastLine = multiply("=", maximum)
        val firstLine = multiply("=", (maximum - customViewHolder.className.length - 2) / 2)
        val firstLineMessage = String.format("\"%s %s %s\" + \n", firstLine, customViewHolder.className, firstLine)

        stringBuilder.append(firstLineMessage)
        stringBuilder.append(variablesBuilder.toString())
        stringBuilder.append(String.format("\"\\n%s\"", lastLine))

        val message = stringBuilder.toString()
        builder.addCode(String.format(Constants.STATEMENT_LOG, customViewHolder.className, message))

        return builder.build()
    }

    private fun createObtainApplyMethodSpec(customViewHolder: CustomViewHolder): FunSpec {
        val classTypeName = customViewHolder.classNameComplete
        val classTypeParameterName = (customViewHolder.className.substring(0, 1).toLowerCase() + customViewHolder.className.substring(1))

        val builder = FunSpec.builder(Constants.OBTAIN_APPLY)
                .addModifiers(KModifier.PUBLIC)
                .addParameter(classTypeParameterName, classTypeName)
                .addParameter(Constants.SET, Constants.ATTRIBUTE_SET_CLASS_NAME)
                .addCode(String.format(Constants.STATEMENT_OBTAIN_APPLY, classTypeParameterName,
                        classTypeParameterName, customViewHolder.className))

        return builder.build()
    }

    private fun createApplyMethodSpec(customViewHolder: CustomViewHolder, models: List<BaseAttrModel<*>>): FunSpec {
        val classTypeName = customViewHolder.classNameComplete
        val classTypeParameterName = (customViewHolder.className.substring(0, 1).toLowerCase() + customViewHolder.className.substring(1))

        val builder = FunSpec.builder(Constants.APPLY)
                .addModifiers(KModifier.PUBLIC)
                .addParameter(classTypeParameterName, classTypeName)
                .addParameter(Constants.ARRAY, Constants.TYPED_ARRAY_CLASS_NAME)
                .addCode(Constants.BIND_ATTRIBUTES_INVOKE)

        models
                .map { it.annotatedElementName }
                .forEach {
                    builder.addCode(String.format(Constants.STATEMENT_APPLY, classTypeParameterName, it, it))
                }

        return builder.build()
    }

    private fun createBindAttributesMethodSpec(customViewHolder: CustomViewHolder, models: List<BaseAttrModel<*>>): FunSpec {
        val builder = FunSpec.builder(Constants.BIND_ATTRIBUTES)
                .addModifiers(KModifier.PRIVATE)
                .addParameter(Constants.ARRAY, Constants.TYPED_ARRAY_CLASS_NAME)
                .addCode(Constants.STATEMENT_BINDATTRIBUTES)

        for (model in models) {
            (builder).addCode(model, customViewHolder.className)
        }

        builder.addCode(Constants.STATEMENT_RECYCLE)

        return builder.build()
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}