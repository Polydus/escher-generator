package com.polydus.eschergen.d3

class Shader {

    enum class Attribute(val id: String, val size: Int){
        POSITION_2D("aVertexPosition", 2),

        POSITION("aVertexPosition", 3),
        NORMAL("aVertexNormal", 3),
        COLOR("aVertexColor", 4),
        TEX_COORD("aVertexPosition", 2),
    }

    enum class Uniform(val id: String, val size: Int){
        PROJ_MATRIX("uProjectionMatrix", 4),
        MODELVIEW_MATRIX("uModelViewMatrix", 4),
        NORMAL_MATRIX("uNormalMatrix", 4),
    }

    enum class Type{
        FRAGMENT, VERTEX
    }

}