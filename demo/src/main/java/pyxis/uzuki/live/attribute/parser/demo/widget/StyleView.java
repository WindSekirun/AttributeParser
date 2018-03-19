package pyxis.uzuki.live.attribute.parser.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

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
    @AttrInt public int intTest;
    @AttrBoolean public boolean booleanTest;
    @AttrColor public int colorTest;
    @AttrDimension public float dimensionTest;
    @AttrFloat public float floatTest;
    @AttrReference public int resourceTest;
    @AttrString public String stringTest;

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