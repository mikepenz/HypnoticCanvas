package com.mikepenz.hypnoticcanvas.shaders

// https://www.shadertoy.com/view/WdjBWD
object InkFlow : Shader {
    override val name: String
        get() = "InkFlow"

    override val credit: String
        get() = "https://www.shadertoy.com/view/WdjBWD"

    override val sksl = """
uniform float uTime;
uniform vec3 uResolution;

vec4 main( vec2 fragCoord )
{

    vec2 st = (fragCoord.xy * 2. - uResolution.xy) / min(uResolution.x, uResolution.y);
    
    st *= 2.5;

    vec2 coord = st;
    float len;
    for (int i = 0; i < 3; i++) {
        len = length(coord);
        coord.x +=  sin(coord.y + uTime * 0.3)*1.;
        coord.y +=  cos(coord.x + uTime * 0.1 + cos(len * 1.0))*6.;
    }
         
    vec3 col = vec3(0.);

    col = mix(col, vec3(cos(len)), 1.0);
    
    return vec4(0.7*col,1.);  
    
}
    """
}