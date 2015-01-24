package org.usfirst.frc.team668.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class ToteGrabber {
		public static void moveHugPistons(boolean in) {
			if (in) {
				Robot.leftHugPiston.set(DoubleSolenoid.Value.kForward);
				Robot.rightHugPiston.set(DoubleSolenoid.Value.kForward);
			}
			else
				Robot.leftHugPiston.set(DoubleSolenoid.Value.kReverse);
				Robot.rightHugPiston.set(DoubleSolenoid.Value.kReverse);
			}
		}

