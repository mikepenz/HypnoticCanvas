package com.mikepenz.hypnoticcanvas.shaders

import com.mikepenz.hypnoticcanvas.RuntimeEffect

class Stripy(
    val gamma: Float = 2.2f
) : Shader {
    override val name: String
        get() = "Stripy"

    override val authorName: String
        get() = "Xor"

    override val authorUrl: String
        get() = "https://www.shadertoy.com/user/Xor"

    override val credit: String
        get() = "https://www.shadertoy.com/view/l3VcWG"

    override val license: String
        get() = "Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License"

    override val licenseUrl: String
        get() = "https://www.shadertoy.com/terms"

    override val speedModifier: Float
        get() = 0.1f

    override val sksl = """
uniform float gamma;
uniform float uTime;
uniform vec3 uResolution;

//For when the derivatives must be manually calculated
float antialias_l2_dxy(float d, vec2 dxy)
{
    //Get gradient width
    float width = length(dxy);
    //Calculate reciprocal scale (avoid division by 0!)
    float scale = width>0.0? 1.0/width : 1e7;
    //Normalize the gradient d with it's scale
    return clamp(0.5 + 0.7 * scale * d, 0.0, 1.0);
}
//Sine-base hue function
vec3 color(float index)
{
    return cos(index+vec3(0,2,4))*0.5+0.5;
}

vec4 main( vec2 fragCoord )
{
    //Normalize screen coordinates (aspect ratio corrected)
    vec2 uv = (fragCoord-0.5*uResolution.xy) / uResolution.y;

    //Generate a little pattern
    float value = cos(uv.x*9.)*cos(uv.y*9.)*3.+uv.x*7.+uv.y*9.+uTime;
    
    //Get the gradient from the pattern
    float stripe_grad = value;
    //Break into stripe steps
    float stripe_step = floor(stripe_grad + 0.5);
    //Find the gradient to the nearest edge
    float stripe_frac = stripe_grad - stripe_step;
    
    //Calculate the derivatives on the continous gradient
    vec2 dxy = vec2(stripe_grad, stripe_grad);
    
    //Blend between the two nearest stripes
    vec3 c1 = color(stripe_step+0.0);
    vec3 c2 = color(stripe_step+1.0);
    //Anti-aliasing like normal
    vec3 col = mix(c1, c2, antialias_l2_dxy(stripe_frac,dxy));

    // Output to screen
    return vec4(pow(col, 1.0/vec3(gamma)),1.0);
}
    """

    override fun applyUniforms(runtimeEffect: RuntimeEffect, time: Float, width: Float, height: Float) {
        super.applyUniforms(runtimeEffect, time, width, height)

        runtimeEffect.setFloatUniform(
            "gamma", gamma
        )
    }
}