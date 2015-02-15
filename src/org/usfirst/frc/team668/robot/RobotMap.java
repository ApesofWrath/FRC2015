package org.usfirst.frc.team668.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 * This essentially is definitions for all of our constants on the robot.
 * 
 * @author The 668 FRC 2015 Programming Team
 */

/*
 * TODO: Change these values
 * THESE VALUES NEED TO BE CHANGED WHEN WE GET A ROBOT:
 * 
 * Limit switch channels
 * Buttons
 * CAN Talon PDP Ports
 * intake motor/elevator motor draws
 * current deadzone
 * confirm canIDs for all CAN objects
 * motor speeds
 * encoder PWMs
 * servo PWMs
 * piston pcmIDs
 * encoder heights
 */

public class RobotMap {
	
	/*
	 * Constants style guide:
	 * 
	 * Define all constants in capital letters with underscores between each word.
	 * End the name of the constant with a general word describing what it is, like CHANNEL.
	 * 
	 * Always define a constant as a public static final int or double
	 * public means that any class can access the constant, not just RobotMap
	 * static means that you don't have to make an instance of RobotMap to access the constant
	 * final means that it cannot be changed
	 * int means that your constant is an integer and double means that your constant is a decimal
	 */
	
	// @formatter:off
	/* 
	  ____ _____ _____ _____ _____ _____ _____ 
	| __  |  |  |_   _|_   _|     |   | |   __|
	| __ -|  |  | | |   | | |  |  | | | |__   |
	|_____|_____| |_|   |_| |_____|_|___|_____|
	
	 */
	 // @formatter:on
	
	// @formatter:off
	// Joystick Left
	public static final int MINIMIZE_DRIVE_SPEED_BUTTON			= 2;
	
	// Joystick Right
	public static final int TANK_DRIVE_BUTTON					= 6;
	public static final int ARCADE_DRIVE_BUTTON					= 7;
	
	// joystick Op // TODO: Reassign
	// NORMAL
	public static final int REVERSING_BUTTON					= 1;
	public static final int LIFT_BUTTON							= 2;
	public static final int SCORING_BUTTON						= 3;
	public static final int GROUND_BUTTON						= 4;
	public static final int COOPERTITION_BUTTON					= 5;
	public static final int TOTE_HEIGHT_BUTTON					= 6;
	public static final int INTAKE_PISTON_BUTTON				= 7;	// SWEEPER ARM
	public static final int EMPTY_OP_NORMAL_8_BUTTON			= 8;	// TODO: EMPTY
	public static final int PRETEND_BIN_DETECTED_BUTTON			= 9;
	public static final int EMPTY_OP_NORMAL_10_BUTTON			= 10;	// TODO: EMPTY
	public static final int MANUAL_OVERRIDE_BUTTON				= 11;
	public static final int ABORT_BUTTON						= 12;
	// MANUAL OVERRIDE
	public static final int RETURN_TO_STATE_MACHINE_BUTTON		= 1;
	public static final int EMPTY_OP_MANUAL_2_BUTTON			= 2;	// TODO: EMPTY
	public static final int EMPTY_OP_MANUAL_3_BUTTON			= 3;	// TODO: EMPTY
	public static final int MANUAL_INTAKE_BUTTON				= 4;
	public static final int EMPTY_OP_MANUAL_5_BUTTON			= 5;	// TODO: EMPTY
	public static final int MANUAL_OUTTAKE_BUTTON				= 6;	// at the request of Sean
//																= 7;	// already used (sweeper)
	public static final int EMPTY_OP_MANUAL_8_BUTTON			= 8;	// TODO: EMPTY
	public static final int MANUAL_PISTON_ACTIVATE_BUTTON		= 9;	// close hug pistons
	public static final int MANUAL_PISTON_DEACTIVATE_BUTTON		= 10;	// open hug pistons
	public static final int EMPTY_OP_MANUAL_11_BUTTON			= 11;	// TODO: EMPTY
	public static final int MANUAL_FUNCTION_BUTTON				= 12;
	
	// @formatter:on
	
	// @formatter:off
	/*
	                                                          
	 ____  _____ _____ _____ _____ _____    _____ ____  _____ 
	|    \|   __|  |  |     |     |   __|  |     |    \|   __|
	|  |  |   __|  |  |-   -|   --|   __|  |-   -|  |  |__   |
	|____/|_____|\___/|_____|_____|_____|  |_____|____/|_____|
 
	 */
	// @formatter:on
	
	// joystick ports
	public static final int JOYSTICK_LEFT_PORT = 0;
	public static final int JOYSTICK_RIGHT_PORT = 1;
	public static final int JOYSTICK_OP_PORT = 2;
	
