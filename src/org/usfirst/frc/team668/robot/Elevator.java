package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class to move the elevator to encoder values and calibrate encoder based on limit switches
 * 
 * @author The 668 FRC 2015 Programming Team 
 */
public class Elevator {
	private static boolean up, done = true;

	/**
	 * Moves the elevator up at a specified speed to a specified stop. Meant to
	 * move elevator up from anywhere to the stop height provided that stop is
	 * between the two limit switches. It decides to finish if it hits a limit
	 * switch or if it is at or past the encoder value. It returns true when it
	 * decides to finish. Otherwise, it returns false.
	 * 
	 * @param speed
	 *            the speed of the elevator from 0.0 to 1.0
	 * @param stop
	 *            the encoder stopping point for the elevator
	 * @return
	 * 		      true if finished moving, false otherwise
	 */
	public static boolean move(double speed, double stop)
	{
		/*in Robot.java we stop the function if we are done, this makes sure that done is switched to false
		 * in Elevator.java, finally, we find out our direction*/
		if (done) {
			direction(stop);
		}

		//this checks the limit switches and whether we have passed our desired height
		checkDone(stop);

		//this moves the elevator motors in the correct direction
		if (!done) {
			if (up == true) {
				speed = Math.abs(speed);
			} else {
				speed = -Math.abs(speed);
			}
			Robot.canTalonElevator.set(speed);
			Robot.canTalonElevatorTop.set(speed);
		} else {
			Robot.canTalonElevator.set(0.0);
			Robot.canTalonElevatorTop.set(0.0);
		}
		
		return done;
	}
	
	public static boolean moveP(double stop) {
		
		double p = 0.05;
		
		double error = 1 * (Robot.encoderElevator.get() - stop);
		double speed = -1.0 * p * error;
		if (Math.abs(speed) > 1.0) {
			if (speed < 0) {
				speed = -1.0;
			} else if (speed > 0) {
				speed = 1.0;
			}
		}
		
		SmartDashboard.putNumber("current", Robot.encoderElevator.get());
		SmartDashboard.putNumber("desired", stop);
		SmartDashboard.putNumber("error", error);
		SmartDashboard.putNumber("speed", speed);
		
		Robot.canTalonElevator.set(speed);
		Robot.canTalonElevatorTop.set(speed);
		
		if (!Robot.limitTop.get() || (Robot.encoderElevator.get() < RobotMap.ELEVATOR_MIN_ENCODER_VAL && stop < Robot.encoderElevator.get())) {
			Robot.canTalonElevator.set(0.0);
			Robot.canTalonElevatorTop.set(0.0);
			SmartDashboard.putNumber("speed", 0);
			return true;
		} else {
			if (Math.abs(error) <= 1) {
				Robot.canTalonElevator.set(0.0);
				Robot.canTalonElevatorTop.set(0.0);
				SmartDashboard.putNumber("speed", 0);
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Moves the elevator up at a specified speed to the specified limit switch
	 * It decides to finish if it hits a limit switch. Returns true when done.
	 * 
	 * @param speed
	 *            the speed of the elevator
	 * @return
	 * 		      true if finished moving, false otherwise
	 * 
	 */
	public static boolean calibration(double speed) {
		//this code requires proper sign on speed
		if (speed > 0) {
			up = true;
			System.out.println("Calibrating up.");
		} else if (speed < 0) {
			up = false;
			System.out.println("Calibrating down.");
		} else {
			return true;
		}
		
		//we make sure that we aren't hitting our limit switches
		if (checkDemSwitches()) {
			done = true;
		}
		else {
			done = false;
		}

		System.out.println("speed " + speed);
		if (!done) {
			Robot.canTalonElevator.set(speed);
			Robot.canTalonElevatorTop.set(speed);
			
		} else {
			Robot.canTalonElevator.set(0.0);
			Robot.canTalonElevatorTop.set(0.0);
			
		}

		return done;
	}

	//this function finds the direction that we are headed in
	private static void direction(double stop) {
		if (stop > Robot.encoderElevator.get()) {
			up = true;
			done = false;
		} else if (stop < Robot.encoderElevator.get()) {
			up = false;
			done = false;
		} // if equal we leave done as true
	}

	//this function tells us if we are done
	private static void checkDone(double stop) {
		if (up) {
			if (stop <= Robot.encoderElevator.get()) {
				done = true;
			}
		} else if (!up) {
			if (stop >= Robot.encoderElevator.get()) {
				done = true;
			}
		}
		
		if (checkDemSwitches()) {
			done = true;
		}
	}

	/*
	 * this function returns true if we are hitting the limit switches in the proper direction, we also reset
	 * the encoders when at the bottom
	 */
	private static boolean checkDemSwitches() {
		if (!Robot.limitBottom.get() && !up) {
			System.out.println("elevator reset");
			Robot.encoderElevator.reset();
			return true;
		} else if (!Robot.limitTop.get() && up) {
			return true;
		} else if (!Robot.limitBottom.get() && up) {
			System.out.println("elevator reset");
			Robot.encoderElevator.reset();
		}
		/*else if ((Robot.encoderElevator.get() <= -10) && (RobotMap.currentState != RobotMap.INIT_STATE)) {
//			we don't want our elevator to DO STUPID! the init exception is because our encoder starts at
//			zero at the start of the match TODO:check with auton
			System.out.println("THE LIMIT SWITCH IS NOT TRIGGERING!!");
			RobotMap.currentState = RobotMap.MANUAL_OVERRIDE_STATE;
			return true;
		}*/ // we commented this section out because when the encoer is not calibrated it stops the encoder
		
		//added as a soft stop to stop the elevator from going too far down
		if (Robot.encoderElevator.get() < RobotMap.ELEVATOR_MIN_ENCODER_VAL && !up) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Stops the elevator.
	 */
	public static void stop() {
		Robot.canTalonElevator.set(0.0);
		Robot.canTalonElevatorTop.set(0.0);
	}
}
