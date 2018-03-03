package pyxis.uzuki.live.attribute.parser.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.FIELD)
@Retention(RetentionPolicy.SOURCE)
annotation class AttrInt(val value: String, val defValue: Int)
