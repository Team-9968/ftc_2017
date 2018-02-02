package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcontroller.internal.CF_Globals;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


/**
 * Created by Ryley on 1/13/18.
 * CF_OpenCV_Library
 */

public class CF_OpenCV_Library {
    CF_Globals globals = new CF_Globals();
    private Mat source = null;
    double red = 0;
    double blue = 0;
    private double[] ret = new double[3];
    private Mat image = new Mat(FtcRobotControllerActivity.getmRgba().cols(), FtcRobotControllerActivity.getmRgba().rows(), CvType.CV_8UC4);
    private Bitmap map = Bitmap.createBitmap(FtcRobotControllerActivity.getmRgba().width(), FtcRobotControllerActivity.getmRgba().height(), Bitmap.Config.ARGB_8888);

    public enum ballColor {
        RED, BLUE, UNKNOWN
    }
    ballColor color;


    public ballColor getColor() {
        //if(source != null) {
            source = FtcRobotControllerActivity.getmRgba();
            red = 0;
            blue = 0;

            Core.rotate(source, image, Core.ROTATE_90_CLOCKWISE);
        for (int y = 0; y < image.size().height - 1; y += 2) {
                for (int x = 0; x < image.size().width - 1; x += 2) {
                    // Original:
//                    if (image.get(i, x)[0] > 208 && image.get(i, x)[0] < 238 && image.get(i, x)[2] < 70) {
//                        red += x;
//                    } else if (image.get(i, x)[2] > 170 && image.get(i, x)[2] < 192 && image.get(i, x)[0] < 58) {
//                        blue += x;
//                    }

                    if (image.get(y, x)[0] > 130 && image.get(y, x)[0] < 238 && image.get(y, x)[2] < 70) {
                        red += x;
                    } else if (image.get(y, x)[2] > 100 && image.get(y, x)[2] < 192 && image.get(y, x)[0] < 58) {
                        blue += x;
                    }
                }
            }
            if (red > blue + 2500) {
                color = ballColor.RED;
            } else if (blue > red + 2500) {
                color = ballColor.BLUE;
            } else {
                color = ballColor.UNKNOWN;
            }

            CF_Globals.setRed(red);
            CF_Globals.setBlue(blue);
//        } else {
//            color = ballColor.UNKNOWN;
//        }

        return color;

    }

    public double[] getRGB(int x, int y) {
        source = null;
        source = FtcRobotControllerActivity.getmRgba().clone();

        Core.rotate(source, image, Core.ROTATE_90_CLOCKWISE);

        if(source != null) {
            // 0 = red;
            // 1 = green;
            // 2 = blue;
            if(source == null) {
                System.out.println("Cnull");
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e){}

            ret[0] = image.get(x, y)[0];
            ret[1] = image.get(x, y)[1];
            ret[2] = image.get(x, y)[2];

        }
        source.release();
        return ret;
    }

    public ballColor getColor(int x, int y) {
        source = FtcRobotControllerActivity.getmRgba().clone();
        red = 0;
        blue = 0;
        Core.rotate(source, image, Core.ROTATE_90_CLOCKWISE);
        if(image != null) {
            if (image.get(x, y)[0] > 130 && image.get(x, y)[0] < 238 && image.get(x, y)[2] < 70) {
                red += x;
            } else if (image.get(x, y)[2] > 100 && image.get(x, y)[2] < 192 && image.get(x, y)[0] < 58) {
                blue += x;
            }

        }
        source.release();
        image.release();

        if(red > blue) {
            return ballColor.RED;
        } else if(blue > red) {
            return ballColor.BLUE;
        } else {
            return ballColor.UNKNOWN;
        }
    }

    public void save(OpMode mode) {
        source = FtcRobotControllerActivity.getmRgba();
        Utils.matToBitmap(source, map);
        System.out.println(MediaStore.Images.Media.insertImage(mode.hardwareMap.appContext.getContentResolver(), map, "FTC Pic", "FTC Pic"));

    }
}