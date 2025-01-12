package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.opencv.core.Mat;

/**
 * Created by Ryley on 10/15/17.
 */

public class CF_Manual_Motor_Library {
    CF_Master_Motor_Library motors = new CF_Master_Motor_Library();
    // Variables to hold motor powers
    double LFPower;
    double RFPower;
    double LRPower;
    double RRPower;

    // Default direction: full power forwards.  When
    // this variable is negative it goes backwards, and this number
    // can't be over 1 or it will try to send too much power
    // to the motors.  If the absolute value of this variable
    // is smaller than 1, then it will be sending the motor
    // a power directly proportional to the number
    double DirectionPower = 1;

    // Tank mode.  It drives the robot based on the left and right
    // sticks.  The left one drives the left side of the robot,
    // and the right one drives the right side of the robot
    void tankMode(CF_Hardware bot, double left, double right) {
        bot.leftFront.setPower(-left);
        bot.rightFront.setPower(-right);
        bot.leftRear.setPower(-left);
        bot.rightRear.setPower(-right);
    }

    // This runs the mech wheels.  The signs are appropriate to drive the correct motors
    // in the correct direction
    void runMechWheels(CF_Hardware bot, double drive, double strafe, double rotate) {
        // Sets the individual powers to the individual motors
        LFPower = -strafe + drive;
        RFPower = +strafe + drive;
        LRPower = +strafe + drive;
        RRPower = -strafe + drive;

        // Sets the mechanum powers
        motors.setMechPowers(bot, DirectionPower, LFPower, RFPower, LRPower, RRPower, -rotate);
    }

    // This runs the mech wheels.  The signs are appropriate to drive in the correct direction.  The exponent multiplies
    // the control inputs to make it less sensitive.
    void runMechWheels(CF_Hardware bot, double drive, double strafe, double rotate, int exp) {
        strafe = Math.pow(strafe, exp);
        drive = Math.pow(drive, exp);
        rotate = 0.80 * Math.pow(rotate, exp);

        // Sets the individual powers to the individual motors
        LFPower = -strafe + drive;
        RFPower = +strafe + drive;
        LRPower = +strafe + drive;
        RRPower = -strafe + drive;

        // Sets the mechanum powers
        motors.setMechPowers(bot, DirectionPower, LFPower, RFPower, LRPower, RRPower, -rotate);
    }

    void runMechWheels(CF_Hardware bot, double drive, double strafe, double rotate, int exp, int expStrafe) {
        strafe = Math.pow(strafe, expStrafe);
        drive = Math.pow(drive, exp);
        rotate = 0.80 * Math.pow(rotate, exp);

        // Sets the individual powers to the individual motors
        LFPower = -strafe + drive;
        RFPower = +strafe + drive;
        LRPower = +strafe + drive;
        RRPower = -strafe + drive;

        // Sets the mechanum powers
        motors.setMechPowers(bot, DirectionPower, LFPower, RFPower, LRPower, RRPower, -rotate);
    }

    void runMechWheels(CF_Hardware bot, double drive, double strafe, double rotate, int exp, int expStrafe, double throttle) {
        strafe = Math.pow(strafe, expStrafe);
        drive = throttle * Math.pow(drive, exp);
        rotate = 0.60 * Math.pow(rotate, exp);

        // Sets the individual powers to the individual motors
        LFPower = -strafe + drive;
        RFPower = +strafe + drive;
        LRPower = +strafe + drive;
        RRPower = -strafe + drive;

        // Sets the mechanum powers
        motors.setMechPowers(bot, DirectionPower, LFPower, RFPower, LRPower, RRPower, -rotate);
    }

    // This is just a mutator method to set the directionpower variable
    void changeDirectonAndPower(double power) {
        DirectionPower = power;
    }
}
