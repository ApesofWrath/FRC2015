package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * 
 * Control-click the "see" commands to automatically jump to methods.
 * 
 * @see Robot#robotInit()
 * @see Robot#autonomousPeriodic()
 * @see Robot#autonomousInit()
 * @see Robot#teleopInit()
 * @see Robot#teleopPeriodic()
 * @see Robot#testPeriodic()
 * 
 * @see TeleopStateMachine#stateMachine(boolean, boolean, boolean, boolean, boolean, boolean)
 *
 * @see ToteGrabber#moveHugPistons(boolean)
 * 
 * @see Intake#spin(double)
 * @see Intake#stop()
 * 
 * @see Elevator#calibration(double)
 * @see Elevator#stop()
 * @see Elevator#move(double, double)
 * 
 * @author The 668 FRC 2015 Programming Team 
 * 
 */



public class Robot extends IterativeRobot {

	public static OI operatorInterface;
	public static Joystick joystickLeft, joystickRight, joystickOp;
	public static CANTalon canTalonFrontLeft, canTalonFrontRight,
			canTalonRearLeft, canTalonRearRight, canTalonIntakeLeft,
			canTalonIntakeRight, canTalonElevator;
	public static Encoder encoderLeft, encoderRight, encoderElevator;
	public static DigitalInput limitTop, limitBottom, toteOptic, binOptic;
	public static Servo camServoVert, camServoHor;
	public static AxisCamera camera;
	public static Compressor compressor1;
	public static DoubleSolenoid leftHugPiston, rightHugPiston, intakePiston;
	public static RobotDrive robotDrive;
	public static PowerDistributionPanel pdp;

	boolean isTankDrive = true;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		// Object Initialization
		operatorInterface = new OI();

		toteOptic = new DigitalInput(0);
		binOptic = new DigitalInput(1);

		joystickLeft = new Joystick(RobotMap.JOYSTICK_LEFT_PORT);
		joystickRight = new Joystick(RobotMap.JOYSTICK_RIGHT_PORT);
		joystickOp = new Joystick(RobotMap.JOYSTICK_OP_PORT);

		canTalonFrontLeft = new CANTalon(RobotMap.DRIVE_MOTOR_FRONT_LEFT_CANID);
		canTalonFrontRight = new CANTalon(
				RobotMap.DRIVE_MOTOR_FRONT_RIGHT_CANID);
		canTalonRearLeft = new CANTalon(RobotMap.DRIVE_MOTOR_REAR_LEFT_CANID);
		canTalonRearRight = new CANTalon(RobotMap.DRIVE_MOTOR_REAR_RIGHT_CANID);

		canTalonIntakeLeft = new CANTalon(RobotMap.INTAKE_MOTOR_LEFT_CANID);
		canTalonIntakeRight = new CANTalon(RobotMap.INTAKE_MOTOR_RIGHT_CANID);

		canTalonElevator = new CANTalon(RobotMap.ELEVATOR_MOTOR_CANID);

		encoderLeft = new Encoder(RobotMap.DRIVE_ENCODER_LEFT_A,
				RobotMap.DRIVE_ENCODER_LEFT_B);
		encoderRight = new Encoder(RobotMap.DRIVE_ENCODER_RIGHT_A,
				RobotMap.DRIVE_ENCODER_RIGHT_B);

		encoderElevator = new Encoder(RobotMap.ELEVATOR_ENCODER_A,
				RobotMap.ELEVATOR_ENCODER_B);

		limitTop = new DigitalInput(RobotMap.ELEVATOR_LIMIT_TOP_CHANNEL);
		limitBottom = new DigitalInput(RobotMap.ELEVATOR_LIMIT_BOTTOM_CHANNEL);

		camServoHor = new Servo(RobotMap.CAMERA_SERVO_HORIZONTAL_PWM);
		camServoVert = new Servo(RobotMap.CAMERA_SERVO_VERTICAL_PWM);

		// camera = new AxisCamera();

		compressor1 = new Compressor(RobotMap.PCM_CANID);
		compressor1.setClosedLoopControl(false); //this needs to be changed

		leftHugPiston = new DoubleSolenoid(RobotMap.PCM_CANID,
				RobotMap.DOUBLE_SOLENOID_LEFT_HUG_PCMID_EXPANSION,
				RobotMap.DOUBLE_SOLENOID_LEFT_HUG_PCMID_RETRACTION);
		rightHugPiston = new DoubleSolenoid(RobotMap.PCM_CANID,
				RobotMap.DOUBLE_SOLENOID_RIGHT_HUG_PCMID_EXPANSION,
				RobotMap.DOUBLE_SOLENOID_RIGHT_HUG_PCMID_RETRACTION);
		intakePiston = new DoubleSolenoid(RobotMap.PCM_CANID,
				RobotMap.DOUBLE_SOLENOID_INTAKE_PCMID_EXPANSION,
				RobotMap.DOUBLE_SOLENOID_INTAKE_PCMID_RETRACTION);

		robotDrive = new RobotDrive(canTalonFrontLeft, canTalonRearLeft,
				canTalonFrontRight, canTalonRearRight);

		pdp = new PowerDistributionPanel();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {

	}

