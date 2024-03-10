package com.mikepenz.hypnoticcanvas

import androidx.compose.ui.graphics.Brush
import com.mikepenz.hypnoticcanvas.shaders.Shader

/**
 * Describes a platform independent runtime effect
 */
internal interface RuntimeEffect {

    /**
     * Indicates if the current platform is supported
     */
    val supported: Boolean

    /**
     * Defines if the effect is ready to be displayed
     */
    val ready: Boolean

    /**
     * Updates the uniforms for the shader, on changes of the size or time.
     */
    fun updateUniforms(time: Float, width: Float, height: Float)

    /**
     * Builds an updates ShaderBrush
     */
    fun build(): Brush
}

internal expect fun buildEffect(shader: Shader): RuntimeEffect