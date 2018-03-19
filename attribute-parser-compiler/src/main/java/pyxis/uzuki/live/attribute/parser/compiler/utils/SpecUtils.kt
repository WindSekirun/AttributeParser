@file:JvmName("Utils")
@file:JvmMultifileClass

package pyxis.uzuki.live.attribute.parser.compiler.utils

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier

fun String.getFunSpec(vararg modifiers: KModifier) = FunSpec.builder(this).addModifiers(modifiers.toList())