	// @formatter:off
	// canIDs
	public static final int DRIVE_MOTOR_FRONT_LEFT_CANID	= 1;
	public static final int DRIVE_MOTOR_FRONT_RIGHT_CANID	= 2;
	public static final int DRIVE_MOTOR_REAR_LEFT_CANID		= 3;
	public static final int DRIVE_MOTOR_REAR_RIGHT_CANID	= 4;
	public static final int INTAKE_MOTOR_LEFT_CANID			= 5;
	public static final int INTAKE_MOTOR_RIGHT_CANID		= 6;
	public static final int ELEVATOR_MOTOR_CANID			= 7;
	public static final int PCM_CANID						= 10;
	public static final int PDP_CANID						= 20;
	// @formatter:on
	
	// pdp ports for current finding; TODO: THESE MUST BE CHANGED
	public static final int CAN_TALON_ELEVATOR_PDP_PORT = 9;
	public static final int CAN_TALON_INTAKE_RIGHT_PDP_PORT = 10;
	public static final int CAN_TALON_INTAKE_LEFT_PDP_PORT = 11;
	public static final int CAN_TALON_DRIVE_RIGHT_FRONT_PDP_PORT = 12;
	public static final int CAN_TALON_DRIVE_LEFT_FRONT_PDP_PORT = 13;
	public static final int CAN_TALON_DRIVE_RIGHT_BACK_PDP_PORT = 14;
	public static final int CAN_TALON_DRIVE_LEFT_BACK_PDP_PORT = 15;
	
	// encoder PWMs
	public static final int DRIVE_ENCODER_LEFT_A = 8;
	public static final int DRIVE_ENCODER_LEFT_B = 9;
	public static final int DRIVE_ENCODER_RIGHT_A = 0;
	public static final int DRIVE_ENCODER_RIGHT_B = 1;
	public static final int ELEVATOR_ENCODER_A = 2;
	public static final int ELEVATOR_ENCODER_B = 3;
	public static final int CORRECTION_INPUT = 6; // TODO Optical
	public static final int LIMIT_INPUT = 7;
	// piston pcmIDs
	public static final int DOUBLE_SOLENOID_HUG_PCMID_RETRACTION = 0; // TODO: Check & maybe flip
	public static final int DOUBLE_SOLENOID_HUG_PCMID_EXPANSION = 1;
	public static final int DOUBLE_SOLENOID_INTAKE_PCMID_RETRACTION = 2;
	public static final int DOUBLE_SOLENOID_INTAKE_PCMID_EXPANSION = 3;
	
	// pwms for the optical sensors used in the intake
	public static final int BIN_OPTIC_DIO = 1;
	public static final int TOTE_OPTIC_DIO = 7;
	
	// limit switch channels
	public static final int ELEVATOR_LIMIT_TOP_CHANNEL = 5;
	public static final int ELEVATOR_LIMIT_BOTTOM_CHANNEL = 4;
	
	// @formatter:off
	
	/*
	                                                                                                             
	 _____ _____ _____ _____ _____    _____ _____ _____ _____ _____    _____ _____ _____ _____ _____ _____ _____ 
	|  _  |  |  |_   _|     |   | |  |   __|_   _|  _  |_   _|   __|  |     |  _  |     |  |  |     |   | |   __|
	|     |  |  | | | |  |  | | | |  |__   | | | |     | | | |   __|  | | | |     |   --|     |-   -| | | |   __|
	|__|__|_____| |_| |_____|_|___|  |_____| |_| |__|__| |_| |_____|  |_|_|_|__|__|_____|__|__|_____|_|___|_____|
	                                                                                                             
	 */
	
	// @formatter:on
	
	// autonomous chooser possibilities
	public static int autonomousState;
	public static final int STOP_AUTONOMOUS = 0;
	public static final int DRIVE_FORWARD_AUTONOMOUS = 1;
	public static final int DELAY_AND_DRIVE_FORWARD_AUTONOMOUS = 2;
	public static final int TOTE_GRAB_AUTONOMOUS = 3;
	public static final int BIN_GRAB_AUTONOMOUS = 4;
	public static final int TOTE_STACK_AUTONOMOUS = 5;
	
	// auto state machine
	public static final int DRIVE_FOWARD = 0;
	public static final int AUTO_INTAKE = 1;
	public static final int TURN_AROUND = 2;
	public static final int DRIVE_THE_OTHER_WAY = 3;
	public static final int EJECT_BIN = 4;
	public static final int CELEBRATE_SUCCESS = 5;  // spin in circles like Atlas celebrating success
													// recursion =)
	
	// @formatter:off
	
	/*
	 _____ _____ __    _____ _____ _____    _____ _____ _____ _____ _____    _____ _____ _____ _____ _____ _____ _____ 
	|_   _|   __|  |  |   __|     |  _  |  |   __|_   _|  _  |_   _|   __|  |     |  _  |     |  |  |     |   | |   __|
	  | | |   __|  |__|   __|  |  |   __|  |__   | | | |     | | | |   __|  | | | |     |   --|     |-   -| | | |   __|
	  |_| |_____|_____|_____|_____|__|     |_____| |_| |__|__| |_| |_____|  |_|_|_|__|__|_____|__|__|_____|_|___|_____|
                                                                                                                   
	 */
	
