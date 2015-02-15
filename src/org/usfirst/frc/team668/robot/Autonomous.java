package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	public static long autoTimer;

	public static void doAuton() { //TODO: different function for each autonomous
		autoTimer = System.currentTimeMillis(); //current time in milliseconds
		switch (RobotMap.autonomousMode) {
			case RobotMap.STOP_AUTONOMOUS:
				break;				
			case RobotMap.DRIVE_FORWARD_AUTONOMOUS:
				if (Math.abs(Robot.encoderLeft.getDistance()) < RobotMap.STOP) {
					Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); //There is literally NO curve and this code doesn't work with a curve
				}
				if (Math.abs(Robot.encoderRight.getDistance()) < RobotMap.STOP) {
					Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); //There is literally NO curve and this code doesn't work with a curve
				}
				break;
			case RobotMap.DELAY_AND_DRIVE_FORWARD_AUTONOMOUS:
				if ((System.currentTimeMillis() - autoTimer) > RobotMap.DELAY_TIME) { // in milliseconds
					if (Math.abs(Robot.encoderLeft.getDistance()) < RobotMap.STOP) {
						Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); //There is literally NO curve and this code doesn't work with a curve
					}
					if (Math.abs(Robot.encoderRight.getDistance()) < RobotMap.STOP) {
						Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); //There is literally NO curve and this code doesn't work with a curve
					}
				}
				break;
			case RobotMap.TOTE_GRAB_AUTONOMOUS: //this takes totes in all the way rather than half-way
				//Elevator init
				if (!Robot.intakePiston.get().equals(DoubleSolenoid.Value.kReverse)) {
					Robot.intakePiston.set(DoubleSolenoid.Value.kReverse);
				}
				boolean finish = Elevator.calibration(-0.8); // TODO: magic number (elevator calibration speed?)
				
				if (finish == true) {
					Elevator.stop();
					ToteGrabber.moveHugPistons(false);
				}
				
				boolean moveFinish = Elevator.move(0.5, RobotMap.ELEVATOR_ENCODER_PICKUP); //should be about 10
				if (moveFinish == true) {
					Elevator.stop();
				}
				//Grab Tote
				Robot.intakePiston.set(DoubleSolenoid.Value.kForward);
				Intake.spin(RobotMap.INTAKE_MOTOR_SPEED);
				while (!Robot.toteOptic.get()) {
					//wait until we have intook the totes
				}
				ToteGrabber.moveHugPistons(true);
				
				//ninety-degree turn
				Robot.robotDrive.drive(RobotMap.AUTONOMOUS_SPEED, RobotMap.AUTONOMOUS_CURVE); //TODO fix
				break;
			
			case RobotMap.BIN_GRAB_AUTONOMOUS:
				
				break;
			
			case RobotMap.TOTE_STACK_AUTONOMOUS:
				
				break;
				
			// TODO: Write new autonomous code
		}
	}

}
