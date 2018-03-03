package pyxis.uzuki.live.attribute.parser.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AttrColor(val value: String = "", val def: Int = 0)