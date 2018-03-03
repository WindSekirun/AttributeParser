package pyxis.uzuki.live.attributesparser.demo;

import android.content.res.TypedArray;

import pyxis.uzuki.live.attributesparser.R;

public class StyleViewAttributes extends BaseAttributes {
    public int hintText = 0;

    public StyleViewAttributes(TypedArray array) {
        if (array == null) {
            return;
        }

        hintText = array.getInt(R.styleable.StyleView_hintText, 0);

        array.recycle();
    }
}