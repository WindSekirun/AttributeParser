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
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import pyxis.uzuki.live.attribute.parser.annotation.AttrBoolean;
import pyxis.uzuki.live.attribute.parser.annotation.AttrColor;
import pyxis.uzuki.live.attribute.parser.annotation.AttrDimension;
import pyxis.uzuki.live.attribute.parser.annotation.AttrDimensionPixelSize;
import pyxis.uzuki.live.attribute.parser.annotation.AttrDrawable;
import pyxis.uzuki.live.attribute.parser.annotation.AttrFloat;
import pyxis.uzuki.live.attribute.parser.annotation.AttrInt;
import pyxis.uzuki.live.attribute.parser.annotation.AttrResource;
import pyxis.uzuki.live.attribute.parser.annotation.AttrString;
import pyxis.uzuki.live.attribute.parser.annotation.CustomView;
import pyxis.uzuki.live.attribute.parser.compiler.holder.CustomViewHolder;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrBooleanModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrColorModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrDimensionModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrDimensionPixelSizeModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrDrawableModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrFloatModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrIntModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrResourceModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrStringModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.BaseAttrModel;
import pyxis.uzuki.live.attribute.parser.compiler.utils.AttrModelUtils;
import pyxis.uzuki.live.attribute.parser.compiler.utils.StringUtils;
import pyxis.uzuki.live.attribute.parser.compiler.utils.TypeNameUtils;

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
    private Map<ClassName, CustomViewHolder> mCustomViewHolderMap = new HashMap<>();
    private Map<String, List<AttrIntModel>> mAttrIntMap = new HashMap<>();
    private Map<String, List<AttrBooleanModel>> mAttrBooleanMap = new HashMap<>();
    private Map<String, List<AttrColorModel>> mAttrColorMap = new HashMap<>();
    private Map<String, List<AttrDimensionModel>> mAttrDimensionMap = new HashMap<>();
    private Map<String, List<AttrDimensionPixelSizeModel>> mAttrDimensionPixelSizeMap = new HashMap<>();
    private Map<String, List<AttrDrawableModel>> mAttrDrawableMap = new HashMap<>();
    private Map<String, List<AttrFloatModel>> mAttrFloatMap = new HashMap<>();
    private Map<String, List<AttrResourceModel>> mAttrResourceMap = new HashMap<>();
    private Map<String, List<AttrStringModel>> mAttrStringMap = new HashMap<>();
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        mFiler = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        processAnnotation(roundEnvironment);
        writeFile();
        return true;
    }

    private void processAnnotation(RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(CustomView.class)) {
            ClassName classFullName = ClassName.get((TypeElement) element);
            String className = element.getSimpleName().toString();
            mCustomViewHolderMap.put(classFullName, new CustomViewHolder(element, classFullName, className));
        }

        for (Element element : env.getElementsAnnotatedWith(AttrInt.class)) {
            AttrIntModel model = new AttrIntModel((VariableElement) element);
            if (!model.isValid()) continue;

            updateAttrMapList(mAttrIntMap, model);
        }

        for (Element element : env.getElementsAnnotatedWith(AttrString.class)) {
            AttrStringModel model = new AttrStringModel((VariableElement) element);
            if (!model.isValid()) continue;

            updateAttrMapList(mAttrStringMap, model);
        }

        for (Element element : env.getElementsAnnotatedWith(AttrBoolean.class)) {
            AttrBooleanModel model = new AttrBooleanModel((VariableElement) element);
            if (!model.isValid()) continue;

            updateAttrMapList(mAttrBooleanMap, model);
        }

        for (Element element : env.getElementsAnnotatedWith(AttrColor.class)) {
            AttrColorModel model = new AttrColorModel((VariableElement) element);
            if (!model.isValid()) continue;

            updateAttrMapList(mAttrColorMap, model);
        }

        for (Element element : env.getElementsAnnotatedWith(AttrDimension.class)) {
            AttrDimensionModel model = new AttrDimensionModel((VariableElement) element);
            if (!model.isValid()) continue;

            updateAttrMapList(mAttrDimensionMap, model);
        }

        for (Element element : env.getElementsAnnotatedWith(AttrDimensionPixelSize.class)) {
            AttrDimensionPixelSizeModel model = new AttrDimensionPixelSizeModel((VariableElement) element);
            if (!model.isValid()) continue;

            updateAttrMapList(mAttrDimensionPixelSizeMap, model);
        }

        for (Element element : env.getElementsAnnotatedWith(AttrDrawable.class)) {
            AttrDrawableModel model = new AttrDrawableModel((VariableElement) element);
            if (!model.isValid()) continue;

            updateAttrMapList(mAttrDrawableMap, model);
        }

        for (Element element : env.getElementsAnnotatedWith(AttrFloat.class)) {
            AttrFloatModel model = new AttrFloatModel((VariableElement) element);
            if (!model.isValid()) continue;

            updateAttrMapList(mAttrFloatMap, model);
        }

        for (Element element : env.getElementsAnnotatedWith(AttrResource.class)) {
            AttrResourceModel model = new AttrResourceModel((VariableElement) element);
            if (!model.isValid()) continue;

            updateAttrMapList(mAttrResourceMap, model);
        }
    }

    private <T extends BaseAttrModel> void updateAttrMapList(Map<String, List<T>> map, T model) {
        List<T> elementsClasses = map.get(model.getEnclosingClass());
        if (elementsClasses == null || elementsClasses.isEmpty()) {
            elementsClasses = new ArrayList<>();
        }

        elementsClasses.add(model);
        map.put(model.getEnclosingClass(), elementsClasses);
    }

    private void writeFile() {
        for (CustomViewHolder customViewHolder : mCustomViewHolderMap.values()) {
            writeAttributes(customViewHolder);
        }
    }

    private void writeClass(TypeSpec typeSpec) {
        JavaFile javaFile = JavaFile.builder(Constants.PACKAGE_NAME, typeSpec).build();

        try {
            javaFile.writeTo(System.out);
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAttributes(CustomViewHolder customViewHolder) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(customViewHolder.className + Constants.ATTRIBUTES)
                .addModifiers(Modifier.PUBLIC);

        List<BaseAttrModel> models = getTargetList(customViewHolder.className);

        for (BaseAttrModel model : models) {
            builder.addField(createAttrsFieldSpec(model));
        }

        builder.addField(createRFieldSpec(customViewHolder));
        builder.addMethod(createObtainApplyMethodSpec(customViewHolder));
        builder.addMethod(createApplyMethodSpec(customViewHolder, models));
        builder.addMethod(createPrintVariableMethodSpec(customViewHolder, models));
        builder.addMethod(createBindAttributesMethodSpec(customViewHolder, models));

        writeClass(builder.build());
    }

    private List<BaseAttrModel> getTargetList(String className) {
        List<BaseAttrModel> models = new ArrayList<>();
        models.addAll(getTargetList(mAttrBooleanMap, className));
        models.addAll(getTargetList(mAttrColorMap, className));
        models.addAll(getTargetList(mAttrDimensionMap, className));
        models.addAll(getTargetList(mAttrDrawableMap, className));
        models.addAll(getTargetList(mAttrIntMap, className));
        models.addAll(getTargetList(mAttrDimensionPixelSizeMap, className));
        models.addAll(getTargetList(mAttrFloatMap, className));
        models.addAll(getTargetList(mAttrResourceMap, className));
        models.addAll(getTargetList(mAttrStringMap, className));
        return models;
    }

    private <T extends BaseAttrModel> List<T> getTargetList(Map<String, List<T>> map, String className) {
        List<T> models = new ArrayList<>();

        for (Map.Entry<String, List<T>> entry : map.entrySet()) {
            if (entry.getKey().contains(className)) {
                models = entry.getValue();
                break;
            }
        }

        return models;
    }

    private FieldSpec createRFieldSpec(CustomViewHolder customViewHolder) {
        TypeName typeName = ClassName.bestGuess(customViewHolder.classNameComplete.packageName() + Constants.R_FILE);

        FieldSpec.Builder builder = FieldSpec.builder(typeName, Constants.R_VARIABLE)
                .addModifiers(Modifier.PRIVATE);

        return builder.build();
    }

    private FieldSpec createAttrsFieldSpec(BaseAttrModel model) {
        String variableName = model.getAnnotatedElementName();
        TypeName typeName = TypeNameUtils.bestGuess(model.getAnnotatedElementClass());

        FieldSpec.Builder builder = FieldSpec.builder(typeName, variableName)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC);

        return builder.build();
    }

    private MethodSpec createPrintVariableMethodSpec(CustomViewHolder customViewHolder, List<BaseAttrModel> models) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PRINT_VARIABLES)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

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
        String lastLine = StringUtils.multiply("=", maxinum);
        String firstLine = StringUtils.multiply("=", (maxinum - customViewHolder.className.length() - 2) / 2);
        String firstLineMessage = String.format("\"%s %s %s\" + \n", firstLine, customViewHolder.className, firstLine);

        stringBuilder.append(firstLineMessage);
        stringBuilder.append(variablesBuilder.toString());
        stringBuilder.append(String.format("\"\\n%s\"", lastLine));

        String message = stringBuilder.toString();
        builder.addCode(String.format(Constants.STATEMENT_LOG, customViewHolder.className, message));

        return builder.build();
    }

    private MethodSpec createObtainApplyMethodSpec(CustomViewHolder customViewHolder) {
        TypeName classTypeName = customViewHolder.classNameComplete;
        String classTypeParameterName = customViewHolder.className.substring(0, 1).toLowerCase() +
                customViewHolder.className.substring(1);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.OBTAIN_APPLY)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(classTypeName, classTypeParameterName)
                .addParameter(Constants.ATTRIBUTE_SET_CLASS_NAME, Constants.SET)
                .addCode(String.format(Constants.STATEMENT_OBTAIN_APPLY, classTypeParameterName,
                        classTypeParameterName, customViewHolder.className));

        return builder.build();
    }

    private MethodSpec createApplyMethodSpec(CustomViewHolder customViewHolder, List<BaseAttrModel> models) {
        TypeName classTypeName = customViewHolder.classNameComplete;
        String classTypeParameterName = customViewHolder.className.substring(0, 1).toLowerCase() +
                customViewHolder.className.substring(1);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.APPLY)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(classTypeName, classTypeParameterName)
                .addParameter(Constants.TYPED_ARRAY_CLASS_NAME, Constants.ARRAY)
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
                .addParameter(Constants.TYPED_ARRAY_CLASS_NAME, Constants.ARRAY)
                .addCode(Constants.STATEMENT_BINDATTRIBUTES);

        for (BaseAttrModel model : models) {
            String className = customViewHolder.className;
            if (model instanceof AttrIntModel) {
                builder.addCode(AttrModelUtils.createIntCode((AttrIntModel) model, className));
            } else if (model instanceof AttrStringModel) {
                builder.addCode(AttrModelUtils.createStringCode((AttrStringModel) model, className));
            } else if (model instanceof AttrBooleanModel) {
                builder.addCode(AttrModelUtils.createBooleanCode((AttrBooleanModel) model, className));
            } else if (model instanceof AttrColorModel) {
                builder.addCode(AttrModelUtils.createColorCode((AttrColorModel) model, className));
            } else if (model instanceof AttrDimensionModel) {
                builder.addCode(AttrModelUtils.createDimensionCode((AttrDimensionModel) model, className));
            } else if (model instanceof AttrDimensionPixelSizeModel) {
                builder.addCode(AttrModelUtils.createDimensionPixelCode((AttrDimensionPixelSizeModel) model, className));
            } else if (model instanceof AttrDrawableModel) {
                builder.addCode(AttrModelUtils.createDrawableCode((AttrDrawableModel) model, className));
            } else if (model instanceof AttrFloatModel) {
                builder.addCode(AttrModelUtils.createFloatCode((AttrFloatModel) model, className));
            } else if (model instanceof AttrResourceModel) {
                builder.addCode(AttrModelUtils.createResourceCode((AttrResourceModel) model, className));
            }
        }

        builder.addCode(Constants.STATEMENT_RECYCLE);

        return builder.build();
    }
}