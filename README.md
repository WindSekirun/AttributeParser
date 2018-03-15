# AttributeParser [![codebeat badge](https://codebeat.co/badges/131cade3-7cd7-498d-97c1-f63e29bc97d1)](https://codebeat.co/projects/github-com-windsekirun-attributeparser-master) [ ![Download](https://api.bintray.com/packages/windsekirun/maven/attribute-parser/images/download.svg) ](https://bintray.com/windsekirun/maven/attribute-parser/_latestVersion)

[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AttributeParser-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6804)

**Inject automatically your Attribute of CustomView, just with simple annotation**

generate own class to handle attributes using Annotation Processor, setting value by reflection.

Written in Kotlin, both language(Java, Kotlin) are fully supported!

## Post
* (Korean) [커스텀 뷰의 XML 속성 파싱 라이브러리, AttributeParser 소개](https://blog.uzuki.live/커스텀-뷰의-xml-속성-파싱-라이브러리-attributeparser-소개/)
* (English) Writing... 

## Import
```
dependencies {
    // AttributeParser
    implementation 'com.github.windsekirun:attribute-parser:1.1.0'
    annotationProcessor 'com.github.windsekirun:attribute-parser-compiler:1.1.0'

    // if your app using Kotlin?
    kapt 'com.github.windsekirun:attribute-parser-compiler:1.1.0'
}
```


## Usages
1. Declare ```@AttributeParser("package name")``` annotation into any class. package name will be your package name.
   * example, i wrote annotation into Application class, like ```@AttributeParser("com.github.windsekirun.abcd")```
2. Declare ```@CustomView``` annotation into CustomView Class
3. Attach ```@AttrInt```, ```@AttrColor``` annotation to field
4. Clean - Build Project
5. Done! Attribute class will generated at compile time.
6. using ```StyleViewAttributes.apply(this, attributeSet)``` or ```StyleViewAttributes.apply(this, typedArray)``` for set variable from ```declare-styleable```

### Advance

#### Index

AttributeParser will generate index automatically, but sometimes you will need custom index to implement CustomView.

Just adding parameter ```value``` to ```@AttrInt```, ```@AttrBoolean```

```Java
public @AttrInt("StyleView_intTest") int intTest;
```

### Default Value

**from 1.1.0, declaring default value has changed**

Just add 'final' modifier into field, and initialize value.

```Java
    @AttrInt public final int intTest = 10;
    @AttrBoolean public final boolean booleanTest = false;
    @AttrColor public final int colorTest = android.R.color.darker_gray;
    @AttrDimension public final float dimensionTest = android.R.dimen.thumbnail_width;
    @AttrFloat public final float floatTest = 10;
    @AttrReference public final int resourceTest = android.R.drawable.arrow_down_float;
    @AttrString public final String stringTest = "ABC";
```

**before 1.1.0**

```Java
public @AttrInt(value = "StyleView_intTest", def = 0) int intTest;
```

#### Logging Variables

after using ```StyleViewAttribute.apply(this, attributeSet)```, you can use ```StyleViewAttributes.printVariables();``` to print out all name and value of attributes.

### Examples

#### XML (attrs.xml)
```XML
 <declare-styleable name="StyleView">
        <attr name="stringTest" format="string" />
        <attr name="intTest" format="integer" />
        <attr name="booleanTest" format="boolean" />
        <attr name="colorTest" format="color" />
        <attr name="dimensionTest" format="dimension" />
        <attr name="flagTest">
            <flag name="none" value="0" />
            <flag name="top" value="1" />
            <flag name="right" value="2" />
            <flag name="bottom" value="4" />
            <flag name="left" value="8" />
            <flag name="all" value="15" />
        </attr>
        <attr name="floatTest" format="float" />
        <attr name="resourceTest" format="reference" />
        <attr name="fractionTest" format="fraction" />
    </declare-styleable>
```

#### XML (activity_main.xml)
```XML
<pyxis.uzuki.live.attribute.parser.demo.widget.StyleView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:booleanTest="true"
        app:colorTest="@android:color/white"
        app:dimensionTest="@android:dimen/app_icon_size"
        app:floatTest="16"
        app:intTest="1"
        app:resourceTest="@drawable/ic_launcher_background"
        app:stringTest="abc" />
```

### Java, Kotlin (StyleView.java, StyleView.kt)

```Java
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
    }
}
```

## Credit

* Author: WindSekirun (DongGil, Seo)
  * mail: pyxis@uzuki.live
  * site: https://blog.uzuki.live

if you find bug or improvement, please send me issue. PR is always welcome!

## License
```
    Copyright 2018 WindSekirun (DongGil, Seo)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```
