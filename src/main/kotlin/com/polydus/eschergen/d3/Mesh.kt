package com.polydus.eschergen.d3

import com.polydus.eschergen.form.FixedColor
import com.polydus.eschergen.form.FormSettingsDto
import com.polydus.eschergen.math.Vector3F
import io.github.jdiemke.triangulation.DelaunayTriangulator
import io.github.jdiemke.triangulation.Triangle2D
import io.github.jdiemke.triangulation.Vector2D
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class Mesh {

    var vertices = HashMap<Shader.Attribute, ArrayList<Double>>()
    var indices = ArrayList<Int>()

    var attributes = ArrayList<Shader.Attribute>()

    //val shapes = HashMap<Shape, ArrayList<ArrayList<Vector3F>>>()

    init {

    }

    companion object{

        fun genEscherRectangle(/*setting: EscherSettings, */dto: FormSettingsDto): Mesh {
            val res = Mesh()

            res.apply {
                vertices[Shader.Attribute.POSITION_2D] = ArrayList()
                vertices[Shader.Attribute.COLOR] = ArrayList()
            }
            val positions = res.vertices[Shader.Attribute.POSITION_2D]!!
            val colors = res.vertices[Shader.Attribute.COLOR]!!
            val indices = res.indices

            //val scale = setting.scale//.toDouble().coerceIn(1.0, 10.0)

            val points = arrayListOf<Vector2D>()
            var width = (20.0).toInt()
            var height = (20.0).toInt()
            val color = Vector3F()
            var index = 0

            val tempVector3F = Vector3F()

            //val color2 = Vector3F()
            //Colors.setHsl(color2, 0f, 50f, 50f)


            when(dto.shape){
                /*Shape.A -> {
                    points.add(Vector2D(-1.0, 1.0))
                    points.add(Vector2D(-1.0, -1.0))
                    points.add(Vector2D(1.0, 1.0))
                    points.add(Vector2D(1.0, -1.0))
                    for(i in 0 until 8){
                        //val vec2 = Vector2D((Math.random() * 2.0) - 1, (Math.random() * 2.0) - 1)
                        //points.add(vec2)
                        //println("${vec2.x} , ${vec2.y}")
                    }
                    genShape(points, 45, 0, 0, 90)

                    val shape = triangulate(points)
                    points.clear()
                    val shape1 = ArrayList<Triangle2D>()
                    shape1.addAll(shape.subList(0, shape.size / 2))
                    val shape2 = ArrayList<Triangle2D>()
                    shape2.addAll(shape.subList(shape1.size, shape.size))

                    for(y in -(height + 1)..height){
                        for(x in -(width + 1)..width){
                            setting.colorSetting1.setHsl(color, x, y, index)
                            addShape(shape1,
                                ((1.0) * 2.0) * x,
                                ((1.0) * 2.0) * y,
                                color, colors, positions, indices)

                            setting.colorSetting2.setHsl(color, x, y, index)
                            addShape(shape2,
                                ((1.0) * 2.0) * x,
                                ((1.0) * 2.0) * y,
                                color, colors, positions, indices)

                            index++
                        }
                    }
                }*/
                Shape.SQUARE -> {
                    width = (33.0).toInt()
                    height = (20.0).toInt()
                    points.add(Vector2D(0.0, 1.0))
                    points.add(Vector2D(0.0, -1.0))
                    points.add(Vector2D(1.0, 0.0))
                    points.add(Vector2D(-1.0, 0.0))
                    val shape1 = triangulate(points)
                    points.clear()
                    val ratio = 0.866
                    val ratioReverse = 1.0 - ratio

                    for(y in -(height + 1)..height){
                        for(x in -(width + 1)..width){

                            setColor(dto, color, x, y, index, tempVector3F.clear(), -(width + 1), -(height + 1))

                            if(y % 2 == 0){
                                addShape(shape1,
                                    (2.0) * x,
                                    (1.0) * y,
                                    color, colors, positions, indices)
                            } else {
                                addShape(shape1,
                                    (2.0) * (x + .5),
                                    (1.0) * (y + 0),
                                    color, colors, positions, indices)
                            }

                            //if(x % 2 == 0){


                            //setColor(dto, setting, color, x, (y - 1) * 2, index, tempVector3F.clear(), -(width + 1), -(height + 2) * 2)
                            //color.set(0f, 1f, 0f)
                            /*addShape(shape1,
                                (1.0 * 2.0) * (x + .5),
                                (1.0 * 2.0) * (y + .5),
                                color, colors, positions, indices)*/
                            //} else {
                                //setColor(dto, setting, color, x, y + 0, index, tempVector3F.clear(), -(width + 1), -(height + 1))
                                /*addShape(shape1,
                                    (1.0 * 2.0) * (x / 2) + 1,
                                    (1.0 * 2.0) * y + 1,
                                    color, colors, positions, indices)*/
                            //}



                            /*setting.colorSetting2.setHsl(color, x, y, index)
                            addShape(shape1, (1.0 * 2.0) * x + 1, (1.0 * 2.0) * y + 1,
                                color, colors, positions, indices)*/
                            index++
                        }
                    }
                }
                Shape.RECTANGLE -> {
                    width = (30.0).toInt()
                    height = (20.0).toInt()
                    points.add(Vector2D(1.0, 2.0))
                    points.add(Vector2D(2.0, 1.0))


                    points.add(Vector2D(0.0, -1.0))
                    points.add(Vector2D(-1.0, 0.0))
                    val shape1 = triangulate(points)
                    points.clear()
                    val ratio = 0.866
                    val ratioReverse = 1.0 - ratio

                    for(y in -(height + 1)..height){
                        for(x in -(width + 1)..width){
                            //setting.colorSetting.setHsl(color, x, y, index)
                            setColor(dto, color, x, y, index, tempVector3F.clear(), -(width + 1), -(height + 1))

                            addShape(shape1,
                                (2.0) * x,
                                (2.0) * y,
                                color, colors, positions, indices)
                            /*if(x % 2 == 0) {
                                addShape(shape1,
                                    (2.0) * x,
                                    (6.0) * y,
                                    color, colors, positions, indices)
                            } else{
                                addShape(shape1,
                                    (2.0) * (x + (2.0 / 6.0)),
                                    (6.0) * (y + (2.0 / 6.0)),
                                    color, colors, positions, indices)
                            }*/

                            index++
                        }
                    }
                }
                Shape.CUBE -> {
                    width = (21.0).toInt()
                    height = (69.0).toInt()
                    //genShape(points, 120, 0, 0, 90)
                    points.add(Vector2D(0.0, 0.5))
                    points.add(Vector2D(0.0, -0.5))
                    points.add(Vector2D(1.0, 0.0))
                    points.add(Vector2D(-1.0, 0.0))
                    val shape1 = triangulate(points)
                    points.clear()


                    //points.add(Vector2D(0.0, 1.0))
                    points.add(Vector2D(0.0, -0.5))
                    points.add(Vector2D(0.0, 0.5))
                    points.add(Vector2D(1.0, 0.0))
                    points.add(Vector2D(1.0, 1.0))
                    val shape2 = triangulate(points)
                    points.clear()


                    points.add(Vector2D(0.0, -0.5))
                    points.add(Vector2D(0.0, 0.5))
                    points.add(Vector2D(-1.0, 0.0))
                    points.add(Vector2D(-1.0, 1.0))
                    val shape3 = triangulate(points)
                    points.clear()

                    val ratio = 0.866
                    val ratioReverse = 1.0 - ratio

                    for(y in -(height + 0)..height){
                        for(x in -(width + 1)..width){
                            /*setColor(dto, color, x, y, index, tempVector3F.clear(), -(width + 1), -(height + 1))
                            addShape(shape1,
                                (1.0 * 2.0) * x +  + ((1.0) * (y % 2)), (0.75 * 2.0) * y,
                                color, colors, positions, indices)

                            setColor(dto, color, x, y, index, tempVector3F.set(0f, 0f, 10f), -(width + 1), -(height + 1))
                            addShape(shape2,
                                (1.0 * 2.0) * x +  + ((1.0) * (y % 2)), (0.75 * 2.0) * y - 1,
                                color, colors, positions, indices)

                            setColor(dto, color, x, y, index, tempVector3F.set(0f, 0f, -10f), -(width + 1), -(height + 1))
                            addShape(shape3,
                                (1.0 * 2.0) * x +  + ((1.0) * (y % 2)), (0.75 * 2.0) * y - 1,
                                color, colors, positions, indices)*/

                            //println("${(abs(-(height + 1)) + y) % 6} | ${(abs(-(height + 1)) + y) % 3}")

                            setColor(dto, color, x, y, index, tempVector3F.clear(), -(width + 1), -(height + 0))
                            if((abs(-(height + 0)) + y) % 3 == 0){
                                val offset = if((abs(-(height + 0)) + y) % 6 > 2){
                                    0.0
                                } else {
                                    1.0
                                }
                                addShape(shape1,
                                    (2.0) * x + offset,
                                    (0.5) * y,
                                    color, colors, positions, indices)
                            } else if((abs(-(height + 0)) + y) % 3 == 1){
                                val offset = if((abs(-(height + 0)) + y) % 6 > 2){
                                    1.0
                                } else {
                                    0.0
                                }
                                addShape(shape2,
                                    (2.0) * x + offset,
                                    (0.5) * y - 0.0,
                                    color, colors, positions, indices)
                            } else if((abs(-(height + 0)) + y) % 3 == 2){
                                val offset = if((abs(-(height + 0)) + y) % 6 > 2){
                                    1.0
                                } else {
                                    0.0
                                }
                                addShape(shape3,
                                    (2.0) * x + offset,
                                    (0.5) * y - 0.5,
                                    color, colors, positions, indices)
                            }


                            index++
                        }
                    }
                }
                Shape.TRIANGLE -> {
                    width = (38.0).toInt()
                    height = (24.0).toInt()
                    genShape(points, 120, 0, 0, 90)
                    val shape1 = triangulate(points)
                    points.clear()
                    genShape(points, 120, 0, 0, -90)
                    val shape2 = triangulate(points)
                    points.clear()

                    val ratio = 0.866
                    val ratioReverse = 1.0 - ratio

                    for(y in -(height + 1)..height){
                        for(x in -(width + 1)..width){
                            //setting.colorSetting.setHsl(color, x, y, index)
                            setColor(dto, color, x, y, index, tempVector3F.clear(), -(width + 1), -(height + 1))
                            if(x % 2 == 0){
                                addShape(shape1,
                                    ((1.0 - ratioReverse) * 2.0) * (x / 2),
                                    ((1.0 - ratioReverse * 2.0) * 2.0) * y,
                                    color, colors, positions, indices)
                            } else {
                                addShape(shape2,
                                    ((1.0 - ratioReverse) * 2.0) * (x / 2) + ratio,
                                    ((1.0 - ratioReverse * 2.0) * 2.0) * y - 1,
                                    color, colors, positions, indices)
                            }
                            /*setting.colorSetting1.setHsl(color, x, y, index)
                            addShape(shape2,
                                ((1.0 - ratioReverse) * 2.0) * x + ratio,
                                ((1.0 - ratioReverse * 2.0) * 2.0) * y - 1,
                                color, colors, positions, indices)*/
                            index++
                        }
                    }
                }
                Shape.HEXAGON -> {
                    width = (24.0).toInt()
                    height = (20.0).toInt()
                    genShape(points, 60, 0, 0)
                    val shape1 = triangulate(points)
                    points.clear()

                    val ratio = 0.866
                    val ratioReverse = 1.0 - ratio

                    for(y in -(height + 0)..height){
                        for(x in -(width + 0)..width){
                            //setting.colorSetting.setHsl(color, x, y, index)
                            setColor(dto, color, x, y, index, tempVector3F.clear(), -(width), -(height))

                            addShape(shape1,
                                ((1.0 - ratioReverse * 2.0) * 2.0) * x,
                                ((ratio * 2.0) * y) + ((ratio) * ((width + x) % 2)),
                                color, colors, positions, indices)
                            index++
                        }
                    }
                }
                Shape.OCTAGON -> {
                    width = (18.0).toInt()
                    height = (18.0).toInt()
                    genShape(points, 45, 0, 0)
                    val shape1 = triangulate(points)
                    points.clear()
                    val shape2 = genStar(points)
                    points.clear()

                    val colorsInUse = dto.fixedColors.filter { it.inUse }
                    val colorsSize = colorsInUse.size

                    for(y in -(height + 0)..height){
                        for(x in -(width + 0)..width){
                            if(colorsSize == 2){
                                setColor(dto, color, colorsInUse[0], tempVector3F.clear())
                            } else {
                                setColor(dto, color, x, y, index, tempVector3F.clear(), -(width + 0), -(height + 0))
                            }

                            addShape(shape1,
                                (1.0 * 2.0) * (x),
                                (2.0) * y,
                                color, colors, positions, indices)

                            if(colorsSize == 3){
                                setColor(dto, color, x, y + 2, index, tempVector3F.clear(), -(width + 0), -(height + 0))
                            } else if(colorsSize == 2){
                                setColor(dto, color, colorsInUse[1], tempVector3F.clear())
                            } else {
                                setColor(dto, color, x, y + colorsSize / 2, index, tempVector3F.clear(), -(width + 0), -(height + 0))
                            }
                            addShape(shape2,
                                (1.0 * 2.0) * (x) + 1,
                                (2.0) * (y + 0.5),
                                color, colors, positions, indices)

                            index++
                        }
                    }
                }
            }




            return res
        }


        private fun addShape(src: ArrayList<Triangle2D>,
                             offsetX: Double, offsetY: Double, color: Vector3F, colors: ArrayList<Double>,
                             positions: ArrayList<Double>,
                             indices: ArrayList<Int>){
            val temp = ArrayList<Triangle2D>()
            addTriangles(src, temp, offsetX, offsetY)
            //println(color)

            for(triangle in temp.withIndex()){
                val t = triangle.value
                addToArray(positions, t.a.x, t.a.y, t.b.x, t.b.y, t.c.x, t.c.y)

                //val color2 = Colors.setHsl(Vector3F(), Random.nextFloat() * 360f, 50f, 50f)
                //val c = arrayOf(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())

                repeat(3) {
                    addColorToArray(colors, color)
                    //addToArray(colors, color.x.toDouble(), color.y.toDouble(), color.z.toDouble(), 1.0)
                    //addToArray(colors, c[0], c[1], c[2], 1.0)
                }
                val last = indices.lastOrNull() ?: -1
                addToArray(indices, last + 1, last + 2, last + 3)
            }
        }

        private fun addTriangles(src: ArrayList<Triangle2D>, target: ArrayList<Triangle2D>, offsetX: Double, offsetY: Double){
            for(t in src){
                val new = Triangle2D(Vector2D(t.a.x, t.a.y), Vector2D(t.b.x, t.b.y), Vector2D(t.c.x, t.c.y)).apply {
                    a.x += offsetX
                    b.x += offsetX
                    c.x += offsetX
                    a.y += offsetY
                    b.y += offsetY
                    c.y += offsetY
                }
                target.add(new)
            }
        }

        private fun genShape(points: ArrayList<Vector2D>, step: Int, offsetX: Int, offsetY: Int){
            genShape(points, step, offsetX, offsetY, 0)
        }

        private fun genShape(points: ArrayList<Vector2D>, step: Int, offsetX: Int, offsetY: Int, start: Int){
            for(i in start until 360 step step){
                val angle = i * Vector3F.DEG_TO_RAD
                points.add(Vector2D(offsetX + cos(angle), offsetY + sin(angle)))
            }
        }

        private fun genStar(points: ArrayList<Vector2D>): ArrayList<Triangle2D>{
            points.add(Vector2D(0.0, 1.0))
            points.add(Vector2D(0.0, -1.0))
            points.add(Vector2D(1.0, 0.0))
            points.add(Vector2D(-1.0, 0.0))

            val size = 1 - cos(45.0 * Vector3F.DEG_TO_RAD)

            points.add(Vector2D(-size, -size))
            points.add(Vector2D(size, -size))
            points.add(Vector2D(size, size))
            points.add(Vector2D(-size, size))

            val res = ArrayList<Triangle2D>()

            res.add(Triangle2D(points[4], points[5], points[6]))
            res.add(Triangle2D(points[4], points[6], points[7]))

            res.add(Triangle2D(points[7], points[6], points[0]))
            res.add(Triangle2D(points[4], points[5], points[1]))
            res.add(Triangle2D(points[5], points[6], points[2]))
            res.add(Triangle2D(points[4], points[7], points[3]))
            return res
        }

        private fun triangulate(points: ArrayList<Vector2D>): ArrayList<Triangle2D>{
            val triangulator = DelaunayTriangulator(points)
            triangulator.triangulate()
            val res = ArrayList<Triangle2D>()
            res.addAll(triangulator.triangles)
            return res
        }

        private fun addToArray(arr: ArrayList<Int>, vararg values: Int){
            for(v in values) arr.add(v)
        }

        private fun addToArray(arr: ArrayList<Double>, vararg values: Double){
            for(v in values) arr.add(v)
        }

        private fun addColorToArray(arr: ArrayList<Double>, color: Vector3F){
            arr.add(color.x.toDouble())
            arr.add(color.y.toDouble())
            arr.add(color.z.toDouble())
            arr.add(1.0)
        }

        private fun setColor(dto: FormSettingsDto, color: Vector3F, x: Int, y: Int, index: Int,
                             additionalHSL: Vector3F, xMin: Int, yMin: Int){
            val fixedColors = dto.fixedColors.filter { it.inUse }
            val size = fixedColors.size
            if(size > 0){
                val xIndex = ((abs(yMin) + y)) % size
                //println("$x $size | $cindex")
                val fixedColor = fixedColors[xIndex]
                setColor(dto, color, fixedColor, additionalHSL)
            } else {
                //setting.colorSetting.setHsl(color, x, y, index)
                /*Colors.setHsl(color,
                    (setting.colorSetting.hue.getValue(x, y, index) + additionalHSL.x).coerceAtMost(360f),
                    (setting.colorSetting.saturation.getValue(x, y, index) + additionalHSL.y).coerceAtMost(100f),
                    (setting.colorSetting.lightness.getValue(x, y, index) + additionalHSL.z).coerceAtMost(100f))*/
            }
        }

        private fun setColor(dto: FormSettingsDto, res: Vector3F, color: FixedColor, additionalHSL: Vector3F){
            if(dto.colorSetting == 0){
                res.set(color.hue / 255f, color.saturation / 255f, color.lightness / 255f)
                Colors.rgbToHsl(res)
                res.set(res.x * 360f, res.y * 100f, res.z * 100f)
                Colors.setHsl(res,
                    (res.x + additionalHSL.x).coerceAtMost(360f),
                    (res.y + additionalHSL.y).coerceAtMost(100f),
                    (res.z + additionalHSL.z).coerceAtMost(100f))
            } else {
                Colors.setHsl(res,
                    (color.hue + additionalHSL.x).coerceAtMost(360f),
                    (color.saturation + additionalHSL.y).coerceAtMost(100f),
                    (color.lightness + additionalHSL.z).coerceAtMost(100f))
            }
        }
    }

    fun stride(): Int{
        return vertices.keys.sumOf { it.size }
    }

}