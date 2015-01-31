package org.usfirst.frc.team668.robot;

public class Autonomous {

	public static void doAuton() {
		switch (Robot.autonSubroutine) {
			case RobotMap.STOP_AUTONOMOUS:
				break;
			case RobotMap.FORWARD_AUTONOMOUS:
				if (Robot.encoderLeft.getDistance() < RobotMap.STOP) {
					Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE);
				}
				if (Robot.encoderRight.getDistance() < RobotMap.STOP) {
					Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE);
				}
				break;
			case RobotMap.DELAY_AND_DRIVE_FORWARD_AUTONOMOUS:
				if(Robot.timer.get() > RobotMap.DELAY_TIME) { // In seconds
					Robot.timer.stop();
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
