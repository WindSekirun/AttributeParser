package pyxis.uzuki.live.attribute.parser.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AttrBoolean(val value: String = "", val def: Boolean = true)