package org.usfirst.frc.team668.robot;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.RGBValue;
import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the IterativeRobot documentation. If you change the name of this class or the package after creating this project, you must also update the manifest file in the resource directory.
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
 * @see Robot#smartDashboardOutputs()
 * 
 * @see TeleopStateMachine#stateMachine(boolean, boolean, boolean, boolean, boolean, boolean, boolean)
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

/*
 * TODO List:
 * Switch from manual mode to state machine <----------- Written
 * Finished -> Fix No-Tote inefficiency <--------------- Finished
 * Manual intake piston automatic
 * Make a new state machine diagram <------------------- Finished
 * Autonomous with one tote
 * Prettify
 * -> Remove magic numbers
 * Encoders <------------------------------------------- Finished
 * Get camera code to work <---------------------------- Finished
 * HANG IN AUTONOMOUS LOSE TIME IN TELEOP <------------- Finished =)
 * DO THE FANCY CAMERA PIX_TAKERING JUST CUZ ITS KEWL
 * Make new state for the new thingy ---- NEW!!! IMPORTANT!!!
 * Make a second motor for the elevator ---- NEW!!!! IMPORTANT!!!!
 */

public class Robot extends IterativeRobot {
	
	// Versioning
	public static String versionNumber = "6.0.0";
	public static String versionName = "Evil Capulet Ape of Wrath";
	
	// Object Declaration
	public static CANTalon canTalonFrontLeft, canTalonFrontRight,
			canTalonRearLeft, canTalonRearRight;
	public static CANTalon canTalonIntakeLeft, canTalonIntakeRight,
			canTalonElevator, canTalonElevatorTop;
	public static Compressor compressor1;
	public static DigitalInput limitTop, limitBottom; // limitTop is default true, limitBottom is default true
	public static DigitalInput correctionOptical, limitOptical, toteOptic; // senses if we have tote
	// tote optic is normally true
	public static DoubleSolenoid hugPiston, intakePiston; // hug : kForward - closed and intake piston: kForward is intake mode, kBackward is open mode to allow elevator to move public static Encoder encoderLeft, encoderRight, encoderElevator; // encoderRight will always be multiplied by 360.0 / 250.0 because the encoders have different ranges
	public static Encoder encoderLeft, encoderRight, encoderElevator;
	public static Joystick joystickLeft, joystickRight, joystickOp, joystickWheel;
	public static PowerDistributionPanel pdp;
	public static PrintWriter debugWriter, continuousVarsWriter; // this for debug files saved to the flashdrive
	public static RobotDrive robotDrive;
	public static Scanner continuousVarsReader;
	public static SendableChooser autonomousChooser, elevatorChooser,
			manualChooser, clampStartChooser; // radio buttons: which autonomous subroutine, elevator speed, if manual upon start and if clamped upon start
	public static Timer t;
	
	// Camera
	CameraServer server; // sends camera feed to screen -- Used for streaming
	// USBCamera usb; TODO: determine whether or not to keep this
	// int camera_session;
	// boolean picture_taking = false;
	// boolean picture_writing = false;
	// long cameraTimer = 0;
	int session; // Used for picture-taking
	Image frame;
	
	// Button Pressed?
	boolean buttonOnePressed = false, buttonSevenPressed = false,
			buttonEightPressed = false;
	
	// Drive
	boolean isSplitArcade = true;
	
	static boolean weAreHoldingABin = false; // This variable tells us whether or not an autonomous that picks up a bin was called
	
