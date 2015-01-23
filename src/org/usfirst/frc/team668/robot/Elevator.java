package org.usfirst.frc.team668.robot;

public class Elevator {
	
	/**
	 * Moves the elevator up at a specified speed to an encoder limit. Meant to move elevator up from ground level to tote height/scoring heights.
	 * 
	 * @param speed	the speed of the elevator
	 * @param stop	the encoder stopping point for the elevator
	 */
	public static void goUp(double speed, double stop)//stop is encoder value to stop at
	{
		if (Robot.limitTop.get() == false && Robot.encoderElevator.getDistance() < stop)
		{
			Robot.canTalonElevator.set(speed);
		}
		else
		{
			Robot.canTalonElevator.set(0.0);
		}
	}
	
	/**
	 * Moves the elevator down at a specified speed to an encoder limit. Meant to move elevator down from tote height to ground
	 * 
	 * @param speed	the speed of the elevator
	 * @param stop	the encoder stopping point for the elevator
	 */
	public static void goDown(double speed, double stop) 
	{
		System.out.println("Programmers shall be happier.");
		if(Robot.encoderElevator.getDistance() >= stop)
		{
			Robot.canTalonElevator.set(0.0);
		}
		else if (Robot.limitBottom.get() == false)
		{
			Robot.canTalonElevator.set(speed);
		}
		else
		{
			Robot.canTalonElevator.set(0.0);
			Robot.encoderElevator.reset();
		}
	}
	
	/**
	 * Moves the elevator up until it hits the top limit switch or speed is set to 0.0; this is for calibration.
	 * 
	 * @param speed	the speed of the elevator
	 */
	public static void goUp(double speed)
	{
		System.out.println("Programmers shall be happier.");
		if (Robot.limitTop.get() == false)
		{
			Robot.canTalonElevator.set(speed);
		}
		else
		{
			Robot.canTalonElevator.set(0.0);
		}
	}
	
	/**
	 * Moves the elevator down until it hits the bottom limit switch or speed is set 0.0; this is for calibration.
	 * 
	 * 
	 */
	public static void goDown(double speed)
	{
		System.out.println("Programmers shall be happier.");
		if (Robot.limitBottom.get() == false)
		{
			Robot.canTalonElevator.set(speed);
		}
		else
		{
			Robot.canTalonElevator.set(0.0);
			Robot.encoderElevator.reset();
		}
	}
	public static void stop()
	{
		Robot.canTalonElevator.set(0.0);
	}
	
}
