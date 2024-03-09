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
import androidx.compose.ui.layout.onGloballyPositioned
import com.mikepenz.hypnoticcanvas.shaders.Shader
import com.mikepenz.hypnoticcanvas.utils.round

@Composable
fun Modifier.shaderBackground(shader: Shader, speed: Float = 1f): Modifier {
    var size: Size by remember { mutableStateOf(Size(-1f, -1f)) }
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

    val runtimeEffect = remember(shader) { buildEffect(shader) }
    runtimeEffect.updateUniforms((time * speed * speedModifier).round(3), size.width, size.height) // set uniforms for the shaders

    return this then Modifier.onGloballyPositioned {
        size = Size(it.size.width.toFloat(), it.size.height.toFloat())
    }.drawBehind {
        if (size.width > 0 && size.height > 0) {
            drawRect(brush = runtimeEffect.build())
        }
    }
}
