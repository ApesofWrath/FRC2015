package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;

public class TeleopStateMachine {
	public void stateMachine(boolean isCoopertition, boolean isScoring,
			boolean isGround, boolean isLift) {
		switch (RobotMap.currentState) {

		case RobotMap.INIT_STATE: // Makes the elevator go down to the bottom level

			Elevator.goDown(0.5);
			Elevator.stop();
			RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
			break;

		case RobotMap.ELEVATOR_HEIGHT_TOTE_STATE: // Lifts the elevator to one tote height
			
     //		Elevator.goUp(speed, stop);
			
			
			RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
			break;

		case RobotMap.WAIT_FOR_BUTTON_STATE:// lets the operator choose the
											// command
			if (isLift) {
				RobotMap.currentState = RobotMap.WAIT_FOR_GAME_PIECE_STATE;
			}
			if (isCoopertition) {
				RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_COOPERTITION_STATE;
			}
			if (isGround) {
				RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_GROUND_STATE;
			}
			if (isScoring) {
				RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_SCORING_STATE;
			}
			break;

		case RobotMap.WAIT_FOR_GAME_PIECE_STATE:// waits for the objective to be
												// within sight of their
												// respective optical sensor

			Intake.spin(0.5);

			if (Robot.toteOptic.get()) {

				Intake.stop();

				RobotMap.currentState = RobotMap.OPEN_HUG_PISTONS_STATE;
			}

			if (Robot.binOptic.get()) {

				Intake.stop();

				RobotMap.currentState = RobotMap.OPEN_HUG_PISTONS_STATE;
			}

			break;

		case RobotMap.OPEN_HUG_PISTONS_STATE:// opens the pistons on the
												// elevator

			Robot.rightHugPiston.set(DoubleSolenoid.Value.kReverse);
			Robot.leftHugPiston.set(DoubleSolenoid.Value.kReverse);

			RobotMap.currentState = RobotMap.ELEVATOR_DOWN_STATE;

			break;

		case RobotMap.ELEVATOR_DOWN_STATE:// brings elevator down

			RobotMap.currentState = RobotMap.CLOSE_HUG_PISTONS_STATE;
			break;

		case RobotMap.CLOSE_HUG_PISTONS_STATE: // closes the hug pistons and
												// clamps objective

			Robot.rightHugPiston.set(DoubleSolenoid.Value.kForward);
			Robot.leftHugPiston.set(DoubleSolenoid.Value.kForward);

			RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;// sets
																		// to
																		// one
																		// tote
																		// height
			break;

		case RobotMap.ELEVATOR_HEIGHT_COOPERTITION_STATE:// sets to coopertition
															// plate height
			RobotMap.currentState = RobotMap.REVERSE_INTAKE_MOTORS_STATE;
			break;

		case RobotMap.ELEVATOR_HEIGHT_SCORING_STATE:// sets to scoring platform
													// height
			RobotMap.currentState = RobotMap.REVERSE_INTAKE_MOTORS_STATE;
			break;

		case RobotMap.ELEVATOR_HEIGHT_GROUND_STATE:// sets to ground height
			RobotMap.currentState = RobotMap.REVERSE_INTAKE_MOTORS_STATE;
			break;

		case RobotMap.REVERSE_INTAKE_MOTORS_STATE:// reverses motors and spits
													// out

			Intake.spin(-0.5); // turns on reverse motors

			Timer.delay(10);// waits a bit

			Intake.stop();// stops

			RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
			break;

		case RobotMap.MANUAL_OVERRIDE_STATE:

			break;

		}
	}
}
