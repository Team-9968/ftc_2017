package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import android.graphics.Bitmap;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Enums.CF_TypeEnum;
import org.opencv.core.Mat;
import java.util.concurrent.TimeUnit;

/**
 * Created by dawson on 2/18/2018.
 */

@Autonomous(name = "Red 2", group = "Sensor")
//@Disabled
public class CF_Red_2 extends OpMode
{
   //Allows this file to access pieces of hardware created in other files.
   CF_Hardware robot = new CF_Hardware();
   static ElapsedTime runTime = new ElapsedTime();
   CF_Autonomous_Motor_Library auto = new CF_Autonomous_Motor_Library();
   CF_Color_Sensor sensor = new CF_Color_Sensor();
   CF_OpenCV_Library cam = new CF_OpenCV_Library();
   CF_Vuforia_Library vuforia = new CF_Vuforia_Library();
   CF_Master_Motor_Library motors = new CF_Master_Motor_Library();
   //CF_OpenCV_Library.ballColor cam_color = null;

   boolean ArmCenter;

   CF_OpenCV_Library.ballColor col;
   RelicRecoveryVuMark pic;

   int counts = 0;
   int strafe = 0;
   int forwards = 0;
   int nudge = 0;
   double offset;

   //A list of all of the steps in this program
   private enum checks
   {
      GRABBLOCK, MOVEMAST, SENSEPICTURE, JEWELHITTER, PASTBALANCE, RELEASEBLOCK, PARK, END
   }

   private enum jewelHitterState
   {
      ARMDOWN, CHECKCOL, ARMUP, OTHERSTUFF, ARMUP2, END
   }

   private enum picSenseState
   {
      INITVUFORIA, DRIVEENCODERS, SENSEPICTURE, END
   }

   private enum releaseBlockState
   {
      RELEASEBLOCK, RESETENCODERS, DRIVE, END
   }

   private enum parkState
   {
      RESETENCODERS1, DRIVE1, RESETENCODERS2, DRIVE2, RESETENCODERS3, DRIVE3, END
   }

   //Sets current stages of the "List"
   checks Check = checks.GRABBLOCK;
   jewelHitterState jewelHitter = jewelHitterState.ARMDOWN;
   picSenseState picSense = picSenseState.INITVUFORIA;
   releaseBlockState releaseBlock = releaseBlockState.RELEASEBLOCK;
   parkState park = parkState.RESETENCODERS1;

   //Ensures that we do not go over thirty seconds of runtime. This endtime variable is
   //a backup method in case the coach forgets to turn on the timer built into the robot app.
   int endTime = 29;
   double servoIncrement = 0;

   private void checkTime()
   {
      // Kills the robot if time is over the endTime
      if(getRuntime() >= endTime)
      {
         requestOpModeStop();
      }
   }

   @Override
   public void init()
   {
      msStuckDetectLoop = 15000;
      robot.init(hardwareMap);
      vuforia.init(this);
   }

