package edu.issc711.integrador.Models

/**
 * Created by Luis Calderon on 03/12/2018.
 */
class Juego {

    private var Id: String = ""
    private var Pregunta: String = ""
    private var Tiempo: Int = 0

    fun setId(option:String){Id=option}
    fun getId():String{return Id}

    fun setPreguntaId(option:String){Pregunta=option}
    fun getPreguntaId():String{return Pregunta}

    fun setTiempo(option:Int){Tiempo=option}
    fun getTiempo():Int{return Tiempo}

}