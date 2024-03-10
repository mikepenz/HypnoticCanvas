// Copyright 2024, Mike Penz and Giorgi Azmaipharashvili, Author of the Glossy Gradients Shader
// SPDX-License-Identifier: MIT
package com.mikepenz.hypnoticcanvas.shaders

object GlossyGradients : Shader {
    override val name: String
        get() = "GlossyGradients"

    override val authorName: String
        get() = "Giorgi Azmaipharashvili"

    override val authorUrl: String
        get() = "https://www.shadertoy.com/user/Peace"

    override val credit: String
        get() = "https://www.shadertoy.com/view/lX2GDR"

    override val license: String
        get() = "MIT License"

    override val licenseUrl: String
        get() = "https://opensource.org/license/mit"

    override val sksl = """
// The MIT License
// Copyright © 2024 Giorgi Azmaipharashvili
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

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