	//
	/**
	 * This function is run when the robot is first started up and should be used for any initialization code.
	 */
	public void robotInit() {
		weAreHoldingABin = false;
		
		// Object Initialization
		
		// Drive motors
		
		canTalonFrontLeft = new CANTalon(RobotMap.DRIVE_MOTOR_FRONT_LEFT_CANID);
		canTalonFrontRight = new CANTalon(RobotMap.DRIVE_MOTOR_FRONT_RIGHT_CANID);
		
		canTalonRearLeft = new CANTalon(RobotMap.DRIVE_MOTOR_REAR_LEFT_CANID);
		canTalonRearRight = new CANTalon(RobotMap.DRIVE_MOTOR_REAR_RIGHT_CANID);
		
		canTalonIntakeLeft = new CANTalon(RobotMap.INTAKE_MOTOR_LEFT_CANID);
		canTalonIntakeRight = new CANTalon(RobotMap.INTAKE_MOTOR_RIGHT_CANID);
		
		// Elevator motor
		
		canTalonElevator = new CANTalon(RobotMap.ELEVATOR_MOTOR_CANID);
		canTalonElevatorTop = new CANTalon(RobotMap.ELEVATOR_TOP_MOTOR_CANID);
		
		// Sensors
		
		encoderLeft = new Encoder(RobotMap.DRIVE_ENCODER_LEFT_A, RobotMap.DRIVE_ENCODER_LEFT_B);
		encoderRight = new Encoder(RobotMap.DRIVE_ENCODER_RIGHT_A, RobotMap.DRIVE_ENCODER_RIGHT_B);
		encoderElevator = new Encoder(RobotMap.ELEVATOR_ENCODER_A, RobotMap.ELEVATOR_ENCODER_B);
		
		encoderLeft.reset();
		encoderRight.reset();
		// we do not reset encoderElevator because it could and likely will start somewhere else
		
		limitTop = new DigitalInput(RobotMap.ELEVATOR_LIMIT_TOP_CHANNEL);
		limitBottom = new DigitalInput(RobotMap.ELEVATOR_LIMIT_BOTTOM_CHANNEL);
		toteOptic = new DigitalInput(RobotMap.TOTE_OPTIC_DIO);
		
		// Pistons
		
		hugPiston = new DoubleSolenoid(RobotMap.PCM_CANID, RobotMap.DOUBLE_SOLENOID_HUG_PCMID_EXPANSION, RobotMap.DOUBLE_SOLENOID_HUG_PCMID_RETRACTION);
		intakePiston = new DoubleSolenoid(RobotMap.PCM_CANID, RobotMap.DOUBLE_SOLENOID_INTAKE_PCMID_EXPANSION, RobotMap.DOUBLE_SOLENOID_INTAKE_PCMID_RETRACTION);
		
		// Joysticks
		
		joystickLeft = new Joystick(RobotMap.JOYSTICK_LEFT_PORT);
		joystickRight = new Joystick(RobotMap.JOYSTICK_RIGHT_PORT);
		joystickOp = new Joystick(RobotMap.JOYSTICK_OP_PORT);
		//joystickWheel = new Joystick(RobotMap.JOYSTICK_WHEEL_PORT);
		
		// TODO: Take photos
		// creating images
		// this is the start of the image that taking a picture will write into
		// Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		//
		// // the camera name can be found through the roboRIO web interface
		// // checks if the camera is connected; if it's not, then set cameraConnected to false
		// // that way, we won't get an error if we forget to plug in the camera
		// try {
		// // camera_session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		// // NIVision.IMAQdxConfigureGrab(camera_session);
		// // NIVision.IMAQdxStartAcquisition(camera_session);
		// usb = new USBCamera("cam0");
		// usb.openCamera();
		// usb.startCapture();
		// RobotMap.cameraConnected = true;
		// } catch (VisionException e) {
		// System.out.println(e);
		// RobotMap.cameraConnected = false;
		// }
		
		// compressor initialization and turning on
		compressor1 = new Compressor(RobotMap.PCM_CANID);
		
		if (RobotMap.isTestRobot) {
			compressor1.setClosedLoopControl(false); // Test robot doesn't have compressor
		} else {
			compressor1.setClosedLoopControl(true);
		}
		
		if (!RobotMap.isTestRobot) { // If it is not a test robot, there are four drive motors
			robotDrive = new RobotDrive(canTalonFrontLeft, canTalonRearLeft, canTalonFrontRight, canTalonRearRight);
			robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true); // They should all be inversed
			robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
			robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
			robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		} else {
			robotDrive = new RobotDrive(canTalonFrontLeft, canTalonFrontRight);
			
		}
		
		// if (!RobotMap.cameraConnected) { // This is camera code that isn't necessary. It is a duplicate from above.
		// server = CameraServer.getInstance();
		// server.startAutomaticCapture(usb);
		// }
		
		pdp = new PowerDistributionPanel();
		
		/*
		 * Save debug files to the flashdrive. continuousVarsReader contains the debugNumber, which is a counter for the filenames of debug files. Debug files will contain everything that happens during an enabling of the robot. They will all be saved to the flashdrive which is at /u/ If the flashdrive isn't plugged in, these will be printed to System.out
		 */
		try {
			// reads a file for the name of our debug final and creates it
			continuousVarsReader = new Scanner(new File("/u/continuousvars.txt"));
			int debugNumber = Integer.parseInt(continuousVarsReader.nextLine());
			debugWriter = new PrintWriter("/u/debug" + debugNumber + ".txt", "UTF-8");
			continuousVarsReader.close();
			continuousVarsWriter = new PrintWriter("/u/continuousvars.txt");
			continuousVarsWriter.println(debugNumber + 1);
			continuousVarsWriter.close();
			
			// takes a startup picture and saves to the flashdrive
			// if the flashdrive isn't plugged in, the error SHOULD have already
			// been caught
			// so it won't take a picture if the flashdrive isn't there
			// if (RobotMap.cameraConnected) {
			// usb.getImage(frame);
			// NIVision.imaqWriteFile(frame, "/u/startup" + debugNumber + ".png", new RGBValue());
			// }
			//
		} catch (FileNotFoundException e) { // goes here if flashdrive isn't plugged in
			debugWriter = new PrintWriter(System.out, true);
			System.out.println(e);
		} catch (UnsupportedEncodingException e) {
			debugWriter = new PrintWriter(System.out, true);
			System.out.println(e);
		} catch (NoSuchElementException e) {
			debugWriter = new PrintWriter(System.out, true);
			System.out.println(e);
		} catch (VisionException e) {
			System.out.println(e);
		}
		
		/*
		 * Naming convention for new versions:
		 * 
		 * Name each new version after a type of ape. This is to make programmers feel fancy like they work at a real programming company.
		 */
		debugWriter.print("Version " + versionNumber + ": " + versionName + "\n");
		debugWriter.println();
		debugWriter.write("Version " + versionNumber + ": " + versionName + "\n");
		SmartDashboard.putString("Version", "Version " + versionNumber + ": " + versionName);
		
		// START CAMERA STREAM CODE
		
		try {
			// This is fancy camera code that only streams to the PC Dashboard.
			server = CameraServer.getInstance();
			server.setQuality(RobotMap.CAMERA_QUALITY); // 50 is currently perfect
			// the camera name (ex "cam0") can be found through the roborio web interface
			server.startAutomaticCapture("cam0");
		} catch (VisionException e) {
			System.out.println("VISION EXCEPTION ~ " + e);
		}
		
