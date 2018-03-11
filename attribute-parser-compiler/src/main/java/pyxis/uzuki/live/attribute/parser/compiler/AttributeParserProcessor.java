package pyxis.uzuki.live.attribute.parser.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttributeParser;
import pyxis.uzuki.live.attribute.parser.annotation.CustomView;
import pyxis.uzuki.live.attribute.parser.compiler.holder.CustomViewHolder;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrBooleanModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrColorModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrDimensionModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrFloatModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrFractionModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrIntModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrIntegerModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrReferenceModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrStringModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.BaseAttrModel;
import pyxis.uzuki.live.attribute.parser.compiler.utils.Utils;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(javax.annotation.processing.Processor.class)
public class AttributeParserProcessor extends AbstractProcessor {
    private List<CustomViewHolder> mCustomViewHolderList = new ArrayList<>();
    private Map<String, List<AttrIntModel>> mAttrIntMap = new HashMap<>();
    private Map<String, List<AttrBooleanModel>> mAttrBooleanMap = new HashMap<>();
    private Map<String, List<AttrColorModel>> mAttrColorMap = new HashMap<>();
    private Map<String, List<AttrDimensionModel>> mAttrDimensionMap = new HashMap<>();
    private Map<String, List<AttrIntegerModel>> mAttrIntegerMap = new HashMap<>();
    private Map<String, List<AttrFractionModel>> mAttrFractionMap = new HashMap<>();
    private Map<String, List<AttrFloatModel>> mAttrFloatMap = new HashMap<>();
    private Map<String, List<AttrReferenceModel>> mAttrResourceMap = new HashMap<>();
    private Map<String, List<AttrStringModel>> mAttrStringMap = new HashMap<>();
    private Filer mFiler;
    private String mPackageName;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        mFiler = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        processAnnotation(roundEnvironment);
        for (CustomViewHolder customViewHolder : mCustomViewHolderList) {
            writeAttributes(customViewHolder);
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Utils.getSupportedAnnotationSet();
    }

    private void processAnnotation(RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(AttributeParser.class)) {
            AttributeParser parser = element.getAnnotation(AttributeParser.class);
            mPackageName = parser.value();
        }

        for (Element element : env.getElementsAnnotatedWith(CustomView.class)) {
            mCustomViewHolderList.add(new CustomViewHolder(ClassName.get((TypeElement) element)));
        }

