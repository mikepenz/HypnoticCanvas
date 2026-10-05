package com.mikepenz.hypnoticcanvas

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asComposeShader
import androidx.compose.ui.graphics.skiaShader
import androidx.compose.ui.graphics.Shader as ComposeShader
import com.mikepenz.hypnoticcanvas.shaders.Shader
import org.jetbrains.skia.RuntimeShaderBuilder
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.SamplingMode
import org.jetbrains.skia.impl.use


class NonAndroidRuntimeEffect(shader: Shader) : RuntimeEffect {
    private val compositeRuntimeEffect = org.jetbrains.skia.RuntimeEffect.makeForShader(shader.sksl)
    private val compositeShaderBuilder = RuntimeShaderBuilder(compositeRuntimeEffect)
    private val dataTextureEffect by lazy {
        org.jetbrains.skia.RuntimeEffect.makeForShader("""
            uniform shader red;
            uniform shader green;
            uniform shader blue;
            half4 main(float2 p) {
                return half4(red.eval(p).a, green.eval(p).a, blue.eval(p).a, 1.0);
            }
        """.trimIndent())
    }

    override val supported: Boolean = true
    override var ready: Boolean = false

    override fun setFloatUniform(name: String, value1: Float) {
        compositeShaderBuilder.uniform(name, value1)
    }

    override fun setFloatUniform(name: String, value1: Float, value2: Float) {
        compositeShaderBuilder.uniform(name, value1, value2)
    }

    override fun setFloatUniform(name: String, value1: Float, value2: Float, value3: Float) {
        compositeShaderBuilder.uniform(name, value1, value2, value3)
    }

    override fun setFloatUniform(name: String, values: FloatArray) {
        compositeShaderBuilder.uniform(name, values)
    }

    override fun setShaderUniform(name: String, shader: ComposeShader) {
        compositeShaderBuilder.child(name, shader.skiaShader)
    }

    override fun setDataTextureUniform(name: String, width: Int, height: Int, rgb: ByteArray) {
        validateDataTexture(width, height, rgb)
        // ponytail: three alpha textures avoid color conversion; use makeRawShader when Skiko exposes it.
        val info = ImageInfo(width, height, ColorType.ALPHA_8, ColorAlphaType.PREMUL, null)
        RuntimeShaderBuilder(dataTextureEffect).use { builder ->
            for ((channel, inputName) in listOf("red", "green", "blue").withIndex()) {
                val bytes = ByteArray(width * height) { rgb[it * 3 + channel] }
                Image.makeRaster(info, bytes, width).use { image ->
                    image.makeShader(sampling = SamplingMode.DEFAULT).use { input ->
                        builder.child(inputName, input)
                    }
                }
            }
            builder.makeShader().use { input ->
                compositeShaderBuilder.child(name, input)
            }
        }
    }

    override fun update(shader: Shader, time: Float, width: Float, height: Float) {
        shader.applyUniforms(this, time, width, height)
        ready = width > 0 && height > 0
    }

    override fun build(): Brush {
        return ShaderBrush(compositeShaderBuilder.makeShader().asComposeShader())
    }
}

internal actual fun buildEffect(shader: Shader): RuntimeEffect {
    return NonAndroidRuntimeEffect(shader)
}
