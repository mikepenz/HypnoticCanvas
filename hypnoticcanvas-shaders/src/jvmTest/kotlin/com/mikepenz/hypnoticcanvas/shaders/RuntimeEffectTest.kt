package com.mikepenz.hypnoticcanvas.shaders

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.graphics.toArgb
import com.mikepenz.hypnoticcanvas.NonAndroidRuntimeEffect
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class RuntimeEffectTest {
    private fun render(shader: Shader, effect: NonAndroidRuntimeEffect, time: Float, width: Int = 160, height: Int = 100): IntArray {
        val bitmap = ImageBitmap(width, height)
        val paint = Paint()
        effect.update(shader, time, width.toFloat(), height.toFloat())
        effect.build().applyTo(Size(width.toFloat(), height.toFloat()), paint, 1f)
        Canvas(bitmap).drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        val pixels = bitmap.toPixelMap()
        return IntArray(width * height) { pixels[it % width, it / width].toArgb() }
    }

    @Test
    fun textureShowcasesAnimateInLandscapeAndPortrait() {
        for (shader in listOf(PrismGlass(), SpectralAurora())) {
            val effect = NonAndroidRuntimeEffect(shader)
            for ((width, height) in listOf(160 to 100, 100 to 160)) {
                val first = render(shader, effect, 0f, width, height)
                val second = render(shader, effect, 10f, width, height)
                assertTrue(first.toSet().size > 100, "${shader.name} must have visible detail")
                assertTrue(!first.contentEquals(second), "${shader.name} must animate")
                assertTrue(second.all { (it ushr 24) == 255 }, "${shader.name} must be opaque")
            }
        }
    }

    @Test
    fun glassUsesItsImageAndAuroraRespondsToBothDataInputs() {
        fun image(color: Color) = ImageBitmap(2, 2).also {
            Canvas(it).drawRect(0f, 0f, 2f, 2f, Paint().apply { this.color = color })
        }
        val red = PrismGlass(image(Color.Red))
        val blue = PrismGlass(image(Color.Blue))
        assertTrue(!render(red, NonAndroidRuntimeEffect(red), 0f)
            .contentEquals(render(blue, NonAndroidRuntimeEffect(blue), 0f)))

        val waveform = ByteArray(64 * 3) { 128.toByte() }
        val spectrum = ByteArray(64 * 3) { 32 }
        val aurora = SpectralAurora(waveform, spectrum)
        val effect = NonAndroidRuntimeEffect(aurora)
        val initial = render(aurora, effect, 0f)
        waveform.fill(220.toByte())
        val changedWave = render(aurora, effect, 0f)
        assertTrue(!initial.contentEquals(changedWave), "Waveform must move the ribbons")
        spectrum.fill(255.toByte())
        assertTrue(!changedWave.contentEquals(render(aurora, effect, 0f)), "Spectrum must change their intensity")
        assertFailsWith<IllegalArgumentException> { SpectralAurora(byteArrayOf(), spectrum) }
        assertFailsWith<IllegalArgumentException> { SpectralAurora(waveform, byteArrayOf(1, 2)) }
    }

    @Test
    fun allBundledShadersRenderWithComposeShaderWrapper() {
        val shaders = listOf(
            GlossyGradients, MesmerizingLens,
            MeshGradient(arrayOf(Color.Red, Color.Green, Color.Blue)),
            BlackCherryCosmos, BubbleRings, GoldenMagma, GradientFlow, Heat(),
            IceReflection, InkFlow, OilFlow, PurpleLiquid, RainbowWater, Stage, Stripy(),
            PrismGlass(), SpectralAurora(),
        )
        for (shader in shaders) {
            val effect = NonAndroidRuntimeEffect(shader)
            assertTrue(effect.supported, shader.name)
            val bitmap = ImageBitmap(16, 16)
            val paint = Paint()
            for (time in listOf(0f, 1f)) {
                effect.update(shader, time, 16f, 16f)
                assertTrue(effect.ready, shader.name)
                effect.build().applyTo(Size(16f, 16f), paint, 1f)
                Canvas(bitmap).drawRect(0f, 0f, 16f, 16f, paint)
                assertTrue(bitmap.toPixelMap()[8, 8].alpha > 0f, shader.name)
            }
        }
    }
}
