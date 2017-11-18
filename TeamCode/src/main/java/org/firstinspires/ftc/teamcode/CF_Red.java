package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.Hardware;

import java.util.concurrent.TimeUnit;

/**
 * Created by dawso on 10/1/2017.
 */


@Autonomous(name = "Team Red", group = "Sensor")
//@Disabled                            // Comment this out to add to the opmode list
public class CF_Red extends LinearOpMode
{
   CF_Hardware robot = new CF_Hardware();
   CF_Color_Sensor sensor = new CF_Color_Sensor();
   CF_Autonomous_Motor_Library auto = new CF_Autonomous_Motor_Library();

   private enum states
   {
      BACKUP, JEWELPUSHER, ENDOPMODE
   }

   @Override
   public void runOpMode() throws InterruptedException
   {
      states State = states.BACKUP;
      float hsvValues[] = {0F,0F,0F};

      robot.init(hardwareMap);

      waitForStart();

      while (opModeIsActive())
      {
         //sensor.turnOffAdafruiLED(robot);

         CF_Color_En sensorColor = sensor.getColorValues(robot);

         switch (State)
         {
            case BACKUP:
               auto.driveIMU(this, robot, 0.15, 350);
               TimeUnit.MILLISECONDS.sleep(500);
               State = states.JEWELPUSHER;
               break;
            case JEWELPUSHER:
               TimeUnit.MILLISECONDS.sleep(500);
               if (sensorColor == CF_Color_En.BLUE)
               {
                  auto.driveIMU(this, robot, -0.15, 80);
                  telemetry.addData("Ball is"," blue");
                  robot.jewelHitter.setPosition(0.2);
                  TimeUnit.MILLISECONDS.sleep(700);
                  auto.driveIMUStrafe(this, robot, -0.3, 250);  //COLOR SENSOR IS RIGHT when robot is viewed from the back.
               }

                else if (sensorColor == CF_Color_En.RED)
               {
                  auto.driveIMU(this, robot, -0.15, 80);
                  telemetry.addData("Ball is"," red");
                  robot.jewelHitter.setPosition(0.2);
                  TimeUnit.MILLISECONDS.sleep(700);
                  auto.driveIMUStrafe(this, robot, 0.3, 400);
               }

               else
               {
                  telemetry.addData("Ball is", " unknown");
               }

               State = states.ENDOPMODE;
               break;
            case ENDOPMODE:
               telemetry.addData("Done", "");
               break;
         }

         //telemetry.addData("Color: ", sensorColor);
         telemetry.addData("Hue: ", sensor.getColorHue());
         telemetry.update();
      }
   }
}