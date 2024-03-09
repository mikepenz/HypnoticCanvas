package com.mikepenz.hypnoticcanvas.shaders

object IceReflection : Shader {
    override val name: String
        get() = "IceReflection"

    override val authorName: String
        get() = "TAKUSAKU"

    override val authorUrl: String
        get() = "https://www.shadertoy.com/user/TAKUSAKU"

    override val credit: String
        get() = "https://www.shadertoy.com/view/3djfzy"

    override val license: String
        get() = "Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License"

    override val licenseUrl: String
        get() = "https://www.shadertoy.com/terms"

    override val sksl = """
uniform float uTime;
uniform vec3 uResolution;

float rand (in vec2 _st) {
    return fract(sin(dot(_st.xy, vec2(-0.820,-0.840)))*4757.153);
}

float noise (in vec2 _st) {
	const vec2 d = vec2(0., 1.);
  vec2 b = floor(_st), f = smoothstep(vec2(0.), vec2(0.1,0.3), fract(_st));
	return mix(mix(rand(b), rand(b + d.yx), f.x), mix(rand(b + d.xy), rand(b + d.yy), f.x), f.y);
}

float fbm ( in vec2 _st) {
    float v = sin(uTime*0.5)*0.2;
    float a = 0.3;
    vec2 shift = vec2(100.);
    // Rotate to reduce axial bias
    mat2 rot = mat2(cos(0.5), sin(1.0), -sin(0.5), acos(0.5));
    for (int i = 0; i <3; ++i) {
        v += a * noise(_st);
        _st = rot * _st * 2. + shift;
        a *= 1.5;
    }
    return v;
}

vec4 main( vec2 fragCoord ) {
    vec2 st = (fragCoord * 2. - uResolution.xy) / min(uResolution.x, uResolution.y) * 0.5;
    
    vec2 coord = st * 0.2;
    float len;
    for (int i = 0; i < 3; i++) {
        len = length(coord);
        coord.x +=  sin(coord.y + uTime * 0.1)*2.1;
        coord.y +=  cos(coord.x + uTime * 0.1 + cos(len * 1.0))*1.;
    }
    len -= 3.;
    
    vec3 color = vec3(0.);

    vec2 q = vec2(0.);
    q.x = fbm( st );
    q.y = fbm( st + vec2(-0.450,0.650));

    vec2 r = vec2(0.);
    r.x = fbm( st + 1.0*q + vec2(0.570,0.520)+ 0.1*uTime );
    r.y = fbm( st + 1.0*q + vec2(0.340,-0.570)+ 0.05*uTime);
    float f = fbm(st+r);
    
    color = mix(color, cos(len + vec3(0.5, 0.0, -0.1)), 1.0);
    color = mix(vec3(0.478,0.738,0.760),vec3(0.563,0.580,0.667),color);
    
    return vec4((f*f*f+.6*f*f+.5*f)*color,1.);
}
    """
}