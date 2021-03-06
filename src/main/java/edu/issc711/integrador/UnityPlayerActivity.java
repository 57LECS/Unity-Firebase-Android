package edu.issc711.integrador;

import com.unity3d.player.*;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.Console;

public class UnityPlayerActivity extends Activity
{
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    String stringFromUnity = "";
    String txtP = "pregunta";
    String txtR1 = "boton1";
    String txtR2 = "boton2";
    String txtR3 = "boton3";
    String txtR4 = "boton4";
    Integer jdr = -1;
    Integer resp = -1;

    // Setup activity layout
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mUnityPlayer = new UnityPlayer(this);
        setContentView(mUnityPlayer);
        setDatos();

    }

    public void setDatos(){
        txtP = getIntent().getStringExtra("question");
        txtR1 = getIntent().getStringExtra("opt1");
        txtR2 = getIntent().getStringExtra("opt2");
        txtR3 = getIntent().getStringExtra("opt3");
        txtR4 = getIntent().getStringExtra("opt4");
        jdr = getIntent().getIntExtra("jugador",-1);
        resp = getIntent().getIntExtra("respCorr",-1);
        String asd = getIntent().getStringExtra("respCorr");
        mUnityPlayer.requestFocus();
        mUnityPlayer.UnitySendMessage("ImageTarget","setPregunta",txtP);
        mUnityPlayer.UnitySendMessage("ImageTarget","setRespuesta1",txtR1);
        mUnityPlayer.UnitySendMessage("ImageTarget","setRespuesta2",txtR2);
        mUnityPlayer.UnitySendMessage("ImageTarget","setRespuesta3",txtR3);
        mUnityPlayer.UnitySendMessage("ImageTarget","setRespuesta4",txtR4);
    }


    public void setStringFromUnity(String input){
        stringFromUnity = input;
        Intent intent = new Intent(this,GameMapActivity.class);
        intent.putExtra("answer",stringFromUnity);
        intent.putExtra("respCorr",getIntent().getStringExtra("respCorr"));
        intent.putExtra("fromUnity",true);
        intent.putExtra("jugador",jdr);
        intent.putExtra("respCorr",resp);
        startActivity(intent);
        //finish();
        //Toast.makeText(this,stringFromUnity,Toast.LENGTH_SHORT).show();
    }


    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        setDatos();
        mUnityPlayer.resume();
    }

    @Override protected void onStart()
    {
        super.onStart();
        mUnityPlayer.start();
    }

    @Override protected void onStop()
    {
        super.onStop();
        mUnityPlayer.stop();

    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
}
