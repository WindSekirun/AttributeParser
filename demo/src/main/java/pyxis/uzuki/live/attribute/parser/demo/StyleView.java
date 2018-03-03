package pyxis.uzuki.live.attribute.parser.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import pyxis.uzuki.live.attribute.parser.StyleViewAttributes;
import pyxis.uzuki.live.attribute.parser.annotation.AttrInt;
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
    public @AttrInt int hintText;

    public StyleView(Context context) {
        super(context);
        init(null);
    }

    public StyleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.StyleView);
        StyleViewAttributes.apply(this, array);
        Log.d(StyleView.class.getSimpleName(), "init: hintText = " + hintText);
    }
}
