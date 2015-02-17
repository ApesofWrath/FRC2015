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
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < RobotMap.STOP || Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP)) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
			
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
		// while (r.isAutonomous() && r.isEnabled() && (System.currentTimeMillis() - toteGrabAutonTimer) < RobotMap.TOTE_GRAB_TIME) {
		// // intake tote
		// Intake.spin(RobotMap.INTAKE_MOTOR_SPEED); // TODO: Used to be 0.4 Remove when verified
		// }
		Intake.stop();
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < Math.abs(RobotMap.TURN_DISTANCE))) {
			if (Robot.encoderLeft.get() * -1 < RobotMap.TURN_DISTANCE) {
				Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED);
				Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED);
			} else {
				Robot.canTalonFrontLeft.set(0.0);
				Robot.canTalonRearLeft.set(0.0);
				break;
			}
			if ((Robot.encoderRight.get() * (360.0 / 250.0)) > RobotMap.TURN_DISTANCE * -1) {
				Robot.canTalonFrontRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED);
				Robot.canTalonRearRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED);
			} else {
				Robot.canTalonFrontRight.set(0.0);
				Robot.canTalonRearRight.set(0.0);
				break;
			}
			System.out.println("TRYING!!!");
		}
		System.out.println("FINISHED!!!!!!");
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		// driveForwardAutonomous(r); // Drive forward the same distance as driveForwardAutonomous
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < RobotMap.STOP || Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP)) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
		}
		Robot.robotDrive.stopMotor();
		return;
	}
	
	public static void binGrabAutonomous(Robot r) {
		// close piston
		Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
		
		// timer
		long toteGrabAutonTimer = System.currentTimeMillis();
		while (r.isAutonomous() && r.isEnabled() && (System.currentTimeMillis() - toteGrabAutonTimer) < RobotMap.BIN_GRAB_TIME) {
			// intake tote
			Intake.spin(RobotMap.INTAKE_MOTOR_SPEED); // TODO: Used to be 0.4 Remove when verified
		}
		Intake.stop();
		
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 > Math.abs(RobotMap.TURN_DISTANCE) * -1)) {
			if (Robot.encoderLeft.get() * -1 > RobotMap.TURN_DISTANCE * -1) {
				Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
				Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
			} else {
				Robot.canTalonFrontLeft.set(0.0);
				Robot.canTalonRearLeft.set(0.0);
				break;
			}
			if ((Robot.encoderRight.get() * (360.0 / 250.0)) < RobotMap.TURN_DISTANCE) {
				Robot.canTalonFrontRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
				Robot.canTalonRearRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
			} else {
				Robot.canTalonFrontRight.set(0.0);
				Robot.canTalonRearRight.set(0.0);
				break;
			}
			System.out.println("TRYING!!!");
		}
		System.out.println("FINISHED!!!!!!");
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		// driveForwardAutonomous(r); // Drive forward the same distance as driveForwardAutonomous
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < RobotMap.STOP || Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP)) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
		}
		Robot.robotDrive.stopMotor();
		return;
		/*
		 * // close piston
		 * Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
		 * 
		 * // timer
		 * long toteGrabAutonTimer = System.currentTimeMillis();
		 * while (r.isAutonomous() && r.isEnabled() && (System.currentTimeMillis() - toteGrabAutonTimer) < RobotMap.TOTE_GRAB_TIME) {
		 * // intake tote
		 * Intake.spin(RobotMap.INTAKE_MOTOR_SPEED); // TODO: Used to be 0.4 Remove when verified
		 * }
		 * Intake.stop();
		 * 
		 * Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		 * 
		 * Robot.hugPiston.set(DoubleSolenoid.Value.kForward);
		 * 
		 * boolean elevatorMoveDone = false;
		 * while (!elevatorMoveDone) {
		 * elevatorMoveDone = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_ONE_TOTE_HEIGHT);
		 * }
		 * 
		 * Robot.hugPiston.set(DoubleSolenoid.Value.kReverse);
		 * 
		 * Robot.robotDrive.drive(RobotMap.TOTE_DISTANCE, RobotMap.AUTONOMOUS_CURVE);
		 * 
		 * Robot.encoderLeft.reset();
		 * Robot.encoderRight.reset();
		 * while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 > Math.abs(RobotMap.TURN_DISTANCE) * -1)) {
		 * if (Robot.encoderLeft.get() * -1 > RobotMap.TURN_DISTANCE * -1) {
		 * Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
		 * Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
		 * } else {
		 * Robot.canTalonFrontLeft.set(0.0);
		 * Robot.canTalonRearLeft.set(0.0);
		 * break;
		 * }
		 * if ((Robot.encoderRight.get() * (360.0 / 250.0)) < RobotMap.TURN_DISTANCE) {
		 * Robot.canTalonFrontRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
		 * Robot.canTalonRearRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
		 * } else {
		 * Robot.canTalonFrontRight.set(0.0);
		 * Robot.canTalonRearRight.set(0.0);
		 * break;
		 * }
		 * System.out.println("TRYING!!!");
		 * }
		 * System.out.println("FINISHED!!!!!!");
		 * Robot.encoderLeft.reset();
		 * Robot.encoderRight.reset();
		 * // driveForwardAutonomous(r); // Drive forward the same distance as driveForwardAutonomous
		 * while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < RobotMap.STOP || Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP)) {
		 * Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
		 * }
		 * Robot.robotDrive.stopMotor();
		 * return;
		 */
	}
	
	public static void binAndToteGrabAutonomous(Robot r) {
		
		// move elevator down
		boolean elevatorCalibration = Elevator.calibration(-0.8);
		while (!elevatorCalibration) {
			elevatorCalibration = Elevator.calibration(-0.8);
		}
		
		// close piston
		Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
		
		// timer
		// drive forward to intake the bin
		long binGrabAutonTimer = System.currentTimeMillis();
		while (r.isAutonomous() && r.isEnabled() && (System.currentTimeMillis() - binGrabAutonTimer) < RobotMap.BIN_GRAB_TIME) {
			// intake bin
			Robot.robotDrive.drive(-0.3, RobotMap.AUTONOMOUS_CURVE); //TODO Magic Number
																	// Needs to be negative
																	// 0.1 doesn't move
			Intake.spin(RobotMap.INTAKE_MOTOR_SPEED); // TODO: Used to be 0.4 Remove when verified
		}
		Robot.robotDrive.drive(0.0, 0.0);
		Intake.stop(); // I have a bin -- Yo tengo un bine.
		
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		
		Robot.hugPiston.set(DoubleSolenoid.Value.kForward);

		// pick up bin
		while(r.isEnabled() && r.isAutonomous()) {
			boolean finished = Elevator.move(-1.0, 410);
			if (finished) {
				break; //leave the loop if we are finished moving
			}
		}
		
//		boolean elevatorMoveDone = false;
//		while (!elevatorMoveDone) {
//			elevatorMoveDone = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_ONE_TOTE_HEIGHT + 10.0); // TODO: 10.0
//			System.out.println("ELEVATOR");
//		} Elevator.stop();
		
		// then it drives no it doesn't because it's commented out because its old code
//		if (Robot.encoderLeft.get() * -1 < RobotMap.STOP) {
//			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
//		}
//		if (Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP) {
//			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
//		}
		
		Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
		
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		
		// NOW we drive forward to take in tote
		while (Robot.encoderLeft.get() * -1 < RobotMap.TOTE_DISTANCE && r.isEnabled() && r.isAutonomous()) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE);
			Intake.spin(RobotMap.INTAKE_MOTOR_SPEED); //intake tote here
		}
		
		// timer
