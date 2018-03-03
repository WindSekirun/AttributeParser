package pyxis.uzuki.live.attribute.parser.compiler.utils;

import pyxis.uzuki.live.attribute.parser.compiler.model.AttrBooleanModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrColorModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrDimensionModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrDimensionPixelSizeModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrDrawableModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrFloatModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrIntModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrResourceModel;
import pyxis.uzuki.live.attribute.parser.compiler.model.AttrStringModel;

/**
 * AttributesParser
 * Class: AttrModelUtils
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class AttrModelUtils {

    public static String createIntCode(AttrIntModel model, String className) {
        String variableName = model.getAnnotatedElementName();
        String source = reviseSource(className, variableName, model.getSource());
        int defValue = model.getDefValue();

        return String.format("%s = array.getInt(%s, %s);\n", variableName, source, defValue);
    }

    public static String createBooleanCode(AttrBooleanModel model, String className) {
        String variableName = model.getAnnotatedElementName();
        String source = reviseSource(className, variableName, model.getSource());
        boolean defValue = model.getDefValue();

        return String.format("%s = array.getBoolean(%s, %s);\n", variableName, source, defValue);
    }

    public static String createColorCode(AttrColorModel model, String className) {
        String variableName = model.getAnnotatedElementName();
        String source = reviseSource(className, variableName, model.getSource());
        int defValue = model.getDefValue();

        return String.format("%s = array.getColor(%s, %s);\n", variableName, source, defValue);
    }

    public static String createDimensionCode(AttrDimensionModel model, String className) {
        String variableName = model.getAnnotatedElementName();
        String source = reviseSource(className, variableName, model.getSource());
        float defValue = model.getDefValue();

        return String.format("%s = array.getDimension(%s, %s);\n", variableName, source, defValue);
    }

    public static String createDimensionPixelCode(AttrDimensionPixelSizeModel model, String className) {
        String variableName = model.getAnnotatedElementName();
        String source = reviseSource(className, variableName, model.getSource());
        int defValue = model.getDefValue();

        return String.format("%s = array.getDimensionPixelSize(%s, %s);\n", variableName, source, defValue);
    }

    public static String createDrawableCode(AttrDrawableModel model, String className) {
        String variableName = model.getAnnotatedElementName();
        String source = reviseSource(className, variableName, model.getSource());

        return String.format("%s = array.getDrawable(%s);\n", variableName, source);
    }

    public static String createFloatCode(AttrFloatModel model, String className) {
        String variableName = model.getAnnotatedElementName();
        String source = reviseSource(className, variableName, model.getSource());
        float defValue = model.getDefValue();

        return String.format("%s = array.getFloat(%s, %s);\n", variableName, source, defValue);
    }

    public static String createResourceCode(AttrResourceModel model, String className) {
        String variableName = model.getAnnotatedElementName();
        String source = reviseSource(className, variableName, model.getSource());
        int defValue = model.getDefValue();

        return String.format("%s = array.getResource(%s, %s);\n", variableName, source, defValue);
    }

    public static String createStringCode(AttrStringModel model, String className) {
        String variableName = model.getAnnotatedElementName();
        String source = reviseSource(className, variableName, model.getSource());

        return String.format("%s = array.getInt(%s);\n", variableName, source);
    }

    private static String reviseSource(String className, String variableName, String source) {
        String newSource = source;

        if (newSource == null || newSource.isEmpty() || newSource.trim().isEmpty()) {
            newSource = String.format("%s_%s", className, variableName);
        }

        newSource = String.format("R.styleable.%s", newSource);

        return newSource;
    }
}
