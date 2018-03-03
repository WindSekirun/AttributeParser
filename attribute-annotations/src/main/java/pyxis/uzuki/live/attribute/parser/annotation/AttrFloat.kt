package pyxis.uzuki.live.attribute.parser.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class AttrFloat(val value: String = "", val def: Float = 0f)