	// @formatter:on
	
	// Teleop State Machine Declarations
	public static final int INIT_STATE = 0;
	public static final int ELEVATOR_HEIGHT_TOTE_STATE = 1;
	public static final int WAIT_FOR_BUTTON_STATE = 2;
	public static final int WAIT_FOR_GAME_PIECE_STATE = 3;
	public static final int ELEVATOR_ADJUST_STATE = 13; // happens before we open hug pistons to adjust elevator
	public static final int OPEN_HUG_PISTONS_STATE = 4;
	public static final int DRIVE_BACKWARDS_STATE = 1000;
	public static final int ELEVATOR_DOWN_STATE = 5;
	public static final int DRIVE_FORWARDS_STATE = 1001;
	public static final int CLOSE_HUG_PISTONS_STATE = 6;
	public static final int ELEVATOR_HEIGHT_GROUND_STATE = 7;
	public static final int ELEVATOR_HEIGHT_SCORING_STATE = 8;
	public static final int ELEVATOR_HEIGHT_COOPERTITION_STATE = 9;
	public static final int REVERSE_INTAKE_MOTORS_STATE = 10;
	public static final int MANUAL_OVERRIDE_STATE = 11;
	public static final int WAITING_FOR_REVERSE_INTAKE_STATE = 12;
	public static final int ELEVATOR_ADJUST_UP_STATE = 14; // happens after init to keep elevator resting at encoder value slightly higher than limit switch
	public static final int TIME_DELAY_AFTER_TOTE_SENSE_STATE = 15;
	public static final int MANUAL_OVERRIDE_RETURN_STATE = 16;
	
	public static final int DEFAULT_STATE = INIT_STATE; // this is the starting state; change to manual override if testing
	
	// State machine values
	public static final int DRIVE_BACKWARDS_DISTANCE = 5;
	public static final int DRIVE_FORWARDS_DISTANCE = 5;
	
	// @formatter:off
	
	/*
	 _____ _____ _____ _____ _____ _____ __       _____ _____ __    _____ _____ _____ 
	|   __|  _  |   __|     |     |  _  |  |     |  |  |  _  |  |  |  |  |   __|   __|
	|__   |   __|   __|   --|-   -|     |  |__   |  |  |     |  |__|  |  |   __|__   |
	|_____|__|  |_____|_____|_____|__|__|_____|   \___/|__|__|_____|_____|_____|_____|
	
	 */
	
	// @formatter:on
	
	// CURRENT STUFF: {
	// DEFAULT VALUES of empty and full draws on the intake/elevator motors
	// these should not be final
	public static double intakeMotorEmptyDraw = 0.0; // This needs the definitions!
	public static double intakeMotorFullDraw = 0.0; // This needs the definitions!
	public static double elevatorMotorEmptyDraw = 0.0; // This needs the definitions!
	// these should be set in code but it's good to have a default
	
	// deadzones for current finding
	public static final double CURRENT_DEAD_ZONE = 0.0; // This needs some help.
	
	public static double ELEVATOR_CURRENT_STOP = 0;
	// }
	
	// constants for autonomous
	public static final double AUTONOMOUS_SPEED = 0.5;
	public static final double AUTONOMOUS_CURVE = 0;
	public static final long DELAY_TIME = 5000; // Timer for delayed autonomous function
	public static final int STOP = 10; // This is how far the robot should go in FORWARD_AUTONOMOUS
	
	// non-final values change throughout code
	public static int currentState = DEFAULT_STATE;
	public static int itemCount = 0; // Bins count as totes. Duh.
	public static int autonomousMode = STOP_AUTONOMOUS; // default is stop autonomous for now
	
	// motor speeds
	public static final double INTAKE_MOTOR_SPEED = 0.65;
	public static final double OUTTAKE_MOTOR_SPEED = -0.4;
	public static double elevatorMotorSpeed = -1.0; // This is not final (SmartDashboard radio buttons)
	
	// encoder heights
	public static final double ELEVATOR_ENCODER_ONE_TOTE_HEIGHT = 400;
	public static final double ELEVATOR_ENCODER_SCORING = 115;
	public static final double ELEVATOR_ENCODER_COOPERTITION = 250;
	public static final double ELEVATOR_ENCODER_GROUND = 15;
	public static final double ELEVATOR_ENCODER_PICKUP = 10; // pickup tote height
	public static final double ELEVATOR_ENCODER_MAX_HEIGHT = Double.MAX_VALUE; // TODO: LO TODO
	
	public static final double MINIMIZING_FACTOR = 0.8;
	
	// debugging constant for test robot
	public static boolean isTestRobot = false;
	public static boolean cameraConnected = false;
}
