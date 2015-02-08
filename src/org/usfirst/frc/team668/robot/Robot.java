package org.usfirst.frc.team668.robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.Scanner;

import camera2015.USBCamera;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.RGBValue;
import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

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
 * @see Robot#disabledInit()
 * @see Robot#testInit()
 * 
 * @see TeleopStateMachine#stateMachine(boolean, boolean, boolean, boolean,
 *      boolean, boolean, boolean)
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
 * @see Autonomous#doAuton()
 * 
 * @author The 668 FRC 2015 Programming Team
 * 
 */

public class Robot extends IterativeRobot {

	// start robot variable declarations
	public static OI operatorInterface;
	public static Joystick joystickLeft, joystickRight, joystickOp;
	public static CANTalon canTalonFrontLeft, canTalonFrontRight,
			canTalonRearLeft, canTalonRearRight, canTalonIntakeLeft,
			canTalonIntakeRight, canTalonElevator;
	public static Encoder encoderLeft, encoderRight, encoderElevator;
	public static DigitalInput limitTop, limitBottom, toteOptic, binOptic;
	public static Servo camServoVert, camServoHor;
	public static Compressor compressor1;
	public static DoubleSolenoid leftHugPiston, rightHugPiston, intakePiston;
	public static RobotDrive robotDrive;
	public static PowerDistributionPanel pdp;
	public static PrintWriter debugWriter, continuousVarsWriter; // this for
																	// debug
																	// files
																	// saved to
																	// the
																	// flashdrive
	public static Scanner continuousVarsReader;

	// camera variables
	USBCamera camera = new USBCamera();

	public static SendableChooser autonomousChooser, elevatorChooser; // for
																		// autonomous
																		// selection
																		// and
																		// elevator
																		// speed
																		// choosing
																		// We
																		// added
																		// elevator
																		// speed
																		// selection
																		// radio
																		// buttons.
																		// This
																		// doesn't
																		// work
																		// yet.

	boolean buttonEightPressed = false; // for test to check if button 8 is
										// pressed
	boolean picture_taking = false;
	boolean picture_writing = false;
	long cameraTimer = 0;
	boolean isTankDrive = true;
	boolean buttonOnePressed = false;;

	// end declarations

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		// Object Initialization
		operatorInterface = new OI();

		joystickLeft = new Joystick(RobotMap.JOYSTICK_LEFT_PORT);
		joystickRight = new Joystick(RobotMap.JOYSTICK_RIGHT_PORT);
		joystickOp = new Joystick(RobotMap.JOYSTICK_OP_PORT);

		canTalonFrontLeft = new CANTalon(RobotMap.DRIVE_MOTOR_FRONT_LEFT_CANID);
		canTalonFrontRight = new CANTalon(
				RobotMap.DRIVE_MOTOR_FRONT_RIGHT_CANID);

		encoderLeft = new Encoder(RobotMap.DRIVE_ENCODER_LEFT_A,
				RobotMap.DRIVE_ENCODER_LEFT_B);
		encoderRight = new Encoder(RobotMap.DRIVE_ENCODER_RIGHT_A,
				RobotMap.DRIVE_ENCODER_RIGHT_B);

		if (!RobotMap.TEST_ROBOT) {
			toteOptic = new DigitalInput(RobotMap.TOTE_OPTIC_DIO);
			binOptic = new DigitalInput(RobotMap.BIN_OPTIC_DIO);

			canTalonRearLeft = new CANTalon(
					RobotMap.DRIVE_MOTOR_REAR_LEFT_CANID);
			canTalonRearRight = new CANTalon(
					RobotMap.DRIVE_MOTOR_REAR_RIGHT_CANID);

			canTalonIntakeLeft = new CANTalon(RobotMap.INTAKE_MOTOR_LEFT_CANID);
			canTalonIntakeRight = new CANTalon(
					RobotMap.INTAKE_MOTOR_RIGHT_CANID);

			canTalonElevator = new CANTalon(
					RobotMap.DRIVE_MOTOR_FRONT_LEFT_CANID); // MOVE THIS BACK
															// WHEN DONE

			encoderElevator = new Encoder(RobotMap.ELEVATOR_ENCODER_A,
					RobotMap.ELEVATOR_ENCODER_B);

			limitTop = new DigitalInput(RobotMap.ELEVATOR_LIMIT_TOP_CHANNEL);
			limitBottom = new DigitalInput(
					RobotMap.ELEVATOR_LIMIT_BOTTOM_CHANNEL);

			camServoHor = new Servo(RobotMap.CAMERA_SERVO_HORIZONTAL_PWM);
			camServoVert = new Servo(RobotMap.CAMERA_SERVO_VERTICAL_PWM);
		}

