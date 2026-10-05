package com.mikepenz.hypnoticcanvas.shaders

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.skiaShader
import androidx.compose.ui.graphics.toArgb
import com.mikepenz.hypnoticcanvas.NonAndroidRuntimeEffect
import com.mikepenz.hypnoticcanvas.RuntimeEffect
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Surface
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TextureUniformTest {
    private fun effect(program: String) = NonAndroidRuntimeEffect(object : Shader by GlossyGradients {
        override val sksl = program
        override fun applyUniforms(runtimeEffect: RuntimeEffect, time: Float, width: Float, height: Float) {}
    })

    private fun render(effect: NonAndroidRuntimeEffect, colorSpace: ColorSpace = ColorSpace.sRGB): IntArray {
        val info = ImageInfo(4, 2, ColorType.RGBA_8888, ColorAlphaType.PREMUL, colorSpace)
        return Surface.makeRaster(info).use { surface ->
            val shader = (effect.build() as ShaderBrush).createShader(Size(4f, 2f)).skiaShader
            shader.use {
                org.jetbrains.skia.Paint().use { paint ->
                    paint.shader = it
                    surface.canvas.drawRect(org.jetbrains.skia.Rect.makeWH(4f, 2f), paint)
                }
            }
            surface.makeImageSnapshot().use { image ->
                image.peekPixels()!!.use { pixels ->
                    IntArray(8) { pixels.getColor(it % 4, it / 4) }
                }
            }
        }
    }

    @Test
    fun rawRgbPreservesChannelsClampsAndRebindsInBothColorSpaces() {
        val effect = effect("""
            uniform shader data;
            half4 main(float2 p) { return data.eval(float2(p.x - 1.25, p.y)); }
        """.trimIndent())
        val bytes = byteArrayOf(32, 96, 160.toByte(), 224.toByte(), 64, 128.toByte(),
            17, 81, 145.toByte(), 209.toByte(), 113, 49)
        effect.setDataTextureUniform("data", 2, 2, bytes)
        bytes.fill(0) // Uploads must not retain the caller's mutable buffer.
        val expected = intArrayOf(0xff2060a0.toInt(), 0xff2060a0.toInt(), 0xffe04080.toInt(), 0xffe04080.toInt(),
            0xff115191.toInt(), 0xff115191.toInt(), 0xffd17131.toInt(), 0xffd17131.toInt())
        for (space in listOf(ColorSpace.sRGB, ColorSpace.sRGBLinear)) {
            assertEquals(expected.toList(), render(effect, space).toList(), "Raw RGB in $space")
        }
        effect.setDataTextureUniform("data", 1, 1, byteArrayOf(73, 137.toByte(), 201.toByte()))
        assertTrue(render(effect).all { it == 0xff4989c9.toInt() })
    }

    @Test
    fun imageAndShaderInputsCanBeBoundTogetherAndReplaced() {
        val effect = effect("""
            uniform shader image;
            uniform shader other;
            half4 main(float2 p) { return p.x < 2 ? image.eval(p) : other.eval(p); }
        """.trimIndent())
        fun image(color: Color) = ImageBitmap(1, 1).also {
            Canvas(it).drawRect(0f, 0f, 1f, 1f, Paint().apply { this.color = color })
        }
        effect.setTextureUniform("image", image(Color.Red))
        effect.setShaderUniform("other", ImageShader(image(Color.Green)))
        assertEquals(listOf(Color.Red, Color.Red, Color.Green, Color.Green).map { it.toArgb() },
            render(effect).take(4))
        effect.setTextureUniform("image", image(Color.Blue))
        assertEquals(Color.Blue.toArgb(), render(effect)[0])
    }

    @Test
    fun imageTextureRespectsTileModes() {
        val effect = effect("uniform shader image; half4 main(float2 p) { return image.eval(p); }")
        val image = ImageBitmap(2, 1)
        val canvas = Canvas(image)
        canvas.drawRect(0f, 0f, 1f, 1f, Paint().apply { color = Color.Red })
        canvas.drawRect(1f, 0f, 2f, 1f, Paint().apply { color = Color.Blue })
        effect.setTextureUniform("image", image)
        assertEquals(listOf(Color.Red, Color.Blue, Color.Blue, Color.Blue).map { it.toArgb() },
            render(effect).take(4))
        effect.setTextureUniform("image", image, tileModeX = TileMode.Repeated)
        assertEquals(listOf(Color.Red, Color.Blue, Color.Red, Color.Blue).map { it.toArgb() },
            render(effect).take(4))
    }

    @Test
    fun malformedDataFails() {
        val effect = effect("uniform shader data; half4 main(float2 p) { return data.eval(p); }")
        for ((width, height, bytes) in listOf(Triple(0, 1, 0), Triple(1, -1, 0),
            Triple(1, 1, 2), Triple(1, 1, 4), Triple(Int.MAX_VALUE, Int.MAX_VALUE, 0))) {
            assertFailsWith<IllegalArgumentException> {
                effect.setDataTextureUniform("data", width, height, ByteArray(bytes))
            }
        }
    }

    @Test
    fun defaultBindingsRemainNoOpsButValidateData() {
        val fallback = object : RuntimeEffect {
            override val supported = false
            override val ready = false
            override fun build(): Brush = Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
        }
        val image = ImageBitmap(1, 1)
        fallback.setShaderUniform("unused", ImageShader(image))
        fallback.setTextureUniform("unused", image)
        fallback.setDataTextureUniform("unused", 1, 1, byteArrayOf(1, 2, 3))
        assertFailsWith<IllegalArgumentException> {
            fallback.setDataTextureUniform("unused", 0, 1, byteArrayOf())
        }
        assertEquals(false, fallback.ready)
    }
}
