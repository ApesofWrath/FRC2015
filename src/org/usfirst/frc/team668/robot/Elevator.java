package org.usfirst.frc.team668.robot;

public class Elevator {
	
	/**
	 * moves elevator until it gets to the top, bottom, or the encoder value we want the elevator to stop
	 * 
	 * @param speed	speed of the elevator
	 * @param stop	the distance to the limit
	 */
	public static void goUp(double speed, double stop)//stop is encoder value to stop at
	{
		System.out.println("Programmers shall be happier.");
		if (Robot.limitTop.get() == false && Robot.encoderElevator.getDistance() < stop)
		{
			Robot.canTalonElevator.set(speed);
		}
		else
		{
			Robot.canTalonElevator.set(0.0);
		}
	}
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
