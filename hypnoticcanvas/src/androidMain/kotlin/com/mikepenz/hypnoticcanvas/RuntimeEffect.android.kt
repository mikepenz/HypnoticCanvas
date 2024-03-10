package com.mikepenz.hypnoticcanvas

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import com.mikepenz.hypnoticcanvas.shaders.Shader

/**
 * No-op implementation of the Runtime effect for devices not supporting the [RuntimeShader].
 */
internal class FallbackAndroidRuntimeEffect : RuntimeEffect {

    override val supported: Boolean = false
    override var ready: Boolean = false

    override fun updateUniforms(time: Float, width: Float, height: Float) {
    }

    override fun build(): Brush {
        return Brush.horizontalGradient(listOf(Color.White, Color.White))
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class AndroidRuntimeEffect(shader: Shader) : RuntimeEffect {
    private val compositeRuntimeEffect = RuntimeShader(shader.sksl)

    override val supported: Boolean = true
    override var ready: Boolean = false

    override fun updateUniforms(time: Float, width: Float, height: Float) {
        compositeRuntimeEffect.setFloatUniform("uResolution", width, height, width / height)
        compositeRuntimeEffect.setFloatUniform("uTime", time)
        ready = width > 0 && height > 0
    }

    override fun build(): Brush {
        return ShaderBrush(compositeRuntimeEffect)
    }
}

internal actual fun buildEffect(shader: Shader): RuntimeEffect {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        AndroidRuntimeEffect(shader)
    } else {
        FallbackAndroidRuntimeEffect()
    }
}