package com.mikepenz.hypnoticcanvas

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ShaderBrush
import com.mikepenz.hypnoticcanvas.shaders.Shader

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class AndroidRuntimeEffect(shader: Shader) : RuntimeEffect {
    private val compositeRuntimeEffect = RuntimeShader(shader.sksl)

    override fun updateUniforms(time: Float, width: Float, height: Float) {
        compositeRuntimeEffect.setFloatUniform("uResolution", width, height, width / height)
        compositeRuntimeEffect.setFloatUniform("uTime", time)
    }

    override fun build(): ShaderBrush {
        return ShaderBrush(compositeRuntimeEffect)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal actual fun buildEffect(shader: Shader): RuntimeEffect {
    return AndroidRuntimeEffect(shader)
}