package com.mikepenz.hypnoticcanvas

import android.graphics.RuntimeShader
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Shader.TileMode
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import com.mikepenz.hypnoticcanvas.shaders.Shader
import androidx.compose.ui.graphics.Shader as ComposeShader

/**
 * No-op implementation of the Runtime effect for devices not supporting the [RuntimeShader].
 */
internal class FallbackAndroidRuntimeEffect : RuntimeEffect {

    override val supported: Boolean = false
    override var ready: Boolean = false

    override fun build(): Brush {
        return Brush.horizontalGradient(listOf(Color.White, Color.White))
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class AndroidRuntimeEffect(shader: Shader) : RuntimeEffect {
    private val compositeRuntimeEffect = RuntimeShader(shader.sksl)

    override val supported: Boolean = true
    override var ready: Boolean = false

    override fun setFloatUniform(name: String, value1: Float) {
        compositeRuntimeEffect.setFloatUniform(name, value1)
    }

    override fun setFloatUniform(name: String, value1: Float, value2: Float) {
        compositeRuntimeEffect.setFloatUniform(name, value1, value2)
    }

    override fun setFloatUniform(name: String, value1: Float, value2: Float, value3: Float) {
        compositeRuntimeEffect.setFloatUniform(name, value1, value2, value3)
    }

    override fun setFloatUniform(name: String, values: FloatArray) {
        compositeRuntimeEffect.setFloatUniform(name, values)
    }

    override fun setShaderUniform(name: String, shader: ComposeShader) {
        compositeRuntimeEffect.setInputShader(name, shader)
    }

    override fun setDataTextureUniform(name: String, width: Int, height: Int, rgb: ByteArray) {
        validateDataTexture(width, height, rgb)
        val pixels = IntArray(width * height) { i ->
            (0xff shl 24) or ((rgb[i * 3].toInt() and 0xff) shl 16) or
                ((rgb[i * 3 + 1].toInt() and 0xff) shl 8) or (rgb[i * 3 + 2].toInt() and 0xff)
        }
        val bitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
        val input = BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP)
        input.setFilterMode(BitmapShader.FILTER_MODE_NEAREST)
        compositeRuntimeEffect.setInputBuffer(name, input)
    }

    override fun update(shader: Shader, time: Float, width: Float, height: Float) {
        shader.applyUniforms(this, time, width, height)
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
