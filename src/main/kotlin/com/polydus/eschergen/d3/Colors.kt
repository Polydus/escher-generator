package com.polydus.eschergen.d3

import com.polydus.eschergen.math.Vector3F
import kotlin.math.*

class Colors {

    companion object{

        private fun hslToRgb(color: Vector3F){
            val h = color.x
            val s = color.y
            val l = color.z
            val r: Float
            val g: Float
            val b: Float
            if (s == 0f) {
                b = l
                g = b
                r = g
            } else {
                val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
                val p = 2 * l - q
                r = hueToRgb(p, q, h + 1f / 3f)
                g = hueToRgb(p, q, h)
                b = hueToRgb(p, q, h - 1f / 3f)
            }
            color.set(r, g, b)
        }

        fun rgbToHsl(color: Vector3F){
            val r = color.x
            val g = color.y
            val b = color.z
            val max = if (r > g && r > b) r else if (g > b) g else b
            val min = if (r < g && r < b) r else if (g < b) g else b
            var h: Float
            val s: Float
            val l: Float
            l = (max + min) / 2.0f
            if (max == min) {
                s = 0.0f
                h = s
            } else {
                val d = max - min
                s = if (l > 0.5f) d / (2.0f - max - min) else d / (max + min)
                h = if (r > g && r > b) (g - b) / d + (if (g < b) 6.0f else 0.0f) else if (g > b) (b - r) / d + 2.0f else (r - g) / d + 4.0f
                h /= 6.0f
            }

            color.set(h, s, l)
        }

        private fun setHslNormal(color: Vector3F, h: Float, s: Float, l: Float){
            color.set(h, s, l)
            hslToRgb(color)
        }

        fun setHsl(color: Vector3F, h: Float, s: Float, l: Float): Vector3F {
            setHslNormal(color, (h % 360) / 360f, (s.coerceIn(0f, 100f)) / 100f, (l.coerceIn(0f, 100f)) / 100f)
            return color
        }

        /** Helper method that converts hue to rgb  */
        private fun hueToRgb(p: Float, q: Float, t: Float): Float {
            var t = t
            if (t < 0f) t += 1f
            if (t > 1f) t -= 1f
            if (t < 1f / 6f) return p + (q - p) * 6f * t
            if (t < 1f / 2f) return q
            return if (t < 2f / 3f) p + (q - p) * (2f / 3f - t) * 6f else p
        }

        fun getHSL(color: Vector3F, res: Vector3F = Vector3F()): Vector3F {
            val r = color.x.toDouble()// / 255.0
            val g = color.y.toDouble()// / 255.0
            val b = color.z.toDouble()/// 255.0

            val cmin: Double = min(min(r, g), b)
            val cmax: Double = max(max(r, g), b)
            val delta: Double = cmax - cmin
            var h = 0.0
            var s = 0.0
            var l = 0.0

            h = if (delta == 0.0){
                0.0
            } else if (cmax == r){
                (g - b) / delta % 6.0
            } else if (cmax == g){
                (b - r) / delta + 2.0
            } else{
                (r - g) / delta + 4.0
            }

            h = round((h * 60.0))
            if (h < 0.0) h += 360.0

            l = (cmax + cmin) / 2.0

            s = if (delta == 0.0){
                0.0
            } else {
                (delta) / ((1.0 - abs(2.0 * l - 1.0)) )
            }

            l = ((l * 100.0)).roundToInt().toDouble()
            s = (s * 100.0).roundToInt().toDouble()

            return res.set(h.toFloat(), s.toFloat(), l.toFloat())
            //return "hsl($h,$s,$l)"
        }


    }
}