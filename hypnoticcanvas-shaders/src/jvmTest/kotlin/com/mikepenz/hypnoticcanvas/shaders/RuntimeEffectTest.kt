package com.mikepenz.hypnoticcanvas.shaders

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toPixelMap
import com.mikepenz.hypnoticcanvas.NonAndroidRuntimeEffect
import kotlin.test.Test
import kotlin.test.assertTrue

class RuntimeEffectTest {
    @Test
    fun allBundledShadersRenderWithComposeShaderWrapper() {
        val shaders = listOf(
            GlossyGradients, MesmerizingLens,
            MeshGradient(arrayOf(Color.Red, Color.Green, Color.Blue)),
            BlackCherryCosmos, BubbleRings, GoldenMagma, GradientFlow, Heat(),
            IceReflection, InkFlow, OilFlow, PurpleLiquid, RainbowWater, Stage, Stripy(),
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
