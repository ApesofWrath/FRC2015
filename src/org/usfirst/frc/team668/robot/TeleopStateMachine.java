package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Wrapper class for the state machine function during teleop
 * 
 * @author The 668 FRC 2015 Programming Team
 */
public class TeleopStateMachine {
	
	/**
	 * Runs the teleop state machine for auto pick-up and elevators. Takes as parameters booleans
	 * that tell which buttons are pressed.
	 * 
	 * @param isCoopertition
	 *            true if coopertition button is pressed
	 * @param isScoring
	 *            true if scoring platform button is pressed
	 * @param isGround
	 *            true if ground level button is pressed
	 * @param isLift
	 *            true if lifting button is pressed
	 * @param isManual
	 *            true if manual override button is pressed
	 * @param isReversing
	 *            true if we are ejecting the totes
	 * @param isAbort
	 *            true if we are aborting an ejection and lifting tote back up to one tote height
	 */
	
	private static double encoderCounter = -1;
	private static long reverseTimer = -1;
	
	public static void stateMachine(boolean isCoopertition, boolean isScoring, boolean isGround, boolean isLift, boolean isManual, boolean isReversing, boolean isAbort) {
		
		// sets to manual override, which turns off the state machine
		if (isManual && !(RobotMap.currentState == RobotMap.MANUAL_OVERRIDE_STATE)) {
			Robot.debugWriter.println("Manual Override State\n");
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
		}
		
		// state machine switch statement based on our current state
		switch (RobotMap.currentState) {
			case RobotMap.INIT_STATE: // Makes the elevator go down to the bottom level
				if (!Robot.intakePiston.get().equals(DoubleSolenoid.Value.kReverse))
					Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
				boolean finish = Elevator.calibration(-0.8); // TODO: magic number
				
				if (finish == true) {
					Elevator.stop();
					ToteGrabber.moveHugPistons(false);
					Robot.debugWriter.println("Adjust up state\n");
					RobotMap.currentState = RobotMap.ELEVATOR_ADJUST_UP_STATE;
					//changed at request of Weissman to go down but not up when we have no totes
				}
				
				break;
				
			case RobotMap.ELEVATOR_ADJUST_UP_STATE: // makes elevator go up a bit after init
				
				boolean moveFinish = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_PICKUP); //should be about 10
				if (moveFinish == true) {
					Elevator.stop();
					Robot.debugWriter.println("Wait for button state\n");
					// RobotMap.currentState = RobotMap.DRIVE_FORWARDS_STATE;
					RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
				}
				break;
			
			case RobotMap.ELEVATOR_HEIGHT_TOTE_STATE: // Lifts the elevator to one tote height
				// if (Robot.pdp.getCurrent(RobotMap.CAN_TALON_ELEVATOR_PDP_PORT) >= RobotMap.elevatorMotorEmptyDraw
				// - RobotMap.CURRENT_DEAD_ZONE) {
				// RobotMap.elevatorMotorEmptyDraw = Robot.pdp.getCurrent(RobotMap.CAN_TALON_ELEVATOR_PDP_PORT);
				// }
				
				boolean done = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_ONE_TOTE_HEIGHT);
				
				if (done == true) {
					Elevator.stop();
					Robot.debugWriter.println("Wait for Button State\n");
					RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
				}
				
				break;
			
			case RobotMap.WAIT_FOR_BUTTON_STATE:// lets the operator choose the command
				if (isLift) {
					Robot.debugWriter.println("Wait for Game Piece State\n");
					RobotMap.currentState = RobotMap.WAIT_FOR_GAME_PIECE_STATE;
				}
				
				if (isCoopertition) {
					Robot.debugWriter.println("Elevator Height Coopertition State\n");
					RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_COOPERTITION_STATE;
				}
				
				if (isGround) {
					Robot.debugWriter.println("Elevator Height Ground State\n");
					RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_GROUND_STATE;
				}
				
				if (isScoring) {
					Robot.debugWriter.println("Elevator Height Scoring State\n");
					RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_SCORING_STATE;
				}
				
				break;
			
			case RobotMap.WAIT_FOR_GAME_PIECE_STATE: // waits for the objective to be within sight of their respective optical sensor
				Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				
				if (Robot.toteOptic.get()) {
					RobotMap.itemCount++;
					Intake.stop();
					SmartDashboard.putNumber("Number of Items", RobotMap.itemCount);
					RobotMap.currentState = RobotMap.ELEVATOR_ADJUST_STATE;
				}
				
