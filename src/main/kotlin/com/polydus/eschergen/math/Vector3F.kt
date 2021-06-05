package com.polydus.eschergen.math

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Vector3F() {

    var x = 0f
    var y = 0f
    var z = 0f

    var index = -1

    companion object{

        val DEG_TO_RAD = Math.PI / 180f
        val RAD_TO_DEG = 180f / Math.PI

    }

    constructor(x: Float, y: Float, z: Float): this(){
        set(x, y, z)
    }

    constructor(v: Vector3F): this(v.x, v.y, v.z)

    fun set(x: Float, y: Float, z: Float): Vector3F {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    fun set(v: Vector3F): Vector3F {
        return set(v.x, v.y, v.z)
    }

    fun add(x: Float, y: Float, z: Float): Vector3F {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun add(v: Vector3F): Vector3F {
        return add(v.x, v.y, v.z)
    }

    fun addAndScale(x: Float, y: Float, z: Float, scale: Float): Vector3F {
        this.x += (x * scale)
        this.y += (y * scale)
        this.z += (z * scale)
        return this
    }

    fun addAndScale(v: Vector3F, scale: Float): Vector3F {
        return this.addAndScale(v.x, v.y, v.z, scale)
    }

    fun min(x: Float, y: Float, z: Float): Vector3F {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun clear(): Vector3F {
        return set(0f, 0f, 0f)
    }

    fun min(v: Vector3F): Vector3F {
        return min(v.x, v.y, v.z)
    }

    fun setPoint(x: Float, y: Float): Vector3F {
        val azAngle = x * DEG_TO_RAD
        val polarAngle = y * DEG_TO_RAD

        val cosPolar = cos(polarAngle).toFloat()
        val sinPolar = sin(polarAngle).toFloat()
        val cosAzim = cos(azAngle).toFloat()
        val sinAzim = sin(azAngle).toFloat()
        return this.set(cosAzim * sinPolar, sinAzim * sinPolar, cosPolar)
    }

    fun pow(amount: Float): Vector3F {
        x = x.pow(amount)
        y = y.pow(amount)
        z = z.pow(amount)
        return this
    }

    fun divide(amount: Float): Vector3F {
        x /= amount
        y /= amount
        z /= amount
        return this
    }

    fun mul(amount: Float): Vector3F {
        x *= amount
        y *= amount
        z *= amount
        return this
    }

    fun nor() : Vector3F {
        val length = length()
        return mul(1f / sqrt(length))//divide(length)
    }

    /*
    *
    * 	public Vector3 nor () {
		final float len2 = this.len2();
		if (len2 == 0f || len2 == 1f) return this;
		return this.scl(1f / (float)Math.sqrt(len2));
	}
    * */

    fun length(): Float{
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    override fun toString(): String {
        return "Vector3(x=$x, y=$y, z=$z)"
    }

}