		// STOP CAMERA STREAM CODE
		
		// TODO START CAMERA PICTURE CODE
		
		// frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		//
		// // the camera name (ex "cam0") can be found through the roborio web interface
		// session = NIVision.IMAQdxOpenCamera("cam0",
		// NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		// NIVision.IMAQdxConfigureGrab(session);
		
		/*
		 * Fancyish code that can create choosers in the SmartDashboard for autonomous. Instead of,
		 * as WPI wants us to do, running new commands that are scheduled with the RobotBuilder,
		 * we simply have the SendableChooser give us an Integer representing the selected program.
		 * 
		 * The strange syntax is because SendableChooser wants an object, not an integer, but just bear with it.
		 * 
		 * We will decide which program to run in autonomousInit() and we will run that program in autonomousPeriodic()
		 */
		autonomousChooser = new SendableChooser();
		
		autonomousChooser.addObject("Stop Autonomous", new Integer(RobotMap.STOP_AUTONOMOUS));
		autonomousChooser.addObject("Drive Forward Autonomous", new Integer(RobotMap.DRIVE_FORWARD_AUTONOMOUS));
		autonomousChooser.addObject("Delay and Drive Forward Autonomous", new Integer(RobotMap.DELAY_AND_DRIVE_FORWARD_AUTONOMOUS));
		autonomousChooser.addObject("Tote Grab Autonomous", new Integer(RobotMap.TOTE_GRAB_AUTONOMOUS));
		autonomousChooser.addObject("Delay and Tote Grab Autonomous", new Integer(RobotMap.DELAY_AND_TOTE_GRAB_AUTONOMOUS));
		autonomousChooser.addObject("Bin Grab Autonomous", new Integer(RobotMap.BIN_GRAB_AUTONOMOUS));
		autonomousChooser.addObject("Delay and Bin Grab Autonomous", new Integer(RobotMap.DELAY_AND_BIN_GRAB_AUTONOMOUS));
		autonomousChooser.addDefault("Tote and Bin Grab Autonomous", new Integer(RobotMap.TOTE_AND_BIN_GRAB_AUTONOMOUS));
		autonomousChooser.addObject("Delay and Tote and Bin Grab Autonomous", new Integer(RobotMap.DELAY_AND_TOTE_AND_BIN_GRAB_AUTONOMOUS));
		autonomousChooser.addObject("Two Tote Bin Stacks Autonomous", new Integer(RobotMap.TWO_TOTE_BIN_STACKS_AUTONOMOUS));
		autonomousChooser.addObject("Delay and Tote Bin Stacks Autonomous", new Integer(RobotMap.DELAY_AND_TWO_TOTE_BIN_STACKS_AUTONOMOUS));
		autonomousChooser.addObject("Spin Away Autonomous", new Integer(RobotMap.SPIN_AWAY_AUTONOMOUS));
		autonomousChooser.addObject("Delay and Spin Away Autonomous", new Integer(RobotMap.DELAY_AND_SPIN_AWAY_AUTONOMOUS));
		
		SmartDashboard.putData("Autonomous Mode Selection", autonomousChooser);
		
		// Same with Elevator speed chooser. We will use this to find the
		// optimal elevator speed
		elevatorChooser = new SendableChooser();
		
		// TODO: remove this because it's kind useless
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
		
		manualChooser = new SendableChooser();
		
		manualChooser.addObject("State Machine", RobotMap.INITIAL_STATE);
		manualChooser.addDefault("Manual Mode", RobotMap.MANUAL_STATE);
		
		SmartDashboard.putData("Manual Mode Chooser", manualChooser);
		
		clampStartChooser = new SendableChooser();
		
		clampStartChooser.addDefault("Open Clamps", false);
		clampStartChooser.addObject("Close Clamps", true);
		
