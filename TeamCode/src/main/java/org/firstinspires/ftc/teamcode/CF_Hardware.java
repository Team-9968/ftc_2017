package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


/**
 * Created by Ryley on 9/15/17.
 */


public class CF_Hardware
{

   //Names all of the motors, sensors, servos, and various other pieces of the robot
   public DcMotor rightFront = null;
   public DcMotor rightRear = null;
   public DcMotor leftFront = null;
   public DcMotor leftRear = null;

   public DcMotor mastMotor = null;
   public DcMotor clawMotor = null;

   public DcMotor tailLight = null;

   public ColorSensor adafruitRGB = null;   //on the left when viewed with the lifter on the left
   public ColorSensor adafruitRGBTwo = null;  //on the right with the lifter on the left

   BNO055IMU imu = null;

   public Servo clamp = null;
   public Servo lowerClamp = null;
   public Servo jewelHitter;
   public Servo colorArm;
   public Servo upperRightClaw = null;
   public Servo upperLeftClaw = null;
   public Servo lowerRightClaw = null;
   public Servo lowerLeftClaw = null;

   public DigitalChannel limit = null;
   HardwareMap hwMap = null;

   public CF_Hardware() {}

   public void init(HardwareMap ahwMap)
   {
      hwMap = ahwMap;

      //Sets motor directions and gives the names (In purple) of what each piece will be
      //named in the configuration file. If these names are off by even one letter, that piece of hardware
      //will not run.
      rightFront = hwMap.get(DcMotor.class, "motorOne");
      rightFront.setDirection(DcMotorSimple.Direction.FORWARD);

      rightRear = hwMap.get(DcMotor.class, "motorTwo");
      rightRear.setDirection(DcMotorSimple.Direction.FORWARD);

      leftRear = hwMap.get(DcMotor.class, "motorThree");
      leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

      leftFront = hwMap.get(DcMotor.class, "motorFour");
      leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

      mastMotor = hwMap.get(DcMotor.class, "mastMotor");
      mastMotor.setDirection(DcMotorSimple.Direction.FORWARD);

      clawMotor = hwMap.get(DcMotor.class, "clawMotor");
      clawMotor.setDirection(DcMotorSimple.Direction.FORWARD);

      tailLight = hwMap.get(DcMotor.class, "tailLight");
      tailLight.setDirection(DcMotorSimple.Direction.FORWARD);


      adafruitRGB = hwMap.get(ColorSensor.class, "adafruitRGB");

      adafruitRGBTwo = hwMap.get(ColorSensor.class, "adafruitRGBTwo");

      clamp = hwMap.get(Servo.class, "clamp");

      lowerClamp = hwMap.get(Servo.class, "lowerClamp");

      jewelHitter = hwMap.get(Servo.class, "jewelHitter");

      colorArm = hwMap.get(Servo.class, "arm");

      upperRightClaw = hwMap.get(Servo.class, "upperRightClaw");
      upperLeftClaw = hwMap.get(Servo.class, "upperLeftClaw");
      lowerRightClaw = hwMap.get(Servo.class, "lowerRightClaw");
      lowerLeftClaw = hwMap.get(Servo.class, "lowerLeftClaw");

      limit = hwMap.get(DigitalChannel.class, "limit");


      //makes sure the LEDS on the sensors are off so we don't blind the drivers
      adafruitRGB.enableLed(false);
      adafruitRGBTwo.enableLed(false);

      //sets init position of the servos so we stay within 18 inches
      colorArm.setPosition(0.90);
      jewelHitter.setPosition(0.333);


      BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
      parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
      parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
      parameters.calibrationDataFile = "BNO055IMUCalibration.json";
      parameters.loggingEnabled = true;
      parameters.loggingTag = "IMU";
      parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

      imu = hwMap.get(BNO055IMU.class, "imu");
      imu.initialize(parameters);

   }

   public boolean isArmDown(float targetPos) {
       if(colorArm.getPosition() < targetPos) {
           return TRUE;
       } else {
           return FALSE;
       }
   }

   public boolean isArmUp(float targetPos) {
       if(colorArm.getPosition() > targetPos) {
           return TRUE;
       } else {
           return FALSE;
       }
   }

   public boolean isJewelHitterAtPos(float targetPos){
      if(jewelHitter.getPosition() + 0.01 > targetPos && jewelHitter.getPosition() - 0.01 < targetPos) {
         return TRUE;
      } else {
         return FALSE;
      }
   }

   public void armDown (double targetPos)
   {
      double currentPos = colorArm.getPosition();

      while (currentPos > targetPos)
      {
         {
            currentPos = colorArm.getPosition();
            colorArm.setPosition(currentPos - 0.0009);
         }
      }
   }

   public void armUp (double targetPos)
   {
      double currentPos = colorArm.getPosition();

      while (currentPos < targetPos)
      {
         {
            currentPos = colorArm.getPosition();
            colorArm.setPosition(currentPos + 0.0009);
         }
      }
   }
}