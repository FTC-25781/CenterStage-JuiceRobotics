package org.firstinspires.ftc.teamcode.subsystems.relocalization;

import android.util.Size;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTagsRelocalization {
    AprilTagProcessor processor;
    VisionPortal visionPortal;
    AprilTagDetection aprilTagDetection;
    List<AprilTagDetection> aprilTagDetections;
    int aprilTagID;

    double cameraOffsetX = 0.0;
    double cameraOffsetY= 0.0;
    double cameraOffsetRotation = 0.0;

    public AprilTagsRelocalization(WebcamName camera) {
        processor = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(processor)
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();
    }

    public void detectBackdrop() {
        aprilTagDetections = processor.getDetections();

        for (AprilTagDetection laprilTagDetection : aprilTagDetections) {
            if (laprilTagDetection.metadata != null && laprilTagDetection.id <= 6) {
                aprilTagID = laprilTagDetection.id;
                aprilTagDetection = laprilTagDetection;
            }
        }
    }

    public AprilTagPoseFtc getRelativePose() {
        return aprilTagDetection.ftcPose;
    }

    public Pose2d getAbsolutePose2d(Pose2d robotPose) {
        AprilTagPoseFtc tagRPose = getRelativePose();
        Pose2d tagPose = new Pose2d(robotPose.getX() + tagRPose.x + cameraOffsetX, robotPose.getY() + tagRPose.y + cameraOffsetY);

        return tagPose;
    }
}
