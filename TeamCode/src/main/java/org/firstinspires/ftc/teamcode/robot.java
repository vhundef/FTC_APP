package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class robot extends LinearOpMode {
    protected static final int LED_CHANNEL = 5;
    protected DcMotor m1Drive = null;
    protected DcMotor m2Drive = null;
    protected DcMotor m3Drive = null;
    protected DcMotor m4Drive = null;
    protected CRServo s1TopClaw = null;
    protected Servo s4Kicker = null;
    protected Servo s3Rotation = null;
    protected Servo s5Shovel = null;
    protected DcMotor m6Intake = null;
    protected ColorSensor sensorRGB;
    protected OpticalDistanceSensor odsSensor;  // Hardware Device Object
    protected float hsvValues[] = {0F, 0F, 0F};
    private String log = "";


    protected void putBox() {
        setMotorsPowerTimed(-0.2, 0.2, 0.2, -0.2, 1200);//движение назад
        sleep(100);
        setMotorsPowerTimed(0.2, -0.2, -0.2, 0.2, 300);//движение вперёд
        rotateClaw(0);
        setMotorsPowerTimed(0.2, -0.2, -0.2, 0.2, 300);//движение вперёд
        setMotorsPowerTimed(-0.2, 0.2, 0.2, -0.2, 600);//движение назад
        setMotorsPowerTimed(0.1, -0.1, -0.1, 0.1, 500);//движение вперёд
        rotateClaw(0.8);
    }

    // Rotate claw
    protected void rotateClaw(double rotate) { //if rotate true then rotate to  180 . else to 0
        s3Rotation.setPosition(rotate);
    }

    protected void setMotorsPower(double D1_power, double D2_power, double D3_power, double D4_power) { //Warning: Эта функция включит моторы но, выключить их надо будет после выполнения какого либо условия
        // Send power to wheels
        m1Drive.setPower(D1_power);
        m2Drive.setPower(D2_power);
        m3Drive.setPower(D3_power);
        m4Drive.setPower(D4_power);
    }

    protected void setMotorsPowerTimed(double m1_power, double m2_power, double m3_power, double m4_power, long ms) {
        m1Drive.setPower(m1_power);
        m2Drive.setPower(m2_power);
        m3Drive.setPower(m3_power);
        m4Drive.setPower(m4_power);
        sleep(ms);
        chassisStopMovement();
    }

    protected void chassisStopMovement() {
        m1Drive.setPower(0);
        m2Drive.setPower(0);
        m3Drive.setPower(0);
        m4Drive.setPower(0);
    }

    protected void log(String WhatToSave, Double Value) {
        log += WhatToSave + ": " + Value + "\n";
    }

    protected void log(String WhatToSave) {
        log += WhatToSave + "\n";
    }

    protected String printLog() {
        return log;
    }

    protected String getColor() {

        double[] hue_arr = new double[5];
        //для точности 4 измерения
        for (int j = 0; j < 4; j++) {
            // convert the RGB values to HSV values.
            telemetry.addData("Blue", sensorRGB.blue());
            telemetry.addData("Red", sensorRGB.red());
            telemetry.update();
            sleep(500);
            Color.RGBToHSV((sensorRGB.red() * 255) / 800, (sensorRGB.green() * 255) / 800, (sensorRGB.blue() * 255) / 800, hsvValues);
            double hue = hsvValues[0];
            hue_arr[j] = hue;
        }

        //Находим среднее арифметическое

        double hue_sr = 0;
        for (int j = 0; j < 4; j++) {
            hue_sr += hue_arr[j];
        }
        hue_sr = hue_sr / 4;
        //
        if (hue_sr > 110 && hue_sr < 290) {
            return "Blue";
        } else {
            return "Red";
        }
    }


    protected void initHW(HardwareMap hardwMap) throws RuntimeException {
        m1Drive = hardwMap.get(DcMotor.class, "m1 drive");
        m2Drive = hardwMap.get(DcMotor.class, "m2 drive");
        m3Drive = hardwMap.get(DcMotor.class, "m3 drive");
        m4Drive = hardwMap.get(DcMotor.class, "m4 drive");
        s1TopClaw = hardwMap.get(CRServo.class, "s1 top claw");
        s4Kicker = hardwMap.get(Servo.class, "s4 kick");
        odsSensor = hardwMap.get(OpticalDistanceSensor.class, "sensor_ods");
        s3Rotation = hardwMap.get(Servo.class, "s3 rotation");
        s5Shovel = hardwMap.get(Servo.class, "s5 shovel");
        m6Intake = hardwMap.get(DcMotor.class, "m6 intake");
        sensorRGB = hardwMap.get(ColorSensor.class, "sensor_color");
    }
}