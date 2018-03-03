package pyxis.uzuki.live.attribute.parser.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
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

import pyxis.uzuki.live.attribute.parser.annotation.CustomView;
import pyxis.uzuki.live.attribute.parser.compiler.holder.CustomViewHolder;

/**
 * AttributesParser
 * Class: AttributeParserProcessor
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */
@SupportedAnnotationTypes({
        "pyxis.uzuki.live.attribute.parser.annotation.AttrInt",
        "pyxis.uzuki.live.attribute.parser.annotation.CustomView"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(javax.annotation.processing.Processor.class)
public class AttributeParserProcessor extends AbstractProcessor {
    private Map<ClassName, CustomViewHolder> mCustomViewHolderMap = new HashMap<>();
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
            TypeSpec.Builder builder = TypeSpec.classBuilder(customViewHolder.className + "Attributes")
                    .addModifiers(Modifier.PUBLIC);

            TypeName typedArrayName = ClassName.bestGuess("android.content.res.TypedArray");
            TypeName classTypeName = customViewHolder.classNameComplete;
            String classTypeParameterName = customViewHolder.className.substring(0, 1).toLowerCase() +
                    customViewHolder.className.substring(1);

            builder.addMethod(MethodSpec.methodBuilder("apply")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(classTypeName, classTypeParameterName)
                    .addParameter(typedArrayName, "array")
                    .addCode("bindAttributes(array);\n")
                    .build()
            );

            builder.addMethod(MethodSpec.methodBuilder("bindAttributes")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .addParameter(typedArrayName, "array")
                    .addCode("array.recycle();")
                    .build()
            );


            final TypeSpec newClass = builder.build();
            final JavaFile javaFile = JavaFile.builder(Constants.PACKAGE_NAME, newClass).build();

            try {
                javaFile.writeTo(System.out);
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private void processAnnotation(RoundEnvironment roundEnvironment) {

        for (Element element : roundEnvironment.getElementsAnnotatedWith(CustomView.class)) {
            final ClassName classFullName = ClassName.get((TypeElement) element);
            final String className = element.getSimpleName().toString();
            mCustomViewHolderMap.put(classFullName, new CustomViewHolder(element, classFullName, className));
        }
    }
}
