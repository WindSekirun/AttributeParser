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
import pyxis.uzuki.live.attribute.parser.annotation.AttrResource;
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
    public @AttrInt int intTest;
    public @AttrBoolean boolean booleanTest;
    public @AttrColor int colorTest;
    public @AttrDimension float dimensionTest;
    public @AttrFloat float floatTest;
    public @AttrResource int resourceTest;
    public @AttrString String stringTest;

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