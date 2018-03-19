package pyxis.uzuki.live.attribute.parser.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class AttrFraction(val value: String = "", val base: Int = 0, val pbase: Int = 0, val defValue: Float = 0f)