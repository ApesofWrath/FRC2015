
package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.vision.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	public static OI operatorInterface;
	public static Joystick joystickLeft, joystickRight, joystickOp;
	public static CANTalon canTalonFrontLeft, canTalonFrontRight, canTalonRearLeft,
	canTalonRearRight, canTalonIntakeLeft, canTalonIntakeRight,
	canTalonElevator;
	public static Encoder encoderLeft;
	public static Encoder encoderRight;
	public static Servo camServoVert, camServoHor;
	public static AxisCamera camera;
	public static Compressor compressor1;
	public static DoubleSolenoid leftHugPiston, rightHugPiston, intakePiston;
	public static RobotDrive robotDrive;
	
	boolean isTankDrive = true;
	
	
    public void robotInit() {
    	operatorInterface = new OI();
    	
    	joystickLeft = new Joystick(RobotMap.JOYSTICK_LEFT_PORT);
		joystickRight = new Joystick(RobotMap.JOYSTICK_RIGHT_PORT);
		joystickOp = new Joystick(RobotMap.JOYSTICK_OP_PORT);

		canTalonFrontLeft = new CANTalon(RobotMap.DRIVE_MOTOR_FRONT_LEFT_CANID);
		canTalonFrontRight = new CANTalon(RobotMap.DRIVE_MOTOR_FRONT_RIGHT_CANID);
		canTalonRearLeft = new CANTalon(RobotMap.DRIVE_MOTOR_REAR_LEFT_CANID);
		canTalonRearRight = new CANTalon(RobotMap.DRIVE_MOTOR_REAR_RIGHT_CANID);

		canTalonIntakeLeft = new CANTalon(RobotMap.INTAKE_MOTOR_LEFT_CANID);
		canTalonIntakeRight = new CANTalon(RobotMap.INTAKE_MOTOR_RIGHT_CANID);

		canTalonElevator = new CANTalon(RobotMap.ELEVATOR_MOTOR_CANID);
		
		
		encoderLeft = new  Encoder(RobotMap.DRIVE_ENCODER_LEFT, RobotMap.DRIVE_ENCODER_LEFT2);
		encoderRight = new Encoder(RobotMap.DRIVE_ENCODER_RIGHT, RobotMap.DRIVE_ENCODER_RIGHT2);

		camServoHor = new Servo(RobotMap.CAMERA_SERVO_HORIZONTAL_PWM);
		camServoVert = new Servo(RobotMap.CAMERA_SERVO_VERTICAL_PWM);
		
		//camera = new AxisCamera();
		
		compressor1 = new Compressor(RobotMap.PCM_CANID);
		compressor1.setClosedLoopControl(false);
		
		leftHugPiston = new DoubleSolenoid(RobotMap.PCM_CANID, RobotMap.DOUBLE_SOLENOID_LEFT_HUG_PCMID_EXPANSION,RobotMap.DOUBLE_SOLENOID_LEFT_HUG_PCMID_RETRACTION);
		rightHugPiston = new DoubleSolenoid(RobotMap.PCM_CANID, RobotMap.DOUBLE_SOLENOID_RIGHT_HUG_PCMID_EXPANSION, RobotMap.DOUBLE_SOLENOID_RIGHT_HUG_PCMID_RETRACTION);
		intakePiston = new DoubleSolenoid(RobotMap.PCM_CANID, RobotMap.DOUBLE_SOLENOID_INTAKE_PCMID_EXPANSION, RobotMap.DOUBLE_SOLENOID_INTAKE_PCMID_RETRACTION);
		
		robotDrive = new RobotDrive(canTalonFrontLeft, canTalonRearLeft, canTalonFrontRight, canTalonRearRight); 
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }
    
    public void teleopInit() {
    	System.out.println("version: the one with the buttons, compressor should be off, servos at zero");
    	camServoHor.set(0);
    	camServoVert.set(0);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	if (joystickRight.getRawButton(RobotMap.TANK_DRIVE_BUTTON)){
    		isTankDrive = true;
    	}
    	if(joystickRight.getRawButton(RobotMap.ARCADE_DRIVE_BUTTON)){
    		isTankDrive = false;
    	}
    	if (isTankDrive){
    	robotDrive.tankDrive(joystickLeft, joystickRight);
    	}
    	if (isTankDrive == false){
    	robotDrive.arcadeDrive(joystickRight); //ARCADE DRIVE IS THE RIGHT STICK!
    	}
    	
    	
    }

    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	if (joystickRight.getRawButton(1)) {
			canTalonFrontLeft.set(joystickRight.getY() * -1);

		} else {
			canTalonFrontLeft.set(0);
		}
		if (joystickRight.getRawButton(2)) {
			canTalonFrontRight.set(joystickRight.getY() * -1);

		} else {
			canTalonFrontRight.set(0);
		}
//		if (joystickRight.getRawButton(3)) {
//			canTalonRearLeft.set(joystickRight.getY() * -1);
//
//		} else {
//			canTalonRearLeft.set(0);
//		}
//		if (joystickRight.getRawButton(4)) {
//			canTalonRearRight.set(joystickRight.getY() * -1);
//
//		} else {
//			canTalonRearRight.set(0);
//		}
//
//		if (joystickRight.getRawButton(5)) {
//			canTalonIntakeLeft.set(joystickRight.getY() * -1);
//
//		} else {
//			canTalonIntakeLeft.set(0);
//		}
//		if (joystickRight.getRawButton(6)) {
//			canTalonIntakeRight.set(joystickRight.getY() * -1);
//
//		} else {
//			canTalonIntakeRight.set(0);
//		}
//
//		if (joystickRight.getRawButton(7)) {
//			canTalonElevator.set(joystickRight.getY() * -1);
//
//		} else {
//			canTalonElevator.set(0);
//		}
//		if (joystickRight.getRawButton(8)) {
//			canTalonElevator2.set(joystickRight.getY() * -1);
//
//		} else {
//			canTalonElevator2.set(0);
//		}
		if (joystickOp.getRawButton(7)){
			leftHugPiston.set(DoubleSolenoid.Value.kForward);
		}
		else if (joystickOp.getRawButton(8)){
			leftHugPiston.set(DoubleSolenoid.Value.kReverse);
		}
		if (joystickOp.getRawButton(9)){
			rightHugPiston.set(DoubleSolenoid.Value.kForward);
		}
		else if (joystickOp.getRawButton(10)){
			rightHugPiston.set(DoubleSolenoid.Value.kReverse);
		}
		if (joystickOp.getRawButton(11)){
			intakePiston.set(DoubleSolenoid.Value.kForward);
		}
		else if (joystickOp.getRawButton(12)){
			intakePiston.set(DoubleSolenoid.Value.kReverse);
		}
    }
    
}