		SmartDashboard.putData("State Machine Init Clamps Position", clampStartChooser);
		
	} // robotInit
	
	/**
	 * This function is called at the start of autonomous
	 */
	public void autonomousInit() {
		
		weAreHoldingABin = false;
		
		if (pdp.getVoltage() < RobotMap.PDP_AUTON_WARNING_VOLTAGE ) { // TODO MAGYK 12.0 teleop too --- no longer a MAGYK number
			SmartDashboard.putString("THE BATTERY!!!@?!#?@!@?#!@?#", "FIXX ITT NOW!!@#!$!@#%!@#$");
		}
		
		robotDrive.drive(0.0, 0.0); // Make sure motors are stopped
		Intake.stop();
		Elevator.stop();
		
		debugWriter.println("Beginning autonomous\n");
		debugWriter.write("YAY!!! Autonomous has begun to begin");
		
		encoderLeft.reset();
		encoderRight.reset();
		
		robotDrive.setSafetyEnabled(false);
		
		RobotMap.autonomousMode = ((Integer) (autonomousChooser.getSelected())).intValue();
		// stupidly complex piece of code that just sets our autonomous mode
		
		// NOTE: "this" refers to the current instance. If you don't understand what I just said,
		// ask someone.
		
		if (RobotMap.autonomousMode == RobotMap.STOP_AUTONOMOUS) {
			Autonomous.stopAutonomous();
		} else if (RobotMap.autonomousMode == RobotMap.DRIVE_FORWARD_AUTONOMOUS) {
			Autonomous.driveForwardAutonomous(this);
		} else if (RobotMap.autonomousMode == RobotMap.DELAY_AND_DRIVE_FORWARD_AUTONOMOUS) {
			Autonomous.delayAutonomous();
			Autonomous.driveForwardAutonomous(this);
		} else if (RobotMap.autonomousMode == RobotMap.TOTE_GRAB_AUTONOMOUS) {
			Autonomous.toteGrabAutonomous(this);
		} else if (RobotMap.autonomousMode == RobotMap.DELAY_AND_TOTE_GRAB_AUTONOMOUS) {
			Autonomous.delayAutonomous();
			Autonomous.toteGrabAutonomous(this);
		} else if (RobotMap.autonomousMode == RobotMap.BIN_GRAB_AUTONOMOUS) {
			Autonomous.binGrabAutonomous(this);
			weAreHoldingABin = true; // set this to true so the state machine knows we are holding a bin
		} else if (RobotMap.autonomousMode == RobotMap.DELAY_AND_BIN_GRAB_AUTONOMOUS) {
			Autonomous.delayAutonomous();
			Autonomous.binGrabAutonomous(this);
			weAreHoldingABin = true;
		} else if (RobotMap.autonomousMode == RobotMap.TOTE_AND_BIN_GRAB_AUTONOMOUS) {
			Autonomous.binAndToteGrabAutonomous(this);
			weAreHoldingABin = true;
		} else if (RobotMap.autonomousMode == RobotMap.DELAY_AND_TOTE_AND_BIN_GRAB_AUTONOMOUS) {
			Autonomous.delayAutonomous();
			Autonomous.binAndToteGrabAutonomous(this);
			weAreHoldingABin = true;
		} else if (RobotMap.autonomousMode == RobotMap.TWO_TOTE_BIN_STACKS_AUTONOMOUS) {
			Autonomous.twoToteBinStacksAutonomous(this);
		} else if (RobotMap.autonomousMode == RobotMap.DELAY_AND_TWO_TOTE_BIN_STACKS_AUTONOMOUS) {
			Autonomous.delayAutonomous();
			Autonomous.twoToteBinStacksAutonomous(this);
		} else if (RobotMap.autonomousMode == RobotMap.SPIN_AWAY_AUTONOMOUS){
			Autonomous.spinAwayAutonomous(this);
		} else if (RobotMap.autonomousMode == RobotMap.DELAY_AND_SPIN_AWAY_AUTONOMOUS){
			Autonomous.delayAutonomous();
			Autonomous.spinAwayAutonomous(this);
		}
	} // autonomousInit
	
	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		RobotMap.currentStateNew = ((Integer) manualChooser.getSelected()).intValue();
	}
	
	/**
	 * This function is called once at the start of teleop
	 */
	public void teleopInit() {
		
		robotDrive.drive(0.0, 0.0); // Make sure motors are stopped
		Intake.stop();
		Elevator.stop();
		
//		if (weAreHoldingABin) {
//			RobotMap.currentStateNew = RobotMap.WAIT_FOR_BUTTON_STATE;
//		} else {
//			RobotMap.currentStateNew = ((Integer) manualChooser.getSelected()).intValue();
//		}
		
		debugWriter.println("Beginning teleop\n");
		// stupid code meaning we set the start state to whatever's on the smartdashboard chooser
		RobotMap.itemCount = 0;
		encoderLeft.reset();
		encoderRight.reset();
		
		for(int i = 0; i < RobotMap.stateLog.length; i++) {
			RobotMap.stateLog[i] = "";
		}
		RobotMap.stateIndex = 0;
		
	} // teleopinit
	
	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		
		smartDashboardOutputs();
		// drive code starts here {
		// drive switch
		if (joystickLeft.getRawButton(RobotMap.ARCADE_DRIVE_BUTTON)) {
			isSplitArcade = true;
		}
//		if (joystickLeft.getRawButton(RobotMap.TANK_DRIVE_BUTTON)) {
//			isSplitArcade = false;
//		}
		
		if (isSplitArcade) { // split arcade: make sure that the wheel is ID 2
			if (joystickLeft.getRawButton(RobotMap.MINIMIZE_DRIVE_SPEED_LEFT_BUTTON)) { // the 1.20 value is the increase in sensitivity that Riley has requested
				robotDrive.drive(joystickLeft.getY() * RobotMap.MINIMIZING_FACTOR_TWO, joystickRight.getX() * RobotMap.MINIMIZING_FACTOR_TWO * 1.25); //use the wheel, joystickRight is only used because it has the same ID. The wheel and the joystick are interchangable. 
			} else {
				robotDrive.drive(joystickLeft.getY() * RobotMap.MINIMIZING_FACTOR, joystickRight.getX() * RobotMap.MINIMIZING_FACTOR * 1.25); 
			}}
//		} else { // tank drive 
//			if (joystickLeft.getRawButton(RobotMap.MINIMIZE_DRIVE_SPEED_LEFT_BUTTON)) {
//				robotDrive.tankDrive(joystickLeft.getY() * RobotMap.MINIMIZING_FACTOR_TWO, joystickRight.getY() * RobotMap.MINIMIZING_FACTOR_TWO);
//			} else {
//				robotDrive.tankDrive(joystickLeft.getY() * RobotMap.MINIMIZING_FACTOR, joystickRight.getY() * RobotMap.MINIMIZING_FACTOR);
//			}
//		}
		
	// } ends here
	
	// this was commented out because we are now defaulting to split arcade for the preference of the driver 
