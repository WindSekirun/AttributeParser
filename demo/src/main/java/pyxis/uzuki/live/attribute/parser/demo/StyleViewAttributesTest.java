package pyxis.uzuki.live.attribute.parser.demo;

import android.content.res.TypedArray;

import pyxis.uzuki.live.attributesparser.R;

public class StyleViewAttributesTest {
    private static int hintText = 0;

    public static void apply(StyleView styleView, TypedArray array) {
        bindVariables(array);

        styleView.hintText = hintText;
    }

    private static void bindVariables(TypedArray array) {
        if (array == null) {
            return;
        }

        hintText = array.getInt(R.styleable.StyleView_hintText, 0);
        array.recycle();
    }
}
