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
	public static boolean startingAtBottom = true;
	public static boolean humanToNormal = false; //going from human player to normal state machine
	
	public static void stateMachine(boolean isCoopertition, boolean isScoring, boolean isLift, boolean isManual, boolean isReversing, boolean isToteHeight, boolean isAbort, boolean isHPStrat) {
		
		SmartDashboard.putBoolean("startingAtBottom", startingAtBottom);
		
		// sets to manual override, which turns off the state machine
		if (isManual && !(RobotMap.currentState == RobotMap.MANUAL_OVERRIDE_STATE)) {
			Robot.debugWriter.println("Manual Override State\n");
			Elevator.stop();
			Intake.stop();
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
		}
		
		if (isReversing && !(RobotMap.currentState == RobotMap.MANUAL_OVERRIDE_STATE)) // while the operator holds reverse button, outtake
		{
			Intake.spin(RobotMap.OUTTAKE_MOTOR_SPEED);
			// go slower when we outtake
		} else {
			Intake.stop();
		}
		
		// state machine switch statement based on our current state
		switch (RobotMap.currentState) {
			case RobotMap.INIT_STATE: // Makes the elevator go down to the bottom level
				SmartDashboard.putString("State:", "Initializing");
				if (!Robot.intakePiston.get().equals(DoubleSolenoid.Value.kReverse)) {
					Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
				}
				
				if (!Robot.hugPiston.get().equals(DoubleSolenoid.Value.kReverse)) {
					ToteGrabber.moveHugPistons(false);
				}
//				boolean finish = Elevator.calibration(-0.8); // TODO: magic number
				boolean finish = Elevator.moveP(0);
				
				if (finish == true) {
					Elevator.stop();
					startingAtBottom = true;
					Robot.debugWriter.println("Adjust up state\n");
					RobotMap.currentState = RobotMap.ELEVATOR_ADJUST_UP_STATE;
					// changed at request of Weissman to go down but not up when we have no totes
				}
				
				int checkState;
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Init State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Init State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.ELEVATOR_ADJUST_UP_STATE: // makes elevator go up a bit after init
				SmartDashboard.putString("State:", "Elevator Adjust Up");
				
//				boolean moveFinish = Elevator.move(0.5, RobotMap.ELEVATOR_ENCODER_PICKUP_ADJUST); // should be about 10
				boolean moveFinish = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_PICKUP_ADJUST);
				
				if (moveFinish == true) {
					Elevator.stop();
					Robot.debugWriter.println("Wait for button state\n");
					// RobotMap.currentState = RobotMap.DRIVE_FORWARDS_STATE;
					RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Elevator Adjust Up State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Elevator Adjust Up State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.ELEVATOR_HEIGHT_TOTE_STATE: // Lifts the elevator to one tote height
				// if (Robot.pdp.getCurrent(RobotMap.CAN_TALON_ELEVATOR_PDP_PORT) >= RobotMap.elevatorMotorEmptyDraw
				// - RobotMap.CURRENT_DEAD_ZONE) {
				// RobotMap.elevatorMotorEmptyDraw = Robot.pdp.getCurrent(RobotMap.CAN_TALON_ELEVATOR_PDP_PORT);
				// }
				SmartDashboard.putString("State:", "Elevator Height Tote");
//				boolean done = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_ONE_TOTE_HEIGHT);
				boolean done = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_ONE_TOTE_HEIGHT);
				
				if (done == true) {
					Elevator.stop();
					Robot.debugWriter.println("Wait for Button State\n");
					RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Elevator Height Tote State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Elevator Height Tote State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.WAIT_FOR_BUTTON_STATE:// lets the operator choose the command
				SmartDashboard.putString("State:", "Wait for Button");
				if (isLift) {
					Robot.debugWriter.println("Wait for Game Piece State\n");
					RobotMap.currentState = RobotMap.WAIT_FOR_GAME_PIECE_STATE;
				}
				
				if (isCoopertition) {
					Robot.debugWriter.println("Elevator Height Coopertition State\n");
					RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_COOPERTITION_STATE;
				}
				
				// if (isGround) {
				// Robot.debugWriter.println("Elevator Height Ground State\n");
				// RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_GROUND_STATE;
				// }
				if (isToteHeight) {
					Robot.debugWriter.println("Elevator Tote Height State\n");
					RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
				}
				if (isScoring) {
					Robot.debugWriter.println("Elevator Height Scoring State\n");
					RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_SCORING_STATE;
				}
				
				if (isHPStrat) {
					System.out.println("Human Player Strategy Start State\n");
					RobotMap.currentState = RobotMap.HUMAN_PLAYER_STRATEGY_STATE_INIT;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Wait for Button State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Wait for Button State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.WAIT_FOR_GAME_PIECE_STATE: // waits for the objective to be within sight of their respective optical sensor
				SmartDashboard.putString("State:", "Wait for Game Piece");
				Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				
				if (!Robot.toteOptic.get() || Robot.joystickOp.getRawButton(RobotMap.PRETEND_BIN_DETECTED_BUTTON) == true) {
					RobotMap.currentState = RobotMap.TIME_DELAY_AFTER_TOTE_SENSE_STATE;
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
					RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
					Intake.stop();
					break;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Wait for Game Piece State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Wait for Game Piece State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.TIME_DELAY_AFTER_TOTE_SENSE_STATE: // waits after getting a game piece
				Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				if (reverseTimer < 0) { // timer to wait for 10 ms
					reverseTimer = System.currentTimeMillis();
				}
				if ((System.currentTimeMillis() - reverseTimer) > 10) {
					reverseTimer = -1;
					RobotMap.itemCount++;
					// Intake.stop();
					// to be done in closehugpistons state
					SmartDashboard.putNumber("Number of Items", RobotMap.itemCount);
//					RobotMap.currentState = RobotMap.ELEVATOR_ADJUST_STATE;
					RobotMap.currentState = RobotMap.OPEN_HUG_PISTONS_STATE;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Time Delay After Tote Sense State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Time Delay After Tote Sense State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.ELEVATOR_ADJUST_STATE:
				SmartDashboard.putString("State:", "Elevator Adjust");
				
				Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				
				if (encoderCounter < 0) { // keeps track of elevator
					encoderCounter = Robot.encoderElevator.get();
				}
				boolean downFinish = Elevator.move(RobotMap.elevatorMotorSpeed, encoderCounter - 20);
				if (downFinish == true) {
					Elevator.stop();
					encoderCounter = -1;
					Robot.debugWriter.println("Open Hug Pistons State\n");
					// RobotMap.currentState = RobotMap.DRIVE_FORWARDS_STATE;
					RobotMap.currentState = RobotMap.OPEN_HUG_PISTONS_STATE;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Elevator Adjust State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Elevator Adjust State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
				
				
			case RobotMap.OPEN_HUG_PISTONS_STATE:// opens the pistons on the elevator
				SmartDashboard.putString("State:", "Open Hug Pistons");
				
				Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				
				if (!Robot.hugPiston.get().equals(DoubleSolenoid.Value.kReverse)) {
					Robot.hugPiston.set(DoubleSolenoid.Value.kReverse);
				}
				
				if (reverseTimer <= 0) {
					reverseTimer = System.currentTimeMillis();
				}
				
				if ((System.currentTimeMillis() - reverseTimer) > 100) { // TODO: Magic number
					reverseTimer = -1;
					
					Robot.debugWriter.println("Elevator Down State\n");
					// RobotMap.currentState = RobotMap.DRIVE_BACKWARDS_STATE;
					if (startingAtBottom==false) {
						RobotMap.currentState = RobotMap.ELEVATOR_PICKUP_HEIGHT_STATE;
						SmartDashboard.putBoolean("Going to Pickup Height", true);
					} else {
						//this
						RobotMap.currentState = RobotMap.CLOSE_HUG_PISTONS_STATE;
						SmartDashboard.putBoolean("Going to Pickup Height", false);
						startingAtBottom = false;
					}
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Open Hug Pistons State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Open Hug Pistons State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.DRIVE_BACKWARDS_STATE: // not currently in use because it is useless
				
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
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Drive Backwards State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Drive Backwards State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			//currently not in use
			case RobotMap.ELEVATOR_DOWN_STATE:// brings elevator down
				SmartDashboard.putString("State:", "Elevator Down");
				
				Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				
				// sets intake piston to open if it isn't open yet
				// boolean finishThis = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_PICKUP);
				boolean finishThis = Elevator.move(RobotMap.elevatorMotorSpeed, 27); //TODO Magic #
				if (finishThis == true) {
					Elevator.stop();
					Robot.debugWriter.println("Elevator Pickup Height State\n");
					// RobotMap.currentState = RobotMap.DRIVE_FORWARDS_STATE;
					// RobotMap.currentState = RobotMap.CLOSE_HUG_PISTONS_STATE;
					RobotMap.currentState = RobotMap.ELEVATOR_PICKUP_HEIGHT_STATE;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Elevator Down State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Elevator Down State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.ELEVATOR_PICKUP_HEIGHT_STATE:
				SmartDashboard.putString("State:", "Elevator Pickup Height");
				
				Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				
				// sets intake piston to open if it isn't open yet
				// boolean finishThis = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_PICKUP);
//				boolean finished = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_PICKUP);
				boolean finished = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_PICKUP);
				if (finished == true) {
					Elevator.stop();
					Robot.debugWriter.println("Close Hug Pistons State\n");
					// RobotMap.currentState = RobotMap.DRIVE_FORWARDS_STATE;
					// RobotMap.currentState = RobotMap.CLOSE_HUG_PISTONS_STATE;
					RobotMap.currentState = RobotMap.CLOSE_HUG_PISTONS_STATE;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Elevator Pickup Height State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Elevator Pickup Height State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
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
				SmartDashboard.putString("State:", "Close Hug Pistons");
				
				Intake.stop();
				
				if (!Robot.hugPiston.get().equals(DoubleSolenoid.Value.kForward)) { // closes hug pistons
					Robot.hugPiston.set(DoubleSolenoid.Value.kForward);
				}
				if (reverseTimer < 0) { // using same timer to wait a little before closing intake piston
					reverseTimer = System.currentTimeMillis();
				}
				SmartDashboard.putNumber("Start Time", reverseTimer);
				SmartDashboard.putNumber("Current Time", System.currentTimeMillis());
				if ((System.currentTimeMillis() - reverseTimer) > 100) {
					reverseTimer = -1;
					Robot.intakePiston.set(DoubleSolenoid.Value.kReverse); // open intake piston
					Robot.debugWriter.println("Wait for Button State\n");
					RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;// sets to one tote height
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Close Hug Pistons State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Close Hug Pistons State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.ELEVATOR_HEIGHT_COOPERTITION_STATE:// sets to coopertition plate height
				SmartDashboard.putString("State:", "Elevator Height Coopertition");
				
				boolean finishCoop = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_COOPERTITION);
				
				if (finishCoop == true) {
					Elevator.stop();
					Robot.debugWriter.println("Waiting for Reverse Intake State\n");
					RobotMap.currentState = RobotMap.WAITING_FOR_REVERSE_INTAKE_STATE;
				}
				if (isAbort) {
					RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
					Intake.stop();
					break;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Elevator Height Coopertition State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Elevator Height Coopertition State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.ELEVATOR_HEIGHT_SCORING_STATE:// sets to scoring platform height
				SmartDashboard.putString("State:", "Elevator Height Scoring");
				
//				boolean finishScore = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_SCORING);
				boolean finishScore = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_SCORING);
				
				if (finishScore == true) {
					Elevator.stop();
					Robot.debugWriter.println("Waiting for Reverse Intake State\n");
					RobotMap.currentState = RobotMap.WAITING_FOR_REVERSE_INTAKE_STATE;
				}
				if (isAbort) {
					RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
					Intake.stop();
					break;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Elevator Height Scoring State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Elevator Height Scoring State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				break;
			
			case RobotMap.ELEVATOR_HEIGHT_GROUND_STATE:// sets to ground height
				SmartDashboard.putString("State:", "Elevator Height Ground");
				
//				boolean finishGround = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_GROUND);
				boolean finishGround = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_GROUND);

				if (finishGround == true) {
					Elevator.stop();
					Robot.debugWriter.println("Waiting for Reverse Intake State\n");
					RobotMap.currentState = RobotMap.WAITING_FOR_REVERSE_INTAKE_STATE;
				}
				if (isAbort) {
					RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
					Intake.stop();
					break;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Elevator Height Ground State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Elevator Height Ground State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.WAITING_FOR_REVERSE_INTAKE_STATE:
				SmartDashboard.putString("State:", "Waiting for Reverse Intake");
				if (isReversing) {
					Robot.debugWriter.println("Reverse Intake Motors State\n");
					RobotMap.currentState = RobotMap.REVERSE_INTAKE_MOTORS_STATE;
				}
				if (isAbort) {
					RobotMap.currentState = RobotMap.INIT_STATE; //after we finish getting rid of totes
					Intake.stop();
					break;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Waiting for Reverse Intake State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Waiting for Reverse Intake State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				// TODO: make sure the intake piston is open here
				
				break;
			
			case RobotMap.REVERSE_INTAKE_MOTORS_STATE: // reverses motors and spits out
				SmartDashboard.putString("State:", "Reverse Intake Motors");
				
				/*
				 * This if statement checks if the talons are not driving at empty current,
				 * implying that they are still pushing a tote out
				 */
				// commenting timer out; not sure why we are using it
				// better to have operator hold fire button
				// if (reverseTimer < 0) {
				// reverseTimer = System.currentTimeMillis();
				// }
				
				// if (System.currentTimeMillis() - reverseTimer < 3000) // 3 seconds
				if (isReversing) // while the operator holds reverse button, outtake
				{
					Intake.spin(RobotMap.OUTTAKE_MOTOR_SPEED);
					// go slower when we outtake
				} else {
					Intake.stop();
					Robot.debugWriter.println("Ejected " + RobotMap.itemCount + " items\n");
					Robot.debugWriter.println("Elevator Height Tote State\n");
					RobotMap.currentState = RobotMap.WAITING_FOR_REVERSE_INTAKE_STATE;
					// go back to waiting for reverse intake in case we aren't done getting rid of totes
					// if we are, press the abort button
					RobotMap.itemCount = 0;
					reverseTimer = -1;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Reverse Intake Motors State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Reverse Inte Motors State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			//added so we can sit near the human station and make stacks
			case RobotMap.HUMAN_PLAYER_STRATEGY_STATE_INIT:
				SmartDashboard.putString("State:", "Human Player Strategy");

				Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
//				boolean finishInit = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_GROUND);
				boolean finishInit = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_GROUND);
				
				if (finishInit == true) {
					Elevator.stop();
					Robot.hugPiston.set(DoubleSolenoid.Value.kReverse);
					RobotMap.currentState = RobotMap.HUMAN_PLAYER_STRATEGY_WAIT_HEIGHT_STATE;	
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Human Player Strategy State Init")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Human Player Strategy State Init";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			case RobotMap.HUMAN_PLAYER_BIN_GRAB_STATE:
				
				boolean finishBin = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_HP_PICKUP_HEIGHT);
				
				if (finishBin){
					Elevator.stop();
					Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
					RobotMap.currentState = RobotMap.HUMAN_PLAYER_STRATEGY_WAIT_HEIGHT_STATE;
				}
				
				
			break;
				
			case RobotMap.HUMAN_PLAYER_STRATEGY_WAIT_HEIGHT_STATE:
				
//				boolean finishWait = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_HP_WAIT);
				boolean finishWait = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_HP_WAIT);
				
				if (finishWait == true) {
					Elevator.stop();
					RobotMap.currentState = RobotMap.HUMAN_PLAYER_STRATEGY_WAIT_STATE;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Human Player Strategy Wait Height State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Human Player Strategy Wait Height State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.HUMAN_PLAYER_STRATEGY_WAIT_STATE:
				
				if (Robot.toteOptic.get()) {
					Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				} else {
					Intake.stop();
				}
				
				if (Robot.joystickOp.getRawButton(RobotMap.HP_PICKUP_BUTTON)) {
					humanToNormal = false;
					RobotMap.currentState = RobotMap.HUMAN_PLAYER_STRATEGY_DROP_STATE;
				} else if (isAbort) { // exit human player state machine
					humanToNormal = true;
					RobotMap.currentState = RobotMap.HUMAN_PLAYER_STRATEGY_DROP_STATE;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Human Player Strategy Wait State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Human Player Strategy Wait State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
				
			case RobotMap.HUMAN_PLAYER_STRATEGY_DROP_STATE:
				
				if (Robot.toteOptic.get()) {
					Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				} else {
					Intake.stop();
				}
				boolean dropTote = Elevator.moveP(718);
				if (dropTote == true) {
					Robot.hugPiston.set(DoubleSolenoid.Value.kReverse);
					RobotMap.currentState = RobotMap.HUMAN_PLAYER_STRATEGY_STATE;
				}
				break;
				
			case RobotMap.HUMAN_PLAYER_STRATEGY_STATE:
				
//				boolean strategyTote = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_HP_PICKUP_HEIGHT);
			//	Robot.intakePiston.set(DoubleSolenoid.Value.kReverse); // TODO: uncomment
				
				if (Robot.toteOptic.get()) {
					Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				} else {
					Intake.stop();
				}

				boolean strategyTote;
				if (humanToNormal) {
					strategyTote = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_PICKUP);
				} else {
					strategyTote = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_HP_PICKUP_HEIGHT);
				}
				
				if (strategyTote == true) {	
	
					Elevator.stop();
					Intake.stop();
					Robot.hugPiston.set(DoubleSolenoid.Value.kForward);
					if (humanToNormal) {
						RobotMap.currentState = RobotMap.WAIT_FOR_BUTTON_STATE;
					} else {
						RobotMap.currentState = RobotMap.HUMAN_PLAYER_DELAY_STATE;
					}
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Human Player Strategy State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Human Player Strategy State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
				

			case RobotMap.HUMAN_PLAYER_DELAY_STATE:
				
				if (reverseTimer <= 0) {
					reverseTimer = System.currentTimeMillis();
				}
				if (System.currentTimeMillis() - reverseTimer > 100) {
					reverseTimer = -1;
					RobotMap.currentState = RobotMap.HUMAN_PLAYER_STRATEGY_RESET_STATE;
				}
			break;
			
			case RobotMap.HUMAN_PLAYER_STRATEGY_RESET_STATE:
//				boolean backToWait = Elevator.move(RobotMap.elevatorMotorSpeed, RobotMap.ELEVATOR_ENCODER_HP_WAIT);
				boolean backToWait = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_HP_WAIT);
				
				if (backToWait) {
					Elevator.stop();
					RobotMap.currentState = RobotMap.HUMAN_PLAYER_STRATEGY_WAIT_STATE;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Human Player Strategy Reset State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Human Player Strategy Reset State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.MANUAL_OVERRIDE_STATE:
				/*
				 * Manual override does not do anything; it simply overrides the state machine
				 * All the actual manual override stuff is in Robot.java
				 */
				SmartDashboard.putString("State:", "Manual Override");
				if (Robot.joystickOp.getRawButton(RobotMap.RETURN_TO_STATE_MACHINE_BUTTON)) {
					Robot.debugWriter.println("Returning to state machine\n");
					RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_RETURN_STATE;
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Manual Override State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Manual Override State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
			
			case RobotMap.MANUAL_OVERRIDE_RETURN_STATE: // Init with closing the clamps optional
				SmartDashboard.putString("State:", "Returning from Override");
				
				boolean clampsClosed = (boolean) Robot.clampStartChooser.getSelected();
				if (!Robot.hugPiston.get().equals(DoubleSolenoid.Value.kReverse) && !clampsClosed) {
					ToteGrabber.moveHugPistons(false);
				}
				if (!Robot.hugPiston.get().equals(DoubleSolenoid.Value.kForward) && clampsClosed) {
					ToteGrabber.moveHugPistons(true);
				}
				
				if (!Robot.intakePiston.get().equals(DoubleSolenoid.Value.kReverse)) {
					Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
				}
//				boolean overrideFinish = Elevator.calibration(-0.8); // TODO: magic number
				boolean overrideFinish = Elevator.moveP(0);
				
				if (overrideFinish == true) {
					Elevator.stop();
					startingAtBottom = true;
					Robot.debugWriter.println("Adjust up state\n");
					RobotMap.currentState = RobotMap.ELEVATOR_ADJUST_UP_STATE;
					// changed at request of Weissman to go down but not up when we have no totes
				}
				
				if (RobotMap.stateIndex == 0) {
					checkState = RobotMap.stateLog.length - 1;
				} else {
					checkState = RobotMap.stateIndex - 1;
				}
				if (!RobotMap.stateLog[checkState].equals("Manual Override Return State")) {
					RobotMap.stateLog[RobotMap.stateIndex] = "Manual Override Return State";
					RobotMap.stateIndex++;
					if (RobotMap.stateIndex >= RobotMap.stateLog.length) {
						RobotMap.stateIndex = 0;
					}
				}
				
				break;
		}
	}
}
