//package org.usfirst.frc.team668.robot;
//
//import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.Timer;
//
//public class TeleopStateMachine {
//	private static long time = -1;
//
//	public static void stateMachine(boolean isCoopertition, boolean isScoring,
//			boolean isGround, boolean isLift, boolean isManual,
//			boolean isReversing) {
//
//		if (isManual) {
//			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
//
//		}
//
//		switch (RobotMap.currentState) {
//
//		case RobotMap.INIT_STATE: // Makes the elevator go down to the bottom
//									// level
//			boolean finish = Elevator.calibration(RobotMap.SPEED_ELEV);
//
//			if (finish == true) {
//
//				Elevator.stop();
//
//				RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
//			}
//
//			break;
//
//		case RobotMap.ELEVATOR_HEIGHT_TOTE_STATE: // Lifts the elevator to one
//													// tote height
//
//			boolean done = Elevator.move(RobotMap.SPEED_ELEV,
//					RobotMap.ELEVATOR_ENCODER_ONE_TOTE_HEIGHT);
//
//			if (done == true) {
//
//				Elevator.stop();
//
//				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
//
//			}
//
//			break;
//
//		case RobotMap.WAIT_FOR_BUTTON_STATE:// lets the operator choose the
//											// command
//			if (isLift) {
//				RobotMap.currentState = RobotMap.WAIT_FOR_GAME_PIECE_STATE;
//			}
//
//			if (isCoopertition) {
//				RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_COOPERTITION_STATE;
//			}
//
//			if (isGround) {
//				RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_GROUND_STATE;
//			}
//
//			if (isScoring) {
//				RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_SCORING_STATE;
//			}
//
//			if (isReversing) {
//
//				RobotMap.currentState = RobotMap.REVERSE_INTAKE_MOTORS_STATE;
//
//			}
//
//			break;
//
//		case RobotMap.WAIT_FOR_GAME_PIECE_STATE:// waits for the objective to be
//												// within sight of their
//												// respective optical sensor
//
//			Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
//
//			if (Robot.toteOptic.get()) {
//
//				Intake.stop();
//
//				RobotMap.currentState = RobotMap.OPEN_HUG_PISTONS_STATE;
//			}
//
//			if (Robot.binOptic.get()) {
//
//				Intake.stop();
//
//				RobotMap.currentState = RobotMap.OPEN_HUG_PISTONS_STATE;
//			}
//
//			break;
//
//		case RobotMap.OPEN_HUG_PISTONS_STATE:// opens the pistons on the
//												// elevator
//
//			Robot.rightHugPiston.set(DoubleSolenoid.Value.kReverse);
//			Robot.leftHugPiston.set(DoubleSolenoid.Value.kReverse);
//
//			RobotMap.currentState = RobotMap.ELEVATOR_DOWN_STATE;
//
//			break;
//
//		case RobotMap.ELEVATOR_DOWN_STATE:// brings elevator down
//
//			boolean downFinish = Elevator.calibration(RobotMap.SPEED_ELEV);
//
//			if (downFinish == true) {
//
//				Elevator.stop();
//
//				RobotMap.currentState = RobotMap.CLOSE_HUG_PISTONS_STATE;
//			}
//
//			break;
//
//		case RobotMap.CLOSE_HUG_PISTONS_STATE: // closes the hug pistons and
//												// clamps objective
//
//			Robot.rightHugPiston.set(DoubleSolenoid.Value.kForward);
//			Robot.leftHugPiston.set(DoubleSolenoid.Value.kForward);
//
//			RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;// sets
//																		// to
//																		// one
//																		// tote
//																		// height
//
//			break;
//
//		case RobotMap.ELEVATOR_HEIGHT_COOPERTITION_STATE:// sets to coopertition
//															// plate height
//
//			boolean finishCoop = Elevator.move(RobotMap.SPEED_ELEV,
//					RobotMap.ELEVATOR_ENCODER_COOPERTITION);
//
//			if (finishCoop = true) {
//
//				Elevator.stop();
//
//				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
//
//			}
//			break;
//
//		case RobotMap.ELEVATOR_HEIGHT_SCORING_STATE:// sets to scoring platform
//													// height
//			boolean finishScore = Elevator.move(RobotMap.SPEED_ELEV,
//					RobotMap.ELEVATOR_ENCODER_SCORING);
//
//			if (finishScore = true) {
//
//				Elevator.stop();
//
//				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
//
//			}
//			break;
//
//		case RobotMap.ELEVATOR_HEIGHT_GROUND_STATE:// sets to ground height
//
//			boolean finishGround = Elevator.move(RobotMap.SPEED_ELEV,
//					RobotMap.ELEVATOR_ENCODER_GROUND);
//
//			if (finishGround = true) {
//
//				Elevator.stop();
//
//				RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
//
//			}
//			break;
//
//		case RobotMap.REVERSE_INTAKE_MOTORS_STATE:// reverses motors and spits
//													// out
//			if (time < 0) {
//				time = System.currentTimeMillis();
//			}
//			if (System.currentTimeMillis() - time > 2500) {
//				Intake.stop();
//				time = -1;
//				RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
//			} else {
//				Intake.spin(-0.5); // turns on reverse motors
//			}
//
//			break;
//
//		case RobotMap.MANUAL_OVERRIDE_STATE:
//
//			break;
//
//		}
//	}
//}
