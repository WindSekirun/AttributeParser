package pyxis.uzuki.live.attribute.parser.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AttrString(val value: String = "", val def: String = "")