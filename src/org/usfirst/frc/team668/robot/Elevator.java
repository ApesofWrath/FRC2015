package org.usfirst.frc.team668.robot;

public class Elevator {
	
	public static void goUp(double speed, double stop)
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
		if(stop == 0.0)
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
