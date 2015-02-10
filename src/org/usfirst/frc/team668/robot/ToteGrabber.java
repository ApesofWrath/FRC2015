package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Class for grabbing totes with the hug pistons
 * 
 * @author The 668 FRC 2015 Programming Team 
 */
public class ToteGrabber {
	
	/**
	 * Opens or closes hug pistons to grab totes based on input
	 * 
	 * @param in	true if closing hug pistons, false if opening
	 */
	public static void moveHugPistons(boolean in) {
		if (in) {
			Robot.hugPiston.set(DoubleSolenoid.Value.kForward);
		} else {
			Robot.hugPiston.set(DoubleSolenoid.Value.kReverse);
		} //end else
	} //end moveHugPistons
	
} //end class ToteGrabber