		// creating images
		Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

		// the camera name can be found through the roboRIO web interface
		// camera_session = NIVision.IMAQdxOpenCamera("cam0",
		// NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		// NIVision.IMAQdxConfigureGrab(camera_session);
		// NIVision.IMAQdxStartAcquisition(camera_session);

		// compressor initialization and turning on
		compressor1 = new Compressor(RobotMap.PCM_CANID);

		if (RobotMap.TEST_ROBOT) {
			compressor1.setClosedLoopControl(false);
		} else {
			compressor1.setClosedLoopControl(true);
		}

		// piston initialization
//		leftHugPiston = new DoubleSolenoid(RobotMap.PCM_CANID,
//				RobotMap.DOUBLE_SOLENOID_LEFT_HUG_PCMID_EXPANSION,
//				RobotMap.DOUBLE_SOLENOID_LEFT_HUG_PCMID_RETRACTION);
//		rightHugPiston = new DoubleSolenoid(RobotMap.PCM_CANID,
//				RobotMap.DOUBLE_SOLENOID_RIGHT_HUG_PCMID_EXPANSION,
//				RobotMap.DOUBLE_SOLENOID_RIGHT_HUG_PCMID_RETRACTION);
//		intakePiston = new DoubleSolenoid(RobotMap.PCM_CANID,
//				RobotMap.DOUBLE_SOLENOID_INTAKE_PCMID_EXPANSION,
//				RobotMap.DOUBLE_SOLENOID_INTAKE_PCMID_RETRACTION);

		if (!RobotMap.TEST_ROBOT) {
			robotDrive = new RobotDrive(canTalonFrontLeft, canTalonRearLeft,
					canTalonFrontRight, canTalonRearRight);
		} else {
			robotDrive = new RobotDrive(canTalonFrontLeft, canTalonFrontRight);
			robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
			robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		}

		pdp = new PowerDistributionPanel();

		/*
		 * continuousVarsReader contains the debugNumber, which is a counter for
		 * the filenames of debug files. Debug files will contain everything
		 * that happens during an enabling of the robot. They will all be saved
		 * to the flashdrive which is at /u/ If the flashdrive isn't plugged in,
		 * these will be printed to System.out
		 */
		System.out.println("Go!");
		try {
			// reads a file for the name of our debug final and creates it
			continuousVarsReader = new Scanner(new File("/u/continuousvars.txt"));
			int debugNumber = Integer.parseInt(continuousVarsReader.nextLine());
			debugWriter = new PrintWriter("/u/debug" + debugNumber + ".txt", "UTF-8");
			continuousVarsReader.close();
			continuousVarsWriter = new PrintWriter("/u/continuousvars.txt");
			continuousVarsWriter.println(debugNumber + 1);
			continuousVarsWriter.close();
			
			// TODO Look at this maybe?
			
			// takes a startup picture and saves to the flashdrive
			// if the flashdrive isn't plugged in, the error SHOULD have already been caught
			// so it won't take a picture if the flashdrive isn't there
			NIVision.IMAQdxGrab(camera.getCameraSession(), frame, 1);
			String location = "/u/startup" + debugNumber + ".png";
			NIVision.imaqWriteFile(frame, location, new RGBValue());
			
			System.out.println("YAY!!!!!!!!!!!!!!!!!!!!!! IT SHOULD HAVE WORKED!!!!!!!!!!");
			System.out.println("THE LOCATION SHOULD BE: " + location + "!!!!!!!!!!!!");

		} catch (FileNotFoundException e) { // goes here if flashdrive isn't
											// plugged in
			debugWriter = new PrintWriter(System.out, true);
			System.out.println(e);
		} catch (UnsupportedEncodingException e) {
			debugWriter = new PrintWriter(System.out, true);
			System.out.println(e);
		} catch (NoSuchElementException e) {
			debugWriter = new PrintWriter(System.out, true);
			System.out.println(e);
		}

		/*
		 * Naming convention for new versions:
		 * 
		 * Name each new version after a type of ape. This is to make
		 * programmers feel fancy like they work at a real programming company.
		 */
		debugWriter.println("Version 2.2.1: Spider Monkey\n");
		SmartDashboard.putString("Version", "2.2.1 -- Spider Monkey");

