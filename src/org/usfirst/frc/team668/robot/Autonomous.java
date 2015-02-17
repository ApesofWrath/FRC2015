package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	public static boolean finished = false;
	
	/*
	 * READ THIS!!!!!!!!!!!!!!!!!
	 * We are passing a Robot as a parameter so that we can check whether or not the Robot is in Autonomous
	 */
	
	public static void stopAutonomous() {
		return;
	}
	
	public static void driveForwardAutonomous(Robot r) {
		while (r.isAutonomous() && r.isEnabled()) {
			if (Robot.encoderLeft.get() * -1 < RobotMap.STOP) {
				Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
			}
			if (Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP) {
				Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
			}
		}
		return;
	}
	
	public static void delayAutonomous() {
		long delayTimer = System.currentTimeMillis();
		while ((System.currentTimeMillis() - delayTimer) < RobotMap.DELAY_TIME) { // in milliseconds
			SmartDashboard.putNumber("Autonomous Timer", delayTimer);
		} // just waits and prints time on SmartDashboard
		
		// then it drives
		if (Robot.encoderLeft.get() * -1 < RobotMap.STOP) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
		}
		if (Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
		}
		return;
	}
	
	public static void toteGrabAutonomous(Robot r) {
		// close piston
		Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
		
		// timer
		long toteGrabAutonTimer = System.currentTimeMillis();
		while (r.isAutonomous() && r.isEnabled() && (System.currentTimeMillis() - toteGrabAutonTimer) < RobotMap.TOTE_GRAB_TIME) {
			// intake tote
			Intake.spin(RobotMap.INTAKE_MOTOR_SPEED); // TODO: Used to be 0.4 Remove when verified
		}
		Intake.stop();
		while (r.isAutonomous() && r.isEnabled()) { // goes until autonomous or robot is disabled
			// drive robot - we only want to do this when intake is done
			if (Robot.encoderLeft.get() * -1 < RobotMap.TOTE_STOP) {
				Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.TOTE_CURVE); // Curve is 0
			}
			if (Robot.encoderRight.get() * (360.0 / 250.0) < RobotMap.TOTE_STOP) {
				Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.TOTE_CURVE); // Curve is 0
			}
		}
		return;
	}
	
	public static void binGrabAutonomous(Robot r) {
		// close piston
		Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
		
		// timer
		long binGrabAutonTimer = System.currentTimeMillis();
		while (r.isAutonomous() && r.isEnabled() && ((System.currentTimeMillis() - binGrabAutonTimer) < RobotMap.BIN_GRAB_TIME)) {
			// intake tote
			Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
		}
		Intake.stop();
		while (r.isAutonomous() && r.isEnabled()) { // goes until autonomous or robot is disabled
			// drive robot
			if (Robot.encoderLeft.get() * -1 < RobotMap.BIN_STOP) {
				Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.BIN_CURVE); // Curve is 0
			}
			if (Robot.encoderRight.get() * (360.0 / 250.0) < RobotMap.TOTE_STOP) {
				Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.BIN_CURVE); // Curve is 0
			}
		}
		return;
	}
	
	public static void toteStackAutonomous(Robot r) {
		// most likely not going to be used
		// close piston
		Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
		
		while (r.isAutonomous() && r.isEnabled()) {
			// time and intake
			
			// timer
			long binGrabAutonTimer = System.currentTimeMillis();
			while (r.isAutonomous() && r.isEnabled() &&
					((System.currentTimeMillis() - binGrabAutonTimer) < RobotMap.BIN_GRAB_TIME)) {
				// intake tote
				Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
			}
			Intake.stop();
			
			ToteGrabber.moveHugPistons(true);
			Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_ONE_TOTE_HEIGHT);
		}
		return;
	}
	
	public static void celebrate(Robot r) {
		while (r.isAutonomous() && r.isEnabled()) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, 0.5);
		}
		return;
	}
}
