package pyxis.uzuki.live.attribute.parser.compiler

import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import pyxis.uzuki.live.attribute.parser.annotation.AttributeParser
import pyxis.uzuki.live.attribute.parser.annotation.CustomView
import pyxis.uzuki.live.attribute.parser.compiler.holder.CustomViewHolder
import pyxis.uzuki.live.attribute.parser.compiler.model.*
import pyxis.uzuki.live.attribute.parser.compiler.utils.*
import java.io.IOException
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
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

        env.getElementsAnnotatedWith(CustomView::class.java).mapTo(mCustomViewHolderList) { CustomViewHolder(ClassName.get(it as TypeElement)) }

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

        val builder = TypeSpec.classBuilder(simpleName + Constants.ATTRIBUTES)
                .addModifiers(Modifier.PUBLIC)

        val models = getModelList(simpleName, mAttrBooleanMap, mAttrColorMap, mAttrDimensionMap,
                mAttrIntegerMap, mAttrIntMap, mAttrFractionMap, mAttrFloatMap, mAttrResourceMap, mAttrStringMap)

        for (model in models) {
            builder.addField(createAttrsFieldSpec(model))
        }

        builder.addField(createRFieldSpec())
        builder.addMethod(createObtainApplyMethodSpec(classTypeName, classTypeParameterName, holder.simpleName))
        builder.addMethod(createApplyMethodSpec(classTypeName, classTypeParameterName, models))
        builder.addMethod(createPrintVariableMethodSpec(simpleName, models))
        builder.addMethod(createBindAttributesMethodSpec(simpleName, models))

        try {
            val file = JavaFile.builder(Constants.PACKAGE_NAME, builder.build())
                    .indent("   ")
                    .build()
            file.writeTo(processingEnv.filer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createRFieldSpec(): FieldSpec {
        val typeName = ClassName.bestGuess(mPackageName!! + Constants.R_FILE)
        return FieldSpec.builder(typeName, Constants.R_VARIABLE, Modifier.PRIVATE).build()
    }

    private fun createAttrsFieldSpec(model: BaseAttrModel): FieldSpec {
        val variableName = model.annotatedElementName
        val typeName = model.annotatedElementClass.bestGuess()
        return FieldSpec.builder(typeName, variableName, Modifier.PRIVATE, Modifier.STATIC).build()
    }

    private fun createPrintVariableMethodSpec(simpleName: String, models: List<BaseAttrModel>): MethodSpec {
        val builder = Constants.PRINT_VARIABLES.getMethodSpec(Modifier.PUBLIC, Modifier.STATIC)
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
        builder.addStatement("\$T.d(\$S, \$L)", Constants.LOG_CLASS, simpleName, message)

        return builder.build()
    }

    private fun createObtainApplyMethodSpec(classTypeName: TypeName, classTypeParameterName: String, simpleName: String): MethodSpec {
        val builder = Constants.APPLY.getMethodSpec(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(classTypeName, classTypeParameterName)
                .addParameter(Constants.ATTRIBUTE_SET_CLASS_NAME, Constants.SET)
                .addCode(Constants.STATEMENT_OBTAIN_APPLY.format(classTypeParameterName, classTypeParameterName, simpleName))

        return builder.build()
    }

    private fun createApplyMethodSpec(classTypeName: TypeName, classTypeParameterName: String, models: List<BaseAttrModel>): MethodSpec {
        val builder = Constants.APPLY.getMethodSpec(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(classTypeName, classTypeParameterName)
                .addParameter(Constants.TYPED_ARRAY_CLASS_NAME, Constants.ARRAY)
                .addCode(Constants.BIND_ATTRIBUTES_INVOKE)


        models.map { it.annotatedElementName }
                .forEach {
                    builder.addStatement("\$T.process(\$L, \$S, \$L)", Constants.FIELD_MODIFIER, classTypeParameterName, it, it)
                }

        return builder.build()
    }

    private fun createBindAttributesMethodSpec(simpleName: String, models: List<BaseAttrModel>): MethodSpec {
        val builder = Constants.BIND_ATTRIBUTES.getMethodSpec(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(Constants.TYPED_ARRAY_CLASS_NAME, Constants.ARRAY)
                .addCode(Constants.STATEMENT_BINDATTRIBUTES)

        for (model in models) {
            builder.addCode(model, simpleName)
        }

        builder.addCode(Constants.STATEMENT_RECYCLE)

        return builder.build()
    }
}