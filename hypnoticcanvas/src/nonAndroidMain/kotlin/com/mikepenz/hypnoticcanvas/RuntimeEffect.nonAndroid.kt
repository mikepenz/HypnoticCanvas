package com.mikepenz.hypnoticcanvas

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ShaderBrush
import com.mikepenz.hypnoticcanvas.shaders.Shader
import org.jetbrains.skia.RuntimeShaderBuilder


class NonAndroidRuntimeEffect(shader: Shader) : RuntimeEffect {
    private val compositeRuntimeEffect = org.jetbrains.skia.RuntimeEffect.makeForShader(shader.sksl)
    private val compositeShaderBuilder = RuntimeShaderBuilder(compositeRuntimeEffect)

    override val supported: Boolean = true
    override var ready: Boolean = false

    override fun updateUniforms(time: Float, width: Float, height: Float) {
        compositeShaderBuilder.uniform("uResolution", width, height, width / height)
        compositeShaderBuilder.uniform("uTime", time)
        ready = width > 0 && height > 0
    }

    override fun build(): Brush {
        return ShaderBrush(compositeShaderBuilder.makeShader())
    }
}

internal actual fun buildEffect(shader: Shader): RuntimeEffect {
    return NonAndroidRuntimeEffect(shader)
}