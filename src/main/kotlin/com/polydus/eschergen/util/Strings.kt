package com.polydus.eschergen.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.ui.Model
import javax.annotation.PostConstruct

@Component
class Strings() {

    @Value("classpath:/string/en/strings_index.json")
    lateinit var resourceFileEN: Resource
    @Value("classpath:/string/nl/strings_index.json")
    lateinit var resourceFileNL: Resource

    lateinit var stringsEN: HashMap<String, String>
    lateinit var stringsNL: HashMap<String, String>

    @PostConstruct
    fun init(){
        val json = Json {  }
        var input = resourceFileEN.inputStream
        var string = String(input.readAllBytes())

        stringsEN = json.decodeFromString<HashMap<String, String>>(string)

        input = resourceFileNL.inputStream
        string = String(input.readAllBytes())
        stringsNL = json.decodeFromString<HashMap<String, String>>(string)
    }

    fun addAllStringsToModel(model: Model, lang: String){
        if(lang == "nl"){
            model.addAttribute("strings", stringsNL)
        } else {
            model.addAttribute("strings", stringsEN)
        }
    }

}