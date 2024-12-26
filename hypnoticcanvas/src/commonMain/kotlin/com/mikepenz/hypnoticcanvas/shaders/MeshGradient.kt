// Copyright 2024, Mike Penz
// SPDX-License-Identifier: MIT
package com.mikepenz.hypnoticcanvas.shaders

import androidx.compose.ui.graphics.Color
import com.mikepenz.hypnoticcanvas.RuntimeEffect

class MeshGradient(
    /**
     * The colors to display.
     * The last provided color acts like a "background"
     */
    colors: Array<Color>,
    /** Adjust the speed of the movement */
    speed: Float = 1f,
    /** Adjusts the scale of the board. Higher number -> larger billboard -> smaller color blobs */
    scale: Float = 2f,
) : Shader {
    private val colorCount = colors.size
    private val colorUniforms = colors.flatMap {
        listOf(it.red, it.green, it.blue)
    }.toTypedArray().toFloatArray()

    override val name: String
        get() = "MeshGradient"

    override val authorName: String
        get() = "Mike Penz"

    override val authorUrl: String
        get() = "https://github.com/mikepenz/"

    override val credit: String
        get() = ""

    override val license: String
        get() = "MIT License"

    override val licenseUrl: String
        get() = "https://opensource.org/license/mit"

    override val sksl = """
uniform float uTime;
uniform vec3 uResolution;

vec3 vColor;
const int MAX_COLORS = ${colorCount};
uniform vec3 uColor[MAX_COLORS];

//	Simplex 3D Noise 
//	by Ian McEwan, Ashima Arts
//  https://gist.github.com/patriciogonzalezvivo/670c22f3966e662d2f83
//
vec4 permute(vec4 x) {
    return mod(((x * 34.0) + 1.0) * x, 289.0);
}
vec4 taylorInvSqrt(vec4 r) {
    return 1.79284291400159 - 0.85373472095314 * r;
}

float snoise(vec3 v) {
    const vec2 C = vec2(1.0 / 6.0, 1.0 / 3.0);
    const vec4 D = vec4(0.0, 0.5, 1.0, 2.0);

    // First corner
    vec3 i = floor(v + dot(v, C.yyy));
    vec3 x0 = v - i + dot(i, C.xxx);

    // Other corners
    vec3 g = step(x0.yzx, x0.xyz);
    vec3 l = 1.0 - g;
    vec3 i1 = min(g.xyz, l.zxy);
    vec3 i2 = max(g.xyz, l.zxy);

    //  x0 = x0 - 0. + 0.0 * C 
    vec3 x1 = x0 - i1 + 1.0 * C.xxx;
    vec3 x2 = x0 - i2 + 2.0 * C.xxx;
    vec3 x3 = x0 - 1. + 3.0 * C.xxx;

    // Permutations
    i = mod(i, 289.0);
    vec4 p = permute(permute(permute(i.z + vec4(0.0, i1.z, i2.z, 1.0)) + i.y + vec4(0.0, i1.y, i2.y, 1.0)) + i.x + vec4(0.0, i1.x, i2.x, 1.0));

    // Gradients
    // ( N*N points uniformly over a square, mapped onto an octahedron.)
    float n_ = 1.0 / 7.0; // N=7
    vec3 ns = n_ * D.wyz - D.xzx;

    vec4 j = p - 49.0 * floor(p * ns.z * ns.z);  //  mod(p,N*N)

    vec4 x_ = floor(j * ns.z);
    vec4 y_ = floor(j - 7.0 * x_);    // mod(j,N)

    vec4 x = x_ * ns.x + ns.yyyy;
    vec4 y = y_ * ns.x + ns.yyyy;
    vec4 h = 1.0 - abs(x) - abs(y);

    vec4 b0 = vec4(x.xy, y.xy);
    vec4 b1 = vec4(x.zw, y.zw);

    vec4 s0 = floor(b0) * 2.0 + 1.0;
    vec4 s1 = floor(b1) * 2.0 + 1.0;
    vec4 sh = -step(h, vec4(0.0));

    vec4 a0 = b0.xzyw + s0.xzyw * sh.xxyy;
    vec4 a1 = b1.xzyw + s1.xzyw * sh.zzww;

    vec3 p0 = vec3(a0.xy, h.x);
    vec3 p1 = vec3(a0.zw, h.y);
    vec3 p2 = vec3(a1.xy, h.z);
    vec3 p3 = vec3(a1.zw, h.w);

    //Normalise gradients
    vec4 norm = taylorInvSqrt(vec4(dot(p0, p0), dot(p1, p1), dot(p2, p2), dot(p3, p3)));
    p0 *= norm.x;
    p1 *= norm.y;
    p2 *= norm.z;
    p3 *= norm.w;

    // Mix final noise value
    vec4 m = max(0.6 - vec4(dot(x0, x0), dot(x1, x1), dot(x2, x2), dot(x3, x3)), 0.0);
    m = m * m;
    return 42.0 * dot(m * m, vec4(dot(p0, x0), dot(p1, x1), dot(p2, x2), dot(p3, x3)));
}

// Author       : Johnny Leek
// https://github.com/JohnnyLeek1/React-Mesh-Gradient
// Inspiration  : Yuri Artiukh
vec4 main( vec2 fragCoord ) {
    float mr = min(uResolution.x, uResolution.y);
    vec2 uv = (fragCoord * $scale - uResolution.xy) / mr;

    // Calculate base coordinate position
    vec2 base = uv / 2;

    // Apply slight tilt + incline
    float tilt = -0.5 * uv.y;
    float incline = uv.x * 0.1;

    // Apply slight offset between -0.25 and 0.25 of the y position
    float offset = incline * mix(-.25, 0.25, uv.y);

    // Calculate noise based on base position
    float noise = snoise(vec3(base.x + uTime * 0.2, base.y, uTime * 0.2));

    // Ignore negative noise values
    noise = max(0., noise);

    // Calculate final position
    vec3 pos = vec3(uResolution.x, uResolution.y, uResolution.z + noise * 0.1 + tilt + incline + offset);

    // Set base color to the last color in our array
    vColor = uColor[MAX_COLORS - 1];

    // Iterate through the other colors in the array
    for(int i = 0; i < MAX_COLORS - 1; i++) {
        // Calculate some more noise values to use for the color
        float flow = 5. + float(i) * 0.3;
        float speed = 6. * $speed + float(i) * 0.3;
        float seed = 1. + float(i) * 4.;

        vec2 frequency = vec2(0.3, 0.7);

        // Create min and max values for our noise (based on the current color index)
        float noiseFloor = 0.00001;
        float noiseCeil = 0.6 + float(i) * 0.07;

        // Calculate noise
        float noise = smoothstep(noiseFloor, noiseCeil, snoise(vec3(base.x * frequency.x + uTime * 0.005 * flow, base.y * frequency.y, uTime * 0.005 * speed + seed)));

        // Mix the color with the base color based on our noise
        vColor = mix(vColor, uColor[i], noise);
    }

    // Set the new uV and position of the vertex
    return vec4(vColor, 1.0);
}
    """

    override fun applyUniforms(runtimeEffect: RuntimeEffect, time: Float, width: Float, height: Float) {
        super.applyUniforms(runtimeEffect, time, width, height)

        runtimeEffect.setFloatUniform(
            "uColor", colorUniforms
        )
    }
}