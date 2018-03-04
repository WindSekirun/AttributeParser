# AttributeParser [![codebeat badge](https://codebeat.co/badges/131cade3-7cd7-498d-97c1-f63e29bc97d1)](https://codebeat.co/projects/github-com-windsekirun-attributeparser-master) [ ![Download](https://api.bintray.com/packages/windsekirun/maven/attribute-parser/images/download.svg) ](https://bintray.com/windsekirun/maven/attribute-parser/_latestVersion)

[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) 

**Inject automatically your Attribute of CustomView, just with simple annotation**

generate own class to handle attributes using Annotation Processor.

Written in Java and Kotlin, both language are fully supported!

## Post
* (Korean) [커스텀 뷰의 XML 속성 파싱 라이브러리, AttributeParser 소개](https://blog.uzuki.live/커스텀-뷰의-xml-속성-파싱-라이브러리-attributeparser-소개/)
* (English) Writing... 

## Import

Waiting for uploading to bintray...

```
dependencies {
    // AttributeParser
    implementation 'com.github.windsekirun:attribute-parser:1.0.0'
    annotationProcessor 'com.github.windsekirun:attribute-parser-compiler:1.0.0'

    // if your app using Kotlin?
    kapt 'com.github.windsekirun:attribute-parser-compiler:1.0.0'
}
```

## Usages

1. Declare ```@CustomView``` annotation into CustomView Class
2. Attach ```@AttrInt```, ```@AttrColor``` annotation to field
3. Clean - Build Project
4. Done! Attribute class will generated at compile time.
5. using ```StyleViewAttributes.apply(this, attributeSet)``` or ```StyleViewAttributes.apply(this, typedArray)``` for set variable from ```declare-styleable```

### Advance

#### Index, Default Value

AttributeParser will generate index automatically, but sometimes you will need custom index to implement CustomView.

Just adding parameter ```value``` and ```def``` to ```@AttrInt```, ```@AttrBoolean```

```Java
public @AttrInt("StyleView_intTest") int intTest;
public @AttrBoolean(value = "StyleView_booleanTest", def = true) boolean booleanTest;
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
        <attr name="floatTest" format="float" />
        <attr name="resourceTest" format="reference" />
</declare-styleable>
```

#### XML (activity_main.xml)
```XML
<pyxis.uzuki.live.attribute.parser.demo.StyleView
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
