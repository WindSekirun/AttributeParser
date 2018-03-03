package pyxis.uzuki.live.attribute.parser.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AttrDimensionPixelSize(val value: String = "", val def: Int = 0)