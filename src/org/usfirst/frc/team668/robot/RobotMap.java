package org.usfirst.frc.team668.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	
	public static final int JOYSTICK_LEFT_PORT = 0;
	public static final int JOYSTICK_RIGHT_PORT = 1;
	public static final int JOYSTICK_OP_PORT = 2;
	
	public static final int TANK_DRIVE_BUTTON = 11;
	public static final int ARCADE_DRIVE_BUTTON = 10;
	
	public static final int DRIVE_MOTOR_FRONT_LEFT_CANID = 0;
	public static final int DRIVE_MOTOR_FRONT_RIGHT_CANID = 1;
	public static final int DRIVE_MOTOR_REAR_LEFT_CANID = 2;
	public static final int DRIVE_MOTOR_REAR_RIGHT_CANID = 3;
	
	public static final int INTAKE_MOTOR_LEFT_CANID = 4;
	public static final int INTAKE_MOTOR_RIGHT_CANID = 5;
	public static final int ELEVATOR_MOTOR_CANID = 6;

	
	public static final int DRIVE_ENCODER_LEFT = 0;
	public static final int DRIVE_ENCODER_LEFT2 = 1;
	public static final int DRIVE_ENCODER_RIGHT = 2;
	public static final int DRIVE_ENCODER_RIGHT2 = 3;
	
	public static final int CAMERA_SERVO_VERTICAL_PWM = 4;
	public static final int CAMERA_SERVO_HORIZONTAL_PWM = 5;
	
	public static final int DOUBLE_SOLENOID_LEFT_HUG_PCMID_RETRACTION = 0;
	public static final int DOUBLE_SOLENOID_LEFT_HUG_PCMID_EXPANSION = 1;
	public static final int DOUBLE_SOLENOID_RIGHT_HUG_PCMID_RETRACTION = 2;
	public static final int DOUBLE_SOLENOID_RIGHT_HUG_PCMID_EXPANSION = 3;
	public static final int DOUBLE_SOLENOID_INTAKE_PCMID_RETRACTION = 4;
	public static final int DOUBLE_SOLENOID_INTAKE_PCMID_EXPANSION = 5;
	
	public static final int PCM_CANID = 10;
	public static final int PDP_CANID = 20;

	public static final int INTAKE_BUTTON_ON = 1; // These need values THEY ARE JUST TEMPORARY
	public static final int INTAKE_BUTTON_OFF = 2;
	
	
}
