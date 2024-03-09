package com.mikepenz.hypnoticcanvas

import androidx.compose.ui.graphics.ShaderBrush
import com.mikepenz.hypnoticcanvas.shaders.Shader
import org.jetbrains.skia.RuntimeShaderBuilder


class NonAndroidRuntimeEffect(shader: Shader) : RuntimeEffect {
    private val compositeRuntimeEffect = org.jetbrains.skia.RuntimeEffect.makeForShader(shader.sksl)
    private val compositeShaderBuilder = RuntimeShaderBuilder(compositeRuntimeEffect)

    override fun updateUniforms(time: Float, width: Float, height: Float) {
        compositeShaderBuilder.uniform("uResolution", width, height, width / height)
        compositeShaderBuilder.uniform("uTime", time)
    }

    override fun build(): ShaderBrush {
        return ShaderBrush(compositeShaderBuilder.makeShader())
    }
}

internal actual fun buildEffect(shader: Shader): RuntimeEffect {
    return NonAndroidRuntimeEffect(shader)
}