package edu.issc711.integrador

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.location.modes.CameraMode
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.*
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import edu.issc711.integrador.Models.Juego
import edu.issc711.integrador.Models.Jugador
import edu.issc711.integrador.Models.Pregunta
import kotlinx.android.synthetic.main.activity_game_map.*
import java.io.FileNotFoundException
import java.util.ArrayList

class GameMapActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener, LocationEngineListener, OnCompleteListener<DocumentSnapshot>{
    companion object {
        var currentQuestion = Pregunta()
        var currentPlayer = Jugador()
        var correctAnswer: Int = -1
        var preguntasId : ArrayList<String> = ArrayList()
    }

    private val db = FirebaseFirestore.getInstance()
    private var jugador: Int = -1
    private var fromUnity: Boolean? = null
    private var updateLocation: Boolean = false
    private var updateScore: Boolean = false
    private var getJuegos: Boolean = false
    private var juegoId : String = ""

    private lateinit var Question: Pregunta
    private lateinit var QuestionId: String
    private lateinit var mapView: MapView
    private lateinit var map: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var originLocation: Location



    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext,getString(R.string.mapbox_token))
        setContentView(R.layout.activity_game_map)
        findViewById<Button>(R.id.btnRealidadAumentada).isEnabled=false
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        jugador = intent.getIntExtra("jugador",-1)
        fromUnity = intent.getBooleanExtra("fromUnity",false)
        correctAnswer = intent.getIntExtra("respCorr",-1)

        findViewById<Button>(R.id.btnRealidadAumentada).setOnClickListener({
            startUnity()
        })

        findViewById<Button>(R.id.btnRegresarMenu).setOnClickListener({
            val intent = Intent(this,MainMenuActivity::class.java)
            intent.putExtra("Player", jugador)
            intent.putExtra("respCorr", Question.getCorrect())
            startActivity(intent)
            //finish()
        })

        if(fromUnity==true)
            fromUnityConfiguration(intent.getStringExtra("answer"))
    }

    private fun startUnity(){
        val intent = Intent(this,UnityPlayerActivity::class.java)
        intent.putExtra("question",Question.getQuestion())
        intent.putExtra("opt1",Question.getOpt1())
        intent.putExtra("opt2",Question.getOpt2())
        intent.putExtra("opt3",Question.getOpt3())
        intent.putExtra("opt4",Question.getOpt4())
        intent.putExtra("respCorr",Question.getCorrect())
        intent.putExtra("jugador",jugador)
        intent.putExtra("respCorr", correctAnswer)
        startActivity(intent)
    }

    @SuppressLint("WrongViewCast")
    private fun fromUnityConfiguration(respuesta: String)
    {
        Question = currentQuestion

        if(preguntasId.count()>0){

            val foundString = preguntasId.find { ss -> ss == Question.getId() }
            if(foundString!=null)
            {
                Toast.makeText(this, "No hagas trampa porfa", Toast.LENGTH_LONG).show()
                return
            }

        }
        preguntasId.add(Question.getId())
        var texto = "Error"
        when (correctAnswer){
            1 -> {
                if(respuesta==Question.getOpt1()){
                    texto = "Respuesta Correcta"
                    currentPlayer.score = currentPlayer.score+100
                }
                else{
                    texto = "Incorrecto, era ${Question.getOpt1()}"
                }
            }
            2 -> {
                if(respuesta==Question.getOpt2()){
                    texto = "Respuesta Correcta"
                    currentPlayer.score = currentPlayer.score+100
                }
                else{
                    texto = "Incorrecto, era ${Question.getOpt2()}"
                }
            }
            3 -> {
                if(respuesta==Question.getOpt3()){
                    texto = "Respuesta Correcta"
                    currentPlayer.score = currentPlayer.score+100
                }
                else{
                    texto = "Incorrecto, era ${Question.getOpt3()}"
                }
            }
            4 -> {
                if(respuesta==Question.getOpt4()){
                    texto = "Respuesta Correcta"
                    currentPlayer.score = currentPlayer.score+100
                }
                else{
                    texto = "Incorrecto, era ${Question.getOpt4()}"
                }
            }
            else -> print("ERROR")
        }
        updateLocation=false
        updateScore=true

        val snapshot = db.collection("jugadores").document("player$jugador")
        Thread(Runnable {
            Tasks.await(snapshot.update("score", currentPlayer.score))
        }).start()

        Toast.makeText(this, "Jugador $jugador "+texto, Toast.LENGTH_LONG).show()
        findViewById<TextView>(R.id.lblPregunta).text = "Ve al nuevo punto"
        findViewById<TextView>(R.id.lblScore).text = currentPlayer.score.toString()+" puntos"

        /*val fileOutput = openFileOutput(juegoId, Context.MODE_PRIVATE)
        fileOutput.write(currentQuestion.getId().toByteArray())
        fileOutput.close()*/

    }

    @SuppressWarnings("MissingPermission")
    private fun enableLocation() = if(PermissionsManager.areLocationPermissionsGranted(this))
    {
        try {
            val options = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .build()
            // Get an instance of the component
            val locationComponent = map.locationComponent
            // Activate with options
            locationComponent.activateLocationComponent(this, options)
            // Enable to make component visible
            locationComponent.isLocationComponentEnabled = true

        //locationComponent.setLocationComponentEnabled(true)
            val location: Location

             location = locationComponent.lastKnownLocation as Location
            setCameraPosition(location)

            val motor = locationComponent.locationEngine
            motor?.requestLocationUpdates()
            motor?.addLocationEngineListener(this)
            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.NORMAL
        }
        catch (e: Exception)
        {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            findViewById<Button>(R.id.btnRegresarMenu).callOnClick()
        }
    }
    else
    {
        permissionsManager = PermissionsManager(this)
        permissionsManager.requestLocationPermissions(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap?) {
        map = mapboxMap as MapboxMap
        enableLocation()
        getFirebaseCollections()
    }


    private fun setCameraPosition(location: Location)
    {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude),13.0))
    }

    override fun onPermissionResult(granted: Boolean) {
        if(granted)
        {
            enableLocation()
        }
        else {
            Toast.makeText(this, "Necesitamos permiso para obtener tu ubicacion", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getFirebaseCollections()
    {
        val jugadorName = "player$jugador"
        db.collection("jugadores").document(jugadorName).get().addOnCompleteListener(this)

        db.collection("juegos").addSnapshotListener(this,EventListener<QuerySnapshot>{querySnapshot,error ->
            if (error != null){
                return@EventListener
            }else if(!querySnapshot.isEmpty){
                val juegos = ArrayList<Juego>()
                for(doc:DocumentSnapshot in querySnapshot){
                    var juego = doc.toObject(Juego::class.java)
                    juego.setId(doc.id)
                    juego.setPreguntaId(doc.getString("pregunta"))
                    juego.setTiempo(doc.getDouble("tiempo").toInt())
                    if(juego.getTiempo()>0)
                        juegos.add(juego)
                }
                if(juegos?.count()==1)
                {
                    getQuestion(juegos.first().getPreguntaId())
                    juegoId = juegos.first().getId()
                }
                else{
                    val intent = Intent(this,MainMenuActivity::class.java)
                    intent.putExtra("jugador",jugador)
                    startActivity(intent)
                    mapView.onDestroy()
                    finish()
                }
            }
        })

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()

    }


    override fun onComplete(p0: Task<DocumentSnapshot>) {
        if(p0.isSuccessful) {
            if(updateLocation){
                val doc = p0.result
                if (doc.exists()){
                    doc.reference.update("ubicacion",GeoPoint(originLocation.latitude,originLocation.longitude))
                }
            }
            else {
                val doc = p0.result
                if (doc.exists()){
                    val jugador = Jugador()
                    jugador.isConnected = doc.getBoolean("isConnected")
                    jugador.ubicacion = doc.getGeoPoint("ubicacion")
                    jugador.score = doc.getDouble("score").toInt()
                    currentPlayer = jugador
                    val puntos = "${currentPlayer.score} puntos"
                    this.findViewById<TextView>(R.id.lblScore).text = puntos
                }
            }
        }
    }

    private fun getQuestion(questionId:String)
    {
        getJuegos=false
        QuestionId = questionId
        db.collection("preguntas").get().addOnCompleteListener({
            if(it.isSuccessful){
                val preguntas = ArrayList<Pregunta>()
                for(doc:DocumentSnapshot in it.result){
                    val pregunta = Pregunta()//doc.toObject(Pregunta::class.java)
                    pregunta.setCorrect(doc.getString("correct"))
                    pregunta.setLocation(doc.getGeoPoint("location"))
                    pregunta.setLugar(doc.getString("lugar"))
                    pregunta.setOpt1(doc.getString("opt1"))
                    pregunta.setOpt2(doc.getString("opt2"))
                    pregunta.setOpt3(doc.getString("opt3"))
                    pregunta.setOpt4(doc.getString("opt4"))
                    pregunta.setQuestion(doc.getString("question"))
                    pregunta.setId(doc.id)
                    preguntas.add(pregunta)
                }

                for(pregunta:Pregunta in preguntas){
                    if(pregunta.getId()==QuestionId)
                    {
                        Question = pregunta
                        currentQuestion = pregunta
                        map.clear()
                        map.addMarker(MarkerOptions().position(LatLng(pregunta.getLocation().latitude,pregunta.getLocation().longitude)).title(pregunta.getQuestion()))
                        correctAnswer = currentQuestion.getCorrect().toInt()
                        findViewById<Button>(R.id.btnRealidadAumentada).isEnabled=false
                        val texto = "Camina al punto" as String
                        this.findViewById<TextView>(R.id.lblPregunta).text = texto
                        //ToogleVrButton()
                        break
                    }
                }
            }
        })//addSnapshotListener(this)
    }

    override fun onLocationChanged(location: Location?) {

        updateLocation = true
        originLocation = location as Location
        val snapshot = db.collection("jugadores").document("player$jugador").get().addOnCompleteListener(this)
        try {
            if(Question?.getId()!="")
            {
                ToogleVrButton()
            }
        }
        catch (Ex: Exception){
            Log.d("Question","No Question Yet")
        }

    }

    fun ToogleVrButton(){
        try{
            val TargetLocation = Location("LocationTarget")
            TargetLocation.latitude = Question.getLocation().latitude
            TargetLocation.longitude = Question.getLocation().longitude

            if(originLocation.distanceTo(TargetLocation)<10)
            {
                findViewById<Button>(R.id.btnRealidadAumentada).isEnabled=true
                findViewById<TextView>(R.id.lblPregunta).text = "Usa la AR y contesta"
            }
            else{
                findViewById<Button>(R.id.btnRealidadAumentada).isEnabled=false
                val texto = "Estas a ${originLocation.distanceTo(TargetLocation).toInt()} mts"
                findViewById<TextView>(R.id.lblPregunta).text = texto
            }
        }
        catch (ex: Exception){
            Toast.makeText(this, "Error en la pregunta", Toast.LENGTH_LONG).show()
            Log.d("Error",ex.message)
            findViewById<Button>(R.id.btnRegresarMenu).callOnClick()
        }

    }

    override fun onConnected() {
        Toast.makeText(this, "Algo PASO", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        map.locationComponent.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }



}
