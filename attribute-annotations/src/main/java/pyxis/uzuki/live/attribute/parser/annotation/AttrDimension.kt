package pyxis.uzuki.live.attribute.parser.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class AttrDimension(val value: String = "", val def: Float = 0f)