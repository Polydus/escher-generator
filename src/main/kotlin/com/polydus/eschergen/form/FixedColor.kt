package com.polydus.eschergen.form

import com.polydus.eschergen.math.Vector3F

class FixedColor(){

    var hue = 0f
    var saturation = 50f
    var lightness = 50f

    var inUse = false

    fun set(vector3F: Vector3F){
        vector3F.set(hue, saturation, lightness)
    }

    fun reset(){
        hue = 0f
        saturation = 50f
        lightness = 50f
        inUse = false
    }

    override fun toString(): String {
        return "FixedColor(hue=$hue, saturation=$saturation, lightness=$lightness, inUse=$inUse)"
    }

}