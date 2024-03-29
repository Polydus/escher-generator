declare var mat4: any;
declare var bootstrap: any;
declare var context: any;

var _ = undefined;
var degToRad = Math.PI / 180.0;
var radToDeg = 180.0 / Math.PI;

var imgFolder = "./img/";
var texturePaths = {
    logo: "logo_64x64.png",
    tile: "tile.png"
}

var keysToCatch = {
    /*ArrowUp: ["ArrowUp", "w"],
    ArrowDown: ["ArrowDown", "s"],
    ArrowLeft: ["ArrowLeft", "a"],
    ArrowRight: ["ArrowRight", "d"],*/

    /*ArrowUp: ["w"],
    ArrowDown: ["s"],
    ArrowLeft: ["a"],
    ArrowRight: ["d"],

    keyR : ["r"]*/
}

const LOG = true;

function log(string: string){
    if(LOG) console.log(string);
}

function randInt(min, max): number {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

class Main {

    private canvas: HTMLCanvasElement = window.document.getElementById("canvas") as HTMLCanvasElement;
    private gl: GL = new GL(this.canvas);
    private cam: Camera = new Camera(this.canvas);

    private saveCanvasButton: HTMLElement = window.document.getElementById("save-image-button");

    private angleLeftButton: HTMLElement = window.document.getElementById("angle-left-button");
    private angleRightButton: HTMLElement = window.document.getElementById("angle-right-button");
    private angleLeftDoubleButton: HTMLElement = window.document.getElementById("angle-left-double-button");
    private angleRightDoubleButton: HTMLElement = window.document.getElementById("angle-right-double-button");
    private angleDesc: HTMLElement = window.document.getElementById("angle-desc");

    private scaleLeftButton: HTMLElement = window.document.getElementById("scale-left-button");
    private scaleRightButton: HTMLElement = window.document.getElementById("scale-right-button");
    private scaleLeftDoubleButton: HTMLElement = window.document.getElementById("scale-left-double-button");
    private scaleRightDoubleButton: HTMLElement = window.document.getElementById("scale-right-double-button");
    private scaleDesc: HTMLElement = window.document.getElementById("scale-desc");

    //private noise = new SimplexNoise();

    private shader = new Shader(context.shaderName);
    private objs: Drawable[] = [];

    private lastTime = 0;
    private lastTimeTick = 0;

    private scale = 1.00;

    constructor(){
        this.canvas.addEventListener('mousedown', (event) => {
            const rect = this.canvas.getBoundingClientRect();
            this.onClick(event.clientX - rect.left, event.clientY - rect.top);
        });

        window.addEventListener("resize", (event) => {
            this.cam.updateCamera();
            log(this.canvas.clientWidth + " " + this.canvas.clientHeight);
        });
        window.addEventListener("focus", (event) => {
            //this.cam.update();
            console.log(event);
        });

        document.addEventListener('keydown', (event) => {
            var key = event.key;
            var keysArrays = Object.keys(keysToCatch);
            for(var i = 0; i < keysArrays.length; i++){
                if(keysArrays[i].includes(key)){
                    event.preventDefault();
                    return
                }
            }
        })
        document.addEventListener('keyup', (event) => {
            this.onKeyPressed(event);
            return false;
        });

        this.shader.init(this.gl);

        for(var i = 0; i < context.uvMeshes.length; i++){
            this.objs[i] = new Drawable();
            this.objs[i].shader = this.shader;
            this.objs[i].texture = this.gl.getTexture(this.objs[i].textureName);
            this.objs[i].fromMesh(this.gl, context.uvMeshes[i]);
        }

        //this.objs[0] = new Drawable();
        //this.objs[0].shader = this.shader;

        //this.objs[0].fromMesh(this.gl, context.uvMesh);

        context.uvMeshes = _;

        //this.objs[0].translation = [0.0, 0.0, 0.0];


        //this.objs[0].rotationSpeed = 10;
        //this.objs[0].rotate = true;

        /*this.objs[1] = new Drawable()
        this.objs[1].shader = this.shader
        this.objs[1].init(this.gl);
        this.objs[1].translation = [-2.0, 0.0, -6.0]*/

        for(var i = 0; i < this.objs.length; i++){
            this.objs[i].texture = this.gl.getTexture(this.objs[i].textureName);
        }

        //var scale = context.settings.scale;

        this.saveCanvasButton.addEventListener("click", (event) => {
            this.saveCanvas();            
        })

        this.angleLeftButton.addEventListener("click", (event) => {
            this.cam.setRotationBy(-1);
            var rot = this.cam.getRotation();
            this.angleDesc.innerHTML = rot + "°";
            sessionStorage.setItem("rotation", rot + "");
        })
        this.angleRightButton.addEventListener("click", (event) => {
            this.cam.setRotationBy(1);
            var rot = this.cam.getRotation();
            this.angleDesc.innerHTML = rot + "°";
            sessionStorage.setItem("rotation", rot + "");
        })
        this.angleLeftDoubleButton.addEventListener("click", (event) => {
            this.cam.setRotationBy(-10);
            var rot = this.cam.getRotation();
            this.angleDesc.innerHTML = rot + "°";
            sessionStorage.setItem("rotation", rot + "");
        })
        this.angleRightDoubleButton.addEventListener("click", (event) => {
            this.cam.setRotationBy(10);
            var rot = this.cam.getRotation();
            this.angleDesc.innerHTML = rot + "°";
            sessionStorage.setItem("rotation", rot + "");
        })

        this.scaleLeftButton.addEventListener("click", (event) => {
            this.cam.setScaleBy(-0.01);
            this.setScaleDesc();
            sessionStorage.setItem("scale", this.cam.getScale() + "");
        })
        this.scaleRightButton.addEventListener("click", (event) => {
            this.cam.setScaleBy(0.01);
            this.setScaleDesc();
            sessionStorage.setItem("scale", this.cam.getScale() + "");
        })
        this.scaleLeftDoubleButton.addEventListener("click", (event) => {
            this.cam.setScaleBy(-0.1);
            this.setScaleDesc();
            sessionStorage.setItem("scale", this.cam.getScale() + "");
        })
        this.scaleRightDoubleButton.addEventListener("click", (event) => {
            this.cam.setScaleBy(0.1);
            this.setScaleDesc();
            sessionStorage.setItem("scale", this.cam.getScale() + "");
        })

        var scale = sessionStorage.getItem("scale");
        var rotation = sessionStorage.getItem("rotation");

        var scaleNum = Number(sessionStorage.getItem("scale"));
        var rotationNum = Number(sessionStorage.getItem("rotation"));
        if(scale === _ || scale === null) scaleNum = 1;
        if(rotation === _ || rotation === null) rotationNum = 0;

        this.cam.setScaleAndRotationDirectly(scaleNum, rotationNum);

        this.setScaleDesc();
        this.setAngleDesc();
        
        requestAnimationFrame(this.loop.bind(this));
    }

    saveCanvas(){
        var dataURL = this.canvas.toDataURL("image/png");
        var url = dataURL.replace(/^data:image\/png/, "data:application/octet-stream");

        var date = new Date();
        var dateStr = date.toISOString().slice(0, 19 - 9);//.replace('T', '_').replace(':', '-');
        var timestamp = Date.now();

        var anchor = document.createElement("a");
        anchor.setAttribute("download", "EscherGen_" + dateStr + "_" + timestamp +  ".png");
        anchor.setAttribute("href", url);
        anchor.click();
    }

    private setScaleDesc(){
        var value = Number(this.cam.getScale());
        var str = value + "";

        var res = str.split(".");
        //if(res.length === 1 || res[1].length < 3){
            str = value.toFixed(2);
        //}

        this.scaleDesc.innerHTML = str + "x";
    }

    private setAngleDesc(){
        var rot = this.cam.getRotation();
        this.angleDesc.innerHTML = rot + "°";
    }

    private onClick(x, y){
        log("x: " + x + " y: " + y);
        this.objs[0].rotationSpeed += 20;
    }

    private onKeyPressed(event: KeyboardEvent){
        var key = event.key;
        log(key);

        /*if(keysToCatch.ArrowUp.includes(key)){
            this.cam.translateBy(0, 0, .1);
            this.cam.updateCamera();
        } else if(keysToCatch.ArrowDown.includes(key)){
            this.cam.translateBy(0, 0, -.1);
            this.cam.updateCamera();
        } else if(keysToCatch.ArrowLeft.includes(key)){
            this.cam.translateBy(0, .1, 0);
            this.cam.updateCamera();
        } else if(keysToCatch.ArrowRight.includes(key)){
            this.cam.translateBy(0, -.1, 0);
            this.cam.updateCamera();
        } else if(keysToCatch.keyR.includes(key)){
            if(this.gl.renderMethod === WebGL2RenderingContext.TRIANGLES){
                this.gl.renderMethod = WebGL2RenderingContext.LINES;
            } else {
                this.gl.renderMethod = WebGL2RenderingContext.TRIANGLES;
            }
        }*/
    }

    private loop(time){
        var delta = time - this.lastTime;
        this.lastTime = time;
        this.lastTimeTick += delta;
        if(this.lastTimeTick > 1000){
            this.lastTimeTick -= 1000;
            //console.log("tick " + time +  ", " + delta);
        }
        this.update(delta)
        this.render(delta, time)
        requestAnimationFrame(this.loop.bind(this));
    }

    private update(delta){
        for(var i = 0; i < this.objs.length; i++){
            this.objs[i].update(delta);
        }
        this.cam.update(delta);
    }

    private render(delta, time){
        this.gl.clear();
        this.gl.draw(this.objs, this.cam, time);
    }

}

class Shader{

    fragment = ""
    vertex = ""
    program: WebGLProgram

    name = ""
    
    attributes = {
        vertexPosition: _,
        vertexNormal: _,
        vertexColor: _,
        textureCoord: _,
    }
    uniforms = {
        projectionMatrix: _,
        modelViewMatrix: _,
        normalMatrix: _,
        uSampler: _,
        uTime: _,
    }

    constructor(name){
        if(name === null) return
        this.name = name;
        //this.vertex = document.getElementById(name + ".vert").innerText;
        //this.fragment = document.getElementById(name + ".frag").innerText;
        this.vertex = context.shaderSourcesVert[name];
        this.fragment = context.shaderSourcesFrag[name];
    }

    init(gl: GL){
        this.program = gl.initShader(this);
        
        this.attributes.vertexPosition = gl.gl.getAttribLocation(this.program, 'aVertexPosition');
        this.attributes.vertexNormal = gl.gl.getAttribLocation(this.program, 'aVertexNormal'),
        this.attributes.vertexColor = gl.gl.getAttribLocation(this.program, 'aVertexColor');
        //this.attributes.textureCoord = gl.gl.getAttribLocation(this.program, 'aTextureCoord');

        this.uniforms.projectionMatrix = gl.gl.getUniformLocation(this.program, 'uProjectionMatrix');
        this.uniforms.modelViewMatrix = gl.gl.getUniformLocation(this.program, 'uModelViewMatrix');
        this.uniforms.normalMatrix = gl.gl.getUniformLocation(this.program, 'uNormalMatrix');
        //this.uniforms.uSampler = gl.gl.getUniformLocation(this.program, 'uSampler');
        this.uniforms.uTime = gl.gl.getUniformLocation(this.program, 'uTime');
    }

}

class Drawable{

    posBuffer    : WebGLBuffer
    colorsBuffer : WebGLBuffer
    normalsBuffer: WebGLBuffer
    indicesBuffer: WebGLBuffer
    textureBuffer: WebGLBuffer

    indicesLength = 0

    textureName = _
    texture: Texture = _

    shader: Shader

    rotation = 0
    rotationSpeed = 0
    rotate = false

    translation = [0.0, 0.0, 0.0]

    update(delta: number){
        if(this.rotate){
            this.rotation += (delta * 0.001) * ((1 + this.rotationSpeed) / 10);
            this.rotationSpeed -= 1;
            if(this.rotationSpeed < 0) this.rotationSpeed = 0;
        }
    }

    fromMesh(gl: GL, mesh){
        if(mesh.vertices.POSITION !== _){
            this.posBuffer = gl.genBuffer(mesh.vertices.POSITION);
            log((mesh.vertices.POSITION.length / 3) + " ");
        } else if(mesh.vertices.POSITION_2D !== _){
            this.posBuffer = gl.genBuffer(mesh.vertices.POSITION_2D);
            log((mesh.vertices.POSITION_2D.length / 3) + " ");
        }

        if(mesh.vertices.NORMAL !== _){
            this.normalsBuffer = gl.genBuffer(mesh.vertices.NORMAL);
        }
        this.colorsBuffer = gl.genBuffer(mesh.vertices.COLOR);
        this.indicesBuffer = gl.genIndicesBuffer(mesh.indices);
        this.indicesLength = mesh.indices.length;
    }

}

class Camera{

    canvas: HTMLCanvasElement

    fov = 45 * Math.PI / 180;
    aspectRatio = _;
    near = 1.0;
    far = 200.0;

    viewWidth = 0;
    viewHeight = 0;

    private projectionMatrix = mat4.create();
    private cameraMatrix = mat4.create();
    private viewMatrix = mat4.create();
    viewProjectionMatrix = mat4.create();

    private position = new Vector3();
    private target = new Vector3();
    private up = new Vector3();

    private rotation = 0;
    private rotationTarget = 0;
    private rotationBonusSpeed = 0;

    private scale = 1;
    private scaleTarget = 1;
    private scaleBonusSpeed = 0;

    /*
            this.cam.setTranslate(0, 0, -10 * this.scale);
        this.cam.updateCamera();
    */

    constructor(canvas: HTMLCanvasElement){
        this.canvas = canvas;
        this.up.set(0, 1, 0);
        this.setTranslate(0, 0, -10 * this.scale);
        this.updateCamera();
    }

    setScaleAndRotationDirectly(scale, rotation){
        this.scaleTarget = scale;
        if(this.scaleTarget < 0.1){
            this.scaleTarget = 0.1;
        } else if(this.scaleTarget > 4.0){
            this.scaleTarget = 4.0;
        } 
        this.scale = this.scaleTarget;
        this.rotationTarget = rotation;
        this.rotation = rotation;

        this.setTranslate(0, 0, -10 * this.scale);
        this.updateCamera();
    }

    setTarget(x: number, y: number, z: number){
        this.target.set(x, y, z);
    }

    setTranslate(x: number, y: number, z: number){
        this.position.set(x, y, z);
    }

    translateBy(x: number, y: number, z: number){
        this.position.add(x, y, z);
    }

    setRotation(rot: number){
        this.rotationTarget = rot;
        /*if(this.rotationTarget < 0){
            this.rotationTarget += 360;
        } else if(this.rotationTarget > 359){
            this.rotationTarget -= 360;
        }*/
    }

    setRotationBy(rot: number){
        this.setRotation(this.rotationTarget + rot);
    }

    getRotation(): number{
        var res = this.rotationTarget % 360;
        if(res < 0) res += 360;
        return res;
    }

    setScale(scl: number){
        this.scaleTarget = scl;
        if(this.scaleTarget < 0.1){
            this.scaleTarget = 0.1;
        } else if(this.scaleTarget > 4.0){
            this.scaleTarget = 4.0;
        } 
    }
    
    setScaleBy(scl: number){
        this.setScale(this.scaleTarget + scl);
    }
    
    getScale(): number{
        return this.scaleTarget;
    }

    update(delta){
        if(this.rotationTarget > this.rotation){
            this.rotation += (delta * (0.01 + this.rotationBonusSpeed));
            if(this.rotationTarget < this.rotation) this.rotation = this.rotationTarget;
            this.updateCamera();
            this.rotationBonusSpeed += 0.0005;
        } else if(this.rotationTarget < this.rotation){
            this.rotation -= (delta * (0.01 + this.rotationBonusSpeed));
            if(this.rotationTarget > this.rotation) this.rotation = this.rotationTarget;
            this.updateCamera();
            this.rotationBonusSpeed += 0.0005;
        } else {
            this.rotationBonusSpeed = 0;
        }


        if(this.scaleTarget > this.scale){
            this.scale += (delta * (0.0002 + this.scaleBonusSpeed));
            if(this.scaleTarget < this.scale) this.scale = this.scaleTarget;
            this.setTranslate(0, 0, -10 * this.scale);
            this.updateCamera();
            this.scaleBonusSpeed += 0.00005;
        } else if(this.scaleTarget < this.scale){
            this.scale -= (delta * (0.0002 + this.scaleBonusSpeed));
            if(this.scaleTarget > this.scale) this.scale = this.scaleTarget;
            this.setTranslate(0, 0, -10 * this.scale);
            this.updateCamera();
            this.scaleBonusSpeed += 0.00005;
        } else {
            this.scaleBonusSpeed = 0;
        }
    }

    updateCamera(){
        //console.log(this.position);
        this.viewWidth = this.canvas.clientWidth;
        this.viewHeight = this.canvas.clientHeight;
        this.aspectRatio = this.viewWidth / this.viewHeight;
        mat4.perspective(this.projectionMatrix, this.fov, this.aspectRatio, this.near, this.far);
    
        //mat4.identity(this.cameraMatrix);
        //mat4.translate(this.cameraMatrix, this.cameraMatrix, this.position);
        mat4.lookAt(this.cameraMatrix, this.position.values(), this.target.values(), this.up.values());

        mat4.rotate(this.cameraMatrix, this.cameraMatrix, this.rotation * degToRad, [0, 0, 1]);

        mat4.invert(this.viewMatrix, this.cameraMatrix);

        mat4.multiply(this.viewProjectionMatrix, this.projectionMatrix, this.viewMatrix);
    }
}

class GL {

    canvas: HTMLCanvasElement
    gl: WebGLRenderingContext
    clearColor = [0.0, 0.0, 0.0, 1.0];

    textures: Texture[] = [];

    renderMethod = 0

    constructor(canvas: HTMLCanvasElement){
        //make sure saving images works
        this.gl = canvas.getContext("webgl", {preserveDrawingBuffer: true });
        this.canvas = canvas;
        this.renderMethod = this.gl.TRIANGLES;

        if(!this.gl) log("DIDN'T LOAD GL!");

        var keys = Object.keys(texturePaths);
        for(var i = 0; i < keys.length; i++){
            this.textures[i] = new Texture();
            this.textures[i].name = texturePaths[keys[i]];
            this.textures[i].texture = this.loadTexture(imgFolder + this.textures[i].name);
        }

        //this.textures.logo = this.loadTexture(imgFolder + "logo_64x64.png");
       // mat4.perspective(this.projectionMatrix, fov, aspectRatio, near, far);
    }

    clear(){
        this.gl.clearColor(this.clearColor[0], this.clearColor[1], this.clearColor[2], this.clearColor[3]);
        this.gl.clearDepth(1.0);
        this.gl.enable(this.gl.DEPTH_TEST);
        this.gl.depthFunc(this.gl.LEQUAL);
        this.gl.clear(this.gl.COLOR_BUFFER_BIT | this.gl.DEPTH_BUFFER_BIT);
    }

    draw(drawables: Drawable[], camera: Camera, time: number){
        for(var i = 0; i < drawables.length; i++){
            var drawable = drawables[i];
            var shader = drawable.shader;

            if(shader.name === "globe"){
                this.drawGlobe(drawable, camera);
            } else {
                this.draw2d(drawable, camera, time);
            }
        }
    }

    draw2d(drawable: Drawable, camera: Camera, time: number){
        var shader = drawable.shader;

        const modelViewMatrix = mat4.create();
        mat4.translate(modelViewMatrix, modelViewMatrix, drawable.translation);
        mat4.rotate(modelViewMatrix, modelViewMatrix, drawable.rotation, [0, 0, 1]);

        //pos
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, drawable.posBuffer);
        this.gl.vertexAttribPointer(shader.attributes.vertexPosition, 2, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(shader.attributes.vertexPosition);    

        //colors
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, drawable.colorsBuffer);
        this.gl.vertexAttribPointer(shader.attributes.vertexColor, 4, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(shader.attributes.vertexColor);

        this.gl.bindBuffer(this.gl.ELEMENT_ARRAY_BUFFER, drawable.indicesBuffer);

        this.gl.useProgram(shader.program);
        this.gl.uniformMatrix4fv(shader.uniforms.projectionMatrix, false, camera.viewProjectionMatrix);
        this.gl.uniformMatrix4fv(shader.uniforms.modelViewMatrix, false, modelViewMatrix);    

        this.gl.uniform1f(shader.uniforms.uTime, time);    


        this.gl.drawElements(this.renderMethod, drawable.indicesLength, this.gl.UNSIGNED_SHORT, 0);
    }

    drawGlobe(drawable: Drawable, camera: Camera){
        var shader = drawable.shader;

        const modelViewMatrix = mat4.create();
        mat4.translate(modelViewMatrix, modelViewMatrix, drawable.translation);
        mat4.rotate(modelViewMatrix, modelViewMatrix, drawable.rotation, [0, 1, 0]);
        //mat4.rotate(modelViewMatrix, modelViewMatrix, drawable.rotation * 0.7, [0, 1, 0]);

        const normalMatrix = mat4.create();
        mat4.invert(normalMatrix, modelViewMatrix);
        mat4.transpose(normalMatrix, normalMatrix);    

        //pos
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, drawable.posBuffer);
        this.gl.vertexAttribPointer(shader.attributes.vertexPosition, 3, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(shader.attributes.vertexPosition);    

        //normal
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, drawable.normalsBuffer);
        this.gl.vertexAttribPointer(shader.attributes.vertexNormal, 3, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(shader.attributes.vertexNormal); 
        
        //colors
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, drawable.colorsBuffer);
        this.gl.vertexAttribPointer(shader.attributes.vertexColor, 4, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(shader.attributes.vertexColor);

        //texture coords
        /*this.gl.bindBuffer(this.gl.ARRAY_BUFFER, drawable.textureBuffer);
        this.gl.vertexAttribPointer(shader.attributes.textureCoord, 2, this.gl.FLOAT, false, 0, 0);
        this.gl.enableVertexAttribArray(shader.attributes.textureCoord);*/

        this.gl.bindBuffer(this.gl.ELEMENT_ARRAY_BUFFER, drawable.indicesBuffer);

        this.gl.useProgram(shader.program);
        this.gl.uniformMatrix4fv(shader.uniforms.projectionMatrix, false, camera.viewProjectionMatrix);
        this.gl.uniformMatrix4fv(shader.uniforms.modelViewMatrix, false, modelViewMatrix);    
        this.gl.uniformMatrix4fv(shader.uniforms.normalMatrix, false, normalMatrix);

        //texture
        /*this.gl.activeTexture(this.gl.TEXTURE0);
        this.gl.bindTexture(this.gl.TEXTURE_2D, drawable.texture.texture);
        this.gl.uniform1i(shader.uniforms.uSampler, 0);*/


        this.gl.drawElements(this.renderMethod, drawable.indicesLength, this.gl.UNSIGNED_SHORT, 0);
    }

    genBuffer(array): WebGLBuffer{
        var buffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, buffer);
        this.gl.bufferData(this.gl.ARRAY_BUFFER, new Float32Array(array),  this.gl.STATIC_DRAW);
        return buffer;
    }

    genIndicesBuffer(array): WebGLBuffer{
        var buffer = this.gl.createBuffer();
        this.gl.bindBuffer(this.gl.ELEMENT_ARRAY_BUFFER, buffer);
        this.gl.bufferData(this.gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(array),  this.gl.STATIC_DRAW);
        return buffer;
    }

    initShader(shader: Shader): WebGLProgram{
        const vs = this.loadShader(this.gl.VERTEX_SHADER, shader.vertex);
        const fs = this.loadShader(this.gl.FRAGMENT_SHADER, shader.fragment);

        const program = this.gl.createProgram();
        this.gl.attachShader(program, vs);
        this.gl.attachShader(program, fs);
        this.gl.linkProgram(program);
        if (!this.gl.getProgramParameter(program, this.gl.LINK_STATUS)) {
            log(this.gl.getProgramInfoLog(program));
            return null;
        }

        return program
    }

    private loadShader(type: number, src: string): WebGLShader{
        const shader = this.gl.createShader(type);
        this.gl.shaderSource(shader, src);
        this.gl.compileShader(shader);
        if (!this.gl.getShaderParameter(shader, this.gl.COMPILE_STATUS)) {
            log(this.gl.getShaderInfoLog(shader));
            this.gl.deleteShader(shader);
            return null;
        }
        return shader
    }

    loadTexture(url: string): WebGLTexture{
        var texture = this.gl.createTexture();
        this.gl.bindTexture(this.gl.TEXTURE_2D, texture);
        this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.RGBA, 1, 1, 0, 
            this.gl.RGBA, this.gl.UNSIGNED_BYTE, new Uint8Array([255, 0, 255, 255]));

        var image = new Image();
        image.onload = this.onLoadTexture.bind(this, texture, image);
        image.src = url;
        return texture
    }

    private onLoadTexture(texture: WebGLTexture, image: HTMLImageElement){
        this.gl.bindTexture(this.gl.TEXTURE_2D, texture);
        this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.RGBA, this.gl.RGBA, this.gl.UNSIGNED_BYTE, image);
        this.gl.generateMipmap(this.gl.TEXTURE_2D);
    }

    getTexture(src: string): Texture{
        for(var i = 0; i < this.textures.length; i++){
            if(this.textures[i].name === src)  return this.textures[i]
        }
        return _;
    }

}

