package com.mikepenz.hypnoticcanvas.shaders

import com.mikepenz.hypnoticcanvas.RuntimeEffect

class Heat(
    private val filmGrainIntensity: Float = 0.1f
) : Shader {
    override val name: String
        get() = "heattttt"

    override val authorName: String
        get() = "ykleij"

    override val authorUrl: String
        get() = "https://www.shadertoy.com/user/ykleij"

    override val credit: String
        get() = "https://www.shadertoy.com/view/l3yyDd"

    override val license: String
        get() = "Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License"

    override val licenseUrl: String
        get() = "https://www.shadertoy.com/terms"

    override val speedModifier: Float
        get() = 0.1f

    override val sksl = """
uniform float filmGrainIntensity;
uniform float uTime;
uniform vec3 uResolution;

// Inspired by https://www.shadertoy.com/view/wdyczG
// Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License:
// https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en
mat2 Rot(float a) {
    float s = sin(a);
    float c = cos(a);
    return mat2(c, -s, s, c);
}

vec2 hash(vec2 p) {
    p = vec2(dot(p, vec2(2127.1, 81.17)), dot(p, vec2(1269.5, 283.37)));
    return fract(sin(p)*43758.5453);
}

float noise(in vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);

    vec2 u = f*f*(3.0-2.0*f);

    float n = mix(mix(dot(-1.0+2.0*hash(i + vec2(0.0, 0.0)), f - vec2(0.0, 0.0)),
    dot(-1.0+2.0*hash(i + vec2(1.0, 0.0)), f - vec2(1.0, 0.0)), u.x),
    mix(dot(-1.0+2.0*hash(i + vec2(0.0, 1.0)), f - vec2(0.0, 1.0)),
    dot(-1.0+2.0*hash(i + vec2(1.0, 1.0)), f - vec2(1.0, 1.0)), u.x), u.y);
    return 0.5 + 0.5*n;
}

float filmGrainNoise(in vec2 uv) {
    return length(hash(vec2(uv.x, uv.y)));
}

vec4 main( vec2 fragCoord ) {
    vec2 uv = fragCoord / uResolution.xy;
    float aspectRatio = uResolution.x / uResolution.y;
    
    // Transformed uv
    vec2 tuv = uv - .5;

    // Rotate with noise
    float degree = noise(vec2(uTime*.05, tuv.x*tuv.y));

    tuv.y *= 1./aspectRatio;
    tuv *= Rot(radians((degree-.5)*720.+180.));
    tuv.y *= aspectRatio;

    // Wave warp with sine
    float frequency = 5.;
    float amplitude = 30.;
    float speed = uTime * 2.;
    tuv.x += sin(tuv.y*frequency+speed)/amplitude;
    tuv.y += sin(tuv.x*frequency*1.5+speed)/(amplitude*.5);
        
    
    // Light gradient colors
    vec3 cornsilk = vec3(237, 203, 182) / vec3(255);
    vec3 indigoDye = vec3(246, 223, 171) / vec3(255);       // Indigo Dye (#1E4872)
    
    vec3 dun = vec3(105, 70, 51) / vec3(255);
    vec3 paynesGray2 = vec3(218, 80, 42) / vec3(255);     // Payne's Gray 2 (#3C576C)
    
    // Intermediate gradient colors
    vec3 blueGray = vec3(64, 33, 18) / vec3(255);      // Blue Gray (#6E96B9)
    vec3 paynesGray = vec3(44, 20, 11) / vec3(255);     // Payne's Gray (#436682)
    
    
    // Interpolate between light and dark gradient
    float cycle = sin(uTime * 0.5);
    float t = (sign(cycle) * pow(abs(cycle), 0.6) + 1.) / 2.;
    vec3 color1 = mix(cornsilk, indigoDye, t);
    vec3 color2 = mix(dun, paynesGray2, t);
    vec3 color3 = mix(blueGray, paynesGray, t);
    vec3 color4 = mix(paynesGray, blueGray, t);
    
    // Blend the gradient colors and apply transformations
    vec3 layer1 = mix(color3, color2, smoothstep(-.03, .2, (tuv * Rot(radians(-5.))).x));
    vec3 layer2 = mix(color4, color1, smoothstep(-.02, .2, (tuv * Rot(radians(-5.))).x));
    
    vec3 color = mix(layer1, layer2, smoothstep(.05, -.3, tuv.y));
    
    // Apply brightness increase and contrast boost
    color = color * 1.2;  // Increase brightness slightly
    color = pow(color, vec3(1.1));  // Apply a mild contrast boost
    
    // Apply film grain
    color = color - filmGrainNoise(uv) * filmGrainIntensity;
    
    return vec4(color, 1.0);
}
    """

    override fun applyUniforms(runtimeEffect: RuntimeEffect, time: Float, width: Float, height: Float) {
        super.applyUniforms(runtimeEffect, time, width, height)

        runtimeEffect.setFloatUniform(
            "filmGrainIntensity", filmGrainIntensity
        )
    }
}