//		if (isTankDrive) { // tank drive
//			if (joystickLeft.getRawButton(RobotMap.MAXIMIZE_DRIVE_SPEED_LEFT_BUTTON)) {
//				robotDrive.tankDrive(joystickLeft.getY(), joystickRight.getY());
//			} else {
//				robotDrive.tankDrive(joystickLeft.getY() * RobotMap.MINIMIZING_FACTOR, joystickRight.getY() * RobotMap.MINIMIZING_FACTOR);
//			}
//		} else { // split arcade
//			if (joystickLeft.getRawButton(RobotMap.MAXIMIZE_DRIVE_SPEED_LEFT_BUTTON)) {
//				robotDrive.drive(joystickRight.getY(), joystickLeft.getX() * -1);
//			} else {
//				robotDrive.drive(joystickRight.getY() * RobotMap.MINIMIZING_FACTOR, joystickWheel.getX() * -1 * RobotMap.MINIMIZING_FACTOR);
//			}
//		}
		
		// // camera code beginning
		// if (joystickOp.getRawButton(RobotMap.MANUAL_FUNCTION_BUTTON) && joystickOp.getRawButton(1)
		// && !picture_taking && !picture_writing && !buttonOnePressed) { // TODO: Magyk #
		// // if (joystickOp.getRawButton(RobotMap.MANUAL_FUNCTION_BUTTON) && RobotMap.isTestRobot &&
		// // joystickOp.getRawButton(1) && !picture_taking && !picture_writing && !buttonOnePressed) {
		// picture_taking = true;
		
		// buttonOnePressed = true;
		// }
		// if (joystickOp.getRawButton(1)) {
		// buttonOnePressed = true;
		// } else {
		// buttonOnePressed = false;
		// }
		//
		// if (RobotMap.cameraConnected) { // only run if we have a camera
		//
		// Image frame = null;
		// if (picture_taking) {
		// if (cameraTimer <= 0) {
		// cameraTimer = System.currentTimeMillis();
		// }
		// frame = CameraThreads.takePicture(usb);
		// if (frame != null) {
		// picture_writing = true;
		// picture_taking = false;
		// System.out.println("Take picture in " + new Long(System.currentTimeMillis() - cameraTimer));
		// cameraTimer = 0;
		// }
		// }
		// if (picture_writing) {
		// try {
		// if (cameraTimer == 0) {
		// cameraTimer = System.currentTimeMillis();
		// }
		// boolean finished = CameraThreads.savePicture(frame, "/u/teleop" + System.currentTimeMillis() + ".png");
		// if (finished) {
		// System.out.println("Save picture in " + new Long(System.currentTimeMillis() - cameraTimer));
		// picture_writing = false;
		// picture_taking = false;
		// cameraTimer = 0;
		// frame = null;
		// }
		// } catch (VisionException e) {
		// System.out.println("no usb for picture");
		// }
		// }
		// // to make sure we don't take too many pictures in one press
		// if (joystickOp.getRawButton(1)) {
		// buttonOnePressed = true;
		// } else {
		// buttonOnePressed = false;
		// debugWriter.println("no usb for picture");
		// picture_taking = false;
		// picture_writing = false;
		// cameraTimer = 0;
		// frame = null;
		// }
		// } // end if(cameraConnected)
		
		// state machine
//		boolean isDoneScoring = joystickOp.getRawButton(RobotMap.DONE_WITH_SCORING_BUTTON);
//		boolean isManualNew = joystickOp.getRawButton(RobotMap.MANUAL_OVERRIDE_BUTTON);
//		boolean isCoopertition = joystickOp.getRawButton(RobotMap.COOPERTITION_BUTTON);
//		boolean isHPStrat = joystickOp.getRawButton(RobotMap.HP_START_BUTTON);
//		boolean isScoring = joystickOp.getRawButton(RobotMap.SCORING_BUTTON);
//		boolean isLift = joystickOp.getRawButton(RobotMap.LIFT_BUTTON);
//		boolean isReversing = joystickOp.getRawButton(RobotMap.REVERSING_BUTTON);
//		boolean isToteHeight = joystickOp.getRawButton(RobotMap.TOTE_HEIGHT_BUTTON);
//		boolean isAbort = joystickOp.getRawButton(RobotMap.ABORT_BUTTON);
//		if (!RobotMap.isTestRobot) { 
//			System.out.println(RobotMap.currentStateNew);
//			TeleopStateMachineNew.stateMachine(isDoneScoring, isScoring,  
//					isLift, isManualNew, isReversing);
//		}
//		
		// declaring buttons for intake pistons
		
		/*
		 * NOTE: we are checking intakePistons out of manual control and out of teleopstatemachine This is because we want to be able to open and close the pistons whether or not we are running statemachine
		 */
		// this opens and closes the intake piston
		
		// boolean isIntake = joystickOp.getRawButton(RobotMap.INTAKE_PISTON_BUTTON); TODO: Use this.
		// if (isIntake && !buttonSevenPressed) {
		// if (intakePiston.get().equals(DoubleSolenoid.Value.kReverse)) {
		// intakePiston.set(DoubleSolenoid.Value.kForward);
		// } else {
		// intakePiston.set(DoubleSolenoid.Value.kReverse);
		// }
		// }
		// buttonSevenPressed = isIntake;