class Texture{

    name: string
    texture: WebGLTexture

}

class Vector3{

    x = 0
    y = 0
    z = 0

    private arrValues = [this.x, this.y, this.z]

    static cross(v1: Vector3, v2: Vector3, res: Vector3){
        res.set(
            v1.y * v2.z - v1.z * v2.y, 
            v1.z * v2.x - v1.x * v2.z, 
            v1.x * v2.y - v1.y * v2.y 
            );
    }

    cross(v: Vector3){
        Vector3.cross(this, v, this);
    }

    subVec(v: Vector3){
        this.sub(v.x, v.y, v.z);
    }

    sub(x: number, y: number, z: number){
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    addVec(v: Vector3){
        this.add(v.x, v.y, v.z);
    }

    add(x: number, y: number, z: number){
        this.x += x;
        this.y += y;
        this.z += z;
    }

    set(x: number, y: number, z: number){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    setVec(v: Vector3){
        this.set(v.x, v.y, v.z);
    }

    addToArray(arr: number[]){
        arr.push(this.x, this.y, this.z);
    }

    nor(){
        var length = this.length();
        this.divide(length);
    }

    pow(amount: number){
        var newX = Math.pow(this.x, amount);
        var newY = Math.pow(this.y, amount);
        var newZ = Math.pow(this.z, amount);
        this.x = newX;
        this.y = newY;
        this.z = newZ;
    }

    divide(amount: number){
        this.x /= amount;
        this.y /= amount;
        this.z /= amount;
    }

    mul(amount: number){
        this.x *= amount;
        this.y *= amount;
        this.z *= amount;
    }

    mulVec(v: Vector3){
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
    }

    length(): number{
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    values(): number[]{
        this.arrValues[0] = this.x;
        this.arrValues[1] = this.y;
        this.arrValues[2] = this.z;
        return this.arrValues
    }

}

class Form{

    //ranges: any[] = []
    //values: any[] = []

    //colorPatterns: ColorForm2[] = [];


    colorForm = new ColorForm();


    constructor(){
        /*for(var i = 0; i < 1; i++){
            this.colorPatterns.push(new ColorForm2(i))
        }*/

        //var form = window.document.getElementById("settings-form");
        //form.oninput = this.updateForm2.bind(this);

        //var form = window.document.getElementById("colors-form");
        //form.oninput = this.updateForm.bind(this);

        var elements: any = window.document.getElementsByClassName("form-update");
        for(var i = 0; i < elements.length; i++){
            elements[i].oninput = this.updateForm.bind(this);
        }
    }

    update(){
        this.updateForm();
    }

    /*private updateForm2(){
        for(var i = 0; i < this.colorPatterns.length; i++){
            this.colorPatterns[i].onInput();
        }
    }*/

    private updateForm(){
        this.colorForm.update();
    }
}

class ColorForm{

    hueValue: any = window.document.getElementById("colors-form-hue-value") as HTMLDivElement
    saturationValue: any = window.document.getElementById("colors-form-saturation-value") as HTMLDivElement
    lightnessValue: any = window.document.getElementById("colors-form-lightness-value") as HTMLDivElement

    hueLabel: any = window.document.getElementById("colors-form-hue-label") as HTMLDivElement
    saturationLabel: any = window.document.getElementById("colors-form-saturation-label") as HTMLDivElement
    lightnessLabel: any = window.document.getElementById("colors-form-lightness-label") as HTMLDivElement

    hexInput: any = window.document.getElementById("hexcode-input") as HTMLDivElement

    rgbInputs: any[] = [
        window.document.getElementById("r-input") as HTMLDivElement,
        window.document.getElementById("g-input") as HTMLDivElement,
        window.document.getElementById("b-input") as HTMLDivElement
    ]
    rgbInputLabels: any[] = [
        window.document.getElementById("r-input-label") as HTMLDivElement,
        window.document.getElementById("g-input-label") as HTMLDivElement,
        window.document.getElementById("b-input-label") as HTMLDivElement
    ]

    fixedColorsSelects: ColoredElement[] = [];

    colorChecks: any[] = [];
    colorStatuses: boolean[] = [];

    colorHueContainers: any[] = [];
    colorHueRanges: any[] = [];
    colorSaturationContainers: any[] = [];
    colorSaturationRanges: any[] = [];
    colorLightnessContainers: any[] = [];
    colorLightnessRanges: any[] = [];

    colorAddButton = window.document.getElementById("colors-form-coloradd") as HTMLDivElement

    currentColorIndex = 0;
    addedColors = 0;

    colorsLimit = context.formSettingsDto.fixedColors.length;

    colorSettingStatusButtons: any[] = [];
    colorSettingStatus = context.formSettingsDto.colorSetting;

    constructor(){
        for(var i = 0; i < this.colorsLimit; i++){
            var select = window.document.getElementById("fixedcolor-select-" + i) as HTMLDivElement
            var check: any = window.document.getElementById("fixedcolor-check-" + i) as HTMLDivElement

            if(check.checked) this.addedColors++;

            this.colorStatuses.push(check.checked);
            this.colorChecks.push(check);

            var hueContainer = window.document.getElementById("colors-form-hue-container-" + i) as HTMLDivElement
            var hueRange = window.document.getElementById("colors-form-hue-range-" + i) as HTMLDivElement
            this.colorHueContainers.push(hueContainer);
            this.colorHueRanges.push(hueRange);

            var saturationContainer = window.document.getElementById("colors-form-saturation-container-" + i) as HTMLDivElement
            var saturationRange = window.document.getElementById("colors-form-saturation-range-" + i) as HTMLDivElement
            this.colorSaturationContainers.push(saturationContainer);
            this.colorSaturationRanges.push(saturationRange);

            var lightnessContainer = window.document.getElementById("colors-form-lightness-container-" + i) as HTMLDivElement
            var lightnessRange = window.document.getElementById("colors-form-lightness-range-" + i) as HTMLDivElement
            this.colorLightnessContainers.push(lightnessContainer);
            this.colorLightnessRanges.push(lightnessRange);

            var coloredElement = new ColoredElement();
            coloredElement.element = select;
            this.fixedColorsSelects.push(coloredElement);
            this.updateElementColor(i);

            select.onclick = this.onColorSelect.bind(this, i);
            check.addEventListener('change', this.removeElementColor.bind(this, i))
        }

        for(var i = 0; i < 2; i++){
            var button: any = window.document.getElementById("color-setting-status-" + i) as HTMLDivElement
            button.onclick = this.onColorSettingButton.bind(this, i);
            this.colorSettingStatusButtons.push(button);
        }

        this.colorSettingStatus = -1;
        this.onColorSettingButton(context.formSettingsDto.colorSetting);

        this.colorAddButton.onclick = this.onColorAddClick.bind(this);

        var hexInput: any = window.document.getElementById("hexcode-input") as HTMLDivElement
        hexInput.oninput = this.onHexEdit.bind(this);

        this.rgbInputs[0].oninput = this.onRgbInputSet.bind(this, 0);
        this.rgbInputs[1].oninput = this.onRgbInputSet.bind(this, 1);
        this.rgbInputs[2].oninput = this.onRgbInputSet.bind(this, 2);
    }

    private onHexEdit(){
        var hex = this.hexInput.value;
        if(hex.length > 0 && hex.charAt(0) !== "#"){
            hex = "#" + hex;
        }
        if(hex.length !== 7) return
        var valid = /^#([0-9A-F]{3}){1,2}$/i.test(hex);
        if(!valid) return

        if(hex.length === 7){
            var r = parseInt(hex.substring(1, 3), 16);
            var g = parseInt(hex.substring(3, 5), 16);
            var b = parseInt(hex.substring(5, 7), 16);
            //console.log(r + " " + g + " " + b);
            this.fixedColorsSelects[this.currentColorIndex].setElementRGB(r, g, b);
            this.updateRangesOf(this.currentColorIndex);
            this.update();
        } else if(hex.length === 4){
            var r = parseInt(hex.substring(1, 2), 16);
            var g = parseInt(hex.substring(2, 3), 16);
            var b = parseInt(hex.substring(3, 4), 16);
            //console.log(r + " " + g + " " + b);
            this.fixedColorsSelects[this.currentColorIndex].setElementRGB(r, g, b);
            this.updateRangesOf(this.currentColorIndex);
            this.update();
        }
    }

    private onRgbInputSet(index: number){
        if(index === 0){
            var r = this.rgbInputs[0].value + "";
            var value = parseInt(r.match(/[0-9]+/g)[0]);

            value = Math.max(value, 0);
            value = Math.min(value, this.colorHueRanges[this.currentColorIndex].max);
            //console.log(value);

            this.colorHueRanges[this.currentColorIndex].value = value;
            this.update();
        } else if(index === 1){
            var g = this.rgbInputs[1].value + "";
            var value = parseInt(g);//parseInt(g.match(/[0-9]+/g)[0]);

            value = Math.max(value, 0);
            value = Math.min(value, this.colorSaturationRanges[this.currentColorIndex].max);
            //console.log(value);

            this.colorSaturationRanges[this.currentColorIndex].value = value;
            this.update();
        } else if(index === 2){
            var b = this.rgbInputs[2].value + "";
            var value = parseInt(b);

            value = Math.max(value, 0);
            value = Math.min(value, this.colorLightnessRanges[this.currentColorIndex].max);
            //console.log(value);

            this.colorLightnessRanges[this.currentColorIndex].value = value;
            this.update();
        }

    }

    private onColorSettingButton(index: number){
        if(this.colorSettingStatus === index) return
       this.colorSettingStatus = index;
       //console.log(index);

       if(index === 0){
        this.hueLabel.innerHTML = context.strings.red_label;
        this.saturationLabel.innerHTML = context.strings.green_label;
        this.lightnessLabel.innerHTML = context.strings.blue_label;
        this.rgbInputLabels[0].innerHTML = context.strings.red_label;
        this.rgbInputLabels[1].innerHTML = context.strings.green_label;
        this.rgbInputLabels[2].innerHTML = context.strings.blue_label;

        for(var i = 0; i < this.colorsLimit; i++){
            this.colorHueRanges[i].max = 255;
            this.colorSaturationRanges[i].max = 255;
            this.colorLightnessRanges[i].max = 255;

            this.colorHueRanges[i].step = 1;
            this.colorSaturationRanges[i].step = 1;
            this.colorLightnessRanges[i].step = 1;

            this.updateRangesOf(i);

            this.colorHueRanges[i].classList.add("form-range-red");
            this.colorSaturationRanges[i].classList.add("form-range-green");
            this.colorLightnessRanges[i].classList.add("form-range-blue");
            this.colorHueRanges[i].classList.remove("form-range-hue");
            this.colorSaturationRanges[i].classList.remove("form-range-saturation");
            this.colorLightnessRanges[i].classList.remove("form-range-lightness");
        }
       } else if(index === 1){
        this.hueLabel.innerHTML = context.strings.hue_label;
        this.saturationLabel.innerHTML = context.strings.saturation_label;
        this.lightnessLabel.innerHTML = context.strings.lightness_label;
        this.rgbInputLabels[0].innerHTML = context.strings.hue_label;
        this.rgbInputLabels[1].innerHTML = context.strings.saturation_label;
        this.rgbInputLabels[2].innerHTML = context.strings.lightness_label;

        for(var i = 0; i < this.colorsLimit; i++){
            this.colorHueRanges[i].max = 360;
            this.colorSaturationRanges[i].max = 100;
            this.colorLightnessRanges[i].max = 100;

            this.colorHueRanges[i].step = 1;
            this.colorSaturationRanges[i].step = 0.00001;
            this.colorLightnessRanges[i].step = 0.00001;
            this.updateRangesOf(i);

            this.colorHueRanges[i].classList.add("form-range-hue");
            this.colorSaturationRanges[i].classList.add("form-range-saturation");
            this.colorLightnessRanges[i].classList.add("form-range-lightness");
            this.colorHueRanges[i].classList.remove("form-range-red");
            this.colorSaturationRanges[i].classList.remove("form-range-green");
            this.colorLightnessRanges[i].classList.remove("form-range-blue");
        }
       }
       this.update();
    }

    private updateRangesOf(i: number){
        if(this.colorSettingStatus === 0){
            this.colorHueRanges[i].value = this.fixedColorsSelects[i].r();
            this.colorSaturationRanges[i].value = this.fixedColorsSelects[i].g();
            this.colorLightnessRanges[i].value = this.fixedColorsSelects[i].b();
        } else {
            this.colorHueRanges[i].value = this.fixedColorsSelects[i].h();
            this.colorSaturationRanges[i].value = this.fixedColorsSelects[i].s();
            this.colorLightnessRanges[i].value = this.fixedColorsSelects[i].l();
        }
    }

    private onColorSelect(index: number){
        //console.log("onColorSelect() " + Date.now());
        this.currentColorIndex = index;
        this.update();
    }

    private onColorAddClick(){
        //console.log("onColorAddClick() "  + this.addedColors + " | " + Date.now());
        if(this.addedColors === this.colorsLimit) return;
    
        this.fixedColorsSelects[this.addedColors].element.style.display = "block";
        this.colorChecks[this.addedColors].checked = true;

        this.addedColors++;
        this.update();
    }

    private removeElementColor(index: number){
        //if(this.addedColors === 1) return
        //console.log("removeElementColor() "  + this.addedColors + " | " + Date.now());
        this.addedColors--;

        this.colorChecks[index].checked = true;

        for(var i = index; i < this.colorsLimit - 1; i++){
            this.colorHueRanges[i].value = this.colorHueRanges[i + 1].value;
            this.colorSaturationRanges[i].value = this.colorSaturationRanges[i + 1].value;
            this.colorLightnessRanges[i].value = this.colorLightnessRanges[i + 1].value;

            this.colorChecks[i].checked = this.colorChecks[i + 1].checked;
        }

        //reset last color
        /*getColors()[0].hue = 0f
        getColors()[1].hue = 120f
        getColors()[2].hue = 240f
        getColors()[3].hue = 180f
        getColors()[4].hue = 300f*/
        this.colorChecks[this.colorsLimit - 1].checked = false;
        this.fixedColorsSelects[this.colorsLimit - 1].setElement(300, 50, 50);
        this.updateRangesOf(this.colorsLimit - 1);

        this.onColorSelect(0);
        //this.update();
    }

    private updateElementColor(index: number){
       if(this.colorSettingStatus === 0){
        var r = this.colorHueRanges[index].value;
        var g = this.colorSaturationRanges[index].value;
        var b = this.colorLightnessRanges[index].value;
        this.fixedColorsSelects[index].setElementRGB(parseFloat(r), parseFloat(g), parseFloat(b));
       } else if(this.colorSettingStatus === 1){
        var h = this.colorHueRanges[index].value;
        var s = this.colorSaturationRanges[index].value;
        var l = this.colorLightnessRanges[index].value;
        this.fixedColorsSelects[index].setElement(parseFloat(h), parseFloat(s), parseFloat(l));
       }
    }

    update(){
        //console.log("update() " + Date.now());

        for(var i = 0; i < this.colorsLimit; i++){
            this.updateElementColor(i);
            //if(this.selectedColor !== i) this.fixedColorsSelects[i].setElement(0, 50, 50);

            var visible = this.colorChecks[i].checked;
            this.colorStatuses[i] = visible;
            if(visible){
                this.fixedColorsSelects[i].element.style.display = "block";
            } else {
                this.fixedColorsSelects[i].element.style.display = "none";
            }

            if(i === this.currentColorIndex){
                this.colorHueContainers[i].style.display = "block";
                this.colorSaturationContainers[i].style.display = "block";
                this.colorLightnessContainers[i].style.display = "block";
                this.fixedColorsSelects[i].element.classList.add("color-select-active");
                this.hexInput.value = this.fixedColorsSelects[i].hex();
            } else {
                this.colorHueContainers[i].style.display = "none";
                this.colorSaturationContainers[i].style.display = "none";
                this.colorLightnessContainers[i].style.display = "none";
                this.fixedColorsSelects[i].element.classList.remove("color-select-active");
            }
        }

        if(this.addedColors === this.colorsLimit){
            this.colorAddButton.style.display = "none";
        } else {
            this.colorAddButton.style.display = "block";

        }
        var rgbStrings = [
            this.colorHueRanges[this.currentColorIndex].value,
            this.colorSaturationRanges[this.currentColorIndex].value, 
            this.colorLightnessRanges[this.currentColorIndex].value];
        var rgbValues = [
            new Number(rgbStrings[0]),
            new Number(rgbStrings[1]),
            new Number(rgbStrings[2])];

        if(this.colorSettingStatus === 0){
            rgbStrings[0] = rgbValues[0].toFixed(0);
            rgbStrings[1] = rgbValues[1].toFixed(0);
            rgbStrings[2] = rgbValues[2].toFixed(0);

            this.hueValue.innerHTML = rgbStrings[0];
            this.saturationValue.innerHTML = rgbStrings[1];
            this.lightnessValue.innerHTML = rgbStrings[2];
            this.rgbInputs[0].value = rgbStrings[0];
            this.rgbInputs[1].value = rgbStrings[1];
            this.rgbInputs[2].value = rgbStrings[2];
        } else {
            rgbStrings[0] = rgbValues[0].toFixed(0);
            rgbStrings[1] = rgbValues[1].toFixed(1);
            rgbStrings[2] = rgbValues[2].toFixed(1);

            this.hueValue.innerHTML = rgbStrings[0];
            this.saturationValue.innerHTML = rgbStrings[1];
            this.lightnessValue.innerHTML = rgbStrings[2];
            this.rgbInputs[0].value = rgbValues[0].toFixed(0);
            this.rgbInputs[1].value = rgbValues[1].toFixed(0);
            this.rgbInputs[2].value = rgbValues[2].toFixed(0);
        }

        /*var str = this.colorHueRanges[this.currentColorIndex].value;
        var value: Number = new Number(str);
        str = value.toFixed(0);
        this.hueValue.innerHTML = str;
        rgbStrings[0] = str;

        var str = this.colorSaturationRanges[this.currentColorIndex].value;
        var value: Number = new Number(str);
        str = value;
        if(this.colorSettingStatus === 0){
            str = value.toFixed(0);
        } else if(this.colorSettingStatus === 1){
            str = value.toFixed(1);
        } 
        this.saturationValue.innerHTML = str;
        if(this.colorSettingStatus === 1) this.saturationValue.innerHTML += "%";
        rgbStrings[1] = str;
  
        var str = this.colorLightnessRanges[this.currentColorIndex].value;
        var value: Number = new Number(str);
        if(this.colorSettingStatus === 0){
            str = value.toFixed(0);
        } else if(this.colorSettingStatus === 1){
            str = value.toFixed(1);
        } 
        this.lightnessValue.innerHTML = str;
        if(this.colorSettingStatus === 1) this.lightnessValue.innerHTML += "%";
        rgbStrings[2] = str;

        this.rgbInputs[0].value = rgbStrings[0];
        this.rgbInputs[1].value = rgbStrings[1];
        this.rgbInputs[2].value = rgbStrings[2];*/
    }
}


class ColoredElement {

    element = _;

    private rgbValue = new Vector3();
    private hslValue = new Vector3();

    private hexCode = "";

    setElementRGB(r: number, g: number, b: number){
        this.rgbValue.set(r, g, b);

        this.hslValue.set(r, g, b);
    
        this.rgbToHsl(this.hslValue);

       // console.log("a hsl=(" + this.hslValue.x + " " + this.hslValue.y + " " + this.hslValue.z + ")");
        //this.hslValue.x = Math.round(this.hslValue.x);
        //this.hslValue.y = Math.round(this.hslValue.y);
        //this.hslValue.z = Math.round(this.hslValue.z);
        //console.log("b hsl=(" + this.hslValue.x + " " + this.hslValue.y + " " + this.hslValue.z + ")");

        //this.getHSL(r / 255, g / 255, b / 255, this.hslValue);
        this.calcHex();
        //console.log(this.hexCode);
        //console.log("setElementRGB rgb=(" + this.rgbValue.x + " " + this.rgbValue.y + " " + this.rgbValue.z + ") hsl=(" + this.hslValue.x + " " + this.hslValue.y + " " + this.hslValue.z + ")");
        this.element.setAttribute("style", "background-color: rgba(" + this.rgbValue.x + "," + this.rgbValue.y + "," + this.rgbValue.z + ",1.0);"); 
    }

    setElement(h: number, s: number, l: number){
        this.setHsl(this.rgbValue, h, s, l);
        this.rgbValue.mul(255.0);
        this.rgbValue.x = Math.round(this.rgbValue.x);
        this.rgbValue.y = Math.round(this.rgbValue.y);
        this.rgbValue.z = Math.round(this.rgbValue.z);

        this.hslValue.set(h, s, l);

        this.calcHex();
        //console.log(this.hexCode);

        //console.log("setElement rgb=(" + this.rgbValue.x + " " + this.rgbValue.y + " " + this.rgbValue.z + ") hsl=(" + this.hslValue.x + " " + this.hslValue.y + " " + this.hslValue.z + ")");
        this.element.setAttribute("style", "background-color: rgba(" + this.rgbValue.x + "," + this.rgbValue.y + "," + this.rgbValue.z + ",1.0);"); 
    }

    private calcHex(){
        var r = this.rgbValue.x.toString(16).substring(0, 2);
        if(r.length === 1 || r.charAt(1) === ".") r = "0" + r.substring(0, 1);
        var g = this.rgbValue.y.toString(16).substring(0, 2);
        if(g.length === 1 || g.charAt(1) === ".") g = "0" + g.substring(0, 1);
        var b = this.rgbValue.z.toString(16).substring(0, 2);
        if(b.length === 1 || b.charAt(1) === ".") b = "0" + b.substring(0, 1);

        this.hexCode = ("#" + r + "" + g + "" + b).toUpperCase();
        //console.log("setElementRGB rgb=(" + this.rgbValue.x + " " + this.rgbValue.y + " " + this.rgbValue.z + ") hsl=(" + this.hslValue.x + " " + this.hslValue.y + " " + this.hslValue.z + ")"  + "|" +this.hexCode);
        //console.log("" + this.hslValue.x + " " + this.hslValue.y + " " + this.hslValue.z + "|" +this.hexCode);
    }

    hex(): String{
        return this.hexCode;
    }

    r(): number{
        return this.rgbValue.x;
    }

    g(): number{
        return this.rgbValue.y;
    }

    b(): number{
        return this.rgbValue.z;
    }

    h(): number{
        return this.hslValue.x;
    }
    
    s(): number{
        return this.hslValue.y;
    }

    l(): number{
        return this.hslValue.z;
    }

    private setHslNormal(color: Vector3, h: number, s: number, l: number){
        color.set(h, s, l)
        this.hslToRgb(color)
    }

    private setHsl(color: Vector3, h: number, s: number, l: number): Vector3 {
        this.setHslNormal(color, (h % 360) / 360.0, (s) / 100.0, (l) / 100.0)
        return color
    }

    private hslToRgb(color: Vector3){
        var h = color.x
        var s = color.y
        var l = color.z
        var r: number
        var g: number
        var b: number
        if (s == 0.0) {
            b = l
            g = b
            r = g
        } else {
            var q = 0;
            if (l < 0.5){
                q = l * (1 + s);
            } else{
                q = l + s - l * s;
            }

            var p = 2 * l - q
            r = this.hueToRgb(p, q, h + 1.0 / 3.0)
            g = this.hueToRgb(p, q, h)
            b = this.hueToRgb(p, q, h - 1.0 / 3.0)
        }
        color.set(r, g, b)
    }

    private hueToRgb(p: number, q: number, t: number): number {
        var res = t
        if (res < 0) res += 1
        if (res > 1) res -= 1
        if (res < 1 / 6) return p + (q - p) * 6 * res
        if (res < 1 / 2) return q
        if (res < 2 / 3) return p + (q - p) * (2 / 3 - res) * 6
        return p
    }

    private getHSL(r: number, g: number, b: number, res: Vector3): Vector3{
        var cmin: number = Math.min(Math.min(r, g), b)
        var cmax: number = Math.max(Math.max(r, g), b)
        var delta: number = cmax - cmin
        var h = 0.0
        var s = 0.0
        var l = 0.0

        if (delta == 0.0){
            h = 0.0
        } else if (cmax == r){
            h = ((g - b) / delta % 6.0)
        } else if (cmax == g){
            h = ((b - r) / delta + 2.0)
        } else{
            h = ((r - g) / delta + 4.0)
        }

        h = Math.round((h * 60.0))
        if (h < 0.0) h += 360.0

        l = (cmax + cmin) / 2.0

        if (delta == 0.0){
            s = 0.0
        } else {
            s = ((delta) / ((1.0 - Math.abs(2.0 * l - 1.0))))
        }

        l = ((l * 100.0))
        s = (s * 100.0)

        res.set(h, s, l);
        return res
    }

    rgbToHsl(color: Vector3){
        var r = color.x / 255.0
        var g = color.y / 255.0
        var b = color.z / 255.0
        var max = 0
        if (r > g && r > b){
            max = r
        } else if (g > b){
            max = g
        }  else {
            max = b
        }
        var min = 0
        if (r < g && r < b){ 
            min = r
        } else if (g < b) {
            min = g
        } else {
            min = b
        }

        var h: number
        var s: number
        var l: number
        l = (max + min) / 2.0
        if (max == min) {
            s = 0.0
            h = s
        } else {
            var d = max - min

            if (l > 0.5){
                s = d / (2.0 - max - min)
            } else{
                s = d / (max + min)
            }
            
            if (r > g && r > b){  
                h = (g - b) / d
                if(g < b){
                    h += 6.0
                }
            } else if (g > b){
                    h = (b - r) / d + 2.0
            } else {
                    h = (r - g) / d + 4.0
            }

            h /= 6.0
        }

        color.set(h * 360.0, s * 100.0, l * 100.0)
    }
}

window.onload = () => {
    var main = main || new Main();

    var settingButton = document.getElementById('setting-button') as any;
    var aboutButton = document.getElementById('about-button') as any;
    var aboutCollapse = document.getElementById('about-collapse') as any;
    var colorFormCollapse = document.getElementById('color-form-collapse') as any;
    var aboutCollapseBs = new bootstrap.Collapse(aboutCollapse, {toggle: false});
    var colorFormCollapseBs = new bootstrap.Collapse(colorFormCollapse, {toggle: false});


    aboutCollapse.addEventListener('show.bs.collapse', function () {
        aboutButton.classList.add("background-color-selected");
        settingButton.classList.remove("background-color-selected");

        colorFormCollapseBs.hide();
    })
    colorFormCollapse.addEventListener('show.bs.collapse', function () {
        settingButton.classList.add("background-color-selected");
        aboutButton.classList.remove("background-color-selected");

        aboutCollapseBs.hide();
    })

    aboutCollapse.addEventListener('hide.bs.collapse', function () {
        aboutButton.classList.remove("background-color-selected");
    })
    colorFormCollapse.addEventListener('hide.bs.collapse', function () {
        settingButton.classList.remove("background-color-selected");
    })

    var form = form || new Form();
    form.update();
};