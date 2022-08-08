package com.example.tamagochi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    ImageView pet;
    TextView hunger;
    TextView boring;
    TextView happy;
    TextView tired;
    TextView time;
    TextView besttime;
    Button btn1;
    Button btnSleep;
    Image[] pets = new Image[9];
    LivePoints hp = new LivePoints();
    LivePriority lv = new LivePriority();
    int timeSp = 0;

    public class CheckThread extends Thread {
        public void run(){
            while(true){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (lv.hungerPr == 2) btn1.setEnabled(true);
                        else btn1.setEnabled(false);
                    }
                });
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class SpeedThread extends Thread{
        public void run(){
            while (lv.hungerPr != 2){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeSp += 1;
                        if (timeSp == 30) {
                            hp.speedH = hp.speedH / 3;
                            hp.speedB = hp.speedB / 3;
                            hp.speedHap = hp.speedHap / 3;
                            hp.speedT = hp.speedT / 3;
                            timeSp = 0;
                        }
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            timeSp = 0;
            hp.speedH = 1000;
            hp.speedB = 1000;
            hp.speedHap = 1000;
            hp.speedT = 1000;
        }
    }

    public class MyThreadH extends Thread {
        public void run(){
            while (lv.hungerPr != 2){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (hp.hungerBar <= 0) {
                            hp.hungerBar = 0;
                            hp.hpWhileAlive = 0;
                        }

                        if (hp.hungerBar > 50) {
                            if (hp.hungerBar >= 100) hp.hungerBar = 100;
                            lv.hungerPr = 0;
                        }

                        if (hp.hungerBar <= 50 && lv.sleepPr != 1){
                            lv.hungerPr = 1;
                            pet.setImageResource(pets[1].imageId);
                        }
                        if (hp.hungerBar == 0){
                            hp.hungerBar = 0;
                            lv.hungerPr = 2;
                            hp.hpWhileAlive = 0;
                            pet.setImageResource(pets[2].imageId);
                        }
                        hp.hungerBar -= hp.hpWhileAlive;
                        String l1 = String.valueOf(hp.hungerBar);
                        hunger.setText("Голод: " + l1);
                    }
                });
                try {
                    Thread.sleep(hp.speedH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class MyThreadB extends Thread {
        public void run(){
            while (lv.hungerPr != 2){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hp.boringBar <= 0) {
                            hp.boringBar = 0;
                        }
                        if (hp.boringBar < 60) lv.boringPr = 0;
                        if (hp.boringBar >= 100){
                            hp.boringBar = 100;
                            hp.hpWhileBoring = 0;
                            hp.wasBored += 1;
                            hungerDebuff();
                        }
                        if (hp.boringBar < 100 && hp.wasBored >= 1) {
                            hp.wasBored = 0;
                            hp.speedH /= hp.debufH;
                        }
                        if (hp.boringBar >= 60 && lv.sleepPr != 1 && lv.hungerPr == 0){
                            pet.setImageResource(pets[5].imageId);
                        }
                        if (lv.hungerPr == 2) hp.hpWhileBoring = 0;
                        hp.boringBar += hp.hpWhileBoring;
                        String l2 = String.valueOf(hp.boringBar);
                        boring.setText("Скука: " + l2);
                    }
                });
                try {
                    Thread.sleep(hp.speedB);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class MyThreadHap extends Thread {
        public void run(){
            while (lv.hungerPr != 2){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hp.happyBar <= 0){
                            hp.happyBar = 0;
                            hp.hpWhileHappy = 0;
                            hp.wasSad += 1;
                            tiredDebuff();
                        }
                        if (hp.happyBar > 0 && hp.wasSad >= 1){
                            hp.wasSad = 0;
                            hp.speedT /= hp.debufT;
                        }
                        if (hp.happyBar >= 100) hp.happyBar = 100;
                        if (hp.happyBar <= 55 && lv.sleepPr != 1 && lv.hungerPr == 0){
                            if (hp.happyBar <= 0){
                                hp.happyBar = 0;
                                hp.hpWhileHappy = 0;
                                pet.setImageResource(pets[7].imageId);
                            }
                            pet.setImageResource(pets[6].imageId);
                        }
                        if (lv.hungerPr == 2) hp.hpWhileHappy = 0;
                        hp.happyBar -= hp.hpWhileHappy;
                        String l3 = String.valueOf(hp.happyBar);
                        happy.setText("Счастье: " + l3);
                    }
                });
                try {
                    Thread.sleep(hp.speedHap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class MyThreadT extends Thread {
        public void run(){
            while (lv.hungerPr != 2 ){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hp.tiredBar <= 35 && lv.sleepPr != 1 && lv.hungerPr == 0){
                            pet.setImageResource(pets[3].imageId);
                        }
                        if (hp.tiredBar <= 0){
                            hp.tiredBar = 0;
                            hp.hpWhileSleep = 0;
                            if (lv.sleepPr == 1 && lv.hungerPr != 2){
                                hp.hpWhileSleep = 0;
                                pet.setImageResource(pets[4].imageId);
                            }
                            if (lv.sleepPr == 0 && lv.hungerPr != 2) {
                                StartSleep();
                            }
                        }
                        if (lv.hungerPr == 2) hp.hpWhileSleep = 0;
                        hp.tiredBar -= hp.hpWhileSleep;
                        String l4 = String.valueOf(hp.tiredBar);
                        tired.setText("Бодрость: " + l4);

                    }
                });
                try {
                    Thread.sleep(hp.speedT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class MyThreadBestTime extends Thread {
        public void run(){
            while (lv.hungerPr != 2){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lv.hungerPr != 2){
                            lv.seconds += lv.whileTime;

                            if (lv.seconds == 60){
                                lv.minutes += lv.whileTime;
                                lv.seconds = 0;
                            }
                            if (lv.minutes == 60){
                                lv.hours += lv.whileTime;
                                lv.minutes = 0;
                            }
                            if (lv.seconds >= lv.currentSeconds){
                                lv.currentSeconds = lv.seconds;
                            }
                            if (lv.minutes > lv.currentMinutes){
                                lv.currentMinutes = lv.minutes;
                                lv.currentSeconds = lv.seconds;
                            }
                            if (lv.hours > lv.currentHours){
                                lv.currentHours = lv.hours;
                                lv.currentMinutes = lv.minutes;
                                lv.currentSeconds = lv.seconds;
                            }
                            time.setText("Часы: "+ lv.hours + " " +
                                    "Минуты: " + lv.minutes + " " +
                                    "Секунды: "+ lv.seconds);
                        }
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            besttime.setText("Часы: "+ lv.currentHours + " " +
                    "Минуты: " + lv.currentMinutes + " " +
                    "Секунды: "+ lv.currentSeconds);
            lv.hours = 0;lv.minutes = 0;lv.seconds = 0;
            lv.currentHours = 0;lv.currentMinutes = 0;lv.currentSeconds = 0;
        }
    }

    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pet = findViewById(R.id.Pet);
        hunger = findViewById(R.id.timerHunger);
        boring = findViewById(R.id.timerBoring);
        happy = findViewById(R.id.timerHappy);
        tired = findViewById(R.id.timerTired);
        btn1 = findViewById(R.id.Start);
        btnSleep = findViewById(R.id.Sleep);
        time = findViewById(R.id.time);
        besttime = findViewById(R.id.besttime);

        pets[0] = new Image(R.drawable.catnormal);
        pets[1] = new Image(R.drawable.cathunger);
        pets[2] = new Image(R.drawable.catdead);
        pets[3] = new Image(R.drawable.cattired);
        pets[4] = new Image(R.drawable.catsleeping);
        pets[5] = new Image(R.drawable.catboring);
        pets[6] = new Image(R.drawable.catlittledepressed);
        pets[7] = new Image(R.drawable.catdepressed);

        pet.setImageResource(pets[0].imageId);


        btnSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSleep();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reset();

            }
        });
    }

    public void Feed(View view){
        if (lv.sleepPr !=1){
            hp.hungerBar += 25;
            hp.hpWhileAlive = 1;
        }
    }
    public void PlayPl(View view) {
        if (lv.sleepPr !=1){
            hp.hpWhileBoring = 1;
            hp.boringBar -= 25;
            hp.tiredBar -= 20;
        }
    }
    public void Petpet(View view){
        if (lv.sleepPr !=1){
            hp.hpWhileHappy = 1;
            hp.happyBar += 25;
        }

    }


    public void StartSleep(){
        lv.sleepPr = 1;
        hp.hpWhileSleep = 0;
        timer = new CountDownTimer(10000,500) {
            @Override
            public void onTick(long l) {
                if (lv.hungerPr == 2) {
                    timer.cancel();
                    btnSleep.setEnabled(true);
                }
                else {
                    btnSleep.setEnabled(false);
                    hp.tiredBar += 1;
                    if (hp.tiredBar > 100) hp.tiredBar = 100;
                    pet.setImageResource(pets[4].imageId);
                    String l5 = String.valueOf(hp.tiredBar);
                    tired.setText( "Бодрость: " + l5);
                }
            }
            @Override
            public void onFinish() {
                btnSleep.setEnabled(true);
                lv.sleepPr = 0;
                hp.hpWhileSleep = 1;
                if (lv.hungerPr == 2) pet.setImageResource(pets[2].imageId);
                if (lv.allPr == 0) pet.setImageResource(pets[0].imageId);
                String l5 = String.valueOf(hp.tiredBar);
                tired.setText( "Бодрость: " + l5);
            }
        }.start();
    }

    public void reset(){
        hp.wasSad = 0;
        hp.wasBored = 0;
        hp.hpWhileAlive = 1;
        hp.hpWhileBoring = 1;
        hp.hpWhileHappy = 1;
        hp.hpWhileTired = 1;
        hp.hpWhileSleep = 1;
        lv.hungerPr = 0;
        lv.boringPr = 0;
        lv.happyPr = 0;
        lv.tiredPr = 0;
        lv.sleepPr = 0;
        lv.hungerPr = 0;
        hp.hungerBar = 100;
        hp.boringBar = 0;
        hp.happyBar = 100;
        hp.tiredBar = 100;

        pet.setImageResource(pets[0].imageId);

        SpeedThread speedThread = new SpeedThread();
        speedThread.start();

        CheckThread ch = new CheckThread();
        ch.start();

        MyThreadH myThreadH = new MyThreadH();
        myThreadH.start();

        MyThreadB myThreadB = new MyThreadB();
        myThreadB.start();

        MyThreadHap myThreadHap = new MyThreadHap();
        myThreadHap.start();

        MyThreadT myThreadT = new MyThreadT();
        myThreadT.start();

        MyThreadBestTime myThreadTime = new MyThreadBestTime();
        myThreadTime.start();
    }

    public void hungerDebuff(){
        if (hp.wasBored == 1) hp.speedH *= hp.debufH;
    }
    public void tiredDebuff(){
        if (hp.wasSad == 1) hp.speedT *= hp.debufT;
    }
}
