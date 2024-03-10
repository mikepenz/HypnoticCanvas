package com.mikepenz.hypnoticcanvas

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import com.mikepenz.hypnoticcanvas.shaders.Shader
import com.mikepenz.hypnoticcanvas.utils.round

/**
 * Draw's the shader as background via the [drawBehind] modifier.
 *
 * When running on Android 13 or newer (Tiramisu), usage of this API renders the shader.
 * On older Android devices, the provided [fallback] Brush is used instead.
 *
 * @param shader Shader to use to draw. [Shader] class. Example [com.mikepenz.hypnoticcanvas.shaders.GlossyGradients].
 * @param speed Adjusts how fast the shader is animated
 * @param fallback The fallback brush to draw on unsupported devices
 */
@Composable
fun Modifier.shaderBackground(
    shader: Shader,
    speed: Float = 1f,
    fallback: () -> Brush = {
        Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
    }
): Modifier {
    val runtimeEffect = remember(shader) { buildEffect(shader) }
    var size: Size by remember { mutableStateOf(Size(-1f, -1f)) }

    if (runtimeEffect.supported) {
        var startMillis = remember(shader) { -1L }
        val speedModifier = shader.speedModifier

        val time by produceState(0f, speedModifier) {
            while (true) {
                withInfiniteAnimationFrameMillis {
                    if (startMillis < 0) startMillis = it
                    value = ((it - startMillis) / 16.6f) / 10f
                }
            }
        }

        runtimeEffect.updateUniforms((time * speed * speedModifier).round(3), size.width, size.height) // set uniforms for the shaders
    }

    return this then Modifier.onGloballyPositioned {
        size = Size(it.size.width.toFloat(), it.size.height.toFloat())
    }.drawBehind {
        if (runtimeEffect.ready) {
            drawRect(brush = runtimeEffect.build())
        } else {
            drawRect(brush = fallback())
        }
    }
}