	/**
	 * This function is called once at the start of teleop
	 */
	public void teleopInit() {
		System.out.println("Version: \"Complete\" Teleop Code");
		camServoHor.set(0);
		camServoVert.set(0);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {

		// drive switch
		if (joystickRight.getRawButton(RobotMap.TANK_DRIVE_BUTTON)) {
			isTankDrive = true;
		}
		if (joystickRight.getRawButton(RobotMap.ARCADE_DRIVE_BUTTON)) {
			isTankDrive = false;
		}
		if (isTankDrive) {
			robotDrive.tankDrive(joystickLeft, joystickRight);
		}
		if (isTankDrive == false) {
			robotDrive.arcadeDrive(joystickRight);
		}

		// state machine
		boolean isManual = joystickOp
				.getRawButton(RobotMap.MANUAL_OVERRIDE_BUTTON);
		boolean isCoopertition = joystickOp
				.getRawButton(RobotMap.COOPERTITION_BUTTON);
		boolean isGround = joystickOp.getRawButton(RobotMap.GROUND_BUTTON);
		boolean isScoring = joystickOp.getRawButton(RobotMap.SCORING_BUTTON);
		boolean isLift = joystickOp.getRawButton(RobotMap.LIFT_BUTTON);
		boolean isReversing = joystickOp
				.getRawButton(RobotMap.REVERSING_BUTTON);

		TeleopStateMachine.stateMachine(isCoopertition, isScoring, isGround,
				isLift, isManual, isReversing);

		// manual control
		if (RobotMap.currentState == RobotMap.MANUAL_OVERRIDE_STATE) {
			boolean isPistonOn = joystickOp.getRawButton(RobotMap.MANUAL_PISTON_ACTIVATE_BUTTON);
			boolean isPistonOff = joystickOp.getRawButton(RobotMap.MANUAL_PISTON_DEACTIVATE_BUTTON);
			boolean isForwardIntake = joystickOp.getRawButton(RobotMap.MANUAL_INTAKE_BUTTON);
			boolean isBackwardsIntake = joystickOp.getRawButton(RobotMap.MANUAL_OUTTAKE_BUTTON);
			boolean isFunction = joystickOp.getRawButton(RobotMap.MANUAL_FUNCTION_BUTTON);

			if (isPistonOn) {
				leftHugPiston.set(DoubleSolenoid.Value.kForward);
				rightHugPiston.set(DoubleSolenoid.Value.kForward);
			} else if (isPistonOff) {
				leftHugPiston.set(DoubleSolenoid.Value.kReverse);
				rightHugPiston.set(DoubleSolenoid.Value.kReverse);
			}

			if (isForwardIntake) {
				Intake.spin(0.5);
			} else if (isBackwardsIntake) {
				Intake.spin(-0.5);
			} else {
				Intake.stop();
			}

			if (isFunction) {
				Elevator.calibration(joystickOp.getY()); // moves Elevator
				Elevator.stop();
			}
		} // end if (RobotMap.currentState == RobotMap.MANUAL_OVERRIDE_STATE)
	} // end function teleopPeriodic

	/**
	 * This function is called periodically during test mode. It contains test
	 * code for all the motors and pistons to be controlled individually.
	 */
	public void testPeriodic() {
		// motor testing code
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
		if (joystickRight.getRawButton(3)) {
			canTalonRearLeft.set(joystickRight.getY() * -1);
		} else {
			canTalonRearLeft.set(0);
		}
		if (joystickRight.getRawButton(4)) {
			canTalonRearRight.set(joystickRight.getY() * -1);
		} else {
			canTalonRearRight.set(0);
		}
		if (joystickRight.getRawButton(5)) {
			canTalonIntakeLeft.set(joystickRight.getY() * -1);
		} else {
			canTalonIntakeLeft.set(0);
		}
		if (joystickRight.getRawButton(6)) {
			canTalonIntakeRight.set(joystickRight.getY() * -1);
		} else {
			canTalonIntakeRight.set(0);
		}
		if (joystickRight.getRawButton(7)) {
			canTalonElevator.set(joystickRight.getY() * -1);
		} else {
			canTalonElevator.set(0);
		}

		// piston testing code
		if (joystickOp.getRawButton(9)) {
			leftHugPiston.set(DoubleSolenoid.Value.kForward);
			rightHugPiston.set(DoubleSolenoid.Value.kForward);
		} else if (joystickOp.getRawButton(10)) {
			leftHugPiston.set(DoubleSolenoid.Value.kReverse);
			rightHugPiston.set(DoubleSolenoid.Value.kReverse);
		}
		if (joystickOp.getRawButton(11)) {
			intakePiston.set(DoubleSolenoid.Value.kForward);
		} else if (joystickOp.getRawButton(12)) {
			intakePiston.set(DoubleSolenoid.Value.kReverse);
		}
		
		// @formatter:off
		//print encoders and currents. we are not formatting this to keep everything on one line
		System.out.println("Left Intake:" + Robot.pdp.getCurrent(RobotMap.CAN_TALON_INTAKE_LEFT_PDP_PORT));
		System.out.println("Right Intake:" + Robot.pdp.getCurrent(RobotMap.CAN_TALON_INTAKE_RIGHT_PDP_PORT));
		System.out.println("");
		System.out.println("Left Back Motor:" + Robot.pdp.getCurrent(RobotMap.CAN_TALON_DRIVE_LEFT_BACK_PDP_PORT));
		System.out.println("Left Front Motor:" + Robot.pdp.getCurrent(RobotMap.CAN_TALON_DRIVE_LEFT_FRONT_PDP_PORT));
		System.out.println("");
		System.out.println("Right Back Motor:" + Robot.pdp.getCurrent(RobotMap.CAN_TALON_DRIVE_RIGHT_BACK_PDP_PORT));
		System.out.println("Right Front Motor:" + Robot.pdp.getCurrent(RobotMap.CAN_TALON_DRIVE_RIGHT_FRONT_PDP_PORT));
		System.out.println("");
		System.out.println("Elevator Motor:" + Robot.pdp.getCurrent(RobotMap.CAN_TALON_ELEVATOR_PDP_PORT));
		System.out.println("");
		// @formatter:on
	}

}