		/*
		 * Fancyish code that can create choosers in the SmartDashboard for
		 * autonomous. Instead of, as WPI wants us to do, running new commands
		 * that are scheduled with the RobotBuilder, we simply have the
		 * SendableChooser give us an Integer representing the selected program.
		 * 
		 * The strange syntax is because SendableChooser wants an object, not an
		 * integer, but just bear with it.
		 * 
		 * We will decide which program to run in autonomousInit() and we will
		 * run that program in autonomousPeriodic()
		 */
		autonomousChooser = new SendableChooser();

		autonomousChooser.addDefault("Stop Autonomous", new Integer(
				RobotMap.STOP_AUTONOMOUS));
		autonomousChooser.addObject("Drive Forward Autonomous", new Integer(
				RobotMap.DRIVE_FORWARD_AUTONOMOUS));
		autonomousChooser.addObject("Delay and Drive Forward Autonomous",
				new Integer(RobotMap.DELAY_AND_DRIVE_FORWARD_AUTONOMOUS));
		autonomousChooser.addObject("Tote Grab Autonomous", new Integer(
				RobotMap.TOTE_GRAB_AUTONOMOUS));
		autonomousChooser.addObject("Bin Grab Autonomous", new Integer(
				RobotMap.BIN_GRAB_AUTONOMOUS));
		autonomousChooser.addObject("Tote Stack Autonomous", new Integer(
				RobotMap.TOTE_STACK_AUTONOMOUS));
		SmartDashboard.putData("Autonomous Mode Selector", autonomousChooser);

		// Same with Elevator speed chooser. We will use this to find the
		// optimal elevator speed
		elevatorChooser = new SendableChooser();

		elevatorChooser.addObject("0.1", 0.1);
		elevatorChooser.addDefault("0.2", 0.2); // the default speed will be 0.2
		elevatorChooser.addObject("0.3", 0.3);
		elevatorChooser.addObject("0.4", 0.4);
		elevatorChooser.addObject("0.5", 0.5);
		elevatorChooser.addObject("0.6", 0.6);
		elevatorChooser.addObject("0.7", 0.7);
		elevatorChooser.addObject("0.8", 0.8);
		elevatorChooser.addObject("0.9", 0.9);
		elevatorChooser.addObject("1.0", 1.0);