//		if (joystickOp.getRawButton(RobotMap.INTAKE_PISTON_CLOSE_BUTTON)) {
//			
//			intakePiston.set(DoubleSolenoid.Value.kForward);
//			
//		} else if (joystickOp.getRawButton(RobotMap.INTAKE_PISTON_OPEN_BUTTON)) {
//			
//			intakePiston.set(DoubleSolenoid.Value.kReverse);
//			
//		}
		
		

		
		// manual control
	//	if (RobotMap.currentStateNew == RobotMap.MANUAL_STATE) {
//			boolean isHugPistonOn = joystickOp.getRawButton(RobotMap.MANUAL_PISTON_ACTIVATE_BUTTON); // button 9
//			boolean isHugPistonOff = joystickOp.getRawButton(RobotMap.MANUAL_PISTON_DEACTIVATE_BUTTON);// button 10
			boolean isForwardIntake = joystickOp.getRawButton(RobotMap.MANUAL_INTAKE_BUTTON);// button 7
			boolean isOtherForwardIntake = joystickOp.getRawButton(RobotMap.OTHER_MANUAL_INTAKE_BUTTON); //button 2
			boolean isBackwardsIntake = joystickOp.getRawButton(RobotMap.MANUAL_OUTTAKE_BUTTON);// button 7
			boolean isFunction = joystickOp.getRawButton(RobotMap.MANUAL_FUNCTION_BUTTON);// button 12
			boolean isHeight = joystickOp.getRawButton(3);
			boolean isScoringButton = joystickOp.getRawButton(9);
			boolean zeroOut = limitBottom.get();
			
			if (!zeroOut){
				encoderElevator.reset();
			}
			if (isHeight){
				SmartDashboard.putString("Moving?", "Height");
				boolean isDone = Elevator.movePID(RobotMap.ELEVATOR_ENCODER_ONE_TOTE_HEIGHT);
				if (isDone){
					Elevator.stop();
				}
			}
			if (isScoringButton){
				SmartDashboard.putString("Moving?", "Scoring");
				boolean isFinish = Elevator.moveP(RobotMap.ELEVATOR_ENCODER_SCORING);
				if(isFinish){
					Elevator.stop();
				}
			}
			if (joystickOp.getRawButton(RobotMap.INTAKE_PISTON_CLOSE_BUTTON)) {
				
				intakePiston.set(DoubleSolenoid.Value.kForward);
				
			} else if (joystickOp.getRawButton(RobotMap.INTAKE_PISTON_OPEN_BUTTON)) {
				
				intakePiston.set(DoubleSolenoid.Value.kReverse);
				
			}
			if (isBackwardsIntake) { // outtake
				Intake.spin(RobotMap.MANUAL_BACKWARDS_INTAKE_SPEED);
			} else if (isForwardIntake || isOtherForwardIntake) { // intake
				Intake.spin(RobotMap.MANUAL_FORWARD_INTAKE_SPEED);
			} else {
				Intake.stop();
			}
			
			// start elevator limit switch moving code
			if (limitTop.get() && limitBottom.get() && !isHeight && !isScoringButton) { // Neither limit switch is hit because limit switches are default true
				SmartDashboard.putString("Hit Switch Limit?", "No");
				SmartDashboard.putString("Moving?", "Manual");
				if (isFunction) {
					canTalonElevator.set(joystickOp.getY());
					canTalonElevatorTop.set(joystickOp.getY());
				} else {
					canTalonElevator.set(0);
					canTalonElevatorTop.set(0);
				}
			} else {
				System.out.println("HIT LIMIT SWITCH!!!");
				if (!limitBottom.get()) { // Hit bottom, because limit switches are default true
					SmartDashboard.putString("Hit Switch Limit?", "Bottom");
					encoderElevator.reset(); // reset elevator encoder at bottom of lift
					double elevatorJoyVal = joystickOp.getY();
					if (isFunction && (elevatorJoyVal > 0)) { // only allows you to move up away from the limit switch
						canTalonElevator.set(elevatorJoyVal);
						canTalonElevatorTop.set(elevatorJoyVal);
					} else {
						canTalonElevator.set(0);
						canTalonElevatorTop.set(0);
					}
				} else if (!limitTop.get()) { // Hit top, because limit switches are default true
					SmartDashboard.putString("Hit Switch Limit?", "Top");
					double elevatorJoyVal = joystickOp.getY();
					if (isFunction && (elevatorJoyVal < 0)) { // only allows you to move down away from the limit switch
						canTalonElevator.set(elevatorJoyVal);
						canTalonElevatorTop.set(elevatorJoyVal);
					} else {
						canTalonElevator.set(0);
						canTalonElevatorTop.set(0);
					}
				} // end else if (!limitTop.get() ...
			} // end else (limitTop.get()...
				// end of elevator moving code
			
			
			
			// TODO: OPTICAL STOP
			// if (!limitOptical.get()) {
			// SmartDashboard.putString("Hit Optical Limit?", "No");
			// if (isFunction) {
			// canTalonElevator.set(joystickOp.getY() * -1);
			// } else {
			// canTalonElevator.set(0);
			// }
			// } else {
			// System.out.println("HIT OPTICAL LIMIT!!!");
			// if (encoderElevator.get() < RobotMap.ELEVATOR_ENCODER_DEADZONE) {
			// SmartDashboard.putString("Hit Optical Limit?", "Bottom");
			// encoderElevator.reset();
			// double elevatorJoyVal = joystickOp.getY();
			// if (isFunction && elevatorJoyVal > 0) {
			// canTalonElevator.set(elevatorJoyVal);
			// } else {
			// canTalonElevator.set(0);
			// }
			// } else if (encoderElevator.get() > RobotMap.ELEVATOR_ENCODER_MAX_HEIGHT - RobotMap.ELEVATOR_ENCODER_DEADZONE) {
			// SmartDashboard.putString("Hit Encoder Limit?", "Top");
			// double elevatorJoyVal = joystickOp.getY();
			// if (isFunction && elevatorJoyVal < 0) {
			// canTalonElevator.set(elevatorJoyVal);
			// } else {
			// canTalonElevator.set(0);
			// }
			// }
			// }
			// end optical stop code
	//	} // end if (RobotMap.currentState == RobotMap.MANUAL_OVERRIDE_STATE)
	}// end function teleopPeriodic
	
	/**
	 * This function is called when test mode starts.
	 */
	public void testInit() {
		
		debugWriter.println("Beginning test\n");
		System.out.println("Test Initiation");
		SmartDashboard.putNumber("Current", pdp.getCurrent(1));
		encoderLeft.reset();
		encoderRight.reset();
		
		robotDrive.setSafetyEnabled(true);
	} // testInit
	
	/**
	 * This function is called periodically during test mode. It contains test code for all the motors and pistons to be controlled individually.
	 */
	public void testPeriodic() {
		
		// motor testing code
		
		SmartDashboard.putNumber("Joystick Y Axis", joystickRight.getY());
		SmartDashboard.putNumber("Left Drive Encoder", encoderLeft.get() * -1);
		SmartDashboard.putNumber("Right Drive Encoder", encoderRight.get() * 360.0 / 250.0);
		SmartDashboard.putNumber("Elevator Encoder", encoderElevator.get());
		// SmartDashboard.putBoolean("Limit Optical", limitOptical.get());
		SmartDashboard.putBoolean("Limit Top", !limitTop.get()); // Inverse needed
		SmartDashboard.putBoolean("Limit Bottom", !limitBottom.get());
		
		for (int i = 0; i < 7; i++) { // Prints out currents for these CAN IDs
			SmartDashboard.putNumber("Current for CAN ID " + i, pdp.getCurrent(i));
		}
		
		if (joystickOp.getRawButton(1)) {
			canTalonFrontLeft.set(joystickOp.getY() * -1);
		} else {
			canTalonFrontLeft.set(0);
		}
		if (joystickOp.getRawButton(2)) {
			canTalonFrontRight.set(joystickOp.getY()); // TODO: Motor needs to be reversed
		} else {
			canTalonFrontRight.set(0);
		}
		if (joystickOp.getRawButton(3)) {
			canTalonRearLeft.set(joystickOp.getY() * -1);
		} else {
			canTalonRearLeft.set(0);
		}
		if (joystickOp.getRawButton(4)) {
			canTalonRearRight.set(joystickOp.getY()); // TODO: Motor needs to be reversed
		} else {
			canTalonRearRight.set(0);
		}
		if (joystickOp.getRawButton(5)) {
			canTalonIntakeLeft.set(joystickOp.getY() * -1);
		} else {
			canTalonIntakeLeft.set(0);
		}
		if (joystickOp.getRawButton(6)) {
			canTalonIntakeRight.set(joystickOp.getY()); // TODO: Motor needs to be reversed
		} else {
			canTalonIntakeRight.set(0);
		}
		
		// if (limitOptical.get()) {
		// SmartDashboard.putString("Hit Limit?", "No");
		// } else {
		// SmartDashboard.putString("Hit Limit?", "Yes");
		// }
		
		// TODO: LIMIT SENSOR IS HERE ^^^
		
		if (joystickOp.getRawButton(7)) {
			canTalonElevator.set(joystickOp.getY());
		} else {
			canTalonElevator.set(0);
		}
		
		if (joystickOp.getRawButton(8)) {
			canTalonFrontLeft.set(0);
			canTalonFrontRight.set(0);
			canTalonRearLeft.set(0);
			canTalonRearRight.set(0);
			canTalonIntakeRight.set(0);
			canTalonIntakeLeft.set(0);
			canTalonElevator.set(0);
		}
		
		// piston testing code
		if (joystickOp.getRawButton(9)) {
			hugPiston.set(DoubleSolenoid.Value.kForward);
		} else if (joystickOp.getRawButton(10)) {
			hugPiston.set(DoubleSolenoid.Value.kReverse);
		}
		if (joystickOp.getRawButton(11)) {
			intakePiston.set(DoubleSolenoid.Value.kForward); // close intake
		} else if (joystickOp.getRawButton(12)) {
			intakePiston.set(DoubleSolenoid.Value.kReverse); // open intake
		}
		//
		// // method testing code
		// if (joystickOp.getRawButton(8)) {
		// buttonEightPressed = true;
		// if (joystickOp.getRawButton(1)) {
		// Elevator.calibration(0.5);
		// } else if (joystickOp.getRawButton(2)) {
		// Elevator.calibration(-0.5);
		// } else if (joystickOp.getRawButton(3)) {
		// Intake.spin(0.5);
		// } else if (joystickOp.getRawButton(4)) {
		// Intake.spin(-0.5);
		// } else if (joystickOp.getRawButton(5)) {
		// ToteGrabber.moveHugPistons(true);
		// } else if (joystickOp.getRawButton(6)) {
		// ToteGrabber.moveHugPistons(false);
		// } else if (joystickOp.getRawButton(7)) {
		// RobotMap.currentState = RobotMap.INIT_STATE;
		// TeleopStateMachine.stateMachine(false, false, false, false, false,
		// false, false);
		// } else if (joystickOp.getRawButton(9)) {
		// RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_TOTE_STATE;
		// TeleopStateMachine.stateMachine(false, false, false, false, false,
		// false, false);
		// } else if (joystickOp.getRawButton(10)) {
		// RobotMap.currentState = RobotMap.WAIT_FOR_GAME_PIECE_STATE;
		// TeleopStateMachine.stateMachine(false, false, false, false, false,
		// false, false);
		// } else if (joystickOp.getRawButton(11)) {
		// RobotMap.currentState = RobotMap.ELEVATOR_HEIGHT_SCORING_STATE;
		// TeleopStateMachine.stateMachine(false, false, false, false, false,
		// false, false);
		// } else if (joystickOp.getRawButton(12)) {
		// RobotMap.currentState = RobotMap.REVERSE_INTAKE_MOTORS_STATE;
		// TeleopStateMachine.stateMachine(false, false, false, false, false,
		// false, false);
		// } else {
		// Elevator.stop();
		// Intake.stop();
		// canTalonRearLeft.set(0.0);
		// canTalonFrontLeft.set(0.0);
		// canTalonRearRight.set(0.0);
		// canTalonFrontRight.set(0.0);
		// }
		// } else if (!joystickOp.getRawButton(8) && buttonEightPressed) {
		// Elevator.stop();
		// Intake.stop();
		// canTalonRearLeft.set(0.0);
		// canTalonFrontLeft.set(0.0);
		// canTalonRearRight.set(0.0);
		// canTalonFrontRight.set(0.0);
		// buttonEightPressed = false;
		// }
		//
		// // @formatter:off
		// //print encoders and currents. we are not formatting this to keep
		// everything on one line
		// // System.out.println("Left Intake: " +
		// Robot.pdp.getCurrent(RobotMap.CAN_TALON_INTAKE_LEFT_PDP_PORT));
		// // System.out.println("Right Intake: " +
		// Robot.pdp.getCurrent(RobotMap.CAN_TALON_INTAKE_RIGHT_PDP_PORT));
		// // System.out.println("");
		// // System.out.println("Left Back Motor: " +
		// Robot.pdp.getCurrent(RobotMap.CAN_TALON_DRIVE_LEFT_BACK_PDP_PORT));
		// // System.out.println("Left Front Motor: " +
		// Robot.pdp.getCurrent(RobotMap.CAN_TALON_DRIVE_LEFT_FRONT_PDP_PORT));
		// // System.out.println("");
		// // System.out.println("Right Back Motor: " +
		// Robot.pdp.getCurrent(RobotMap.CAN_TALON_DRIVE_RIGHT_BACK_PDP_PORT));
		// // System.out.println("Right Front Motor: " +
		// Robot.pdp.getCurrent(RobotMap.CAN_TALON_DRIVE_RIGHT_FRONT_PDP_PORT));
		// // System.out.println("");
		// // System.out.println("Elevator Motor: " +
		// Robot.pdp.getCurrent(RobotMap.CAN_TALON_ELEVATOR_PDP_PORT));
		// // System.out.println("");

		// @formatter:on
	}
	
	/**
	 * This function is called at the beginning of the robot being disabled
	 */
	public void disabledInit() {
		// turns off camera and closes debug writer after disable
		// TODO: make this work with enable/disabling multiple times
		debugWriter.println("Disabling");
		debugWriter.close();
		
		SmartDashboard.putNumber("Current", pdp.getCurrent(1));
		// NIVision.IMAQdxStopAcquisition(camera_session);
	}
	
	/**
	 * This function is called in teleopPeriodic to print out all of the smartdashboard stuff
	 */
	public void smartDashboardOutputs() {
		SmartDashboard.putNumber("Elevator Encoder", encoderElevator.get());
		SmartDashboard.putNumber("Left Encoder", encoderLeft.get() * -1);
		SmartDashboard.putNumber("Right Encoder", encoderRight.get() * 360.0 / 250.0);
		SmartDashboard.putBoolean("isTote", !toteOptic.get());
		SmartDashboard.putBoolean("Limit Top", !limitTop.get()); // inverts needed
		SmartDashboard.putBoolean("Limit Bottom", !limitBottom.get());
		//SmartDashboard.putBoolean("Hug Piston Closed", hugPiston.get().value == DoubleSolenoid.Value.kForward_val);
		//SmartDashboard.putBoolean("Intake Piston Closed", intakePiston.get().value == DoubleSolenoid.Value.kForward_val);
		SmartDashboard.putNumber("Elevator Current from PDP", pdp.getCurrent(RobotMap.CAN_TALON_ELEVATOR_PDP_PORT));
		SmartDashboard.putNumber("Elevator Current from Talon", canTalonElevator.getOutputCurrent());
		
		SmartDashboard.putNumber("State", RobotMap.currentStateNew);
		
		int count = 0;
		for (String s : RobotMap.stateLog) {
			SmartDashboard.putString(count+" state ", s);
			count++;
		}
		SmartDashboard.putNumber("Last state index", RobotMap.stateIndex);
	}
}
