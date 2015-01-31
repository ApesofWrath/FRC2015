package org.usfirst.frc.team668.robot;

public class Autonomous {
	public static long autoTimer;

	public static void doAuton() { //TODO: different function for each autonomous
		autoTimer = System.currentTimeMillis(); //current time in milliseconds
		switch (RobotMap.autonomousMode) {
			case RobotMap.STOP_AUTONOMOUS:
				break;
			case RobotMap.DRIVE_FORWARD_AUTONOMOUS:
				if (Robot.encoderLeft.getDistance() < RobotMap.STOP) {
					Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE);
				}
				if (Robot.encoderRight.getDistance() < RobotMap.STOP) {
					Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE);
				}
				break;
			case RobotMap.DELAY_AND_DRIVE_FORWARD_AUTONOMOUS:
				if((System.currentTimeMillis() - autoTimer) > RobotMap.DELAY_TIME) { // in milliseconds
					if (Robot.encoderLeft.getDistance() < RobotMap.STOP) {
						Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE);
					}
					if (Robot.encoderRight.getDistance() < RobotMap.STOP) {
						Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE);
					}
					break;
				}
		}
	}

}
