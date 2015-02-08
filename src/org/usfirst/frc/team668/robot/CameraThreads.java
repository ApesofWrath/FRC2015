package org.usfirst.frc.team668.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.RGBValue;
import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 * The class CameraThreads can run threads for taking and saving pictures to allow us to
 * simultaneously run code and take pictures.
 * 
 * @author FRC Team 668 Programmers
 *
 */
public class CameraThreads {
	
	public static TakePictureThread picture_thread_running;
	public static WritePictureThread write_thread_running;
	
	/** 
	 * Creates a new thread to take pictures in the camera session. Continuously call this
	 * to get the picture once the process has been completed.
	 * 
	 * @param camera_session The session of the camera, obtainable from NIVision.IMAQdxOpenCamera
	 * @return The Image that it took or null if the Image hasn't been taken yet
	 */
	public static Image takePicture(int camera_session) {
		// TODO: Actually make this work.
		try {
			System.out.println("TRYING");
			if (picture_thread_running == null) {
				TakePictureThread tpt = new TakePictureThread(camera_session);
				picture_thread_running = tpt;
				new Thread(tpt).start();
				System.out.println("PICTURETHREADRUNNING NULL");
			} else if (picture_thread_running.picture_taken) {
				Image temp = picture_thread_running.pic;
				picture_thread_running = null;
				System.out.println("NOT NULL");
				return temp;
			}
			System.out.println("CODEZ: " + picture_thread_running);
		}
		catch(VisionException e) {
			System.out.println("CATCHING");
			System.out.println("TAKE PICTURE" + e);
		}
		System.out.println("Sadface Null");
		return null;
	}
	
	/**
	 * Saves the picture at the path in a new thread. Continuously call this until it returns true.
	 * 
	 * @param picture The NIVision Image to be saved (get this using takePicture)
	 * @param path The full path and filename of the image to be written
	 * @return Whether or not the picture has been saved.
	 */
	public static boolean savePicture(Image picture, String path) {
		// TODO: Make this work.
		// TODO: Make it so you don't have to continuously call this
		if (write_thread_running == null) {
			WritePictureThread wpt = new WritePictureThread(picture, path);
			write_thread_running = wpt;
			new Thread(wpt).start();
			return false;
		} else if (write_thread_running.picture_written) {
			write_thread_running = null;
			return true;
		} else {
			return false;
		}
	}
	
	
}

/**
 * This class is a Runnable thread that saves a picture to the roboRIO.
 */
class WritePictureThread implements Runnable {
	
	boolean picture_written;
	Image pic;
	String picture_path;
	
	/**
	 * Constructor for WritePictureThread.
	 * 
	 * @param picture The picture to write, an Image obtainable from takePicture
	 * @param path The path and filename and extension to write it to
	 */
	public WritePictureThread(Image picture, String path) {
		
		picture_written = false;
		pic = picture;
		picture_path = path; 
	
	}
	
	/**
	 * Runs the thread
	 */
	public void run() {
		try {
			NIVision.imaqWriteFile(pic, picture_path, new RGBValue());
			picture_written = true;
		}
		catch (VisionException e) {
			System.out.println("WRITE ERROR " + e);
		}
	}
	
}

/**
 * Class to take pictures in a new thread
 */
class TakePictureThread implements Runnable {
	public Image pic;
	int camera_session;
	boolean picture_taken;
	
	/**
	 * Constructor for runnable TakePictureThread.
	 * 
	 * @param camera_session The camera session, obtainable from NIVision.IMAQdxOpenCamera
	 */
	public TakePictureThread(int camera_session) {
		pic = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		if(pic == null) {
			System.out.println("PIC IS NULL");
		} else {System.out.println("PIC ISNOT NULL");}
		picture_taken = false;
		this.camera_session = camera_session;
		System.out.println("Session: " + camera_session);
		System.out.println("YAY ME GOTS A THREADS !@!#!@#");
	}
	
	/**
	 * Runs the thread containing the camera.
	 */
	public void run() {
		try {
			System.out.println("Trying to run");
			NIVision.IMAQdxStartAcquisition(camera_session);
			System.out.println("A");
			NIVision.IMAQdxGrab(camera_session, pic, 1);
			System.out.println("B");
			NIVision.IMAQdxStopAcquisition(camera_session);
			System.out.println("C");
			picture_taken = true;
		}
		catch (VisionException e) {
			System.out.println("RUN ERROR " + e);
		}
	}
	
}