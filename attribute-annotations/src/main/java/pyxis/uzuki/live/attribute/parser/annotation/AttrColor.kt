package pyxis.uzuki.live.attribute.parser.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class AttrColor(val value: String = "", val def: Int = 0)