				// if (Robot.pdp.getCurrent(RobotMap.CAN_TALON_INTAKE_LEFT_PDP_PORT) <= RobotMap.intakeMotorFullDraw
				// + RobotMap.CURRENT_DEAD_ZONE
				// && Robot.pdp.getCurrent(RobotMap.CAN_TALON_INTAKE_RIGHT_PDP_PORT) <= RobotMap.intakeMotorFullDraw
				// + RobotMap.CURRENT_DEAD_ZONE) {
				//
				// RobotMap.intakeMotorFullDraw = Robot.pdp.getCurrent(RobotMap.CAN_TALON_INTAKE_LEFT_PDP_PORT);
				//
				// Intake.stop();
				// Robot.debugWriter.println("Open Hug Pistons State\n");
				//
				// RobotMap.currentState = RobotMap.OPEN_HUG_PISTONS_STATE;
				// RobotMap.itemCount++;
				// } else {
				// RobotMap.intakeMotorEmptyDraw = Robot.pdp.getCurrent(RobotMap.CAN_TALON_INTAKE_LEFT_PDP_PORT);
				// }
				
				if (!isLift) {
					RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
					Robot.debugWriter.println("Wait for Button State\n");
					Intake.stop();
				}
				
				if (isAbort) {
					if (RobotMap.itemCount == 0) { //if we have no totes, go down but not up
						RobotMap.currentState = RobotMap.INIT_STATE;
					} else {
						RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
					}
					Intake.stop();
					break;
				}
				
				break;
			
			case RobotMap.ELEVATOR_ADJUST_STATE:
				
				if (encoderCounter < 0) { // keeps track of elevator
					encoderCounter = Robot.encoderElevator.getDistance();
				}
				boolean downFinish = Elevator.move(RobotMap.elevatorMotorSpeed, encoderCounter - 5);
				if (downFinish == true) {
					Elevator.stop();
					encoderCounter = -1;
					Robot.debugWriter.println("Open Hug Pistons State\n");
					// RobotMap.currentState = RobotMap.DRIVE_FORWARDS_STATE;
					RobotMap.currentState = RobotMap.OPEN_HUG_PISTONS_STATE;
				}
				
			case RobotMap.OPEN_HUG_PISTONS_STATE:// opens the pistons on the elevator
				
				Robot.hugPiston.set(DoubleSolenoid.Value.kReverse);
				
				Robot.debugWriter.println("Elevator Down State\n");
				// RobotMap.currentState = RobotMap.DRIVE_BACKWARDS_STATE;
				RobotMap.currentState = RobotMap.ELEVATOR_DOWN_STATE;
				
				break;
			
			case RobotMap.DRIVE_BACKWARDS_STATE: // not currently in use
				
				if (encoderCounter < 0) {
					encoderCounter = Robot.encoderLeft.getDistance();
				}
				if (Math.abs(Robot.encoderLeft.getDistance() - encoderCounter) < RobotMap.DRIVE_BACKWARDS_DISTANCE) {
					Robot.robotDrive.drive(-0.5, 0);
				} else {
					Robot.robotDrive.drive(0, 0);
					encoderCounter = -1.0;
					RobotMap.currentState = RobotMap.ELEVATOR_DOWN_STATE;
				}
				break;
			
			case RobotMap.ELEVATOR_DOWN_STATE:// brings elevator down
				
