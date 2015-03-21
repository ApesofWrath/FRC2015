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
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		boolean elevatorCalibration = Elevator.calibration(-0.8);
		while (!elevatorCalibration) {
			elevatorCalibration = Elevator.calibration(-0.8);
		}
		return;
	}
	
	public static void driveForwardAutonomous(Robot r) {
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		boolean elevatorCalibration = Elevator.calibration(-0.8);
		while (!elevatorCalibration) {
			elevatorCalibration = Elevator.calibration(-0.8);
		}
		while (r.isAutonomous() && r.isEnabled() && ((Robot.encoderLeft.get() * -1 < RobotMap.STOP) || (Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP))) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
		}
		Robot.robotDrive.drive(0.0, 0.0);
		return;
	}
	
	public static void delayAutonomous() {
		long delayTimer = System.currentTimeMillis();
		while ((System.currentTimeMillis() - delayTimer) < RobotMap.DELAY_TIME) { // in milliseconds
			SmartDashboard.putNumber("Autonomous Timer", delayTimer);
		} // just waits and prints time on SmartDashboard
		return;
	}
	
	public static void toteGrabAutonomous(Robot r) {
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		boolean elevatorCalibration = Elevator.calibration(-0.8);
		while (!elevatorCalibration) {
			elevatorCalibration = Elevator.calibration(-0.8);
		}
		
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
		
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < Math.abs(RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT))) {
			if (Robot.encoderLeft.get() * -1 < RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) {
				Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED);
				Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED);
			} else {
				Robot.canTalonFrontLeft.set(0.0);
				Robot.canTalonRearLeft.set(0.0);
				break;
			}
			if ((Robot.encoderRight.get() * (360.0 / 250.0)) > RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT * -1) {
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
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		boolean elevatorCalibration = Elevator.calibration(-0.8);
		while (!elevatorCalibration) {
			elevatorCalibration = Elevator.calibration(-0.8);
		}
		
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
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 > Math.abs(RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) * -1)) {
			if (Robot.encoderLeft.get() * -1 > RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT * -1) {
				Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
				Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
			} else {
				Robot.canTalonFrontLeft.set(0.0);
				Robot.canTalonRearLeft.set(0.0);
				break;
			}
			if ((Robot.encoderRight.get() * (360.0 / 250.0)) < RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) {
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
		 * while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 > Math.abs(RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) * -1)) {
		 * if (Robot.encoderLeft.get() * -1 > RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT * -1) {
		 * Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
		 * Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
		 * } else {
		 * Robot.canTalonFrontLeft.set(0.0);
		 * Robot.canTalonRearLeft.set(0.0);
		 * break;
		 * }
		 * if ((Robot.encoderRight.get() * (360.0 / 250.0)) < RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) {
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

		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
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
			boolean finished = Elevator.move(-1.0, 410); //TODO: make a constant for this
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
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 > Math.abs(RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) * -1)) {
			if (Robot.encoderLeft.get() * -1 > RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT * -1) {
				Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
				Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
			} else {
				Robot.canTalonFrontLeft.set(0.0);
				Robot.canTalonRearLeft.set(0.0);
				break;
			}
			if ((Robot.encoderRight.get() * (360.0 / 250.0)) < RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) {
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
		// GET THIS FROM GIT 2/17/15 2:07 PM If you really want it
	}
	
	public static void spinAwayAutonomous(Robot r){
		
		
		// This autonomous code will spin away the bins in the way of the totes and stack all three totes
		//opens the intake TODO: may want to have it closed
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		
		//moves the elevator down
		boolean elevatorCalibration = Elevator.calibration(-0.8);
		while (!elevatorCalibration) {
			elevatorCalibration = Elevator.calibration(-0.8);
		}
		 
	
		
		// closes piston 
		Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
		
		// timer and drives forward while intaking the first yellow tote

		while (r.isAutonomous() && r.isEnabled() && Robot.encoderLeft.get() * -1 < RobotMap.FIRST_TOTE_DISTANCE * RobotMap.AUTON_DISTANCE_CONSTANT) {
			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.AUTONOMOUS_CURVE);
			
			Intake.spin(RobotMap.INTAKE_MOTOR_SPEED); // intakes the first yellow tote
		}
		Robot.robotDrive.drive(0.0, 0.0); //TODO: sudden stops may jerk the tote out of the robot. we may need a gradual slowdown
		Intake.stop();
		
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		
		Robot.hugPiston.set(DoubleSolenoid.Value.kForward);
		
		while(r.isEnabled() && r.isAutonomous()) {  // lifts the tote to 410 encoder height
			boolean finished = Elevator.move(-1.0, 410); //TODO: please make a constant for this
			if (finished) {
				break; 
			}
		}
		
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		
		Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
		
		while (Robot.encoderLeft.get() * -1 < RobotMap.FIRST_BIN_DISTANCE * RobotMap.AUTON_DISTANCE_CONSTANT && r.isAutonomous() && r.isEnabled()){ // goes up to the first bin
			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.AUTONOMOUS_CURVE);
		}
		
		Robot.robotDrive.drive(0.0, 0.0);
		
		Robot.encoderRight.reset();
		Robot.encoderLeft.reset();
		
		long spinAwayTimer1 = System.currentTimeMillis();
		while (r.isAutonomous() && r.isEnabled() && (System.currentTimeMillis() - spinAwayTimer1) < RobotMap.SPIN_AWAY_TIME){
			Intake.spinAway(RobotMap.INTAKE_MOTOR_SPEED);
		}
		
		Intake.stop();
		
		
		while (Robot.encoderLeft.get() * -1 < RobotMap.SECOND_TOTE_DISTANCE * RobotMap.AUTON_DISTANCE_CONSTANT && r.isAutonomous() && r.isEnabled()){
			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.AUTONOMOUS_CURVE);
		
			Intake.spin(RobotMap.INTAKE_MOTOR_SPEED); // intakes the second yellow tote
		}	
		
	    	Robot.robotDrive.drive(0.0, 0.0);
	    
		Intake.stop();
		
		Robot.encoderRight.reset();
		Robot.encoderLeft.reset();
		
		Robot.hugPiston.set(DoubleSolenoid.Value.kReverse);
		
		//picks up tote
		elevatorCalibration = Elevator.calibration(-0.8);
		while (!elevatorCalibration) {
			elevatorCalibration = Elevator.calibration(-0.8);
		}
		
		Robot.hugPiston.set(DoubleSolenoid.Value.kForward);
		
		while (r.isAutonomous() && r.isEnabled()) { // lifts the second tote to 410 encoder height
			
			boolean finished = Elevator.move(-1.0, 410); //TODO: Magic number
			if (finished) {
				break; 
			}
			
		}
		
		Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
		
		while (Robot.encoderLeft.get() * -1 < RobotMap.SECOND_BIN_DISTANCE * RobotMap.AUTON_DISTANCE_CONSTANT && r.isAutonomous() && r.isEnabled()) {
			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.AUTONOMOUS_CURVE);
		}
		
		Robot.robotDrive.drive(0.0, 0.0);
		
		Robot.encoderRight.reset();
		Robot.encoderLeft.reset();
		
		long spinAwayTimer2 = System.currentTimeMillis(); // spin away the second tote 
		while (r.isAutonomous() && r.isEnabled() && (System.currentTimeMillis() - spinAwayTimer2) < RobotMap.SPIN_AWAY_TIME){
			Intake.spinAway(RobotMap.INTAKE_MOTOR_SPEED); 
		}
		
		Intake.stop();
		

		while (r.isEnabled() && r.isAutonomous() && Robot.encoderLeft.get() * -1 < RobotMap.THIRD_TOTE_DISTANCE * RobotMap.AUTON_DISTANCE_CONSTANT){
			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.AUTONOMOUS_CURVE);
			
			Intake.spin(RobotMap.INTAKE_MOTOR_SPEED); // intakes the third yellow tote
			
		}
		
		Robot.robotDrive.drive(0.0, 0.0);
		Intake.stop();
		
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		
		Robot.hugPiston.set(DoubleSolenoid.Value.kReverse); // unclamps the two totes on the top so it can go down and clamp the next
		
		elevatorCalibration = Elevator.calibration(-0.8);
		while (!elevatorCalibration) {
			elevatorCalibration = Elevator.calibration(-0.8);
		}
		
		Robot.hugPiston.set(DoubleSolenoid.Value.kForward); // clamps the bottom of the stack. IT will not go up, we are just going to drag it.
		
		// turns in place to the RIGHT
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * 1 > Math.abs(RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) * -1)) {
			if (Robot.encoderLeft.get() * -1 > RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT * -1) {
				Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * 1); //TODO:Make sure these motors are flipped n stuff
				Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * 1);
			} else {
				Robot.canTalonFrontLeft.set(0.0);
				Robot.canTalonRearLeft.set(0.0);
				break;
			}
			if ((Robot.encoderRight.get() * (-360.0 / 250.0)) < RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) {
				Robot.canTalonFrontRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED * 1);
				Robot.canTalonRearRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED * 1);
			} else {
				Robot.canTalonFrontRight.set(0.0);
				Robot.canTalonRearRight.set(0.0);
				break;
			}
		}
