package camera2015;

import org.usfirst.frc.team668.robot.CameraThreads;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.*;

public class USBCamera {
	
	private int camera_session;
	private Image picture;
	
	public USBCamera() {
		camera_session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(camera_session);
	}
	
	public boolean isFreshImage() {
		return picture != null;
	}
	
	public void clearImage() {
		picture = null;
	}
	
	public void on() {
		NIVision.IMAQdxStartAcquisition(camera_session);
	}
	
	public void off() {
		NIVision.IMAQdxStopAcquisition(camera_session);
	}
	
	public Image takePicture() {
		System.out.println("Trying to take");
		picture = CameraThreads.takePicture(camera_session);
		return picture;
	}
	
	public boolean savePicture(String path) {
		if(isFreshImage()) {
			return CameraThreads.savePicture(picture, path);
		}
		return false;
	}
	
	public boolean savePicture(Image pic, String path) {
		picture = pic;
		return savePicture(path); // This is the other savePicture (so this is not recursion)
	}
	
	public boolean takeAndSavePicture(String path) {
		return savePicture(takePicture(), path);
	}
}