   @Override
   public void loop()
   {
      switch (Check)
      {
         //This method grabs the glyph and uses the phone's camera to take a picture
         //to help determine ball color
         case GRABBLOCK:
            resetStartTime();
            runTime.reset();
            robot.clamp.setPosition(0.81);
            robot.lowerClamp.setPosition(0.3);
            checkTime();
            Check = checks.JEWELHITTER;
            Mat x = vuforia.getFrame();
            telemetry.addData("Found picture", "Found");
            telemetry.update();
            col = cam.getColor(this, x);

            Bitmap y = vuforia.getMap();
            cam.save(this, y);
            servoIncrement = robot.colorArm.getPosition();
            break;

         //This method runs the color sensors and determines which jewel is which color
         //and uses that info to hit the correct jewel
         case JEWELHITTER:
            telemetry.addData("Case Jewelpusher", "");
            switch (jewelHitter) {
               case ARMDOWN:
                  servoIncrement -= 0.001;
                  robot.colorArm.setPosition(servoIncrement);
                  if(robot.isArmDown(0.11f)) {
                     jewelHitter = jewelHitterState.CHECKCOL;
                  }
                  break;
               case CHECKCOL:
                  try
                  {
                     TimeUnit.MILLISECONDS.sleep(300);
                  } catch(InterruptedException e) {}

                  robot.tailLight.setPower(1);

                  try
                  {
                     TimeUnit.MILLISECONDS.sleep(700);
                  } catch(InterruptedException e) {}

                  sensor.setType(robot);
                  CF_TypeEnum classification = sensor.setType(robot);

                  if (classification == CF_TypeEnum.LEFTISBLUE)

                  {
                     telemetry.addData("Right is"," blue");
                     robot.jewelHitter.setPosition(0.7);

                     try
                     {
                        TimeUnit.MILLISECONDS.sleep(500);
                     } catch(InterruptedException e) {}

                     ArmCenter = true;
                     checkTime();
                  }

                  else if ((classification == CF_TypeEnum.LEFTISRED))

                  {
                     telemetry.addData("Right is"," red");
                     robot.jewelHitter.setPosition(0.0);

                     try
                     {
                        TimeUnit.MILLISECONDS.sleep(500);
                     } catch(InterruptedException e) {}

                     ArmCenter = true;
                     checkTime();
                  }

                  //If the color sensors failed to determine the jewels' colors, this
                  //else statement relies on the camera as a backup.
                  else
                  {
                     telemetry.addData("Ball is", " unknown");

                     //if the ball on the right is blue, the arm will move to knock off that ball.
                     if(col == CF_OpenCV_Library.ballColor.RIGHTISBLUE) {
                        telemetry.addData("Right is"," blue - Camera");
                        robot.jewelHitter.setPosition(0.0);

                        try
                        {
                           TimeUnit.MILLISECONDS.sleep(500);
                        } catch(InterruptedException e) {}

                        ArmCenter = true;
                        checkTime();
                     }

                     //if the ball on the right is red, the arm will hit the blue ball.
                     else if(col == CF_OpenCV_Library.ballColor.RIGHTISRED) {
                        telemetry.addData("Right is"," red - Camera");
                        robot.jewelHitter.setPosition(0.7);

                        try
                        {
                           TimeUnit.MILLISECONDS.sleep(500);
                        } catch(InterruptedException e) {}

                        ArmCenter = true;
                        checkTime();
                     }

                     checkTime();
                  }

                  telemetry.update();
                  robot.tailLight.setPower(0.0);
                  servoIncrement = robot.colorArm.getPosition();
                  jewelHitter = jewelHitterState.ARMUP;
                  break;

               case ARMUP:
                  servoIncrement += 0.001;
                  robot.colorArm.setPosition(servoIncrement);
                  if (robot.isArmUp(0.45f)) {

                     jewelHitter = jewelHitterState.OTHERSTUFF;
                  }
                  break;
               case OTHERSTUFF:
                  robot.jewelHitter.setPosition(0.15);

                  try
                  {
                     TimeUnit.MILLISECONDS.sleep(100);
                  } catch(InterruptedException e) {}

                  if (ArmCenter = true)
                  {
                     robot.jewelHitter.setPosition(0.333);
                  }
                  jewelHitter = jewelHitterState.ARMUP2;
                  servoIncrement = robot.colorArm.getPosition();
                  break;

               case ARMUP2:
                  servoIncrement += 0.001;
                  robot.colorArm.setPosition(servoIncrement);
                  if(robot.isArmUp(0.99f)) {
                     jewelHitter = jewelHitterState.END;
                  }
                  break;

               case END:
                  Check = checks.MOVEMAST;
                  break;




            }
            break;

         //After the jewel has been hit off, this state raises the glyph so
         //it does not interfere with the robot driving off of the balancing stone.
         case MOVEMAST:
            auto.clawMotorMove(robot, -1.0f, 2000);
            Check = checks.SENSEPICTURE;
            break;

         //This state incorporates Vuforia to look at the the picture attached to the wall.
         //Based off of the input from Vuforia, the robot will drive a certain number of encoder counts
         case SENSEPICTURE:
            switch (picSense) {
               case INITVUFORIA:
                  vuforia.activate();
                  offset = auto.resetEncoders(robot);
                  picSense = picSenseState.DRIVEENCODERS;
                  break;
               case DRIVEENCODERS:
                  if(auto.encoderDriveState(robot, 0.2f, 130, offset)) {
                     motors.setMechPowers(robot, 1,0,0,0,0,0);
                     picSense = picSenseState.SENSEPICTURE;
                  }
                  break;
               case SENSEPICTURE:
                  try{
                     TimeUnit.MILLISECONDS.sleep(2000);
                  } catch (InterruptedException e) {}
                  pic = vuforia.getMark();
                  try{
                     TimeUnit.MILLISECONDS.sleep(500);
                  } catch (InterruptedException e) {}
                  telemetry.addData("pic", pic);
                  telemetry.update();
                  //1200 counts forward
                  if(pic == RelicRecoveryVuMark.LEFT) {
                     counts = 1200;
                     strafe = 400;  // this is wrong
                     forwards = 240;
                     nudge = 60;
                  } else if (pic == RelicRecoveryVuMark.CENTER) {
                     counts = 1200;
                     strafe = 300;   //also wrong
                     forwards = 240;
                     nudge = 60;
                  } else {
                     counts = 1200;
                     strafe = 200;  //wrong again
                     forwards = 240;
                     nudge = 60;
                  }
                  vuforia.deactivate();
                  picSense = picSenseState.END;
                  break;
               case END:
                  Check = checks.PASTBALANCE;
                  motors.setMechPowers(robot, 1,0,0,0,0,0);
            }
            break;

         //Turns the robot and drives forward to place the glyph in the cryptobox
         case PASTBALANCE:
            auto.resetEncoders(robot);
            auto.EncoderIMUDrive(this, robot, CF_Autonomous_Motor_Library.mode.DRIVE, 0.5f, counts);
            auto.EncoderIMUDrive(this, robot, CF_Autonomous_Motor_Library.mode.STRAFE, 0.5f, strafe);
            auto.EncoderIMUDrive(this, robot, CF_Autonomous_Motor_Library.mode.DRIVE, 0.5f, forwards);

         //This state is fairly self - explanatory. It releases the claws' hold on the glyph.
         case RELEASEBLOCK:

            switch(releaseBlock) {
               case RELEASEBLOCK:
                  auto.clawMotorMove(robot, 1.0f, 1500);
                  try
                  {
                     TimeUnit.MILLISECONDS.sleep(500);
                  } catch(InterruptedException e) {}

                  robot.clamp.setPosition(0.4);
                  robot.lowerClamp.setPosition(0.6);
                  releaseBlock = releaseBlockState.RESETENCODERS;
                  break;
               case RESETENCODERS:
                  offset = auto.resetEncoders(robot);
                  releaseBlock = releaseBlockState.DRIVE;
                  break;
               case DRIVE:
                  if(auto.encoderDriveState(robot, 0.2f, nudge, offset)) {
                     motors.setMechPowers(robot, 1,0,0,0,0,0);
                     releaseBlock = releaseBlockState.END;
                  }
                  break;
               case END:
                  motors.setMechPowers(robot, 1,0,0,0,0,0);
                  Check = checks.PARK;
            }
            break;

         // Backs robot up slightly so we aren't touching the block, but are still parking
         case PARK:
            switch(park) {
               case RESETENCODERS1:
                  offset = auto.resetEncoders(robot);
                  try
                  {
                     TimeUnit.MILLISECONDS.sleep(500);
                  } catch(InterruptedException e) {}
                  park = parkState.DRIVE1;
                  break;
               case DRIVE1:
                  if(auto.encoderDriveState(robot, -0.2f, 275, offset)){
                     motors.setMechPowers(robot, 1,0,0,0,0,0);
                     park = parkState.RESETENCODERS2;
                  }
                  break;
               case RESETENCODERS2:
                  offset = auto.resetEncoders(robot);
                  try
                  {
                     TimeUnit.MILLISECONDS.sleep(200);
                  } catch(InterruptedException e) {}
                  park = parkState.DRIVE2;
                  break;
               case DRIVE2:
                  if(auto.encoderDriveState(robot, 0.2f, 200, offset)){
                     motors.setMechPowers(robot, 1,0,0,0,0,0);
                     park = parkState.RESETENCODERS3;
                  }
                  break;
               case RESETENCODERS3:
                  offset = auto.resetEncoders(robot);
                  try
                  {
                     TimeUnit.MILLISECONDS.sleep(100);
                  } catch(InterruptedException e) {}
                  park = parkState.DRIVE3;
                  break;
               case DRIVE3:
                  if(auto.encoderDriveState(robot, -0.2f, 200, offset)){
                     motors.setMechPowers(robot, 1,0,0,0,0,0);
                     park = parkState.END;
                  }
                  break;
               case END:
                  motors.setMechPowers(robot, 1,0,0,0,0,0);
                  Check = checks.END;
            }
            break;

         //End state. Does nothing.
         case END:
            requestOpModeStop();
            break;
      }
      checkTime();
   }
}