package com.example.da08.musicplayerproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

public class SensorService extends Service implements ShakeDetector.Listener{
    //Shake 감지 센서 및 리스너
    SensorManager shakedSensorManger = null;
    ShakeListner shakeListner;

    //Shake 감지 라이브러리
    ShakeDetector shakeDetector;

    //가속도계 감지 센서
    SensorManager gradientSensorManger = null;
    SensorEventListener eventListener = null;
    Sensor sensor = null;
    //좌우기울기
    double angleXY;

    //효과음
    SoundPool sound;
    int lock,unlock;
    boolean isLocked = false;

    public SensorService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        setAccelometerSensor();
        setEffectSound();
        setShakedSensor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //센서 종료
        gradientSensorManger.unregisterListener(eventListener);
        shakeDetector.stop();
        Log.d("Sensor","센서 및 서비스 종료");
    }

    /**
     * Shake 모션 감지시 자동으로 호출되는 콜백 함수.
     */
    @Override
    public void hearShake() {
        if(isLocked == true){
            sound.play(unlock,1,1,0,0,1);
            isLocked = false;
            gradientSensorManger.registerListener(eventListener,sensor,SensorManager.SENSOR_DELAY_UI);
        }else{
            sound.play(lock,1,1,0,0,1);
            isLocked = true;
            gradientSensorManger.unregisterListener(eventListener);
        }
    }

    /**
     * 사운드 관련 메소드
     */
    private void setEffectSound(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            sound = new SoundPool.Builder().setMaxStreams(2).build();
        }else{
            sound = new SoundPool(10, AudioManager.STREAM_MUSIC,1);
        }
        lock = sound.load(this,R.raw.cat_meow,1);
        unlock = sound.load(this,R.raw.dog_howl,1);
    }

    private void setShakedSensor(){
        shakeDetector = new ShakeDetector(this);
        //센서서비스 시작
        shakedSensorManger = (SensorManager)getSystemService(SENSOR_SERVICE);
        //센서리스너 생성
        shakeListner = new ShakeListner();
        //센서리스너 장입
        shakedSensorManger.registerListener(shakeListner,                    //리스너
                                            Sensor.TYPE_ACCELEROMETER,       //가속도계 사용
                                            SensorManager.SENSOR_DELAY_GAME);//읽어들이는 간격
        //shake 센서시작
        shakeDetector.start(shakedSensorManger);
        Log.d("Sensor","shake 센서 시작");
    }
    private void setAccelometerSensor(){
        //센서 서비스 시작
        gradientSensorManger = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //가속도계 센서 사용
        sensor = gradientSensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //리스너 등록
        eventListener = new AccelometerListener();
        //가속도계 센서 시작
        gradientSensorManger.registerListener(eventListener,sensor,SensorManager.SENSOR_DELAY_UI);
        Log.d("Sensor","가속도계 센서 시작");
    }


    /**
     * 가속도계 센서 리스너
     */
    class AccelometerListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            double accX = event.values[0];
            double accY = event.values[1];

            angleXY = Math.atan2(accX, accY) * 180 / Math.PI;
            if (angleXY > 70){

            }else if(angleXY < -70){

            }else {

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    /**
     * Shake 리스너
     */
    class ShakeListner implements SensorListener{
        @Override
        public void onSensorChanged(int sensor, float[] values) {

        }

        @Override
        public void onAccuracyChanged(int sensor, int accuracy) {

        }
    }
}