//		long toteGrabAutonTimer = System.currentTimeMillis();
//		while (r.isAutonomous() && r.isEnabled() && (System.currentTimeMillis() - toteGrabAutonTimer) < RobotMap.BIN_GRAB_TIME) {
//			// intake tote
//			Intake.spin(RobotMap.INTAKE_MOTOR_SPEED); // TODO: Used to be 0.4 Remove when verified
//		}
		Intake.stop(); // I have a tote -- Yo tengo un tote.
		
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		
		// turn left
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 > Math.abs(RobotMap.TURN_DISTANCE) * -1)) {
			if (Robot.encoderLeft.get() * -1 > RobotMap.TURN_DISTANCE * -1) {
				Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
				Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
			} else {
				Robot.canTalonFrontLeft.set(0.0);
				Robot.canTalonRearLeft.set(0.0);
				break;
			}
			if ((Robot.encoderRight.get() * (360.0 / 250.0)) < RobotMap.TURN_DISTANCE) {
				Robot.canTalonFrontRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
				Robot.canTalonRearRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
			} else {
				Robot.canTalonFrontRight.set(0.0);
				Robot.canTalonRearRight.set(0.0);
				break;
			}
		}
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		// drive forward to get into the auto zone
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < RobotMap.STOP || Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP)) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
		}
		Robot.robotDrive.stopMotor();
		long toteSpitAutonTimer = System.currentTimeMillis();
		while ((r.isAutonomous()) && (r.isEnabled()) && ((System.currentTimeMillis() - toteSpitAutonTimer) < RobotMap.TOTE_SPIT_TIME)) {
			Intake.spin(-RobotMap.INTAKE_MOTOR_SPEED); // TODO: Used to be 0.4 Remove when verified
		}
		Intake.stop();
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
			while (r.isAutonomous() && r.isEnabled() && ((System.currentTimeMillis() - binGrabAutonTimer) < RobotMap.BIN_GRAB_TIME)) {
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