//		double turnInPlaceTimer = System.currentTimeMillis(); // turns in place towards the auto zone
//		while (r.isEnabled() && r.isAutonomous() && (System.currentTimeMillis() - turnInPlaceTimer) < RobotMap.TURN_IN_PLACE_TIME){
//			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.TURN_IN_PLACE_CURVE);
//		}
		
		Robot.robotDrive.drive(0.0, 0.0);
		
		// drives to the auto zone. 
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < RobotMap.STOP || Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP)) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
		}
		Robot.robotDrive.stopMotor();
		
//		double driveToAutoZone = System.currentTimeMillis();// driving towards the auto zone
//		while (r.isEnabled() && r.isAutonomous() && (System.currentTimeMillis() - turnInPlaceTimer) < RobotMap.TURN_IN_PLACE_TIME){
//			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.AUTONOMOUS_CURVE);
//		}
		
		 Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		 Robot.hugPiston.set(DoubleSolenoid.Value.kReverse);
	
		 
		 return;
	}
	
	
	
	
	public static void twoToteBinStacksAutonomous(Robot r) {
		// open intake
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
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
		while (r.isAutonomous() && r.isEnabled() && (System.currentTimeMillis() - binGrabAutonTimer) < RobotMap.BIN_GRAB_TIME * RobotMap.AUTON_DISTANCE_CONSTANT) {
			// intake bin
			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.AUTONOMOUS_CURVE); //TODO Magic Number
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
		while (Robot.encoderLeft.get() * -1 < RobotMap.TOTE_DISTANCE * RobotMap.AUTON_DISTANCE_CONSTANT && r.isEnabled() && r.isAutonomous()) {
			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.AUTONOMOUS_CURVE);
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
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 > Math.abs(RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) * -1)) {
			if (Robot.encoderLeft.get() * -1 > RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT * -1) {
				Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
				Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED * -1);
			} else {
				Robot.canTalonFrontLeft.set(0.0);
				Robot.canTalonRearLeft.set(0.0);
				break;
			}
			if ((Robot.encoderRight.get() * (360.0 / 250.0)) < RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) {
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
		
		ToteGrabber.moveHugPistons(false);
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < RobotMap.STOP * RobotMap.AUTON_DISTANCE_CONSTANT || Robot.encoderRight.get() * 360.0 / 250.0 < RobotMap.STOP * RobotMap.AUTON_DISTANCE_CONSTANT)) {
			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
			Elevator.move(1.0, 0.0);
		}
		ToteGrabber.moveHugPistons(false);
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		
		Robot.robotDrive.stopMotor();
		long toteSpitAutonTimer = System.currentTimeMillis();
		while ((r.isAutonomous()) && (r.isEnabled()) && ((System.currentTimeMillis() - toteSpitAutonTimer) < RobotMap.TOTE_SPIT_TIME)) {
			Intake.spin(-RobotMap.INTAKE_MOTOR_SPEED); // TODO: Used to be 0.4 Remove when verified
		}
		Intake.stop();

