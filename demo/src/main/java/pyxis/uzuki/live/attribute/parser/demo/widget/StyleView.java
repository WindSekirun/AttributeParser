package pyxis.uzuki.live.attribute.parser.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import pyxis.uzuki.live.attribute.parser.StyleViewAttributes;
import pyxis.uzuki.live.attribute.parser.annotation.AttrBoolean;
import pyxis.uzuki.live.attribute.parser.annotation.AttrColor;
import pyxis.uzuki.live.attribute.parser.annotation.AttrDimension;
import pyxis.uzuki.live.attribute.parser.annotation.AttrFloat;
import pyxis.uzuki.live.attribute.parser.annotation.AttrInt;
import pyxis.uzuki.live.attribute.parser.annotation.AttrReference;
import pyxis.uzuki.live.attribute.parser.annotation.AttrString;
import pyxis.uzuki.live.attribute.parser.annotation.CustomView;

/**
 * AttributesParser
 * Class: StyleView
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

@CustomView
public class StyleView extends LinearLayout {
    @AttrInt public final int intTest = 10;
    @AttrBoolean public final boolean booleanTest = false;
    @AttrColor public final int colorTest = android.R.color.darker_gray;
    @AttrDimension public final float dimensionTest = android.R.dimen.thumbnail_width;
    @AttrFloat public final float floatTest = 10;
    @AttrReference public final int resourceTest = android.R.drawable.arrow_down_float;
    @AttrString public final String stringTest = "ABC";

    public StyleView(Context context) {
        super(context);
        init(null);
    }

    public StyleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        StyleViewAttributes.apply(this, attributeSet);
        StyleViewAttributes.printVariables();
    }
}