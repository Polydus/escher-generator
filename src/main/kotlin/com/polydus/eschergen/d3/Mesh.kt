package com.polydus.eschergen.d3

import com.polydus.eschergen.form.FixedColor
import com.polydus.eschergen.form.FormSettingsDto
import com.polydus.eschergen.math.Vector3F
import io.github.jdiemke.triangulation.DelaunayTriangulator
import io.github.jdiemke.triangulation.Triangle2D
import io.github.jdiemke.triangulation.Vector2D
import org.poly2tri.Poly2Tri
import org.poly2tri.geometry.polygon.Polygon
import org.poly2tri.geometry.polygon.PolygonPoint
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Mesh {

    var vertices = HashMap<Shader.Attribute, ArrayList<Double>>()
    var indices = ArrayList<Int>()

    var attributes = ArrayList<Shader.Attribute>()

    //val shapes = HashMap<Shape, ArrayList<ArrayList<Vector3F>>>()

    init {

    }

    companion object{

        private val ratio = 0.866
        private val ratioReverse = 1.0 - ratio

        fun genEscherRectangle(/*setting: EscherSettings, */dto: FormSettingsDto): List<Mesh> {
            val meshes = ArrayList<Mesh>()
            //var currentMesh = Mesh()

            //val positionVertices = currentMesh.vertices[Shader.Attribute.POSITION_2D]!!
            //val colorVertices = currentMesh.vertices[Shader.Attribute.COLOR]!!
            //val indices = currentMesh.indices

            val positionVertices = arrayListOf<Double>()
            val colorVertices = arrayListOf<Double>()
            val indices = arrayListOf<Int>()
            buildNewMesh(null, meshes, positionVertices, colorVertices, indices)

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
                    width = (10).toInt()
                    height = (10).toInt()

                    points.clear()
                    points.addAll(listOf(
                        Vector2D(0 / 100.0, 0 / 100.0),
                        Vector2D(15 / 100.0, 0 / 100.0),
                        Vector2D(31 / 100.0, 8 / 100.0),
                        Vector2D(39 / 100.0, 23 / 100.0),
                        Vector2D(58 / 100.0, 24 / 100.0),
                        Vector2D(63 / 100.0, 31 / 100.0),
                        Vector2D(47 / 100.0, 31 / 100.0),
                        Vector2D(47 / 100.0, 35 / 100.0),
                        Vector2D(51 / 100.0, 39 / 100.0),
                        Vector2D(47 / 100.0, 47 / 100.0),
                        Vector2D(31 / 100.0, 48 / 100.0),
                        Vector2D(23 / 100.0, 63 / 100.0),
                        Vector2D(8 / 100.0, 63 / 100.0),
                        Vector2D(16 / 100.0, 39 / 100.0),
                        Vector2D(4 / 100.0, 39 / 100.0),
                        Vector2D(0 / 100.0, 35 / 100.0),
                        Vector2D(0 / 100.0, 32 / 100.0),
                        Vector2D(16 / 100.0, 32 / 100.0),
                        Vector2D(9 / 100.0, 23 / 100.0),
                        Vector2D(0 / 100.0, 6 / 100.0),


                        ))

                    val shape3 = triangulate(points)
                    points.clear()

                    points.addAll(listOf(
                        Vector2D((100 - 0) / 100.0, 0 / 100.0),
                        Vector2D((100 - 15) / 100.0, 0 / 100.0),
                        Vector2D((100 - 31) / 100.0, 8 / 100.0),
                        Vector2D((100 - 39) / 100.0, 23 / 100.0),
                        Vector2D((100 - 58) / 100.0, 24 / 100.0),
                        Vector2D((100 - 63) / 100.0, 31 / 100.0),
                        Vector2D((100 - 47) / 100.0, 31 / 100.0),
                        Vector2D((100 - 47) / 100.0, 35 / 100.0),
                        Vector2D((100 - 51) / 100.0, 39 / 100.0),
                        Vector2D((100 - 47) / 100.0, 47 / 100.0),
                        Vector2D((100 - 31) / 100.0, 48 / 100.0),
                        Vector2D((100 - 23) / 100.0, 63 / 100.0),
                        Vector2D((100 - 8) / 100.0, 63 / 100.0),
                        Vector2D((100 - 16) / 100.0, 39 / 100.0),
                        Vector2D((100 - 4) / 100.0, 39 / 100.0),
                        Vector2D((100 - 0) / 100.0, 35 / 100.0),
                        Vector2D((100 - 0) / 100.0, 32 / 100.0),
                        Vector2D((100 - 16) / 100.0, 32 / 100.0),
                        Vector2D((100 - 9) / 100.0, 23 / 100.0),
                        Vector2D((100 - 0) / 100.0, 6 / 100.0),

                    ))

                    val shape4 = triangulate(points)
                    points.clear()

                    for(y in -(height)..height){
                        for(x in -(width)..width) {
                            setColor(dto, color, x, y, index, tempVector3F.clear(), -(width), -(height))

                            if(y % 2 == 0){
                                addShape(shape4,
                                    (0.5) * x,
                                    (0.8) * y,
                                    color, colorVertices, positionVertices, indices)
                            } else {
                                addShape(shape3,
                                    (0.5) * x,
                                    (0.8) * y,
                                    color, colorVertices, positionVertices, indices)
                            }
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

                    for(y in -(height + 1)..height){
                        for(x in -(width + 1)..width){

                            setColor(dto, color, x, y, index, tempVector3F.clear(), -(width + 1), -(height + 1))

                            if(y % 2 == 0){
                                addShape(shape1,
                                    (2.0) * x,
                                    (1.0) * y,
                                    color, colorVertices, positionVertices, indices)
                            } else {
                                addShape(shape1,
                                    (2.0) * (x + .5),
                                    (1.0) * (y + 0),
                                    color, colorVertices, positionVertices, indices)
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

                    for(y in -(height + 1)..height){
                        for(x in -(width + 1)..width){
                            //setting.colorSetting.setHsl(color, x, y, index)
                            setColor(dto, color, x, y, index, tempVector3F.clear(), -(width + 1), -(height + 1))

                            addShape(shape1,
                                (2.0) * x,
                                (2.0) * y,
                                color, colorVertices, positionVertices, indices)
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
                                    color, colorVertices, positionVertices, indices)
                            } else if((abs(-(height + 0)) + y) % 3 == 1){
                                val offset = if((abs(-(height + 0)) + y) % 6 > 2){
                                    1.0
                                } else {
                                    0.0
                                }
                                addShape(shape3,
                                    (2.0) * x + offset,
                                    (0.5) * y - 0.0,
                                    color, colorVertices, positionVertices, indices)
                            } else if((abs(-(height + 0)) + y) % 3 == 2){
                                val offset = if((abs(-(height + 0)) + y) % 6 > 2){
                                    1.0
                                } else {
                                    0.0
                                }
                                addShape(shape2,
                                    (2.0) * x + offset,
                                    (0.5) * y - 0.5,
                                    color, colorVertices, positionVertices, indices)
                            }

                            index++
                        }
                    }
                }
                Shape.CUBE_HOLLOW -> {
                    height = 20
                    width = 17
                    val topOuter = genHollowRect(points, 0.0, 0)
                    val topInnerLeft = genHollowRect(points, 0.0, 1)
                    val topInnerRight = genHollowRect(points, 0.0, 2)

                    val leftOuter = genHollowRect(points, -120.0, 0)
                    val leftInnerLeft = genHollowRect(points, -120.0, 1)
                    val leftInnerRight = genHollowRect(points, -120.0, 2)

                    val rightOuter = genHollowRect(points, 120.0, 0)
                    val rightInnerLeft = genHollowRect(points, 120.0, 1)
                    val rightInnerRight = genHollowRect(points, 120.0, 2)

                    val color0 = Vector3F()
                    val color1 = Vector3F()
                    val color2 = Vector3F()

                    for(y in -(height)..height){
                        for(x in -(width)..width){
                            setColor(dto, color0, x, (y * 3) + 0, index, tempVector3F.clear(), -(width), -(height * 3))
                            setColor(dto, color1, x, (y * 3) + 1, index, tempVector3F.clear(), -(width), -(height * 3))
                            setColor(dto, color2, x, (y * 3) + 2, index, tempVector3F.clear(), -(width), -(height * 3))

                            val xPos = if(y % 2 == 0){
                                x * 2.0
                            } else {
                                x * 2.0 + 1.0
                            }

                            drawHollowCube(topOuter, topInnerLeft, topInnerRight,
                                leftOuter, leftInnerLeft, leftInnerRight,
                                rightOuter, rightInnerLeft, rightInnerRight,
                                color, colorVertices, positionVertices, indices, color0, color1, color2,
                                xPos, y * 1.73)

                            if(indices.size > 30000) buildNewMesh(meshes.last(), meshes, positionVertices, colorVertices, indices)
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

                    for(y in -(height + 1)..height){
                        for(x in -(width + 1)..width){
                            //setting.colorSetting.setHsl(color, x, y, index)
                            setColor(dto, color, x, y, index, tempVector3F.clear(), -(width + 1), -(height + 1))
                            if(x % 2 == 0){
                                addShape(shape1,
                                    ((1.0 - ratioReverse) * 2.0) * (x / 2),
                                    ((1.0 - ratioReverse * 2.0) * 2.0) * y,
                                    color, colorVertices, positionVertices, indices)
                            } else {
                                addShape(shape2,
                                    ((1.0 - ratioReverse) * 2.0) * (x / 2) + ratio,
                                    ((1.0 - ratioReverse * 2.0) * 2.0) * y - 1,
                                    color, colorVertices, positionVertices, indices)
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
                    for(y in -(height + 0)..height){
                        for(x in -(width + 0)..width){
                            //setting.colorSetting.setHsl(color, x, y, index)
                            setColor(dto, color, x, y, index, tempVector3F.clear(), -(width), -(height))

                            addShape(shape1,
                                ((1.0 - ratioReverse * 2.0) * 2.0) * x,
                                ((ratio * 2.0) * y) + ((ratio) * ((width + x) % 2)),
                                color, colorVertices, positionVertices, indices)
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
                                color, colorVertices, positionVertices, indices)

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
                                color, colorVertices, positionVertices, indices)

                            index++
                        }
                    }
                }
            }

            //println("${currentMesh.indices.size} | ${positionVertices.size}")
            if(indices.isNotEmpty()){
                setMesh(meshes.last(), positionVertices, colorVertices, indices)
            }

            return meshes
        }

        private fun setMesh(mesh: Mesh, positionVertices: ArrayList<Double>,
                      colorVertices: ArrayList<Double>,
                      indices: ArrayList<Int>){
            mesh.indices.addAll(indices)
            mesh.vertices[Shader.Attribute.POSITION_2D]?.addAll(positionVertices)
            mesh.vertices[Shader.Attribute.COLOR]?.addAll(colorVertices)
        }

        private fun buildNewMesh(oldMesh: Mesh?, meshes: ArrayList<Mesh>,
                                 positionVertices: ArrayList<Double>,
                                 colorVertices: ArrayList<Double>,
                                 indices: ArrayList<Int>){
            if(oldMesh != null){
                setMesh(oldMesh, positionVertices, colorVertices, indices)
            }

            val new = Mesh()
            new.apply {
                vertices[Shader.Attribute.POSITION_2D] = ArrayList()
                vertices[Shader.Attribute.COLOR] = ArrayList()
            }
            positionVertices.clear()
            positionVertices.addAll(new.vertices[Shader.Attribute.POSITION_2D]!!)
            colorVertices.clear()
            colorVertices.addAll(new.vertices[Shader.Attribute.COLOR]!!)
            indices.clear()
            indices.addAll(new.indices)

            meshes.add(new)
        }

        private fun drawHollowCube(
            topOuter: ArrayList<Triangle2D>, topInnerLeft: ArrayList<Triangle2D>, topInnerRight: ArrayList<Triangle2D>,
            leftOuter: ArrayList<Triangle2D>, leftInnerLeft: ArrayList<Triangle2D>, leftInnerRight: ArrayList<Triangle2D>,
            rightOuter: ArrayList<Triangle2D>, rightInnerLeft: ArrayList<Triangle2D>, rightInnerRight: ArrayList<Triangle2D>,
            color: Vector3F, colors: ArrayList<Double>,
            positions: ArrayList<Double>,
            indices: ArrayList<Int>, color0: Vector3F, color1: Vector3F, color2: Vector3F, x: Double, y: Double){
            color.set(color0)
            addShape(topOuter, x + (0.0), y + (0.0),
                color, colors, positions, indices)
            color.set(color1)
            addShape(topInnerLeft, x + (0.0), y + (0.0),
                color, colors, positions, indices)
            color.set(color2)
            addShape(topInnerRight, x + (0.0), y + (0.0),
                color, colors, positions, indices)

            color.set(color2)
            addShape(leftOuter, x + (0.5), y + (-ratio),
                color, colors, positions, indices)
            color.set(color0)
            addShape(leftInnerLeft, x + (0.5), y + (-ratio),
                color, colors, positions, indices)
            color.set(color1)
            addShape(leftInnerRight, x + (0.5), y + (-ratio),
                color, colors, positions, indices)

            color.set(color1)
            addShape(rightOuter, x + (-0.5), y + (-ratio),
                color, colors, positions, indices)
            color.set(color2)
            addShape(rightInnerLeft, x + (-0.5), y + (-ratio),
                color, colors, positions, indices)
            color.set(color0)
            addShape(rightInnerRight, x + (-0.5), y + (-ratio),
                color, colors, positions, indices)
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

        private fun genHollowRect(points: ArrayList<Vector2D>, angle: Double, type: Int): ArrayList<Triangle2D>{
            val cos = cos(angle * Vector3F.DEG_TO_RAD)
            val sin = sin(angle * Vector3F.DEG_TO_RAD)
            val ratio = 0.58//115.0 / 200.0
            points.clear()
            points.addAll(
                listOf(
                    Vector2D(0.0, ratio),
                    Vector2D(0.0, -ratio),
                    Vector2D(1.0, 0.0),
                    Vector2D(-1.0, 0.0),

                    Vector2D(0.0, (ratio / 2.0)),
                    Vector2D(0.0, -(ratio / 2.0)),
                    Vector2D(0.5, 0.0),
                    Vector2D(-0.5, 0.0),
                )
            )
            points.forEach {
                val x = it.x * cos - it.y * sin
                val y = it.x * sin + it.y * cos
                it.x = x
                it.y = y
            }

            val res = ArrayList<Triangle2D>()
            /*
            * 0: outer ring
            * 1: inner left
            * 2: inner right
            *
            * */
            if(type == 0){
                res.add(Triangle2D(points[0], points[6], points[2]))
                res.add(Triangle2D(points[0], points[7], points[3]))
                res.add(Triangle2D(points[1], points[6], points[2]))
                res.add(Triangle2D(points[1], points[7], points[3]))

                res.add(Triangle2D(points[0], points[4], points[6]))
                res.add(Triangle2D(points[0], points[4], points[7]))
                res.add(Triangle2D(points[1], points[5], points[6]))
                res.add(Triangle2D(points[1], points[5], points[7]))
            } else if(type == 1){
                res.add(Triangle2D(points[4], points[5], points[6]))
            } else if(type == 2){
                res.add(Triangle2D(points[4], points[5], points[7]))
            }

            return res
        }


        private fun triangulate(points: ArrayList<Vector2D>): ArrayList<Triangle2D>{
            val res = ArrayList<Triangle2D>()

            try {
                val set = Polygon(points.map { PolygonPoint(it.x, it.y) })
                Poly2Tri.triangulate(set)
                val triangles = set.triangles
                triangles.forEach {
                    res.add(Triangle2D(
                        Vector2D(it.points[0].x, it.points[0].y),
                        Vector2D(it.points[1].x, it.points[1].y),
                        Vector2D(it.points[2].x, it.points[2].y)
                    ))
                }
            } catch (e: Exception){
                val triangulator = DelaunayTriangulator(points)
                triangulator.triangulate()
                res.addAll(triangulator.triangles)
            }

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

/*
*
*                     points.addAll(listOf(
                        Vector2D(-8 / 10.0, -8 / 10.0),
                        Vector2D(-4 / 10.0, -8 / 10.0),
                        Vector2D(-4 / 10.0, -6 / 10.0),
                        Vector2D(-0 / 10.0, -6 / 10.0),

                        Vector2D(-0 / 10.0, -4 / 10.0),
                        Vector2D(2 / 10.0, -4 / 10.0),
                        Vector2D(2 / 10.0, -2 / 10.0),

                        Vector2D(-2 / 10.0, -2 / 10.0),
                        Vector2D(-2 / 10.0, -1 / 10.0),
                        Vector2D(-5 / 10.0, -1 / 10.0),
                        Vector2D(-5 / 10.0, -2 / 10.0),
                        Vector2D(-6 / 10.0, -2 / 10.0),
                        Vector2D(-6 / 10.0, -6 / 10.0),
                        Vector2D(-8 / 10.0, -6 / 10.0),
                        ))

                    val shape1 = triangulate(points)
                    points.clear()

                    points.addAll(listOf(
                        Vector2D(-8 / 10.0, 0 / 10.0),
                        Vector2D(-4 / 10.0, 0 / 10.0),
                        Vector2D(-4 / 10.0, -1 / 10.0),
                        Vector2D(-2 / 10.0, -1 / 10.0),
                        Vector2D(-2 / 10.0, -2 / 10.0),

                        Vector2D(7 / 10.0, -2 / 10.0),
                        Vector2D(7 / 10.0, -1 / 10.0),
                        Vector2D(8 / 10.0, -1 / 10.0),
                        Vector2D(8 / 10.0, 0 / 10.0),
                        Vector2D(4 / 10.0, 0 / 10.0),
                        Vector2D(4 / 10.0, 1 / 10.0),
                        Vector2D(2 / 10.0, 1 / 10.0),
                        Vector2D(2 / 10.0, 2 / 10.0),

                        Vector2D(-7 / 10.0, 2 / 10.0),
                        Vector2D(-7 / 10.0, 1 / 10.0),
                        Vector2D(-8 / 10.0, 1 / 10.0),

                        ))

                    val shape2 = triangulate(points)

                    points.clear()
                    points.addAll(listOf(
                        Vector2D(2 / 100.0, 0 / 100.0),
                        Vector2D(7 / 100.0, 0 / 100.0),
                        Vector2D(22 / 100.0, 9 / 100.0),
                        Vector2D(34 / 100.0, 6 / 100.0),
                        Vector2D(45 / 100.0, 8 / 100.0),
                        Vector2D(55 / 100.0, 26 / 100.0),
                        Vector2D(77 / 100.0, 33 / 100.0),
                        Vector2D(95 / 100.0, 34 / 100.0),
                        Vector2D(82 / 100.0, 41 / 100.0),
                        Vector2D(77 / 100.0, 42 / 100.0),
                        Vector2D(67 / 100.0, 41 / 100.0),
                        Vector2D(55 / 100.0, 43 / 100.0),
                        Vector2D(44 / 100.0, 60 / 100.0),
                        Vector2D(37 / 100.0, 62 / 100.0),
                        Vector2D(12 / 100.0, 67 / 100.0),

                        Vector2D(5 / 100.0, 66 / 100.0),
                        Vector2D(12 / 100.0, 67 / 100.0),
                        Vector2D(15 / 100.0, 59 / 100.0),
                        Vector2D(23 / 100.0, 51 / 100.0),
                        Vector2D(23 / 100.0, 47 / 100.0),
                        Vector2D(0 / 100.0, 47 / 100.0),
                        Vector2D(12 / 100.0, 34 / 100.0),
                        Vector2D(24 / 100.0, 27 / 100.0),
                        Vector2D(9 / 100.0, 14 / 100.0),
                        Vector2D(0 / 100.0, 2 / 100.0),
                        ))

                    val shape3 = triangulate(points)
                    points.clear()

                    points.addAll(listOf(
                        Vector2D((100 - 2) / 100.0,0 / 100.0),
                        Vector2D((100 - 7) / 100.0,0 / 100.0),
                        Vector2D((100 - 22) / 100.0,9 / 100.0),
                        Vector2D((100 - 34) / 100.0,6 / 100.0),
                        Vector2D((100 - 45) / 100.0,8 / 100.0),
                        Vector2D((100 - 55) / 100.0,26 / 100.0),
                        Vector2D((100 - 77) / 100.0,33 / 100.0),
                        Vector2D((100 - 95) / 100.0,34 / 100.0),
                        Vector2D((100 - 82) / 100.0,41 / 100.0),
                        Vector2D((100 - 77) / 100.0,42 / 100.0),
                        Vector2D((100 - 67) / 100.0,41 / 100.0),
                        Vector2D((100 - 55) / 100.0,43 / 100.0),
                        Vector2D((100 - 44) / 100.0,60 / 100.0),
                        Vector2D((100 - 37) / 100.0,62 / 100.0),
                        Vector2D((100 - 12) / 100.0,67 / 100.0),

                        Vector2D((100 - 5) / 100.0,66 / 100.0),
                        Vector2D((100 - 12) / 100.0,67 / 100.0),
                        Vector2D((100 - 15) / 100.0,59 / 100.0),
                        Vector2D((100 - 23) / 100.0,51 / 100.0),
                        Vector2D((100 - 23) / 100.0,47 / 100.0),
                        Vector2D((100 - 0) / 100.0,47 / 100.0),
                        Vector2D((100 - 12) / 100.0,34 / 100.0),
                        Vector2D((100 - 24) / 100.0,27 / 100.0),
                        Vector2D((100 - 9) / 100.0,14 / 100.0),
                        Vector2D((100 - 0) / 100.0,2 / 100.0),
                    ))

* */