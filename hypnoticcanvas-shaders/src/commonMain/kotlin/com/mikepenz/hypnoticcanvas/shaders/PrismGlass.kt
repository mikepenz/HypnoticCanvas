// Copyright 2026, Mike Penz
// SPDX-License-Identifier: MIT
// See hypnoticcanvas-shaders/LICENSE-MIT.
package com.mikepenz.hypnoticcanvas.shaders

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import com.mikepenz.hypnoticcanvas.RuntimeEffect

/** A floating glass panel with chromatic refraction of [image]. */
class PrismGlass(private val image: ImageBitmap = prismBackdrop()) : Shader {
    override val name = "Prism Glass"
    override val authorName = ""
    override val authorUrl = ""
    override val credit = "https://github.com/mikepenz/HypnoticCanvas"
    override val license = "MIT License"
    override val licenseUrl = "https://opensource.org/license/mit"
    override val speedModifier = 0.12f

    override fun applyUniforms(runtimeEffect: RuntimeEffect, time: Float, width: Float, height: Float) {
        super.applyUniforms(runtimeEffect, time, width, height)
        runtimeEffect.setFloatUniform("imageSize", image.width.toFloat(), image.height.toFloat())
        runtimeEffect.setTextureUniform("image", image)
    }

    override val sksl = """
        uniform float uTime;
        uniform float3 uResolution;
        uniform float2 imageSize;
        uniform shader image;

        float panel(float2 p) {
            float2 d = abs(p) - float2(0.43, 0.25);
            return length(max(d, 0.0)) + min(max(d.x, d.y), 0.0) - 0.13;
        }
        half3 background(float2 uv) {
            return image.eval(clamp(uv, 0.0, 1.0) * imageSize).rgb;
        }
        half4 main(float2 p) {
            float2 uv = p / uResolution.xy;
            float2 q = (p - 0.5 * uResolution.xy) / min(uResolution.x, uResolution.y);
            q /= min(0.85, 0.72 * uResolution.x / min(uResolution.x, uResolution.y));
            float angle = 0.12 * sin(uTime * 0.55) - 0.12;
            float c = cos(angle), s = sin(angle);
            q = float2(c*q.x - s*q.y, s*q.x + c*q.y);
            q.y += 0.025 * sin(uTime);
            float d = panel(q);
            float aa = 1.5 / min(uResolution.x, uResolution.y);
            float mask = 1.0 - smoothstep(-aa, aa, d);
            float2 normal = normalize(float2(panel(q + float2(0.001, 0.0)) - d,
                                            panel(q + float2(0.0, 0.001)) - d) + 0.00001);
            float bend = exp(-abs(d) * 15.0);
            float2 refracted = uv + q * 0.09 + normal * bend * 0.055;
            float2 split = normal * (0.004 + 0.009 * bend);
            half3 glass = half3(background(refracted + split).r,
                                background(refracted).g,
                                background(refracted - split).b);
            glass = glass * 0.88 + half3(0.035, 0.055, 0.085);
            float shine = pow(max(0.0, dot(normal, normalize(float2(-0.65, -0.8)))), 5.0);
            float rim = exp(-abs(d) * 180.0);
            float shadow = exp(-abs(panel(q - float2(0.025, 0.05))) * 22.0) * (1.0-mask);
            half3 color = background(uv) * (0.78 - 0.3 * shadow);
            color = mix(color, glass, mask);
            color += rim * mix(half3(0.2, 0.55, 0.85), half3(1.0, 0.87, 0.96), shine) * 0.8;
            color += mask * pow(max(0.0, 1.0 - abs(q.y + 0.22 + q.x*0.16) * 3.0), 12.0) * 0.09;
            return half4(clamp(color, 0.0, 1.0), 1.0);
        }
    """.trimIndent()
}

private fun prismBackdrop(): ImageBitmap = ImageBitmap(256, 256).also { image ->
    val canvas = Canvas(image)
    val paint = Paint()
    Brush.linearGradient(
        listOf(Color(0xFF15102D), Color(0xFF5935BF), Color(0xFFFA7C91), Color(0xFFFFCF98)),
        start = Offset.Zero, end = Offset(256f, 220f),
    ).applyTo(Size(256f, 256f), paint, 1f)
    canvas.drawRect(0f, 0f, 256f, 256f, paint)
    Brush.radialGradient(
        listOf(Color(0xFF6DE8E4), Color(0x006DE8E4)), center = Offset(42f, 210f), radius = 170f,
    ).applyTo(Size(256f, 256f), paint, 1f)
    canvas.drawRect(0f, 0f, 256f, 256f, paint)
    paint.shader = null
    paint.color = Color(0x3059F4EF)
    for (x in 0..8) canvas.drawRect(x * 36f - 10f, 0f, x * 36f - 8f, 256f, paint)
}
