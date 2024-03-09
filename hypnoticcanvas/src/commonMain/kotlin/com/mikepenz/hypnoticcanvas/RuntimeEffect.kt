package com.mikepenz.hypnoticcanvas

import androidx.compose.ui.graphics.ShaderBrush
import com.mikepenz.hypnoticcanvas.shaders.Shader

/**
 * Describes a platform independent runtime effect
 */
internal interface RuntimeEffect {
    /**
     * Updates the uniforms for the shader, on changes of the size or time.
     */
    fun updateUniforms(time: Float, width: Float, height: Float)

    /**
     * Builds an updates ShaderBrush
     */
    fun build(): ShaderBrush
}

internal expect fun buildEffect(shader: Shader): RuntimeEffect