package com.mikepenz.hypnoticcanvas.shaders

object GoldenMagma : Shader {
    override val name: String
        get() = "Golden Magma"

    override val credit: String
        get() = "https://www.shadertoy.com/view/tdBBRV"

    override val sksl = """
uniform float uTime;
uniform vec3 uResolution;

float random (in vec2 _st) {
    return fract(sin(dot(_st.xy, vec2(0.9,-0.5)))*757.153);
}

// Based on Morgan McGuire @morgan3d
// https://www.shadertoy.com/view/4dS3Wd
float noise (in vec2 _st) {
    vec2 i = floor(_st);
    vec2 f = fract(_st);

    // Four corners in 2D of a tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3. - 2. * f);

    return mix(a, b, u.x) + (c - a)* u.y * (1. - u.x) + (d - b) * u.x * u.y;
}

float fbm ( in vec2 _st) {
    float v = sin(uTime*0.3)*0.1;
    float a = 0.1;
    vec2 shift = vec2(100.);
    // Rotate to reduce axial bias
    mat2 rot = mat2(cos(0.5), sin(1.0), -sin(0.5), acos(0.5));
    for (int i = 0; i < 3; ++i) {
        v += a * noise(_st);
        _st = rot * _st * 2.0 + shift;
        a *= 2.8;
    }
    return v;
}

vec4 main( vec2 fragCoord )
{
    vec2 st = (fragCoord * 2.0 - uResolution.xy) / min(uResolution.x, uResolution.y) * 0.8;
   
    vec2 coord = st;
    coord.x += 0.2*uTime;
    coord.y += 0.2*uTime;
    
    float len = length(coord) - 3.;     
    
    vec3 color = vec3(0.);

    vec2 q = vec2(0.);
    q.x = fbm( st + 1.0);
    q.y = fbm( st + vec2(-0.450,0.650));

    vec2 r = vec2(0.);
    r.x = fbm( st + 1.0*q + vec2(0.570,0.520)+ 0.1*uTime );
    r.y = fbm( st + 1.0*q + vec2(0.340,-0.570)+ 0.07*uTime);
    
    color = mix(color, cos(len + vec3(0.2, 0.0, 0.5)), 1.0);
    color = mix(vec3(0.730,0.237,0.003), vec3(0.667,0.295,0.005), color);
    
    float f = fbm(st+r);
    return vec4(2.0*(f*f*f+.6*f*f+.5*f)*color,1.);
}

    """
}