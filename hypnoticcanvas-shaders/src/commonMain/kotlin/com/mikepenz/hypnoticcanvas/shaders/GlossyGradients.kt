package com.mikepenz.hypnoticcanvas.shaders

object GlossyGradients : Shader {
    override val name: String
        get() = "GlossyGradients"

    override val authorName: String
        get() = "Peace"
    
    override val authorUrl: String
        get() = "https://www.shadertoy.com/user/Peace"

    override val credit: String
        get() = "https://www.shadertoy.com/view/lX2GDR"

    override val license: String
        get() = "Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License"

    override val licenseUrl: String
        get() = "https://www.shadertoy.com/terms"

    override val sksl = """
uniform float uTime;
uniform vec3 uResolution;

vec4 main( vec2 fragCoord )
{
    float mr = min(uResolution.x, uResolution.y);
    vec2 uv = (fragCoord * 2.0 - uResolution.xy) / mr;

    float d = -uTime * 0.5;
    float a = 0.0;
    for (float i = 0.0; i < 8.0; ++i) {
        a += cos(i - d - a * uv.x);
        d += sin(uv.y * i + a);
    }
    d += uTime * 0.5;
    vec3 col = vec3(cos(uv * vec2(d, a)) * 0.6 + 0.4, cos(a + d) * 0.5 + 0.5);
    col = cos(col * cos(vec3(d, a, 2.5)) * 0.5 + 0.5);
    return vec4(col,1.0);
}
    """
}