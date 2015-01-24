package org.usfirst.frc.team668.robot;

public class Elevator {
	private static boolean up, done = true;

	/**
	 * Moves the elevator up at a specified speed to a specified stop. Meant to
	 * move elevator up from anywhere to the stop height provided that stop is
	 * between the two limit switches. It decides to finish if it hits a limit
	 * switch or if it is at or past the encoder value. It returns true when it
	 * decides to finish. Otherwise it returns false.
	 * 
	 * @param speed
	 *            the speed of the elevator from 0.0 to 1.0
	 * @param stop
	 *            the encoder stopping point for the elevator
	 * @return
	 * 		      true if finished moving, false otherwise
	 * 
	 * @param speed	speed of the elevator
	 * @param stop	the distance to the limit
	 */
	public static boolean move(double speed, double stop)
	{
		if (done) {
			direction(stop);
		}

		checkDone(stop);

		if (!done) {
			if (up == true) {
				speed = Math.abs(speed);
			} else {
				speed = -Math.abs(speed);
			}
			Robot.canTalonElevator.set(speed);
		} else {
			Robot.canTalonElevator.set(0.0);
		}
		
		return done;
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
		if (speed > 0) {
			up = true;
		} else if (speed < 0) {
			up = false;
		} else {
			return true;
		}
		
		if (checkDemSwitches()) {
			done = true;
		}

		if (!done) {
			Robot.canTalonElevator.set(speed);
		} else {
			Robot.canTalonElevator.set(0.0);
		}

		return done;
	}

	private static void direction(double stop) {
		if (stop > Robot.encoderElevator.get()) {
			up = true;
			done = false;
		} else if (stop < Robot.encoderElevator.get()) {
			up = false;
			done = false;
		} // if equal we leave done as true
	}

	private static void checkDone(double stop) {
		if (up) {
			if (stop <= Robot.encoderElevator.get()) {
				done = true;
			}
		} else if (!up) {
			if (stop >= Robot.encoderElevator.get()) {
				done = true;
			}
		} else if (checkDemSwitches()) {
			done = true;
		}
	}

	private static boolean checkDemSwitches() {
		if (Robot.limitBottom.get() && !up) {
			Robot.encoderElevator.reset();
			return true;
		} else if (Robot.limitTop.get() && up) {
			return true;
		} else if (Robot.limitBottom.get() && up) {
			Robot.encoderElevator.reset();
		}
		return false;
	}
	
	/**
	 * Stops the elevator.
	 */
	public static void stop() {
		Robot.canTalonElevator.set(0.0);
	}
}
