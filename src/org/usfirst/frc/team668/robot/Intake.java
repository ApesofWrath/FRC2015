package org.usfirst.frc.team668.robot;

/**
 * Class to spin and stop the intake motors
 * 
 * @author The 668 FRC 2015 Programming Team
 */
public class Intake {
	/**
	 * Spins the intake motors in the desired direction at the desired speed
	 * 
	 * @param speed
	 *            the speed to spin from -1.0 to 1.0; positive is intake, negative is outtake
	 */
	public static void spin(double speed) {
		Robot.canTalonIntakeLeft.set(speed);
		Robot.canTalonIntakeRight.set(-speed);
		
	}
	
	/**
	 * Stops the intake motors
	 */
	public static void stop() {
		Robot.canTalonIntakeLeft.set(0);
		Robot.canTalonIntakeRight.set(0);
	}
	
	/**
	 * Spins the intake motors in the same direction in order to push away bins during the spin away tote autonomous.
	 * 
	 * @param speed
	 * 		the speed to spin from -1.0 to 1.0; positive is clockwise, negative is counterclockwise
	 */
	public static void spinAway(double speed) {
		//this is for the spin away tote autonomous as it requires that the two intake motors turn in the same direction
		Robot.canTalonIntakeLeft.set(speed);
		Robot.canTalonIntakeRight.set(speed);
		
	}
}