        Utils.findAnnotatedWith(env, Utils.getAttrIntPair(), mAttrIntMap);
        Utils.findAnnotatedWith(env, Utils.getAttrStringPair(), mAttrStringMap);
        Utils.findAnnotatedWith(env, Utils.getAttrBooleanPair(), mAttrBooleanMap);
        Utils.findAnnotatedWith(env, Utils.getAttrColorPair(), mAttrColorMap);
        Utils.findAnnotatedWith(env, Utils.getAttrDimensionPair(), mAttrDimensionMap);
        Utils.findAnnotatedWith(env, Utils.getAttrIntegerPair(), mAttrIntegerMap);
        Utils.findAnnotatedWith(env, Utils.getAttrFractionPair(), mAttrFractionMap);
        Utils.findAnnotatedWith(env, Utils.getAttrFloatPair(), mAttrFloatMap);
        Utils.findAnnotatedWith(env, Utils.getAttrResourcePair(), mAttrResourceMap);
    }

    private void writeAttributes(CustomViewHolder holder) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(holder.getSimpleName() + Constants.ATTRIBUTES)
                .addModifiers(Modifier.PUBLIC);

        TypeName classTypeName = holder.getClassName();
        String classTypeParameterName = holder.getSimpleName().substring(0, 1).toLowerCase() +
                holder.getSimpleName().substring(1);
        String simpleName = holder.getSimpleName();

        List<BaseAttrModel> models = Utils.getModelList(holder.getSimpleName(), mAttrBooleanMap,
                mAttrColorMap, mAttrDimensionMap, mAttrIntegerMap, mAttrIntMap, mAttrFractionMap,
                mAttrFloatMap, mAttrResourceMap, mAttrStringMap);

        for (BaseAttrModel model : models) {
            builder.addField(createAttrsFieldSpec(model));
        }

        builder.addField(createRFieldSpec());
        builder.addMethod(createObtainApplyMethodSpec(classTypeName, classTypeParameterName, holder.getSimpleName()));
        builder.addMethod(createApplyMethodSpec(classTypeName, classTypeParameterName, models));
        builder.addMethod(createPrintVariableMethodSpec(simpleName, models));
        builder.addMethod(createBindAttributesMethodSpec(simpleName, models));

        try {
            JavaFile.builder(Constants.PACKAGE_NAME, builder.build()).build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FieldSpec createRFieldSpec() {
        TypeName typeName = ClassName.bestGuess(mPackageName + Constants.R_FILE);
        return FieldSpec.builder(typeName, Constants.R_VARIABLE, Modifier.PRIVATE).build();
    }

    private FieldSpec createAttrsFieldSpec(BaseAttrModel model) {
        String variableName = model.getAnnotatedElementName();
        TypeName typeName = Utils.bestGuess(model.getAnnotatedElementClass());
        return FieldSpec.builder(typeName, variableName, Modifier.PRIVATE, Modifier.STATIC).build();
    }

    private MethodSpec createPrintVariableMethodSpec(String simpleName, List<BaseAttrModel> models) {
        MethodSpec.Builder builder = Utils.getMethodSpec(Constants.PRINT_VARIABLES, Modifier.PUBLIC, Modifier.STATIC);
        StringBuilder variablesBuilder = new StringBuilder();
        int maxinum = 0;
        for (int i = 0; i < models.size(); i++) {
            BaseAttrModel model = models.get(i);
            String variableName = model.getAnnotatedElementName();
            String className = model.getAnnotatedElementClass();
            String line = "\"\\n" + className + " " + variableName + " = \" + " + variableName + " +  \n";

            variablesBuilder.append(line);

            int count = line.length();
            maxinum = Math.max(maxinum, count);
        }

        StringBuilder stringBuilder = new StringBuilder();
        String lastLine = Utils.multiply(Constants.EQUALS, maxinum);
        String firstLine = Utils.multiply(Constants.EQUALS, (maxinum - simpleName.length() - 2) / 2);
        String firstLineMessage = String.format(Constants.STATEMENT_LOG_FIRST_LINE, firstLine, simpleName, firstLine);

        stringBuilder.append(firstLineMessage);
        stringBuilder.append(variablesBuilder.toString());
        stringBuilder.append(String.format(Constants.STATEMENT_LOG_LAST_LINE, lastLine));

        String message = stringBuilder.toString();
        builder.addStatement("$T.d($S, $L)", Constants.LOG_CLASS, simpleName, message);

        return builder.build();
    }

    private MethodSpec createObtainApplyMethodSpec(TypeName classTypeName, String classTypeParameterName, String simpleName) {
        MethodSpec.Builder builder = Utils.getMethodSpec(Constants.APPLY, Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(classTypeName, classTypeParameterName)
                .addParameter(Constants.ATTRIBUTE_SET_CLASS_NAME, Constants.SET)
                .addCode(String.format(Constants.STATEMENT_OBTAIN_APPLY, classTypeParameterName,
                        classTypeParameterName, simpleName));

        return builder.build();
    }

    private MethodSpec createApplyMethodSpec(TypeName classTypeName, String classTypeParameterName, List<BaseAttrModel> models) {
        MethodSpec.Builder builder = Utils.getMethodSpec(Constants.APPLY, Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(classTypeName, classTypeParameterName)
                .addParameter(Constants.TYPED_ARRAY_CLASS_NAME, Constants.ARRAY)
                .addCode(Constants.BIND_ATTRIBUTES_INVOKE);


        for (BaseAttrModel model : models) {
            String variableName = model.getAnnotatedElementName();

            builder.addStatement("$T.process($L, $S, $L)", Constants.FIELD_MODIFIER, classTypeParameterName,
                    variableName, variableName);
        }

        return builder.build();
    }

    private MethodSpec createBindAttributesMethodSpec(String simpleName, List<BaseAttrModel> models) {
        MethodSpec.Builder builder = Utils.getMethodSpec(Constants.BIND_ATTRIBUTES, Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(Constants.TYPED_ARRAY_CLASS_NAME, Constants.ARRAY)
                .addCode(Constants.STATEMENT_BINDATTRIBUTES);

        for (BaseAttrModel model : models) {
            Utils.addCode(builder, model, simpleName);
        }

        builder.addCode(Constants.STATEMENT_RECYCLE);

        return builder.build();
    }
}