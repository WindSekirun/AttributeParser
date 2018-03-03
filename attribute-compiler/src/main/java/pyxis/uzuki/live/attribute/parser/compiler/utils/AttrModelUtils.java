package pyxis.uzuki.live.attribute.parser.compiler.utils;

import pyxis.uzuki.live.attribute.parser.compiler.model.AttrIntModel;

/**
 * AttributesParser
 * Class: AttributeCodeGenerator
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class AttributeCodeGenerator {

    public static String createIntCode(AttrIntModel model, String className) {
        String variableName = model.getAnnotatedElementName();
        String source = model.getSource();
        int defValue = model.getDefValue();

        if (source == null || source.isEmpty() || source.trim().isEmpty()) {
            source = String.format("%s_%s", className, variableName);
        }

        source = String.format("R.styleable.%s", source);

        return String.format("%s = array.getInt(%s, %s);\n", variableName, source, defValue);
    }

    public static String createBooleanCode(String variableName, String source, boolean defValue) {
        return String.format("%s = array.getBoolean(%s, %s);\n", variableName, source, defValue);
    }

    public static String createColorCode(String variableName, String source, int defValue) {
        return String.format("%s = array.getColor(%s, %s);\n", variableName, source, defValue);
    }

    public static String createDimensionCode(String variableName, String source, float defValue) {
        return String.format("%s = array.getDimension(%s, %s);\n", variableName, source, defValue);
    }

    public static String createDimensionPixelCode(String variableName, String source, int defValue) {
        return String.format("%s = array.getDimensionPixelSize(%s, %s);\n", variableName, source, defValue);
    }

    public static String createDrawableCode(String variableName, String source) {
        return String.format("%s = array.getDrawable(%s);\n", variableName, source);
    }

    public static String createFloatCode(String variableName, String source, float defValue) {
        return String.format("%s = array.getFloat(%s, %s);\n", variableName, source, defValue);
    }

    public static String createResourceCode(String variableName, String source, int defValue) {
        return String.format("%s = array.getResource(%s, %s);\n", variableName, source, defValue);
    }

    public static String createStringCode(String variableName, String source) {
        return String.format("%s = array.getInt(%s);\n", variableName, source);
    }

    private static String 
}
