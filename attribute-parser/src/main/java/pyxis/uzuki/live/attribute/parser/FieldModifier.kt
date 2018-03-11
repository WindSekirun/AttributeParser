package pyxis.uzuki.live.attribute.parser

import java.lang.reflect.Field
import java.lang.reflect.Modifier


/**
 * FieldModifier will execute extremely evil things...
 *
 * 1. Find fields in Class<*> (in demo, it will be StyleViewAttributes)
 * 2. remove final modifier of field
 * 3. write value into field
 *
 * What i'm thinking??
 *
 * reference: https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/reflect/FieldUtils.java
 */
object FieldModifier {

    @JvmStatic
    fun process(receiver: Any, fieldName: String, value: Any) {
        // It can be nullable cause field doesn't exists.
        // for extension uses.
        val field = findField(receiver.javaClass, fieldName) ?: return
        removeFinalModifierAndSet(receiver, field, value)
    }

    private fun findField(cls: Class<*>, fieldName: String): Field? {
        // Locate the field going back to parent.
        // for extension uses.
        var targetClass = cls
        while (true) {
            try {
                val field = targetClass.getDeclaredField(fieldName)
                // getDeclaredField checks for non-public scopes as well and it returns accurate results
                // In this situation (used in AttributeParser), all field will public cause processing by
                // annotation processor, but i'm plan to use this methods in future.
                if (!Modifier.isPublic(field.modifiers)) {
                    field.isAccessible = true
                }
                return field
            } catch (ex: NoSuchFieldException) {
                // ignored
            }

            targetClass = cls.superclass
        }
    }

    private fun removeFinalModifierAndSet(receiver: Any, field: Field, value: Any) {
        try {
            if (Modifier.isFinal(field.modifiers)) {
                val modifiersField = Field::class.java.getDeclaredField("modifiers")
                // of course, it always will public in this situation.
                // but you don't know what happened imn future?
                if (!modifiersField.isAccessible) {
                    modifiersField.isAccessible = true
                }

                try {
                    modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
                } finally {
                    if (!modifiersField.isAccessible) {
                        modifiersField.isAccessible = false
                    }
                }

                if (Modifier.isPrivate(field.modifiers)) {
                    field.isAccessible = true
                }

                try {
                    field.set(receiver, value)
                } finally {
                    if (Modifier.isPrivate(field.modifiers)) {
                        field.isAccessible = false
                    }
                }
            }
        } catch (ex: NoSuchFieldException) {
            // ignored
        } catch (ex: IllegalAccessException) {
            // ignored
        }
    }
}