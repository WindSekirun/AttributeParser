package pyxis.uzuki.live.attribute.parser.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(RetentionPolicy.SOURCE)
annotation class CustomView
