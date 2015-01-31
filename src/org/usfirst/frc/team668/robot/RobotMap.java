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

/* THESE VALUES NEED TO BE CHANGED WHEN WE GET A ROBOT:
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

	/* Constants style guide:
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
	// intake pistons control buttons
	public static final int INTAKE_PISTON_ACTIVATE_BUTTON = 10;
	public static final int INTAKE_PISTON_DEACTIVATE_BUTTON = 11;
	
	//limit switch channels
	public static final int ELEVATOR_LIMIT_TOP_CHANNEL = 1;
	public static final int ELEVATOR_LIMIT_BOTTOM_CHANNEL = 2;
	
	//joystick ports
	public static final int JOYSTICK_LEFT_PORT = 0;
	public static final int JOYSTICK_RIGHT_PORT = 1;
	public static final int JOYSTICK_OP_PORT = 2;
	
	//Joystick Left
	public static final int TANK_DRIVE_BUTTON = 6;
	public static final int ARCADE_DRIVE_BUTTON = 7;
	
	//joystick Op
	//buttons need reassignment; we are using 11 out of 12 on the operator joystick
	public static final int MANUAL_OVERRIDE_BUTTON = 11;
	public static final int ABORT_BUTTON = 6;
	public static final int COOPERTITION_BUTTON = 5;
	public static final int GROUND_BUTTON = 4;
	public static final int SCORING_BUTTON = 3;
	public static final int LIFT_BUTTON = 2;
	public static final int REVERSING_BUTTON = 1;
	
	public static final int MANUAL_PISTON_ACTIVATE_BUTTON = 9;
	public static final int MANUAL_PISTON_DEACTIVATE_BUTTON = 10;
	public static final int MANUAL_INTAKE_BUTTON = 7;
	public static final int MANUAL_OUTTAKE_BUTTON = 8;
	public static final int MANUAL_FUNCTION_BUTTON = 12;
	
	//pdp ports for current finding; THESE MUST BE CHANGED
	public static final int CAN_TALON_DRIVE_LEFT_BACK_PDP_PORT = 15;
	public static final int CAN_TALON_DRIVE_RIGHT_BACK_PDP_PORT = 14;
	public static final int CAN_TALON_DRIVE_LEFT_FRONT_PDP_PORT = 13;
	public static final int CAN_TALON_DRIVE_RIGHT_FRONT_PDP_PORT = 12;
	public static final int CAN_TALON_INTAKE_LEFT_PDP_PORT = 11;
	public static final int CAN_TALON_INTAKE_RIGHT_PDP_PORT = 10;
	public static final int CAN_TALON_ELEVATOR_PDP_PORT = 9;
	
	//DEFAULT VALUES of empty and full draws on the intake/elevator motors
	//these should not be final
	public static double intakeMotorEmptyDraw = 0.0; // This needs the definitions!
	public static double intakeMotorFullDraw = 0.0; // This needs the definitions!
	public static double elevatorMotorEmptyDraw = 0.0; // This needs the definitions!
	//these should be set in code but it's good to have a default
	
	//deadzones for current finding
	public static final double CURRENT_DEAD_ZONE = 0.0; //This needs some help.
	
	//motor canIDs
	public static final int DRIVE_MOTOR_FRONT_LEFT_CANID = 0;
	public static final int DRIVE_MOTOR_FRONT_RIGHT_CANID = 1;
	public static final int DRIVE_MOTOR_REAR_LEFT_CANID = 2;
	public static final int DRIVE_MOTOR_REAR_RIGHT_CANID = 3;
	
	public static final int INTAKE_MOTOR_LEFT_CANID = 4;
	public static final int INTAKE_MOTOR_RIGHT_CANID = 5;
	public static final int ELEVATOR_MOTOR_CANID = 6;

	//motor speeds
	public static final double INTAKE_MOTOR_SPEED = 0.0;
	public static final double ELEVATOR_MOTOR_SPEED = 0.5;

	//encoder PWMs
	public static final int DRIVE_ENCODER_LEFT_A = 0;
	public static final int DRIVE_ENCODER_LEFT_B = 1;
	public static final int DRIVE_ENCODER_RIGHT_A = 2;
	public static final int DRIVE_ENCODER_RIGHT_B = 3;
	
	public static final int ELEVATOR_ENCODER_A = 0;
	public static final int ELEVATOR_ENCODER_B = 1;
	
	//servo PWMs
	public static final int CAMERA_SERVO_VERTICAL_PWM = 4;
	public static final int CAMERA_SERVO_HORIZONTAL_PWM = 5;
	
	//piston pcmIDs
	public static final int DOUBLE_SOLENOID_LEFT_HUG_PCMID_RETRACTION = 0;
	public static final int DOUBLE_SOLENOID_LEFT_HUG_PCMID_EXPANSION = 1;
	public static final int DOUBLE_SOLENOID_RIGHT_HUG_PCMID_RETRACTION = 2;
	public static final int DOUBLE_SOLENOID_RIGHT_HUG_PCMID_EXPANSION = 3;
	public static final int DOUBLE_SOLENOID_INTAKE_PCMID_RETRACTION = 4;
	public static final int DOUBLE_SOLENOID_INTAKE_PCMID_EXPANSION = 5;
	
	//miscellaneous canIDs
	public static final int PCM_CANID = 10;
	public static final int PDP_CANID = 20;

	//encoder heights
	public static final double ELEVATOR_ENCODER_ONE_TOTE_HEIGHT = 0;
	public static final double ELEVATOR_ENCODER_SCORING = 0;
	public static final double ELEVATOR_ENCODER_COOPERTITION = 0;
	public static final double ELEVATOR_ENCODER_GROUND = 0;
		
	//Teleop State Machine Declarations
	public static final int INIT_STATE = 0;
	public static final int ELEVATOR_HEIGHT_TOTE_STATE = 1;
	public static final int WAIT_FOR_BUTTON_STATE = 2;
	public static final int WAIT_FOR_GAME_PIECE_STATE = 3;
	public static final int OPEN_HUG_PISTONS_STATE = 4;
	public static final int ELEVATOR_DOWN_STATE = 5;
	public static final int CLOSE_HUG_PISTONS_STATE = 6;
	public static final int ELEVATOR_HEIGHT_GROUND_STATE = 7;
	public static final int ELEVATOR_HEIGHT_SCORING_STATE = 8;
	public static final int ELEVATOR_HEIGHT_COOPERTITION_STATE = 9;
	public static final int REVERSE_INTAKE_MOTORS_STATE = 10;
	public static final int MANUAL_OVERRIDE_STATE = 11;
	public static final int WAITING_FOR_REVERSE_INTAKE =12;
	
	//non-final values change throughout code
	public static int currentState = INIT_STATE; 
	public static int itemCount = 0; // Bins count as totes. Duh.
	
	//pwms for the optical sensors used in the intake 
	public static final int TOTE_OPTIC_DIO = 0;
	public static final int BIN_OPTIC_DIO = 1;
	
}
