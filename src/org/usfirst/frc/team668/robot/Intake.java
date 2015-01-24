package org.usfirst.frc.team668.robot;

public class Intake {
	public static void spin(double speed) {
		Robot.canTalonIntakeLeft.set(speed);
		Robot.canTalonIntakeRight.set(-speed);
	
		
	}
	public static void stop() {
		Robot.canTalonIntakeLeft.set(0);
		Robot.canTalonIntakeRight.set(0);
	}
}
