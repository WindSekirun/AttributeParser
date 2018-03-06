package pyxis.uzuki.live.attribute.parser.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import pyxis.uzuki.live.attribute.parser.annotation.CustomView;
import pyxis.uzuki.live.attribute.parser.compiler.utils.AttrModelUtils;
import pyxis.uzuki.live.attribute.parser.compiler.utils.CommonEx;
import pyxis.uzuki.live.attribute.parser.model.AttrBooleanModel;
import pyxis.uzuki.live.attribute.parser.model.AttrColorModel;
import pyxis.uzuki.live.attribute.parser.model.AttrDimensionModel;
import pyxis.uzuki.live.attribute.parser.model.AttrDimensionPixelSizeModel;
import pyxis.uzuki.live.attribute.parser.model.AttrDrawableModel;
import pyxis.uzuki.live.attribute.parser.model.AttrFloatModel;
import pyxis.uzuki.live.attribute.parser.model.AttrIntModel;
import pyxis.uzuki.live.attribute.parser.model.AttrResourceModel;
import pyxis.uzuki.live.attribute.parser.model.AttrStringModel;
import pyxis.uzuki.live.attribute.parser.model.BaseAttrModel;
import pyxis.uzuki.live.attribute.parser.model.CustomViewHolder;

import static pyxis.uzuki.live.attribute.parser.compiler.utils.AttrModelUtils.parseModelIntoMap;

