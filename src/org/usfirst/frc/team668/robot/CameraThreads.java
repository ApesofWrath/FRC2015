package org.usfirst.frc.team668.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.RGBValue;

public class CameraThreads {
	
	public static TakePictureThread picture_thread_running;
	public static WritePictureThread write_thread_running;
	
	public static Image takePicture(int camera_session) {
		
		if (picture_thread_running == null) {
			TakePictureThread tpt = new TakePictureThread(camera_session);
			picture_thread_running = tpt;
			tpt.run();
		} else if (picture_thread_running.picture_taken) {
			Image temp = picture_thread_running.pic;
			picture_thread_running = null;
			return temp;
		}
		
		return null;
	}
	
	public static boolean savePicture(Image picture, String path) {
		
		if (write_thread_running == null) {
			WritePictureThread wpt = new WritePictureThread(picture, path);
			write_thread_running = wpt;
			wpt.run();
			return false;
		} else if (write_thread_running.picture_written) {
			write_thread_running = null;
			return true;
		} else {
			return false;
		}
	}
	
	
}

class WritePictureThread implements Runnable {
	
	boolean picture_written;
	Image pic;
	String picture_path;
	
	public WritePictureThread(Image picture, String path) {
		
		picture_written = false;
		pic = picture;
		picture_path = path; 
	
	}
	
	public void run() {
		NIVision.imaqWriteFile(pic, picture_path, new RGBValue());
		picture_written = true;
	}
	
}

class TakePictureThread implements Runnable {
	public Image pic;
	int camera_session;
	boolean picture_taken;
	
	public TakePictureThread(int camera_session) {
		pic = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		picture_taken = false;
		this.camera_session = camera_session;
	}
	
	public void run() {
		NIVision.IMAQdxStartAcquisition(camera_session);
		NIVision.IMAQdxGrab(camera_session, pic, 1);
		NIVision.IMAQdxStopAcquisition(camera_session);
		picture_taken = true;
	}
	
}