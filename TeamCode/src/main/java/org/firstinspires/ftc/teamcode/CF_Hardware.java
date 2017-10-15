package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by Ryley on 9/15/17.
 */


public class CF_Hardware {
    public DcMotor rightFront = null;
    public DcMotor rightRear = null;
    public DcMotor leftFront = null;
    public DcMotor leftRear = null;

    public DcMotor Winch = null;
    public DcMotor otherWinch = null;

    public Servo Clamp = null;
    public Servo Spinner = null;
    public Servo otherClamp = null;

    CF_Color_Sensor sensor = new CF_Color_Sensor();

    HardwareMap hwMap = null;

    public CF_Hardware() {}

    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        rightFront = hwMap.get(DcMotor.class, "motorOne");
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);

        rightRear = hwMap.get(DcMotor.class, "motorTwo");
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);

        leftRear = hwMap.get(DcMotor.class, "motorThree");
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront = hwMap.get(DcMotor.class, "motorFour");
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        Winch = hwMap.get(DcMotor.class, "Winch");

        Clamp = hwMap.get(Servo.class, "Clamp");

        Spinner = hwMap.get(Servo.class, "Spinner");

        otherWinch = hwMap.get(DcMotor.class, "otherWinch");

        otherClamp = hwMap.get(Servo.class, "otherClamp");

        sensor.adafruitRGB = hwMap.get(ColorSensor.class, "adafruitRGB");

    }



   /***
    * Convenience method to assign motor power for mecanum drive.  Each mecanum
    * wheel is independently driven.
    *
    * @param lFPower Left front mecanum wheel
    * @param rFPower Right front mecanum wheel
    * @param lRPower Left rear mecanum wheel
    * @param rRPower Right Rear mecanum wheel
    */
   public void setMecanumPowers(double lFPower, double rFPower, double lRPower, double rRPower)
   {
      leftFront.setPower(lFPower);
      rightFront.setPower(rFPower);
      leftRear.setPower(lRPower);
      rightRear.setPower(rRPower);
   }


   /***
    * Convenience method for setting encoder runmode for all four mecanum drive motors
    *
    * @param mode Encoder run mode
    */
   public void setMecanumEncoderMode(DcMotor.RunMode mode)
   {
      leftFront.setMode(mode);
      rightFront.setMode(mode);
      leftRear.setMode(mode);
      rightRear.setMode(mode);
   }


   /***
    * Convenience method for setting encoder counts to all four mecanum drive motors
    *
    * @param lFcount Left front encoder counts
    * @param rFcount Right front encoder counts
    * @param lRcount Left rear encoder counts
    * @param rRcount Right rear encoder counts
    */
   public void setMecanumEncoderTargetPosition(int lFcount, int rFcount, int lRcount, int rRcount)
   {
      // Only want to use absolute values.  Take abs of inputs in case user sent negative value.
      leftFront.setTargetPosition(Math.abs(lFcount));
      rightFront.setTargetPosition(Math.abs(rFcount));
      leftRear.setTargetPosition(Math.abs(lRcount));
      rightRear.setTargetPosition(Math.abs(rRcount));
   }

   public void SetPosition()
   {

   }
}