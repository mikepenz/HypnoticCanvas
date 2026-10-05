package com.mikepenz.hypnoticcanvas

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.Shader as ComposeShader
import com.mikepenz.hypnoticcanvas.shaders.Shader

/**
 * Describes a platform-independent runtime effect
 */
interface RuntimeEffect {

    /** Indicates if the current platform is supported*/
    val supported: Boolean

    /** Defines if the effect is ready to be displayed */
    val ready: Boolean

    /** Sets a float array uniform for this shader */
    fun setFloatUniform(name: String, value1: Float) {}

    /** Sets a float array uniform for this shader */
    fun setFloatUniform(name: String, value1: Float, value2: Float) {}

    /** Sets a float array uniform for this shader */
    fun setFloatUniform(name: String, value1: Float, value2: Float, value3: Float) {}

    /** Sets a float array uniform for this shader */
    fun setFloatUniform(name: String, values: FloatArray) {}

    /** Binds a `uniform shader` input, sampled with `.eval()` in pixel coordinates. */
    fun setShaderUniform(name: String, shader: ComposeShader) {}

    /** Binds an image input with normal color-space and alpha handling. */
    fun setTextureUniform(
        name: String,
        image: ImageBitmap,
        tileModeX: TileMode = TileMode.Clamp,
        tileModeY: TileMode = TileMode.Clamp,
    ) {
        setShaderUniform(name, ImageShader(image, tileModeX, tileModeY))
    }

    /**
     * Uploads a snapshot of tightly packed, row-major unsigned RGB bytes.
     * Samples return RGB / 255 and alpha 1, without color conversion, using nearest
     * filtering and clamp addressing. Rebind after changing [rgb].
     * Unsupported platforms validate the input but do not upload it.
     */
    fun setDataTextureUniform(name: String, width: Int, height: Int, rgb: ByteArray) {
        validateDataTexture(width, height, rgb)
    }

    /** Updates the uniforms for the shader, on changes of the size or time.*/
    fun update(shader: Shader, time: Float, width: Float, height: Float) {}

    /** Builds an updates ShaderBrush*/
    fun build(): Brush
}

internal expect fun buildEffect(shader: Shader): RuntimeEffect

internal fun validateDataTexture(width: Int, height: Int, rgb: ByteArray) {
    require(width > 0 && height > 0) { "Texture dimensions must be positive" }
    val pixels = width.toLong() * height
    require(pixels <= Int.MAX_VALUE / 4) { "Texture is too large" }
    require(rgb.size.toLong() == pixels * 3) { "Expected width * height * 3 RGB bytes" }
}