//		while (!Elevator.calibration(1.0)) {} // While the elevator isn't at the lower limit switch
		
		Robot.hugPiston.set(DoubleSolenoid.Value.kReverse);
		Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
		// It should all be on the ground
		
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		// drive backwards to get out of the auto zone
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 > RobotMap.STOP * RobotMap.AUTON_DISTANCE_CONSTANT * -1 || Robot.encoderRight.get() * 360.0 / 250.0 > RobotMap.STOP * RobotMap.AUTON_DISTANCE_CONSTANT * -1)) {
			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED * -1, RobotMap.AUTONOMOUS_CURVE); // Curve is 0 (this still doesn't work properly with a curve)
			Intake.spin(-RobotMap.INTAKE_MOTOR_SPEED); // Spit out just so that we don't drag the tote
		}
		Robot.robotDrive.stopMotor();
		Intake.stop();
		
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		
		// turn right
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < Math.abs(RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT))) {
			if (Robot.encoderLeft.get() * -1 < RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT) {
				Robot.canTalonFrontLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED);
				Robot.canTalonRearLeft.set(RobotMap.AUTONOMOUS_CURVE_SPEED);
			} else {
				Robot.canTalonFrontLeft.set(0.0);
				Robot.canTalonRearLeft.set(0.0);
				break;
			}
			if ((Robot.encoderRight.get() * (360.0 / 250.0)) > RobotMap.TURN_DISTANCE * RobotMap.AUTON_TURN_CONSTANT * -1) {
				Robot.canTalonFrontRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED);
				Robot.canTalonRearRight.set(RobotMap.AUTONOMOUS_CURVE_SPEED);
			} else {
				Robot.canTalonFrontRight.set(0.0);
				Robot.canTalonRearRight.set(0.0);
				break;
			}
		}
		
		while (r.isAutonomous() && r.isEnabled() && (Robot.encoderLeft.get() * -1 < RobotMap.BOX_DISTANCE * RobotMap.AUTON_DISTANCE_CONSTANT || Robot.encoderRight.get() * 360.0 / 250.0 > RobotMap.BOX_DISTANCE * RobotMap.AUTON_DISTANCE_CONSTANT)) {
			Robot.robotDrive.drive(RobotMap.AUTON_FAST_SPEED, RobotMap.AUTONOMOUS_CURVE);
		}
		Robot.robotDrive.drive(0.0, 0.0);
		
		Robot.encoderLeft.reset();
		Robot.encoderRight.reset();
		
		binAndToteGrabAutonomous(r);
		
		return;
	}
	
	public static void celebrate(Robot r) {
		while (r.isAutonomous() && r.isEnabled()) {
			Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, 0.5);
		}
		return;
	}
	
	
	
	
	
	
}
