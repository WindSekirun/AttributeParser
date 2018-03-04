package pyxis.uzuki.live.attribute.parser.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class AttrBoolean(val value: String = "", val def: Boolean = false)