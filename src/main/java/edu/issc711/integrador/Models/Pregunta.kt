package edu.issc711.integrador.Models

import com.google.firebase.firestore.GeoPoint

/**
 * Created by Luis Calderon on 03/12/2018.
 */
class Pregunta {
    private var Id: String = ""
    private var Opt1: String = ""
    private var Opt2: String = ""
    private var Opt3: String = ""
    private var Opt4: String = ""
    private var Question: String = ""
    private var Location: GeoPoint = GeoPoint(0.0,0.0)
    private var Lugar: String = ""
    private var Correct: String = ""

    fun setId(option:String){Id=option}
    fun getId():String{return Id}

    fun setOpt1(option:String){Opt1=option}
    fun getOpt1():String{return Opt1}

    fun setOpt2(option:String){Opt2=option}
    fun getOpt2():String{return Opt2}

    fun setOpt3(option:String){Opt3=option}
    fun getOpt3():String{return Opt3}

    fun setOpt4(option:String){Opt4=option}
    fun getOpt4():String{return Opt4}

    fun setQuestion(option:String){Question=option}
    fun getQuestion():String{return Question}

    fun setCorrect(option:String){Correct=option}
    fun getCorrect():String{return Correct}

    fun setLugar(option:String){Lugar=option}
    fun getLugar():String{return Lugar}

    fun setLocation(option:GeoPoint){Location=option}
    fun getLocation():GeoPoint{return Location}

}