@file:JvmName("Utils")
@file:JvmMultifileClass

package pyxis.uzuki.live.attribute.parser.compiler.utils

import com.squareup.javapoet.MethodSpec
import javax.lang.model.element.Modifier

fun String.getMethodSpec(vararg modifiers: Modifier) = MethodSpec.methodBuilder(this).addModifiers(modifiers.toList())