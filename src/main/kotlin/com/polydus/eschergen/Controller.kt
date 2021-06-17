package com.polydus.eschergen

import com.polydus.eschergen.d3.Mesh
import com.polydus.eschergen.form.FormSettingsDto
import com.polydus.eschergen.util.Strings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse


@Controller
@SessionAttributes("formSettingsDto")
class Controller {

    @Autowired
    private lateinit var strings: Strings

    @Autowired
    lateinit var applicationContext: ApplicationContext//? = null
    private var shaderSourcesVert: HashMap<String, String>? = null
    private var shaderSourcesFrag: HashMap<String, String>? = null

    @PostConstruct
    fun init(){

    }

    @GetMapping("/")
    fun getIndex(model: Model,
                 @ModelAttribute("formSettingsDto") formSettingsDto: FormSettingsDto,
                 @CookieValue("lang") lang: String?): String {
        setModel(model, formSettingsDto, lang ?: "en")

        return "index"
    }

    @GetMapping("/reset")
    fun reset(model: Model,
              @ModelAttribute("formSettingsDto") formSettingsDto: FormSettingsDto,
              @CookieValue("lang") lang: String?): String{
        formSettingsDto.reset()

        return "redirect:/"
    }

    @GetMapping("/setlang")
    fun setCookie(model: Model, @RequestParam(value="lang") lang: String?, response: HttpServletResponse): String {
        val c = Cookie("lang", lang)
        c.apply {
            maxAge = (7 * 24 * 60 * 60)
            path = "/"
            isHttpOnly = true
            secure = true
        }
        response.addCookie(c)
        return "redirect:/"
    }

    @PostMapping("/colors")
    fun set(
        @ModelAttribute("formSettingsDto")
        fixedColorDto: FormSettingsDto, model: Model,
        @RequestParam(value="lang") lang: String?): RedirectView {

        setModel(model, fixedColorDto, lang ?: "en")

        return RedirectView("/")
    }

    private fun setModel(model: Model, dto: FormSettingsDto, lang: String){
        strings.addAllStringsToModel(model, lang)

        if(shaderSourcesFrag == null || shaderSourcesVert == null) loadShaders()
        model.addAttribute("shaderSourcesVert", shaderSourcesVert)
        model.addAttribute("shaderSourcesFrag", shaderSourcesFrag)

        model.addAttribute("shaderName", "basic")

        val mesh = Mesh.genEscherRectangle(dto)

        model.addAttribute("uvMeshes", mesh)
        model.addAttribute("formSettingsDto", dto)
        model.addAttribute("lang", lang)
    }

    private fun loadShaders(){
        val shaders = applicationContext.getResources("classpath:/shader/*")
        val mapFrag = HashMap<String, String>()
        val mapVert = HashMap<String, String>()
        for(s in shaders){
            val input = s.inputStream
            val name = s.filename ?: ""
            if(name.endsWith("frag")){
                mapFrag[name.substring(0, name.length - 5)] = String(input.readAllBytes())
            } else if(name.endsWith("vert")){
                mapVert[name.substring(0, name.length - 5)] = String(input.readAllBytes())
            }
        }
        shaderSourcesFrag = mapFrag
        shaderSourcesVert = mapVert
    }


    @ModelAttribute("formSettingsDto")
    fun emptyFormSettingsDto(): FormSettingsDto {
        return FormSettingsDto().apply {
            init()
        }
    }

}