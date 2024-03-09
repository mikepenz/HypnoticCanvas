package com.mikepenz.hypnoticcanvas.shaders

object RainbowWater : Shader {
    override val name: String
        get() = "RainbowWater"

    override val authorName: String
        get() = "flylo"

    override val authorUrl: String
        get() = "https://www.shadertoy.com/user/flylo"

    override val credit: String
        get() = "https://www.shadertoy.com/view/dtySRR"

    override val license: String
        get() = "Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License"

    override val licenseUrl: String
        get() = "https://www.shadertoy.com/terms"

    override val speedModifier: Float
        get() = 0.01f

    override val sksl = """
uniform float uTime;
uniform vec3 uResolution;

vec3 pools(in vec2 uv)
{
    float dt  = .5+.5*sin(.18*uTime);
    float dt2 = .5+.5*sin(.038*uTime);
    float mdt = .2+.8*dt;
    float mdt2 = .7+.3*dt2;
    
    float dt3 = .5+.5*sin(.14*uTime);
    float dt4 = .5+.5*sin(.033*uTime);
    float mdt3 = .2+.8*dt3;
    float mdt4 = .7+.3*dt4;

    float dt5  = .5+.5*sin(.11*uTime);
    float dt6 = .5+.5*sin(1.5+.036*uTime);
    float mdt5 = .2+.8*dt5;
    float mdt6 = .7+.3*dt6;

    float dt7  = .5+.5*sin(.3*uTime);
    float mdt7 = .1+.9*dt7;

    float dt8  = .5+.5*sin(1.5+.2*uTime);
    float mdt8 = .9+.1*dt8;


    //uv = vec2(sin(25.*uv));


    vec3 c = vec3(0.);
    float a = 1.;
    float f = 1.;
    float as = 0.;
    for (int i = 0; i < 1; i++) {
        for (int j = 0; j < 25; j++) {
            //float p[] = float[](.2, .5, .3, 0.1, .4, .8, .8, .5);
            //float wc[] = float[](.0, 1., .0, 1., .1, .2, .0, .1, 1.);
            f = 2.*(1.+.3*float(j));//*(.5+.5*sin(157.*float(j)));
            a = 1./(pow(1.23,float(j)));
            //a = 1./(1.+float(j));
            
            float r2 = length(uv-vec2(sin(142.31*float(j)), sin(29.321*float(j))));

            c.r += mdt*1.4*a*(.5+.5*sin(7.*uTime+f*mdt2*7.1*r2));
            c.g += mdt3*1.4*a*(.5+.5*sin(7.*uTime+f*mdt4*7.1*r2));
            //c.b += .1*a*(.5+.5*sin(11.*uTime+f*7.1*r2));
            //c.g += .5*a*(.5+.5*sin(11.*uTime+f*5.1*r2));
            c.b += mdt5*1.4*a*(.5+.5*sin(7.*uTime+f*mdt6*7.1*r2));
            //c += vec3(r2);
            //c.b += a*.7*sin(22.*mdt6+f*5.4*r2);
            as += a;
            //f *= 1.3+length(c);
            //f *= 1.1;
   
            //a = 0.;
        }
    }
    c /= as;
   
 
    return c;
    
}


vec3 warp(in vec2 uv) {
    vec3 c = pools(uv);
    
    const int iter = 5;
    float amp = .55;
    float f = .9;

    float a = 1.; 
    for (int i = 0; i < iter; i++) {
       a *= amp;
    }
       
    for (int i = 0; i < iter; i++) {
        a /= amp;
        f *= 1.2;
        c = a*pools(uv + f*vec2(c.r-c.g, c.g - c.b)) ;
    }
    return c;
}


vec4 main( vec2 fragCoord )
{

    ivec2 px = ivec2( fragCoord );
    vec2 uv = fragCoord/uResolution.xy;
    return vec4(warp(uv), 1.);

}
    """
}