		SmartDashboard.putData("Elevator Speed Chooser", elevatorChooser);

	}

	/**
	 * This function is called at the start of autonomous
	 */
	public void autonomousInit() {
		debugWriter.println("Beginning autonomous\n");

		RobotMap.autonomousMode = ((Integer) (autonomousChooser.getSelected()))
				.intValue(); // stupidly complex piece of code that just sets
								// our autonomous mode
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Autonomous.doAuton(); // runs the selected autonomous mode
	}

	/**
	 * This function is called once at the start of teleop
	 */
	public void teleopInit() {
		debugWriter.println("Beginning teleop\n");
		// camServoHor.set(0);
		// camServoVert.set(0);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {

		// gets required elevator motor value from SmartDashboard
		RobotMap.elevatorMotorSpeed = ((Double) (elevatorChooser.getSelected()))
				.doubleValue();
		// to ensure that radio buttons work
		SmartDashboard.putNumber("elevator motor speed",
				RobotMap.elevatorMotorSpeed);

		// drive switch
		if (joystickRight.getRawButton(RobotMap.TANK_DRIVE_BUTTON)) {
			isTankDrive = true;
		}
		if (joystickRight.getRawButton(RobotMap.ARCADE_DRIVE_BUTTON)) {

			isTankDrive = false;
		}
		if (isTankDrive) {
			robotDrive.tankDrive(joystickLeft, joystickRight);
		} else {
			robotDrive.arcadeDrive(joystickRight);
		}

		// this takes pictures while driving but it's still experimental

		// TODO: IMAGES
		
		if (joystickOp.getRawButton(RobotMap.MANUAL_FUNCTION_BUTTON)
				&& RobotMap.TEST_ROBOT && joystickOp.getRawButton(1)
				&& !picture_taking && !picture_writing && !buttonOnePressed) {
			picture_taking = true;
			buttonOnePressed = true;
		}
		if (joystickOp.getRawButton(1)) {
			buttonOnePressed = true;
		} else {
			buttonOnePressed = false;
		}

		Image frame = null;
		if (picture_taking) {
			System.out.println("PICTURE_TAKING is TRUE");
			if (cameraTimer == 0) {
				cameraTimer = System.currentTimeMillis();
			}
			frame = camera.takePicture();
			System.out.println("ATTEMPTING TAKE PICTURE");
			if (frame != null) {
				picture_writing = true;
				picture_taking = false;
				System.out.println("Take picture in "
						+ new Long(System.currentTimeMillis() - cameraTimer));
				cameraTimer = 0;
			}
		}
		
		if (picture_writing) {
			System.out.println("PICTURE_WRITING is TRUE");
			try {
				if (cameraTimer == 0) {
					cameraTimer = System.currentTimeMillis();
				}
				boolean finished = camera.savePicture(frame, "/u/teleop"
						+ System.currentTimeMillis() + ".png");
				if (finished) {
					System.out
							.println("Save picture in "
									+ new Long(System.currentTimeMillis()
											- cameraTimer));
					picture_writing = false;
					picture_taking = false;
					cameraTimer = 0;
					frame = null;
				}
			} catch (VisionException e) {
				System.out.println("no usb for picture");
			}
		}
		// to make sure we don't take too many pictures in one press
		if (joystickOp.getRawButton(1)) {
			buttonOnePressed = true;
		} else {
			buttonOnePressed = false;
			picture_taking = false;
			picture_writing = false;
			cameraTimer = 0;
			frame = null;
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
		boolean isAbort = joystickOp.getRawButton(RobotMap.ABORT_BUTTON);
		if (!RobotMap.TEST_ROBOT) {
			TeleopStateMachine.stateMachine(isCoopertition, isScoring,
					isGround, isLift, isManual, isReversing, isAbort);
		}

		// declaring buttons for intake pistons
		boolean isIntakePistonOn = joystickRight
				.getRawButton(RobotMap.INTAKE_PISTON_ACTIVATE_BUTTON);
		boolean isIntakePistonOff = joystickRight
				.getRawButton(RobotMap.INTAKE_PISTON_DEACTIVATE_BUTTON);

		/*
		 * NOTE: we are checking intakePistons out of manual control and out of
		 * teleopstatemachine This is because we want to be able to open and
		 * close the pistons whether or not we are running statemachine
		 */
		if (isIntakePistonOn) {
//			intakePiston.set(DoubleSolenoid.Value.kForward);
		}
		if (isIntakePistonOff) {
//			intakePiston.set(DoubleSolenoid.Value.kReverse);
		}

		// manual control
		if (RobotMap.currentState == RobotMap.MANUAL_OVERRIDE_STATE) {
			boolean isHugPistonOn = joystickOp
					.getRawButton(RobotMap.MANUAL_PISTON_ACTIVATE_BUTTON);
			boolean isHugPistonOff = joystickOp
					.getRawButton(RobotMap.MANUAL_PISTON_DEACTIVATE_BUTTON);
			boolean isForwardIntake = joystickOp
					.getRawButton(RobotMap.MANUAL_INTAKE_BUTTON);
			boolean isBackwardsIntake = joystickOp
					.getRawButton(RobotMap.MANUAL_OUTTAKE_BUTTON);
			boolean isFunction = joystickOp
					.getRawButton(RobotMap.MANUAL_FUNCTION_BUTTON);

//			if (isHugPistonOn) {
//				leftHugPiston.set(DoubleSolenoid.Value.kForward);
//				rightHugPiston.set(DoubleSolenoid.Value.kForward);
//			} else if (isHugPistonOff) {
//				leftHugPiston.set(DoubleSolenoid.Value.kReverse);
//				rightHugPiston.set(DoubleSolenoid.Value.kReverse);
//			}

			if (isForwardIntake) {
				Intake.spin(0.5); // TODO magic numbers?
			} else if (isBackwardsIntake) {
				Intake.spin(-0.5);
			} else {
				Intake.stop();
			}

			if (isFunction) {
				if(!Elevator.calibration(joystickOp.getY())) { // moves Elevator and checks if
					Elevator.stop();
				}
			}
		} // end if (RobotMap.currentState == RobotMap.MANUAL_OVERRIDE_STATE)
	} // end function teleopPeriodic

	/**
	 * This function is called when test mode starts.
	 */
	public void testInit() {
		debugWriter.println("Beginning test\n");
	}

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
//		if (joystickOp.getRawButton(9)) {
//			leftHugPiston.set(DoubleSolenoid.Value.kForward);
//			rightHugPiston.set(DoubleSolenoid.Value.kForward);
//		} else if (joystickOp.getRawButton(10)) {
//			leftHugPiston.set(DoubleSolenoid.Value.kReverse);
//			rightHugPiston.set(DoubleSolenoid.Value.kReverse);
//		}
//		if (joystickOp.getRawButton(11)) {
//			intakePiston.set(DoubleSolenoid.Value.kForward);
//		} else if (joystickOp.getRawButton(12)) {
//			intakePiston.set(DoubleSolenoid.Value.kReverse);
//		}

		// method testing code
		if (joystickOp.getRawButton(8)) {
			buttonEightPressed = true;
			if (joystickOp.getRawButton(1)) {
				Elevator.calibration(0.5);
			} else if (joystickOp.getRawButton(2)) {
				Elevator.calibration(-0.5);
			} else if (joystickOp.getRawButton(3)) {
				Intake.spin(0.5);
			} else if (joystickOp.getRawButton(4)) {
				Intake.spin(-0.5);
			} else if (joystickOp.getRawButton(5)) {
				ToteGrabber.moveHugPistons(true);
			} else if (joystickOp.getRawButton(6)) {
				ToteGrabber.moveHugPistons(false);
			} else if (joystickOp.getRawButton(7)) {
				RobotMap.currentState = RobotMap.INIT_STATE;
				TeleopStateMachine.stateMachine(false, false, false, false,
						false, false, false);
			} else if (joystickOp.getRawButton(9)) {
				RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
				TeleopStateMachine.stateMachine(false, false, false, false,
						false, false, false);
			} else if (joystickOp.getRawButton(10)) {
				RobotMap.currentState = RobotMap.WAIT_FOR_GAME_PIECE_STATE;
				TeleopStateMachine.stateMachine(false, false, false, false,
						false, false, false);
			} else if (joystickOp.getRawButton(11)) {
				RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_SCORING_STATE;
				TeleopStateMachine.stateMachine(false, false, false, false,
						false, false, false);
			} else if (joystickOp.getRawButton(12)) {
				RobotMap.currentState = RobotMap.REVERSE_INTAKE_MOTORS_STATE;
				TeleopStateMachine.stateMachine(false, false, false, false,
						false, false, false);
			} else {
				Elevator.stop();
				Intake.stop();
				canTalonRearLeft.set(0.0);
				canTalonFrontLeft.set(0.0);
				canTalonRearRight.set(0.0);
				canTalonFrontRight.set(0.0);
			}
		} else if (!joystickOp.getRawButton(8) && buttonEightPressed) {
			Elevator.stop();
			Intake.stop();
			canTalonRearLeft.set(0.0);
			canTalonFrontLeft.set(0.0);
			canTalonRearRight.set(0.0);
			canTalonFrontRight.set(0.0);
			buttonEightPressed = false;
		}

		// @formatter:off
		// print encoders and currents. we are not formatting this to keep
		// everything on one line
		System.out
				.println("Left Intake: "
						+ Robot.pdp
								.getCurrent(RobotMap.CAN_TALON_INTAKE_LEFT_PDP_PORT));
		System.out.println("Right Intake: "
				+ Robot.pdp
						.getCurrent(RobotMap.CAN_TALON_INTAKE_RIGHT_PDP_PORT));
		System.out.println("");
		System.out
				.println("Left Back Motor: "
						+ Robot.pdp
								.getCurrent(RobotMap.CAN_TALON_DRIVE_LEFT_BACK_PDP_PORT));
		System.out
				.println("Left Front Motor: "
						+ Robot.pdp
								.getCurrent(RobotMap.CAN_TALON_DRIVE_LEFT_FRONT_PDP_PORT));
		System.out.println("");
		System.out
				.println("Right Back Motor: "
						+ Robot.pdp
								.getCurrent(RobotMap.CAN_TALON_DRIVE_RIGHT_BACK_PDP_PORT));
		System.out
				.println("Right Front Motor: "
						+ Robot.pdp
								.getCurrent(RobotMap.CAN_TALON_DRIVE_RIGHT_FRONT_PDP_PORT));
		System.out.println("");
		System.out.println("Elevator Motor: "
				+ Robot.pdp.getCurrent(RobotMap.CAN_TALON_ELEVATOR_PDP_PORT));
		System.out.println("");
		System.out.println("Elevator Encoder: " + Robot.encoderElevator.get());
		System.out.println("Left Encoder: " + Robot.encoderLeft.get());
		System.out.println("Right Encoder: " + Robot.encoderRight.get());
		System.out.println("");
		// @formatter:on
	}

	/**
	 * This function is called at the beginning of the robot being disabled
	 */
	public void disabledInit() {
		// turns off camera and closes debug writer after disable
		// TODO: make this work with enable/disabling multiple times
//		debugWriter.println("Disabling");
//		debugWriter.close();
//		camera.off();
	}

}
