package com.mikepenz.hypnoticcanvas.shaders

object Stage : Shader {
    override val name: String
        get() = "Stage"

    override val credit: String
        get() = "https://www.shadertoy.com/view/wtfcDj"

    override val speedModifier: Float
        get() = 0.3f

    override val sksl = """
uniform float uTime;
uniform vec3 uResolution;

vec4 main( vec2 fragCoord )
{
    vec4 o;
    vec3 u=normalize(vec3(2.*fragCoord-uResolution.xy,uResolution.xy.y))*1.2;
    for(float i=0.;i<4.;i++)
        u.y+=cos(u.y*i*5.)*.1,
        u.z+=sin(u.x*u.x+u.y*u.y-uTime*.3),
        u+=cos(u*i*7.)*.1,
        o = max(o,cos(3.*dot(u,u)+vec4(.3,.1,0,0)));
    return o;
}

    """
}