				// sets intake piston to open if it isn't open yet
				boolean finishThis = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_PICKUP);
				if (finishThis == true) {
					Elevator.stop();
					Robot.debugWriter.println("Close Hug Pistons State\n");
					// RobotMap.currentState = RobotMap.DRIVE_FORWARDS_STATE;
					RobotMap.currentState = RobotMap.CLOSE_HUG_PISTONS_STATE;
				}
				
				break;
			
			case RobotMap.DRIVE_FORWARDS_STATE: // not currently in use
				
				if (encoderCounter < 0) {
					encoderCounter = Robot.encoderLeft.getDistance();
				}
				if (Math.abs(Robot.encoderLeft.getDistance() - encoderCounter) < RobotMap.DRIVE_FORWARDS_DISTANCE) {
					Robot.robotDrive.drive(0.5, 0);
				} else {
					Robot.robotDrive.drive(0, 0);
					encoderCounter = -1.0;
					RobotMap.currentState = RobotMap.CLOSE_HUG_PISTONS_STATE;
				}
				break;
			
			case RobotMap.CLOSE_HUG_PISTONS_STATE: // closes the hug pistons and clamps objective
				
				if (!Robot.hugPiston.get().equals(DoubleSolenoid.Value.kForward)) {
					Robot.hugPiston.set(DoubleSolenoid.Value.kForward);
				}
				if (reverseTimer < 0) { // using same timer to wait a little before closing intake piston
					reverseTimer = System.currentTimeMillis();
				} 
				if ((System.currentTimeMillis() - reverseTimer) > 50){
					reverseTimer = -1;
					Robot.intakePiston.set(DoubleSolenoid.Value.kReverse); // open intake piston
					Robot.debugWriter.println("Elevator Height Coopertition State\n");
					RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;// sets to one tote height
				}
				break;
			
			case RobotMap.ELEVATOR_HEIGHT_COOPERTITION_STATE:// sets to coopertition plate height
				
				boolean finishCoop = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_COOPERTITION);
				
				if (finishCoop == true) {
					Elevator.stop();
					Robot.debugWriter.println("Waiting for Reverse Intake State\n");
					RobotMap.currentState = RobotMap.WAITING_FOR_REVERSE_INTAKE;
				}
				if (isAbort) {
					if (RobotMap.itemCount == 0) { //if we have no totes, go down but not up
						RobotMap.currentState = RobotMap.INIT_STATE;
					} else {
						RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
					}
					Intake.stop();
					break;
				}
				break;
			
			case RobotMap.ELEVATOR_HEIGHT_SCORING_STATE:// sets to scoring platform
														// height
				boolean finishScore = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_SCORING);
				
				if (finishScore == true) {
					Elevator.stop();
					Robot.debugWriter.println("Waiting for Reverse Intake State\n");
					RobotMap.currentState = RobotMap.WAITING_FOR_REVERSE_INTAKE;
				}
				if (isAbort) {
					if (RobotMap.itemCount == 0) { //if we have no totes, go down but not up
						RobotMap.currentState = RobotMap.INIT_STATE;
					} else {
						RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
					}
					Intake.stop();
					break;
				}
				break;
			
			case RobotMap.ELEVATOR_HEIGHT_GROUND_STATE:// sets to ground height
				
				boolean finishGround = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_GROUND);
				
				if (finishGround == true) {
					Elevator.stop();
					Robot.debugWriter.println("Waiting for Reverse Intake State\n");
					RobotMap.currentState = RobotMap.WAITING_FOR_REVERSE_INTAKE;
				}
				if (isAbort) {
					if (RobotMap.itemCount == 0) { //if we have no totes, go down but not up
						RobotMap.currentState = RobotMap.INIT_STATE;
					} else {
						RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
					}
					Intake.stop();
					break;
				}
				break;
			
			case RobotMap.WAITING_FOR_REVERSE_INTAKE:
				
				if (isReversing) {
					Robot.debugWriter.println("Reverse Intake Motors State\n");
					RobotMap.currentState = RobotMap.REVERSE_INTAKE_MOTORS_STATE;
				}
				if (isAbort) {
					if (RobotMap.itemCount == 0) { //if we have no totes, go down but not up
						RobotMap.currentState = RobotMap.INIT_STATE;
					} else {
						RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
					}
					Intake.stop();
					break;
				}
				
				// TODO: make sure the intake piston is open here
				
				break;
			
			case RobotMap.REVERSE_INTAKE_MOTORS_STATE: // reverses motors and spits out
				
				/*
				 * This if statement checks if the talons are not driving at empty current,
				 * implying that they are still pushing a tote out
				 */
				if (reverseTimer < 0) {
					reverseTimer = System.currentTimeMillis();
				}
				
				if (System.currentTimeMillis() - reverseTimer < 3000) // 3 seconds
				{
					Intake.spin(-0.4); //TODO: magic number
					//go slower when we outtake
				} else {
					Intake.stop();
					Robot.debugWriter.println("Ejected " + RobotMap.itemCount + " items\n");
					Robot.debugWriter.println("Elevator Height Tote State\n");
					RobotMap.currentState = RobotMap.INIT_STATE; //when we remove totes, go down but not up
					RobotMap.itemCount = 0;
					reverseTimer = 0;
				}
				
				break;
			
			case RobotMap.MANUAL_OVERRIDE_STATE:
				/*
				 * Manual override does not do anything; it simply overrides the state machine
				 * All the actual manual override stuff is in Robot.java
				 */
				break;
		}
	}
}
