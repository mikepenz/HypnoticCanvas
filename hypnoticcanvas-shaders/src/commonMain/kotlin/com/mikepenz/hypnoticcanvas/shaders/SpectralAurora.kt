// Copyright 2026, Mike Penz
// SPDX-License-Identifier: MIT
// See hypnoticcanvas-shaders/LICENSE-MIT.
package com.mikepenz.hypnoticcanvas.shaders

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import com.mikepenz.hypnoticcanvas.RuntimeEffect
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.roundToInt

/** Animated ribbons driven by packed RGB waveform and normalized spectrum samples in the red channel. */
class SpectralAurora(
    private val waveformRgb: ByteArray = auroraSamples { x -> 0.5 + 0.28*sin(x*18.0) + 0.12*sin(x*43.0) },
    private val spectrumRgb: ByteArray = auroraSamples { x ->
        0.08 + 0.85*exp(-((x-0.22)*8.0)*((x-0.22)*8.0)) + 0.5*exp(-((x-0.7)*12.0)*((x-0.7)*12.0))
    },
) : Shader {
    init {
        require(waveformRgb.isNotEmpty() && waveformRgb.size % 3 == 0) { "Waveform needs packed RGB samples" }
        require(spectrumRgb.isNotEmpty() && spectrumRgb.size % 3 == 0) { "Spectrum needs packed RGB samples" }
    }

    private val palette = LinearGradientShader(
        Offset.Zero, Offset(256f, 0f),
        listOf(Color(0xFF46EBD5), Color(0xFF6B89FF), Color(0xFFBD71F5), Color(0xFFFF8DB2)),
    )
    override val name = "Spectral Aurora"
    override val authorName = ""
    override val authorUrl = ""
    override val credit = "https://github.com/mikepenz/HypnoticCanvas"
    override val license = "MIT License"
    override val licenseUrl = "https://opensource.org/license/mit"
    override val speedModifier = 0.15f

    override fun applyUniforms(runtimeEffect: RuntimeEffect, time: Float, width: Float, height: Float) {
        super.applyUniforms(runtimeEffect, time, width, height)
        runtimeEffect.setFloatUniform("sampleCounts", waveformRgb.size / 3f, spectrumRgb.size / 3f)
        runtimeEffect.setDataTextureUniform("waveform", waveformRgb.size / 3, 1, waveformRgb)
        runtimeEffect.setDataTextureUniform("spectrum", spectrumRgb.size / 3, 1, spectrumRgb)
        runtimeEffect.setShaderUniform("palette", palette)
    }

    override val sksl = """
        uniform float uTime;
        uniform float3 uResolution;
        uniform float2 sampleCounts;
        uniform shader waveform;
        uniform shader spectrum;
        uniform shader palette;

        half4 main(float2 p) {
            float2 uv = p / uResolution.xy;
            float waveIndex = clamp(uv.x, 0.0, 1.0) * (sampleCounts.x - 1.0);
            float spectrumIndex = clamp(uv.x, 0.0, 1.0) * (sampleCounts.y - 1.0);
            float wave = mix(waveform.eval(float2(floor(waveIndex)+0.5, 0.5)).r,
                             waveform.eval(float2(floor(waveIndex)+1.5, 0.5)).r, fract(waveIndex)) * 2.0 - 1.0;
            float energy = mix(spectrum.eval(float2(floor(spectrumIndex)+0.5, 0.5)).r,
                               spectrum.eval(float2(floor(spectrumIndex)+1.5, 0.5)).r, fract(spectrumIndex));
            half3 color = mix(half3(0.012, 0.019, 0.065), half3(0.04, 0.045, 0.12), uv.y);
            for (int i = 0; i < 4; i++) {
                float layer = float(i);
                float center = 0.47 + wave*0.13 + sin(uv.x*5.0 + uTime*0.65 + layer*0.7)*0.09;
                center += (layer-1.5)*0.042;
                float distance = abs(uv.y-center);
                float thickness = 0.012 + energy*0.018;
                float core = exp(-distance/thickness);
                float bloom = exp(-distance/(0.065 + energy*0.09));
                half3 tint = palette.eval(float2(fract(uv.x*0.6 + layer*0.17 + uTime*0.025)*256.0, 0.5)).rgb;
                color += tint * (core*0.45 + bloom*0.11) * (0.3+energy*0.7);
            }
            float reflection = exp(-abs(uv.y-0.79)*12.0) * (0.03+energy*0.08);
            color += palette.eval(float2(uv.x*256.0, 0.5)).rgb * reflection;
            float vignette = 1.0 - 0.35 * length((uv-0.5)*1.3);
            return half4(clamp(color*vignette, 0.0, 1.0), 1.0);
        }
    """.trimIndent()
}

private fun auroraSamples(value: (Double) -> Double): ByteArray = ByteArray(128 * 3) { i ->
    if (i % 3 == 0) (value((i / 3) / 127.0).coerceIn(0.0, 1.0)*255.0).roundToInt().toByte() else 0
}
