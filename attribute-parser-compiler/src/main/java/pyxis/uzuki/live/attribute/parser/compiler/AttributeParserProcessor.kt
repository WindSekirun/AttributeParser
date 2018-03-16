package pyxis.uzuki.live.attribute.parser.compiler

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import pyxis.uzuki.live.attribute.parser.annotation.AttributeParser
import pyxis.uzuki.live.attribute.parser.annotation.CustomView
import pyxis.uzuki.live.attribute.parser.compiler.holder.CustomViewHolder
import pyxis.uzuki.live.attribute.parser.compiler.model.*
import pyxis.uzuki.live.attribute.parser.compiler.utils.*
import java.io.File
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor::class)
class AttributeParserProcessor : AbstractProcessor() {
    private val mCustomViewHolderList = ArrayList<CustomViewHolder>()
    private val mAttrIntMap = HashMap<String, List<AttrIntModel>>()
    private val mAttrBooleanMap = HashMap<String, List<AttrBooleanModel>>()
    private val mAttrColorMap = HashMap<String, List<AttrColorModel>>()
    private val mAttrDimensionMap = HashMap<String, List<AttrDimensionModel>>()
    private val mAttrIntegerMap = HashMap<String, List<AttrIntegerModel>>()
    private val mAttrFractionMap = HashMap<String, List<AttrFractionModel>>()
    private val mAttrFloatMap = HashMap<String, List<AttrFloatModel>>()
    private val mAttrResourceMap = HashMap<String, List<AttrReferenceModel>>()
    private val mAttrStringMap = HashMap<String, List<AttrStringModel>>()
    private var mPackageName: String? = null

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        processAnnotation(roundEnvironment)
        for (customViewHolder in mCustomViewHolderList) {
            writeAttributes(customViewHolder)
        }
        return true
    }

    override fun getSupportedAnnotationTypes() = supportedAnnotationSet

    private fun processAnnotation(env: RoundEnvironment) {
        env.getElementsAnnotatedWith(AttributeParser::class.java)
                .map { it.getAnnotation(AttributeParser::class.java) }
                .forEach { mPackageName = it.value }

        env.getElementsAnnotatedWith(CustomView::class.java).mapTo(mCustomViewHolderList) {
            CustomViewHolder((it as TypeElement).asClassName(), processingEnv.elementUtils.getPackageOf(it).toString())
        }

        env.run {
            findAnnotatedWith(attrIntPair, mAttrIntMap)
            findAnnotatedWith(attrStringPair, mAttrStringMap)
            findAnnotatedWith(attrBooleanPair, mAttrBooleanMap)
            findAnnotatedWith(attrColorPair, mAttrColorMap)
            findAnnotatedWith(attrDimensionPair, mAttrDimensionMap)
            findAnnotatedWith(attrIntegerPair, mAttrIntegerMap)
            findAnnotatedWith(attrFractionPair, mAttrFractionMap)
            findAnnotatedWith(attrFloatPair, mAttrFloatMap)
            findAnnotatedWith(attrResourcePair, mAttrResourceMap)
        }
    }

    private fun writeAttributes(holder: CustomViewHolder) {
        val classTypeName = holder.className
        val classTypeParameterName = holder.simpleName.substring(0, 1).toLowerCase() + holder.simpleName.substring(1)
        val simpleName = holder.simpleName
        val fileName = simpleName + Constants.ATTRIBUTES

        val builder = TypeSpec.objectBuilder(fileName)

        val models = getModelList(simpleName, mAttrBooleanMap, mAttrColorMap, mAttrDimensionMap,
                mAttrIntegerMap, mAttrIntMap, mAttrFractionMap, mAttrFloatMap, mAttrResourceMap, mAttrStringMap)

        for (model in models) {
            builder.addProperty(createAttrsFieldSpec(model))
        }

        builder.addProperty(createRFieldSpec())
        builder.addFunction(createObtainApplyMethodSpec(classTypeName, classTypeParameterName, holder.simpleName))
        builder.addFunction(createApplyMethodSpec(classTypeName, classTypeParameterName, models))
        builder.addFunction(createPrintVariableMethodSpec(simpleName, models))
        builder.addFunction(createBindAttributesMethodSpec(simpleName, models))

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
                ?.replace("kaptKotlin", "kapt")
                ?.let { File(it, "$fileName.kt") }
                ?: throw IllegalArgumentException("No output directory")

        val typeSpec = builder.build()
        val fileSpec = FileSpec.builder(holder.packageName, fileName).addType(typeSpec).build()
        fileSpec.writeTo(kaptKotlinGeneratedDir)
    }

    private fun createRFieldSpec(): PropertySpec {
        val typeName = ClassName.bestGuess(mPackageName!! + Constants.R_FILE)
        return PropertySpec.builder(Constants.R_VARIABLE, typeName, KModifier.PRIVATE).initializer("R()").build()
    }

    private fun createAttrsFieldSpec(model: BaseAttrModel): PropertySpec {
        val variableName = model.annotatedElementName
        val typeName = model.annotatedElementClass.bestGuess()
        return PropertySpec.builder(variableName, typeName, KModifier.PRIVATE)
                .mutable(true).initializer(typeName.initializer()).build()
    }

    private fun createPrintVariableMethodSpec(simpleName: String, models: List<BaseAttrModel>): FunSpec {
        val builder = Constants.PRINT_VARIABLES.getFunSpec(KModifier.PUBLIC)
                .addAnnotation(JvmStatic::class)
        val variablesBuilder = StringBuilder()
        var maxinum = 0
        for (model in models) {
            val variableName = model.annotatedElementName
            val className = model.annotatedElementClass
            val line = "\"\\n$className $variableName = \" + $variableName +  \n"
            variablesBuilder.append(line)
            maxinum = Math.max(maxinum, line.length)
        }

        val stringBuilder = StringBuilder()
        val lastLine = multiply(Constants.EQUALS, maxinum)
        val firstLine = multiply(Constants.EQUALS, (maxinum - simpleName.length - 2) / 2)
        val firstLineMessage = String.format(Constants.STATEMENT_LOG_FIRST_LINE, firstLine, simpleName, firstLine)

        stringBuilder.append(firstLineMessage)
        stringBuilder.append(variablesBuilder.toString())
        stringBuilder.append(String.format(Constants.STATEMENT_LOG_LAST_LINE, lastLine))

        val message = stringBuilder.toString()
        builder.addStatement("%T.d(%S, %L)", Constants.LOG_CLASS, simpleName, message)

        return builder.build()
    }

    private fun createObtainApplyMethodSpec(classTypeName: TypeName, classTypeParameterName: String, simpleName: String): FunSpec {
        val builder = Constants.APPLY.getFunSpec(KModifier.PUBLIC)
                .addParameter(classTypeParameterName, classTypeName)
                .addParameter(Constants.SET, Constants.ATTRIBUTE_SET_CLASS_NAME)
                .addAnnotation(JvmStatic::class)
                .addCode(Constants.STATEMENT_OBTAIN_APPLY.format(classTypeParameterName, classTypeParameterName, simpleName))

        return builder.build()
    }

    private fun createApplyMethodSpec(classTypeName: TypeName, classTypeParameterName: String, models: List<BaseAttrModel>): FunSpec {
        val builder = Constants.APPLY.getFunSpec(KModifier.PUBLIC)
                .addParameter(classTypeParameterName, classTypeName)
                .addParameter(Constants.ARRAY, Constants.TYPED_ARRAY_CLASS_NAME)
                .addAnnotation(JvmStatic::class)
                .addCode(Constants.BIND_ATTRIBUTES_INVOKE)

        models.map { it.annotatedElementName }
                .forEach {
                    builder.addCode("%L.%L = %L;\n", classTypeParameterName, it, it)
                }

        return builder.build()
    }

    private fun createBindAttributesMethodSpec(simpleName: String, models: List<BaseAttrModel>): FunSpec {
        val builder = Constants.BIND_ATTRIBUTES.getFunSpec(KModifier.PRIVATE)
                .addParameter(Constants.ARRAY, Constants.TYPED_ARRAY_CLASS_NAME)
                .addCode(Constants.STATEMENT_BINDATTRIBUTES)

        for (model in models) {
            builder.addCode(model, simpleName)
        }

        builder.addCode(Constants.STATEMENT_RECYCLE)

        return builder.build()
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}