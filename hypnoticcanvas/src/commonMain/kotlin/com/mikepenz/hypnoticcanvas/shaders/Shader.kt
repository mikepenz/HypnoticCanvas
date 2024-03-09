package com.mikepenz.hypnoticcanvas.shaders

/**
 * Interface to describe shaders supported by the [shaderBackground] Modifier.
 */
interface Shader {
    /** The name for this shader. */
    val name: String

    /** Contains the url to the source of this shader. */
    val credit: String

    /** Defaut time modifier for this shader */
    val speedModifier: Float
        get() = 0.5f

    /** Contains the sksl shader*/
    val sksl: String
}