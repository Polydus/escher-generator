package com.polydus.eschergen.form

import com.polydus.eschergen.d3.Shape
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("session")
class FormSettingsDto() {

    var fixedColors: ArrayList<FixedColor> = arrayListOf()

    var colorSetting = 1

    var shape = Shape.values().first()

    var serviceID = 0L

    fun init(){
        repeat(10){
            addColor(FixedColor())
        }

        reset()
    }

    fun reset(){
        for(c in fixedColors){
            c.reset()
        }
        colorSetting = 1
        getColors()[0].hue = 0f
        getColors()[1].hue = 0f
        getColors()[2].hue = 0f

        getColors()[3].hue = 120f
        getColors()[4].hue = 240f
        getColors()[5].hue = 180f
        getColors()[6].hue = 300f
        getColors()[7].hue = 60f
        getColors()[8].hue = 150f
        getColors()[9].hue = 210f

        getColors()[1].lightness = 40f
        getColors()[2].lightness = 60f

        getColors()[0].inUse = true
        getColors()[1].inUse = true
        getColors()[2].inUse = true
    }

    fun addColor(color: FixedColor){
        fixedColors.add(color)
    }

    fun setColors(fixedColors: ArrayList<FixedColor>){
        this.fixedColors = fixedColors
    }

    fun getColors(): List<FixedColor>{
        return fixedColors
    }

    override fun toString(): String {
        return "FormSettingsDto(fixedColors=$fixedColors, colorSetting=$colorSetting, shape=$shape, serviceID=$serviceID)"
    }

}