/**
 * AttributesParser
 * Class: AttributeParserProcessor
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */
@SupportedAnnotationTypes({
        "pyxis.uzuki.live.attribute.parser.annotation.AttrInt",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrBoolean",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrColor",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrDimension",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrDimensionPixelSize",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrDrawable",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrFloat",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrResource",
        "pyxis.uzuki.live.attribute.parser.annotation.AttrString",
        "pyxis.uzuki.live.attribute.parser.annotation.CustomView"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(javax.annotation.processing.Processor.class)
public class AttributeParserProcessor extends AbstractProcessor {
    private HashMap<ClassName, CustomViewHolder> mCustomViewHolderMap = new HashMap<>();
    private HashMap<String, List<AttrIntModel>> mAttrIntMap = new HashMap<>();
    private HashMap<String, List<AttrBooleanModel>> mAttrBooleanMap = new HashMap<>();
    private HashMap<String, List<AttrColorModel>> mAttrColorMap = new HashMap<>();
    private HashMap<String, List<AttrDimensionModel>> mAttrDimensionMap = new HashMap<>();
    private HashMap<String, List<AttrDimensionPixelSizeModel>> mAttrDimensionPixelSizeMap = new HashMap<>();
    private HashMap<String, List<AttrDrawableModel>> mAttrDrawableMap = new HashMap<>();
    private HashMap<String, List<AttrFloatModel>> mAttrFloatMap = new HashMap<>();
    private HashMap<String, List<AttrResourceModel>> mAttrResourceMap = new HashMap<>();
    private HashMap<String, List<AttrStringModel>> mAttrStringMap = new HashMap<>();
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        mFiler = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        processAnnotation(roundEnvironment);
        for (CustomViewHolder customViewHolder : mCustomViewHolderMap.values()) {
            writeAttributes(customViewHolder);
        }
        return true;
    }

    private void processAnnotation(RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(CustomView.class)) {
            ClassName classFullName = ClassName.get((TypeElement) element);
            String className = element.getSimpleName().toString();
            mCustomViewHolderMap.put(classFullName, new CustomViewHolder(element, classFullName, className));
        }

        parseModelIntoMap(env, mAttrIntMap, AttrModelUtils.getAttrIntPair());
        parseModelIntoMap(env, mAttrStringMap, AttrModelUtils.getAttrStringPair());
        parseModelIntoMap(env, mAttrBooleanMap, AttrModelUtils.getAttrBooleanPair());
        parseModelIntoMap(env, mAttrColorMap, AttrModelUtils.getAttrColorPair());
        parseModelIntoMap(env, mAttrDimensionMap, AttrModelUtils.getAttrDimensionPair());
        parseModelIntoMap(env, mAttrDimensionPixelSizeMap, AttrModelUtils.getAttrDimensionPixelSizePair());
        parseModelIntoMap(env, mAttrDrawableMap, AttrModelUtils.getAttrDrawablePair());
        parseModelIntoMap(env, mAttrFloatMap, AttrModelUtils.getAttrFloatPair());
        parseModelIntoMap(env, mAttrResourceMap, AttrModelUtils.getAttrResourcePair());
    }

    private void writeAttributes(CustomViewHolder customViewHolder) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(customViewHolder.getClassName() + Constants.ATTRIBUTES)
                .addModifiers(Modifier.PUBLIC);

        List<BaseAttrModel> models = AttrModelUtils.getModelList(customViewHolder.getClassName(),
                mAttrIntMap, mAttrStringMap, mAttrBooleanMap, mAttrColorMap, mAttrDimensionMap, mAttrDimensionPixelSizeMap,
                mAttrDrawableMap, mAttrFloatMap, mAttrResourceMap);

        for (BaseAttrModel model : models) {
            builder.addField(createAttrsFieldSpec(model));
        }

        builder.addField(createRFieldSpec(customViewHolder));
        builder.addMethod(createObtainApplyMethodSpec(customViewHolder));
        builder.addMethod(createApplyMethodSpec(customViewHolder, models));
        builder.addMethod(createPrintVariableMethodSpec(customViewHolder, models));
        builder.addMethod(createBindAttributesMethodSpec(customViewHolder, models));

        JavaFile javaFile = JavaFile.builder(Constants.PACKAGE_NAME, builder.build()).build();

        try {
            javaFile.writeTo(System.out);
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FieldSpec createRFieldSpec(CustomViewHolder customViewHolder) {
        TypeName typeName = ClassName.bestGuess(customViewHolder.getClassNameComplete().packageName() + Constants.R_FILE);

        FieldSpec.Builder builder = FieldSpec.builder(typeName, Constants.R_VARIABLE)
                .addModifiers(Modifier.PRIVATE);

        return builder.build();
    }

    private FieldSpec createAttrsFieldSpec(BaseAttrModel model) {
        String variableName = model.getAnnotatedElementName();
        TypeName typeName = CommonEx.bestGuess(model.getAnnotatedElementClass());

        FieldSpec.Builder builder = FieldSpec.builder(typeName, variableName)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC);

        return builder.build();
    }

    private MethodSpec createPrintVariableMethodSpec(CustomViewHolder customViewHolder, List<BaseAttrModel> models) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PRINT_VARIABLES)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        StringBuilder variablesBuilder = new StringBuilder();
        int maximum = 0;
        for (BaseAttrModel model : models) {
            String variableName = model.getAnnotatedElementName();
            String className = model.getAnnotatedElementClass();
            String line = "\"\\n" + className + " " + variableName + " = \" + " + variableName + " +  \n";

            variablesBuilder.append(line);

            int count = line.length();
            maximum = Math.max(maximum, count);
        }

        StringBuilder stringBuilder = new StringBuilder();
        String lastLine = CommonEx.multiply("=", maximum);
        String firstLine = CommonEx.multiply("=", (maximum - customViewHolder.getClassName().length() - 2) / 2);
        String firstLineMessage = String.format("\"%s %s %s\" + \n", firstLine, customViewHolder.getClassName(), firstLine);

        stringBuilder.append(firstLineMessage);
        stringBuilder.append(variablesBuilder.toString());
        stringBuilder.append(String.format("\"\\n%s\"", lastLine));

        String message = stringBuilder.toString();
        builder.addCode(String.format(Constants.STATEMENT_LOG, customViewHolder.getClassName(), message));

        return builder.build();
    }

    private MethodSpec createObtainApplyMethodSpec(CustomViewHolder customViewHolder) {
        TypeName classTypeName = customViewHolder.getClassNameComplete();
        String classTypeParameterName = customViewHolder.getClassName().substring(0, 1).toLowerCase() +
                customViewHolder.getClassName().substring(1);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.OBTAIN_APPLY)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(classTypeName, classTypeParameterName)
                .addParameter(Constants.INSTANCE.getATTRIBUTE_SET_CLASS_NAME(), Constants.SET)
                .addCode(String.format(Constants.STATEMENT_OBTAIN_APPLY, classTypeParameterName,
                        classTypeParameterName, customViewHolder.getClassName()));

        return builder.build();
    }

    private MethodSpec createApplyMethodSpec(CustomViewHolder customViewHolder, List<BaseAttrModel> models) {
        TypeName classTypeName = customViewHolder.getClassNameComplete();
        String classTypeParameterName = customViewHolder.getClassName().substring(0, 1).toLowerCase() +
                customViewHolder.getClassName().substring(1);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.APPLY)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(classTypeName, classTypeParameterName)
                .addParameter(Constants.INSTANCE.getTYPED_ARRAY_CLASS_NAME(), Constants.ARRAY)
                .addCode(Constants.BIND_ATTRIBUTES_INVOKE);

        for (BaseAttrModel model : models) {
            String variableName = model.getAnnotatedElementName();

            builder.addCode(String.format(Constants.STATEMENT_APPLY, classTypeParameterName,
                    variableName, variableName));
        }

        return builder.build();
    }

    private MethodSpec createBindAttributesMethodSpec(CustomViewHolder customViewHolder, List<BaseAttrModel> models) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.BIND_ATTRIBUTES)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(Constants.INSTANCE.getTYPED_ARRAY_CLASS_NAME(), Constants.ARRAY)
                .addCode(Constants.STATEMENT_BINDATTRIBUTES);

        for (BaseAttrModel model : models) {
            AttrModelUtils.addCode(builder, model, customViewHolder.getClassName());
        }

        builder.addCode(Constants.STATEMENT_RECYCLE);

        return builder.build();
    }
}