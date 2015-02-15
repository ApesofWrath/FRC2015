package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	public static long autoTimer = -1;
	public static boolean finished = false;
	
	public static void doAuton() { // TODO: different function for each autonomous
	// autoTimer = System.currentTimeMillis(); //current time in milliseconds
		switch (RobotMap.autonomousMode) {
			case RobotMap.STOP_AUTONOMOUS:
				break;
			case RobotMap.DRIVE_FORWARD_AUTONOMOUS:
				if (Robot.encoderLeft.get() * -1 < RobotMap.STOP) {
					Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0
				}
				if (Robot.encoderRight.get() * 360 / 250 < RobotMap.STOP) {
					Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0
				}
				break;
			case RobotMap.DELAY_AND_DRIVE_FORWARD_AUTONOMOUS:
				SmartDashboard.putNumber("Autonomous Timer", autoTimer);
				if (autoTimer <= 0) { // This value shouldn't be 0
					autoTimer = System.currentTimeMillis();
				}
				if ((System.currentTimeMillis() - autoTimer) > RobotMap.DELAY_TIME) { // in milliseconds
					if (Robot.encoderLeft.get() * -1 < RobotMap.STOP) {
						Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // There is literally NO curve and this code doesn't work with a curve
					}
					if (Robot.encoderRight.get() * (360 / 250) < RobotMap.STOP) {
						Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // There is literally NO curve and this code doesn't work with a curve
					}
				}
				break;
			case RobotMap.TOTE_GRAB_AUTONOMOUS: // this takes totes in all the way rather than half-way
//				switch (RobotMap.autonToteGrabState) {
//					case RobotMap.AUTON_TOTE_GRAB_INTAKE_STATE:
//						if (autoTimer <= 0) {
//							autoTimer = System.currentTimeMillis();
//						}
//						if (System.currentTimeMillis() - autoTimer < RobotMap.TOTE_GRAB_TIME) {
//							Intake.spin(0.4);
//						} else {
//							Intake.stop();
//							RobotMap.autonToteGrabState = RobotMap.AUTON_TOTE_GRAB_DRIVE_STATE;
//						}
//					case RobotMap.AUTON_TOTE_GRAB_DRIVE_STATE:
//						if (Robot.encoderLeft.get() * -1 < RobotMap.TOTE_STOP) {
//							Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.TOTE_CURVE); // Curve is 0
//						}
//						if (Robot.encoderRight.get() * 360 / 250 < RobotMap.TOTE_STOP) {
//							Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.TOTE_CURVE); // Curve is 0
//						}
//				}
				break;
			/*
			 * //Elevator init
			 * if (!Robot.intakePiston.get().equals(DoubleSolenoid.Value.kReverse)) { // If closed
			 * Robot.intakePiston.set(DoubleSolenoid.Value.kReverse); // Open it
			 * }
			 * 
			 * // state machine inside state machine
			 * 
			 * if (!finished) {
			 * finished = Elevator.calibration(-0.8); // TODO: magic number (elevator calibration speed?)
			 * } else {
			 * Elevator.stop();
			 * ToteGrabber.moveHugPistons(false);
			 * }
			 * 
			 * boolean moveFinish = Elevator.move(0.5, RobotMap.ELEVATOR_ENCODER_PICKUP); //should be about 10
			 * if (moveFinish == true) {
			 * Elevator.stop();
			 * }
			 * //Grab Tote
			 * Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
			 * Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
			 * while (!Robot.toteOptic.get()) {
			 * //wait until we have intook the totes
			 * }
			 * ToteGrabber.moveHugPistons(true);
			 * 
			 * //ninety-degree turn
			 * Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); //TODO fix
			 * break;
			 */
			
			case RobotMap.BIN_GRAB_AUTONOMOUS:
				
				break;
			
			case RobotMap.TOTE_STACK_AUTONOMOUS:
				
				break;
		
		// TODO: Write new autonomous code
		}
	}
	
}
