package com.mikepenz.hypnoticcanvas.shaders

object OilFlow : Shader {
    override val name: String
        get() = "OilFlow"

    override val credit: String
        get() = "https://www.shadertoy.com/view/Wd2fDW"

    override val sksl = """
uniform float uTime;
uniform vec3 uResolution;

vec4 main( vec2 fragCoord )
{

    vec2 uv = fragCoord/uResolution.xy;

    uv *= 6.5;
    
    float len;
    for(int i = 0; i < 3; i++) {
        len = length(uv);
        uv.x +=  sin(uv.y + uTime * 0.3)*5.;
        uv.y +=  cos(uv.x + uTime * 0.1 + cos(len * 2.0))*2.;
    }
    
    vec3 col = vec3(cos(len + 0.3), cos(len + 0.1), cos(len - 0.1));
    
    return vec4(col,1.0);
    
}

    """
}