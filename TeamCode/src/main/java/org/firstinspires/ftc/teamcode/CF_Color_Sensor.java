package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;

/*
 *
 * This is an example LinearOpMode that shows how to use
 * the Adafruit RGB Sensor.  It assumes that the I2C
 * cable for the sensor is connected to an I2C port on the
 * Core Device Interface Module.
 *
 * It also assuems that the LED pin of the sensor is connected
 * to the digital signal pin of a digital port on the
 * Core Device Interface Module.
 *
 * You can use the digital port to turn the sensor's onboard
 * LED on or off.
 *
 * The op mode assumes that the Core Device Interface Module
 * is configured with a name of "dim" and that the Adafruit color sensor
 * is configured as an I2C device with a name of "sensor_color".
 *
 * It also assumes that the LED pin of the RGB sensor
 * is connected to the signal pin of digital port #5 (zero indexed)
 * of the Core Device Interface Module.
 *
 * You can use the X button on gamepad1 to toggle the LED on and off.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "CF_Color_Sensor", group = "Sensor")
//@Disabled                            // Comment this out to add to the opmode list
public class CF_Color_Sensor extends LinearOpMode {

   CF_Hardware robot = new CF_Hardware();

   int BlueLowerLimit = 185;
   int BlueUpperLimit = 330;
   int RedLowerLimit = 1;
   int RedUpperLimit = 13;

   boolean hueVal;

   // we assume that the LED pin of the RGB sensor is connected to
   // digital port 5 (zero indexed).
   static final int LED_CHANNEL = 3;

   @Override
   public void runOpMode() {

      // hsvValues is an array that will hold the hue, saturation, and value information.
      float hsvValues[] = {0F,0F,0F};

      // values is a reference to the hsvValues array.
      final float values[] = hsvValues;

      // get a reference to the RelativeLayout so we can change the background
      // color of the Robot Controller app to match the hue detected by the RGB sensor.
      int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
      final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);


      // bPrevState and bCurrState represent the previous and current state of the button.
      boolean bPrevState = false;
      boolean bCurrState = false;

      // bLedOn represents the state of the LED.
      boolean bLedOn = true;

      // get a reference to our ColorSensor object.
      robot.adafruitRGB = hardwareMap.colorSensor.get("adafruitRGB");

      // wait for the start button to be pressed.
      waitForStart();

      // loop and read the RGB data.
      // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
      while (opModeIsActive())  {

         // check the status of the x button on gamepad.
         bCurrState = gamepad1.x;

         // update previous state variable.
         bPrevState = bCurrState;

         // convert the RGB values to HSV values.
         Color.RGBToHSV((robot.adafruitRGB.red() * 255) / 800, (robot.adafruitRGB.green() * 255) / 800, (robot.adafruitRGB.blue() * 255) / 800, hsvValues);

//         // send the info back to driver station using telemetry function.
         telemetry.addData("Hue", hsvValues[0]);

         float hue = hsvValues[0];

         if ((hue >= BlueLowerLimit) && (hue <= BlueUpperLimit))
         {
            //find limits. Run w/ two sensors w/ both  saying if b/w this and this     or      run w/ two sensors w/ one saying "if b/w this and this" and another saying
            //"if greater than my other sensor buddy"
            hueVal = true;
            telemetry.addData("Blue", "");
            telemetry.update();
         }

         else if ((hue >= RedLowerLimit) && (hue <= RedUpperLimit))
         {
            hueVal = false;
            telemetry.addData("Red", "");
            telemetry.update();
         }

         else
         {
            telemetry.addData("Nope", "");
            telemetry.update();
         }

         // change the background color to match the color detected by the RGB sensor.
         // pass a reference to the hue, saturation, and value array as an argument
         // to the HSVToColor method.
         relativeLayout.post(new Runnable() {
            public void run() {
               relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
            }
         });

         telemetry.update();

      }

      // Set the panel back to the default color
      relativeLayout.post(new Runnable() {
         public void run() {
            relativeLayout.setBackgroundColor(Color.WHITE);
         }
      });
   }
}