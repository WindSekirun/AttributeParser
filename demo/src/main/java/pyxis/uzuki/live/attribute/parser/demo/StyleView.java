package pyxis.uzuki.live.attributesparser.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import pyxis.uzuki.live.attributesparser.R;

/**
 * AttributesParser
 * Class: StyleView
 * Created by Pyxis on 3/4/18.
 * <p>
 * Description:
 */

public class StyleView extends LinearLayout {

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

        StyleViewAttributes attributes = new StyleViewAttributes(array);
    }
}
