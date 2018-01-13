package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.vuforia.STORAGE_TYPE;

import java.security.cert.CertStoreParameters;

/**
 * Created by Ryley on 10/22/17.
 */

public class CF_Autonomous_Motor_Library {
   CF_Master_Motor_Library motors = new CF_Master_Motor_Library();
   CF_IMU_Library imuLib = new CF_IMU_Library();


   enum mode {
      DRIVE, STRAFE, ROTATE
   }

   void EncoderIMUDrive(CF_Hardware robot, mode m, float power, int counts) {
      // Enum tells the method what operation it wants the robot to perform
      // DRIVE
      double RFPower = 0;
      double LFPower = 0;
      double RRPower = 0;
      double LRPower = 0;

      if(m == mode.DRIVE) {
         // This is a trial and error value
         double kP = 0.05;
         // Values for the P controller
         double error = 0;
         double gain = error * kP;
         // Reset encoders
         motors.setMode(robot, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
         motors.setMode(robot, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
         // The offset for the encoder counts
         double offset = motors.getEncoderCounts(robot, 1);
         // Gets the rotation in the Z axis(Up and down through the center of the bot)
         imuLib.updateNumbers(robot);
         double rot = imuLib.getRotation(3);
         // This logic should work whether the encoder counts are positive or negative, and either way they go
         while((motors.getEncoderCounts(robot, 1) - offset) < counts && (motors.getEncoderCounts(robot, 1) - offset) > (-1 * counts)) {
            imuLib.updateNumbers(robot);
            error = imuLib.getRotation(3) - rot;
            gain = error * kP;
            RFPower = power + gain;
            LFPower = power - gain;
            RRPower = power + gain;
            LRPower = power - gain;
            motors.setMechPowers(robot, 1, LFPower, RFPower, LRPower, RRPower, 0);
         }
      }

      // STRAFE
      else if(m == mode.STRAFE) {
         double kP = 0.05;
         double error = 0;
         double gain = error * kP;
         motors.setMode(robot, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
         motors.setMode(robot, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
         double offset = motors.getEncoderCounts(robot, 1);
         imuLib.updateNumbers(robot);
         double rot = imuLib.getRotation(3);
         while((motors.getEncoderCounts(robot, 1) - offset) < counts && (motors.getEncoderCounts(robot, 1) - offset) > (-1 * counts)) {
            imuLib.updateNumbers(robot);
            error = imuLib.getRotation(3) - rot;
            gain = error * kP;
            RFPower = power - gain;
            LFPower = -power + gain;
            RRPower = -power - gain;
            LRPower = power + gain;
            motors.setMechPowers(robot, 1, LFPower, RFPower, LRPower, RRPower, 0);
         }
      }

      // ROTATE
      else if(m == mode.ROTATE) {
         double kP = 0.05;
         double error = 0;
         double gain = error * kP;
         motors.setMode(robot, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
         motors.setMode(robot, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
         double offset = imuLib.getRotation(3);
         imuLib.updateNumbers(robot);
         double rot = imuLib.getRotation(3);
         while((imuLib.getRotation(3) - offset) < counts - 2 && (imuLib.getRotation(3) - offset) > (-1 * counts) + 2 ) {
            imuLib.updateNumbers(robot);
            error = imuLib.getRotation(3) - rot;
            gain = error * kP;
            RFPower = power + gain;
            LFPower = power - gain;
            RRPower = power + gain;
            LRPower = power - gain;
            motors.setMechPowers(robot, 1, LFPower, RFPower, LRPower, RRPower, 0);
         }

      }
      motors.setMechPowers(robot,1,0,0,0,0,0);



   }



   // Unused method, may be refined in the future to be used
   void strafeIMUCombined(CF_Hardware robot, double power, double encoderCounts) {
      motors.setMode(robot, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      motors.setMode(robot, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      imuLib.updateNumbers(robot);
      double kPy = 0.05;
      double kPr = 0.008;
      double count = Math.abs(encoderCounts);
      double start = imuLib.getRotation(3);
      double errorRotate = 0;
      double errorY = 0;
      double RFPower = 0;
      double LFPower = 0;
      double RRPower = 0;
      double LRPower = 0;

      while(Math.abs(motors.getEncoderCounts(robot, 1)) < count /* && Math.abs(motors.getEncoderCounts(robot, 2)) < count && Math.abs(motors.getEncoderCounts(robot, 3)) < count && Math.abs(motors.getEncoderCounts(robot, 4)) < count*/)
      {
         imuLib.updateNumbers(robot);
         errorY = (0 + imuLib.getyAccel()) * kPy;
         errorRotate = (start + imuLib.getRotation(3)) * kPr;
         RFPower = power + errorY - errorRotate;
         LFPower = -power + errorY + errorRotate;
         RRPower = -power + errorY - errorRotate;
         LRPower = power + errorY + errorRotate;

         motors.setMechPowers(robot, 1, LFPower, RFPower, LRPower, RRPower, 0);
      }

      motors.setMechPowers(robot,1,0,0,0,0,0);

   }

   void strafeIMURotation(CF_Hardware robot, double power, double encoderCounts) {

      //LinearOpMode mode,

      motors.setMode(robot, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      motors.setMode(robot, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      imuLib.updateNumbers(robot);
      double kP = 0.05;
      double count = Math.abs(encoderCounts);
      double start = imuLib.getRotation(3);
      double error = 0;
      double RFPower = 0;
      double LFPower = 0;
      double RRPower = 0;
      double LRPower = 0;

      while(Math.abs(motors.getEncoderCounts(robot, 1)) < count /* && Math.abs(motors.getEncoderCounts(robot, 2)) < count && Math.abs(motors.getEncoderCounts(robot, 3)) < count && Math.abs(motors.getEncoderCounts(robot, 4)) < count*/)
      {
         imuLib.updateNumbers(robot);
         error = (start + (imuLib.getRotation(3) * kP));
         RFPower = power - error;
         LFPower = -power + error;
         RRPower = -power - error;
         LRPower = power + error;

         motors.setMechPowers(robot, 1, LFPower, RFPower, LRPower, RRPower, 0);
      }

      motors.setMechPowers(robot,1,0,0,0,0,0);

   }

   void driveIMU(OpMode mode, CF_Hardware robot, double power, double encoderCounts) {
      motors.setMode(robot, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      motors.setMode(robot, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      imuLib.updateNumbers(robot);
      double kP = 0.05;
      double count = Math.abs(encoderCounts);
      double start = imuLib.getRotation(3);
      double error = 0;
      double RFPower = 0;
      double LFPower = 0;
      double RRPower = 0;
      double LRPower = 0;

      while(Math.abs(motors.getEncoderCounts(robot, 1)) < count /* && Math.abs(motors.getEncoderCounts(robot, 2)) < count && Math.abs(motors.getEncoderCounts(robot, 3)) < count && Math.abs(motors.getEncoderCounts(robot, 4)) < count*/)
      {


         //LineaerOpmode mode




         //!mode.isStopRequested() &&



         imuLib.updateNumbers(robot);
         error = (start + (imuLib.getRotation(3) * kP));
         RFPower = power;// + error;
         LFPower = power;// - error;
         RRPower = power;// + error;
         LRPower = power;// - error;

         motors.setMechPowers(robot, 1, LFPower, RFPower, LRPower, RRPower, 0);
      }

      motors.setMechPowers(robot,1,0,0,0,0,0);


   }


   void driveIMUStrafe(OpMode mode, CF_Hardware robot, double power, double encoderCounts) {
      motors.setMode(robot, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      motors.setMode(robot, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      imuLib.updateNumbers(robot);
      double kP = 0.05;
      double count = Math.abs(encoderCounts);
      double start = imuLib.getRotation(3);
      double error = 0;
      double RFPower = 0;
      double LFPower = 0;
      double RRPower = 0;
      double LRPower = 0;

      while(Math.abs(motors.getEncoderCounts(robot, 1)) < count /* && Math.abs(motors.getEncoderCounts(robot, 2)) < count && Math.abs(motors.getEncoderCounts(robot, 3)) < count && Math.abs(motors.getEncoderCounts(robot, 4)) < count*/)
      {


         // same mode changes as above method





         imuLib.updateNumbers(robot);
         error = (start + (imuLib.getRotation(3) * kP));
         RFPower = -power;// + error;
         LFPower = power;// - error;
         RRPower = power;// + error;
         LRPower = -power;// - error;

         motors.setMechPowers(robot, 1, LFPower, RFPower, LRPower, RRPower, 0);
      }

      motors.setMechPowers(robot,1,0,0,0,0,0);


   }

   void driveIMUTurnLeft(OpMode mode, CF_Hardware robot, double power, double encoderCounts)
   {
      motors.setMode(robot, DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      motors.setMode(robot, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      imuLib.updateNumbers(robot);
      double kP = 0.05;
      double count = Math.abs(encoderCounts);
      double start = imuLib.getRotation(3);
      double error = 0;
      double RFPower = 0;
      double LFPower = 0;
      double RRPower = 0;
      double LRPower = 0;

      RFPower = -power;// + error;
      LFPower = power;// - error;
      RRPower = -power;// + error;
      LRPower = power;// - error;

//      while(Math.abs(motors.getEncoderCounts(robot, 1)) < count /* && Math.abs(motors.getEncoderCounts(robot, 2)) < count && Math.abs(motors.getEncoderCounts(robot, 3)) < count && Math.abs(motors.getEncoderCounts(robot, 4)) < count*/)
//      {
//
//
//         //LineaerOpmode mode
//
//
//
//
//         //!mode.isStopRequested() &&
//
//
//
//         //Could be wrong and the whole method could make the robot turn left
//         imuLib.updateNumbers(robot);
//         error = (start + (imuLib.getRotation(3) * kP));
//         RFPower = -power;// + error;
//         LFPower = power;// - error;
//         RRPower = -power;// + error;
//         LRPower = power;// - error;
//
//         motors.setMechPowers(robot, 1, LFPower, RFPower, LRPower, RRPower, 0);
//      }

      if (Math.abs(motors.getEncoderCounts(robot, 1)) < count)
      {
         motors.setMechPowers(robot, 1, 0, 0, 0, 0, 0);
      }


   }


}