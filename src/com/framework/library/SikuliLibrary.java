package com.framework.library;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.sikuli.script.Env;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Location;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.sikuli.script.Settings;
import org.testng.Assert;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/*************************************************************************************************************
 * 
 *    	This library deals with image recognition method to find images on screen
 * 		and perform various operations related to that.
 * 		For usage, create a "SikuliLibrary" variable and use the various functions available
 * 
 * 		It used the tool Sikuli
 * 		(Sikuli is a visual technology to automate and test graphical user interfaces (GUI)
 * 		using images (screenshots))
 * 		It is an open-source research project developed at User Interface Design Group, MIT Computer Science
 * 		and Artificial Intelligence Laboratory (CSAIL)
 * 		
 * 
 *************************************************************************************************************/


public class SikuliLibrary{
	
	private Screen regionToSearch;	// Screen object provided by sikuli to define an area to search
	private Pattern lastPattern;	// store last Pattern after waitForImage base function is called
	private Match lastMatch;		// store last Match after waitForImage base function is called
	
	/**
	 * Constructor
	 */
	public SikuliLibrary()
	{	
		// screen object provided by sikuli to define of region on the screen to search
		regionToSearch = new Screen();
		
		// this is set smooth moves off on start
		Settings.MoveMouseDelay = 0;
	}

	
	/*-------------------------------------------------------------------------------------------------
	 * 
	 * 			SIKULI METHODS - IMAGE RECOGNITION TOOL
	 * 
	 * 
	 *------------------------------------------------------------------------------------------------- 
	 */
	
	/*-------------------------------------------------------------------------------------------------
	 * 
	 * 			MODIFIER FUNCTIONS
	 * 
	 *------------------------------------------------------------------------------------------------- 
	 */

	/**
	 * Enable smooth mouse move animation
	 */
	public void EnableSmoothMoves() {
		setSmoothMoves(true);
	}

	/**
	 * Disable smooth mouse move animation
	 */
	public void DisableSmoothMoves() {
		setSmoothMoves(false);
	}

	/**
	 * Set smooth moves 
	 * @param smoothMovesIsEnabled
	 */
	private void setSmoothMoves(boolean smoothMovesIsEnabled) {
		if (smoothMovesIsEnabled)
			Settings.MoveMouseDelay = 1.0f;
		else
			Settings.MoveMouseDelay = 0;
	}
	
	/**
	 * Set smooth moves for Performance use
	 * @param smoothMovesIsEnabled
	 */
	public void setSmoothMovesPerf(boolean smoothMovesIsEnabled) {
		if (smoothMovesIsEnabled)
			Settings.MoveMouseDelay = 12.0f;
		else
			Settings.MoveMouseDelay = 0;
	}
	
	/**
	 * Checks if mouse smooth moves is on
	 * @return true is smooth moves is on, else returns false
	 */
	private boolean isSmoothMovesOn() {
		if (Settings.MoveMouseDelay > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * Set the search region to the specified rectangle
	 * @param rectangularArea
	 */
	public void setSearchRect(Rectangle rectangularArea)
	{
		regionToSearch.setRect(rectangularArea);
		log("Search region set to : " + rectangularArea.toString());
	}
	
	/**
	 * Set the search region to the bounds formed by two images, will perform an image search first to get images location
	 * @param image1
	 * @param image2
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @return
	 * @throws FindFailed
	 */
	public Rectangle setSearchRect(String image1, String image2, Integer tolerance, Integer timeout) throws FindFailed
	{
		log("In setSearchRect: timeout = [" + timeout + "s]");
		Assert.assertTrue(new File(image1).exists(), "Image not found in file system" + image1);
		Assert.assertTrue(new File(image2).exists(), "Image not found in file system" + image2);
		
		Point p1, p2;
		long time = System.currentTimeMillis();
		try {
			waitForImage(image1, tolerance, timeout);
			p1 = getLocationOnScreen();
			log("Successfully found first image: ["+ image1 + "]");
		} catch (Exception e) {
			return resetSearchRect();
		}
		int time_elapsed = (int)(System.currentTimeMillis() - time)/1000;
		try {
			waitForImage(image2, tolerance, timeout - time_elapsed);
			p2 = getLocationOnScreen();
			log("Successfully found second image : [" + image2 + "]");
		} catch (Exception e) {
			return resetSearchRect();
		}
		
		Rectangle r = new Rectangle(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
		setSearchRect(r);
		return r;
	}
	
	/**
	 * resets the search rectangle to the entire screen
	 * @return Rectangle defining the region
	 */
	public Rectangle resetSearchRect()
	{
		// Default search area (whole screen)
		Rectangle ScreenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		regionToSearch.setRect(ScreenSize);
		
		log("Search region set to : " + ScreenSize.toString());
		return ScreenSize;
	}

	/**
	 * get the point of interest from the imagePath
	 * @param imagePath
	 * @return point from .png.properties file, if no such file will return mid point of the image
	 */
	private Point getSelectionPoint(String imagePath)
	{
		@SuppressWarnings("unused")
		int Height = 0, Width = 0, ClickX = 0, ClickY = 0;
		try
		{
			String str, s[];
			File properties = new File(imagePath + ".properties");
			BufferedReader reader = new BufferedReader(new FileReader(properties));;
		    while((str = reader.readLine()) != null){
		    	s = str.split("=");
		    	if(s[0].contains("height")) { Height = Integer.parseInt(s[1]); }
		        if(s[0].contains("width"))  { Width = Integer.parseInt(s[1]); }
		        if(s[0].contains("clickX")) { ClickX = Integer.parseInt(s[1]); }
		        if(s[0].contains("clickY")) { ClickY = Integer.parseInt(s[1]); }
		    }
		    reader.close();
		}
		catch(IOException e)
		{
			// get mid point of the image in case the properties file is not available
			logError("No properties file available for [" + imagePath + "], will use mid point to perform action");
			try {
				BufferedImage im = ImageIO.read(new File(imagePath));
				ClickX = im.getWidth()/2;
				ClickY = im.getHeight()/2;
				im.flush();
				return new Point(ClickX, ClickY);
			} catch (IOException e1) {
				return new Point(0,0);
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			// This should not happen unless until someone externally modifies the png.properties file for this image
			logError("The properties file for the image is not in proper format, try recapturing the image: " + imagePath);
		}
		return new Point(ClickX, ClickY);
	}
	
	/**
	 * gets all the .png image files from the directory specified
	 * @param directory
	 * @return ArrayList of files
	 * @Exception throws exception if given path is not a valid directory
	 */
	private ArrayList<File> getImagesInDirectory(String directory) throws Exception
	{
		File loc = new File(directory);
		ArrayList<File> files = new ArrayList<File>();
		if (loc.isDirectory()) {
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return (name.endsWith(".png"));
				}
			};
			files.addAll(Arrays.asList(loc.listFiles(filter)));
		}
		else
		{
			throw new Exception("Given location is not a valid directory");
		}
		
		if(files.size() < 1) throw new Exception("I'm sorry, No image files found in [" + directory + "]");
		return files;
	}
	
	/**
	 * Returns the current mouse location
	 * @return
	 */
	public Point getCurrentXY() {
		try {
			Location loc = Env.getMouseLocation();
			return new Point(loc.x, loc.y);
		} catch (Exception e) {
			return new Point(0, 0);
		}
	}
	
	/**
	 * gets the location on the screen, this function is majorly called by all functions which use wait for image
	 * @return Point
	 * @throws Exception (happens if lastPattern or lastMatch is null, which means this function is called when an image find failed)
	 */
	public Point getLocationOnScreen() throws Exception {
		Point selectionPoint = getSelectionPoint(lastPattern.getFilename());
		Point matchLocation = new Point(lastMatch.getX(), lastMatch.getY());
		return new Point(matchLocation.x + selectionPoint.x, matchLocation.y + selectionPoint.y);
	}
	
	/**
	 * gets the location of the last match thats been found on screen
	 * @return Point
	 */
	public Point getLastMatch() {
		try {
			return new Point(lastMatch.getX(), lastMatch.getY());
		} catch (Exception e) {
			return new Point(-1, -1);
		}
	}
	
	/**
	 * gets a string representation of java.awt.Point
	 * @param point
	 * @return String representation
	 */
	private String pointToString(Point point) {
		return "(" + point.x + "," + point.y + ")";
	}
	
	/*-------------------------------------------------------------------------------------------------
	 * 
	 * 			 IMAGE RECOGNITION FUNCTIONS
	 * 
	 *------------------------------------------------------------------------------------------------- 
	 */
	
	/*------------------------------------------------------
	 * 			 VERIFY IMAGE AND ITS VARIANTS
	 *------------------------------------------------------ 
	 */
	
	/**
	 * Waits for the image to appear on screen for the timeout(in seconds) specified, will throw exception if not found
	 * @param imageLocation
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @throws Exception
	 */
	public void verifyImage(String imageLocation, Integer tolerance, Integer timeout) throws Exception
	{
		verifyImage(imageLocation, tolerance, timeout, false);
	}
	
	/**
	 * Waits for the image to appear on screen for the timeout(in seconds) specified, will continue if not found
	 * @param imageLocation
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @return (0 for found, 1 for fail)
	 * @throws Exception
	 */
	public int verifyImageContinueOnFail(String imageLocation, Integer tolerance, Integer timeout) throws Exception
	{
		return verifyImage(imageLocation, tolerance, timeout, true);
	}
	
	/**
	 * Helper function for the above two functions
	 * @param imageName
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @param ContinueOnFail
	 * @return
	 * @throws Exception
	 */
	public int verifyImage(String imageName, Integer tolerance, Integer timeout, boolean ContinueOnFail) throws Exception {
		log("In verifyImage: [" + imageName + "], timeout = [" + timeout.toString() + "s]");
		Assert.assertTrue(new File(imageName).exists(), "Image not found in file system: [" + imageName + "]");
		
		try{
			waitForImage(imageName, tolerance, timeout);
			log("Successfully found the image: [" + imageName + "] at " + pointToString(getLastMatch()));
			return 0;
		}
		catch (Exception e){
			if(!ContinueOnFail)
			{
				log("Failed to find the image: [" + imageName + "]");
				throw e;
			}
				else
				log("Failed to find the image: [" + imageName + "] (will continue)");
		}
		return 1;
	}
	
	/**
	 * searches all the images in the folder to be available on screen
	 * @param location
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @throws Exception
	 */
	public void verifyEveryImage(String location, Integer tolerance, Integer timeout) throws Exception
	{
		ArrayList<File> files = getImagesInDirectory(location);
		for(int i=0; i<files.size(); i++)
		{
			long time = System.currentTimeMillis();
			verifyImage(location, tolerance, timeout);
			long time_elapsed = System.currentTimeMillis() - time;
			timeout -= (int)(time_elapsed/1000);
			if(timeout <= 0)
				throw new Exception("Failed to find all images in the timeout specified");
		}
	}
	
	
	/*------------------------------------------------------
	 * 			 OPPOSITE OF FINDING IMAGE FUNCTIONS
	 *------------------------------------------------------ 
	 */
	
	/**
	 * verifies if the specified image is not present on the screen, will fail if found
	 * @param location
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @throws Exception
	 */
	public void verifyNoImage(String location, Integer tolerance, Integer timeout) throws Exception
	{
		log("In verifyNoImage: [" + location + "], timeout = [" + timeout.toString() + "s]");
		try
		{
			waitForImage(location, tolerance, timeout);
		}
		catch(Exception e)
		{
			log("Image not found: [" + location + "], hence proceeding...");
			return;
		}
		
		throw new Exception("Image found: [" + location + "], hence failing");
	}
	
	/**
	 * Waits until the specified image vanishes from the screen
	 * @param imagePath
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @throws Exception
	 */
	public void waitVanish(String imagePath, Integer tolerance, Integer timeout) throws Exception
	{
		Assert.assertTrue(new File(imagePath).exists(), "Image not found in file system: [" + imagePath + "]");
		Assert.assertTrue(new File(imagePath).isFile(), "Wait vanish cannot be used on directory: [" + imagePath + "]");
		log("In waitVanish: [" + imagePath + "], timeout = [" + timeout.toString() + "s]");
		try
		{
			regionToSearch.setAutoWaitTimeout(timeout);
			Pattern pattern = new Pattern(imagePath).similar((float)tolerance/ 100);
			Assert.assertTrue(regionToSearch.waitVanish(pattern), "Image failed to vanish in the timeout specified: [" + imagePath + "]");
			log("Image disappeared: [" + imagePath + "]");
		}
		catch(Exception e)
		{
			log(e.toString());
		}
	}
	
	
	/*------------------------------------------------------
	 * 			 WAITANDCLICK AND ITS VARIANTS
	 *------------------------------------------------------ 
	 */
	
	/**
	 * searches image and clicks, throws Exception if image not found
	 * @param imageName
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @throws Exception
	 */
	public void waitAndClick(String imageName, Integer tolerance, Integer timeout) throws Exception
	{
		waitAndClick(imageName, tolerance, timeout, false);
	}
	
	/**
	 * searches image and clicks, will continue if failed to find
	 * @param imageName
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @return (0 if found, 1 if failed)
	 * @throws Exception
	 */
	public int waitAndClickContinueOnFail(String imageName, Integer tolerance, Integer timeout) throws Exception
	{
		return waitAndClick(imageName, tolerance, timeout, true);
	}

	/**
	 * Helper function for the above two functions
	 * @param imageName
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @param ContinueOnFail
	 * @return (0 if found, 1 if failed)
	 * @throws Exception
	 */
	public int waitAndClick(String imagePath, Integer tolerance, Integer timeout, boolean ContinueOnFail) throws Exception
	{
		log("In waitAndClick image: [" + imagePath + "], timeout = [" + timeout.toString() + "s]");
		Assert.assertTrue(new File(imagePath).exists(), "Image not found in file system: [" + imagePath + "]");
		
		try {
			Pattern pattern = waitForImage(imagePath, tolerance, timeout);
			Point clickLocation = getLocationOnScreen();
			mouseClick(clickLocation);
			log("Successfully found : [" + pattern.getFilename()
					+ "] at " + pointToString(getLastMatch())
					+ " and clicked at " + pointToString(clickLocation));
			Point coordinates = new Point(0, 0);
			mouseMove(coordinates);
			return 0;
		} catch (Exception e) {
			if (!ContinueOnFail) {
				log("Failed to find the image and click: [" + imagePath
						+ "]");
				throw e;
			} else
				log("Failed to find the image and click: [" + imagePath
						+ "] (will continue)");
		}
		return 1;
	}
	
	/**
	 * Helper function for the above two functions
	 * @param imageName
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @param ContinueOnFail
	 * @return (0 if found, 1 if failed)
	 * @throws Exception
	 */
	public int waitAndRightClick(String imagePath, Integer tolerance, Integer timeout, boolean ContinueOnFail) throws Exception
	{
		log("In waitAndRightClick image: [" + imagePath + "], timeout = [" + timeout.toString() + "s]");
		Assert.assertTrue(new File(imagePath).exists(), "Image not found in file system: [" + imagePath + "]");
		
		try {
			Pattern pattern = waitForImage(imagePath, tolerance, timeout);
			Point clickLocation = getLocationOnScreen();
			mouserightClick(clickLocation);
			log("Successfully found : [" + pattern.getFilename()
					+ "] at " + pointToString(getLastMatch())
					+ " and clicked at " + pointToString(clickLocation));
			return 0;
		} catch (Exception e) {
			if (!ContinueOnFail) {
				log("Failed to find the image and click: [" + imagePath
						+ "]");
				throw e;
			} else
				log("Failed to find the image and click: [" + imagePath
						+ "] (will continue)");
		}
		return 1;
	}
	
	public int waitAndRightClick(Point clickLocation, boolean ContinueOnFail) throws Exception
	{
		log("In waitAndRightClick image: [" +  "], timeout = [" + "s]");
		try {
			mouserightClick(clickLocation);
			log("Successfully found : ["
					+ "] at " + pointToString(getLastMatch())
					+ " and clicked at " + pointToString(clickLocation));
			return 0;
		} catch (Exception e) {
			if (!ContinueOnFail) {
				log("Failed to find the image and click: [" + "]");
				throw e;
			} else
				log("Failed to find the image and click: [" + "] (will continue)");
		}
		return 1;
	}
	public int waitAndClick(Point clickLocation, boolean ContinueOnFail) throws Exception
	{
		log("In waitAndRightClick image: [" +  "], timeout = [" + "s]");
		
		try {
			mouseClick(clickLocation);
			return 0;
		} catch (Exception e) {
			if (!ContinueOnFail) {
				log("Failed to find the image and click: [" + "]");
				throw e;
			} else
				log("Failed to find the image and click: [" + "] (will continue)");
		}
		return 1;
	}
	
	/**
	 * clicks the specified image for the number of clicks specified, throws exception if image not found
	 * @param imagePath
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @param number_of_clicks
	 * @throws Exception
	 */
	public void waitAndMultiClick(String imagePath, Integer tolerance, Integer timeout, int number_of_clicks) throws Exception
	{
		log("In waitAndMultiClick image: [" + imagePath + "], timeout = [" + timeout.toString() + "s]");
		Assert.assertTrue(new File(imagePath).exists(), "Image not found in file system: [" + imagePath + "]");
		Assert.assertTrue(number_of_clicks > 0, "Invalid number of clicks");
		
		try
		{
			Pattern pattern = waitForImage(imagePath, tolerance, timeout);
			Point clickLocation = getLocationOnScreen();
			for(int i=0; i<number_of_clicks; i++)
				mouseClick(clickLocation);
			log("Successfully found : [" + pattern.getFilename() + "] at "+ pointToString(getLastMatch()) + " and clicked [" + number_of_clicks + "] time(s) at " + pointToString(clickLocation));
		}
		catch (Exception e){
			log("Failed to find the image and multi click: [" + imagePath + "]");
			throw e;
		}
	}
	
	/**
	 * Double clicks the image, throws Exception if image not found
	 * @param imagePath
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @throws Exception
	 */
	public void waitAndDoubleClick(String imagePath, Integer tolerance, Integer timeout) throws Exception
	{
		log("In waitAndClick image: [" + imagePath + "], timeout = [" + timeout.toString() + "s]");
		Assert.assertTrue(new File(imagePath).exists(), "Image not found in file system: [" + imagePath + "]");
		
		try {
			Pattern pattern = waitForImage(imagePath, tolerance, timeout);
			Point clickLocation = getLocationOnScreen();
			mouseDoubleClick(clickLocation);
			log("Successfully found : [" + pattern.getFilename()
					+ "] at " + pointToString(getLastMatch())
					+ " and clicked at " + pointToString(clickLocation));
		} catch (Exception e) {
				log("Failed to find the image and double click: [" + imagePath
						+ "]");
				throw e;
		}
	}
	
	/*------------------------------------------------------
	 * 		MOUSE OVER AND VERIFY IMAGE
	 *------------------------------------------------------ 
	 */
	
	/**
	 * mouses over one image and verifies the second image, throws exception if failed to find either images
	 * @param mouseOverImage
	 * @param verifyImage
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @throws Exception
	 */
	public void mouseOverAndVerifyImage(String mouseOverImage, String verifyImage, Integer tolerance, Integer timeout) throws Exception {
		log("In mouseOverAndVerifyImage: timeout = [" + timeout + "s]");
		Assert.assertTrue(new File(mouseOverImage).exists(), "Image not found in file system [" + mouseOverImage + "]");
		Assert.assertTrue(new File(verifyImage).exists(), "Image not found in file system [" + verifyImage + "]");
		
		long time = System.currentTimeMillis();
		try {
			Pattern mouseOver = waitForImage(mouseOverImage, tolerance, timeout);
			Point moveTo = getLocationOnScreen();
			boolean smoothMoves;
			if(!(smoothMoves=isSmoothMovesOn())) EnableSmoothMoves();
			mouseMove(moveTo);
			setSmoothMoves(smoothMoves);
			log("Successfully found image : [" + mouseOver.getFilename() + "] at " + pointToString(getLastMatch()) + " and moused over at " + pointToString(moveTo));
		} catch (Exception e) {
			log("Failed to mouse over image: ["+ mouseOverImage + "]");
			throw e;
		}
		int time_elapsed = (int)(System.currentTimeMillis() - time)/1000;
		try {
			Pattern verify = waitForImage(verifyImage, tolerance, timeout - time_elapsed);
			verify.getFilename();
			log("Successfully found image : [" + verify.getFilename() + "] at " + pointToString(getLastMatch()));
		} catch (Exception e) {
			log("Failed to verify image: ["+ verifyImage + "]");
			throw e;
		}
	}
	
	/*------------------------------------------------------
	 * 		MOUSE OVER AND CLICK IMAGE
	 *------------------------------------------------------ 
	 */
	
	/**
	 * mouses over one image and clicks other image, throws exception if fails to find either of the images
	 * @param mouseOverImage
	 * @param clickImage
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @throws Exception
	 */
	public void mouseOverAndClick(String mouseOverImage, String clickImage, Integer tolerance, Integer timeout) throws Exception {
		log("In mouseOverAndClick: timeout = [" + timeout + "s]");
		Assert.assertTrue(new File(mouseOverImage).exists(), "Image not found in file system [" + mouseOverImage + "]");
		Assert.assertTrue(new File(clickImage).exists(), "Image not found in file system [" + clickImage + "]");
		
		long time = System.currentTimeMillis();
		try {
			Pattern mouseOver = waitForImage(mouseOverImage, tolerance, timeout);
			Point moveTo = getLocationOnScreen();
			boolean smoothMoves = isSmoothMovesOn();
			if(!smoothMoves) EnableSmoothMoves();
			mouseMove(moveTo);
			setSmoothMoves(smoothMoves);
			log("Successfully found image : [" + mouseOver.getFilename() + "] at "+ pointToString(getLastMatch()) + " and moused over at " + pointToString(moveTo));
		} catch (Exception e) {
			log("Failed to mouse over image: ["+ mouseOverImage + "]");
			throw e;
		}
		int time_elapsed = (int)(System.currentTimeMillis() - time)/1000;
		try {
			Pattern click = waitForImage(clickImage, tolerance, timeout - time_elapsed);
			Point clickOn = getLocationOnScreen();
			mouseClick(clickOn);
			log("Successfully found image and clicked : [" + click.getFilename() + "] at "+ pointToString(getLastMatch()) + " and clicked at " + pointToString(clickOn));
		} catch (Exception e) {
			log("Failed to verify image: ["+ clickImage + "]");
			throw e;
		}
	}
	
	public void mouseOverAndMultiClick(String mouseOverImage, String clickImage, Integer tolerance, Integer timeout, int number_of_clicks) throws Exception {
		log("In mouseOverAndMultiClick: timeout = [" + timeout + "s]");
		Assert.assertTrue(new File(mouseOverImage).exists(), "Image not found in file system [" + mouseOverImage + "]");
		Assert.assertTrue(new File(clickImage).exists(), "Image not found in file system [" + clickImage + "]");
		
		long time = System.currentTimeMillis();
		try {
			Pattern mouseOver = waitForImage(mouseOverImage, tolerance, timeout);
			Point moveTo = getLocationOnScreen();
			boolean smoothMoves = isSmoothMovesOn();
			if(!smoothMoves) EnableSmoothMoves();
			mouseMove(moveTo);
			setSmoothMoves(smoothMoves);
			log("Successfully found image : [" + mouseOver.getFilename() + "] at "+ pointToString(getLastMatch()) + " and moused over at " + pointToString(moveTo));
		} catch (Exception e) {
			log("Failed to mouse over image: ["+ mouseOverImage + "]");
			throw e;
		}
		int time_elapsed = (int)(System.currentTimeMillis() - time)/1000;
		try {
			Pattern click = waitForImage(clickImage, tolerance, timeout - time_elapsed);
			Point clickOn = getLocationOnScreen();
			for(int i=0; i<number_of_clicks; i++)
				mouseClick(clickOn);
			log("Successfully found image and clicked : [" + click.getFilename() + "] at "+ pointToString(getLastMatch()) + " and clicked at " + pointToString(clickOn));
		} catch (Exception e) {
			log("Failed to verify image: ["+ clickImage + "]");
			throw e;
		}
	}
	
	/**
	 * mouse over the specified image, throws Exception if fails to find the image
	 * @param mouseOverImage
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @throws Exception
	 */
	public void mouseOver(String mouseOverImage, Integer tolerance, Integer timeout) throws Exception{
		log("In mouseOver: timeout = [" + timeout + "s]");
		Assert.assertTrue(new File(mouseOverImage).exists(), "Image not found in file system [" + mouseOverImage + "]");
		
		try {
			Pattern mouseOver = waitForImage(mouseOverImage, tolerance, timeout);
			Point moveTo = getLocationOnScreen();
			boolean smoothMoves = isSmoothMovesOn();
			if(!smoothMoves) EnableSmoothMoves();
			mouseMove(moveTo);
			setSmoothMoves(smoothMoves);
			log("Successfully found image : [" + mouseOver.getFilename() + "] at "+ pointToString(getLastMatch()) + " and moused over at " + pointToString(moveTo));
		} catch (Exception e) {
			log("Failed to mouse over image: ["+ mouseOverImage + "]");
			throw e;
		}
	}
	
	
	/*------------------------------------------------------
	 * 		DRAG AND DROP
	 *------------------------------------------------------ 
	 */
	
	/**
	 * Drags from one image and drops on other image, throws exception if fails to find either of the images
	 * @param clickImage
	 * @param dropImage
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @throws Exception
	 */
	public void dragAndDrop(String clickImage, String dropImage, Integer tolerance, Integer timeout) throws Exception {
		log("In dragAndDrop: timeout = [" + timeout + "s]");
		Assert.assertTrue(new File(clickImage).exists(), "Image not found in file system [" + clickImage + "]");
		Assert.assertTrue(new File(dropImage).exists(), "Image not found in file system [" + dropImage + "]");
		
		Pattern click;
		long time = System.currentTimeMillis();
		try {
			click = waitForImage(clickImage, tolerance, timeout);
		} catch (Exception e) {
			log("Failed to find the drag image: ["+ clickImage + "]");
			throw e;
		}
		int time_elapsed = (int)(System.currentTimeMillis() - time)/1000;
		try {
			Point dragStartLocation = getSelectionPoint(click.getFilename());
			String dragLocationString = pointToString(getLocationOnScreen());
			Pattern drop = waitForImage(dropImage, tolerance, timeout - time_elapsed);
			Point dropLocation = getSelectionPoint(drop.getFilename());
			boolean smoothMoves = isSmoothMovesOn();
			if(!smoothMoves) EnableSmoothMoves();
			regionToSearch.dragDrop(click.targetOffset(dragStartLocation.x, dragStartLocation.y), drop.targetOffset(dropLocation.x, dropLocation.y), 0);
			setSmoothMoves(smoothMoves);
			log("Successfully dragged from " + dragLocationString + " and dropped at " + pointToString(getLocationOnScreen()));
		} catch (Exception e) {
			log("Failed to drag and drop on image: ["+ dropImage + "]");
			throw e;
		}
	}
	
	/**
	 * Drags from one Point and drops on other Point
	 * @param P1
	 * @param P2
	 * @throws Exception
	 */
	public void dragAndDrop(Point P1, Point P2) throws Exception {
		log("In dragAndDrop: ");
		Location click = new Location(P1.x, P1.y);
		Location drop = new Location(P2.x, P2.y);
		regionToSearch.dragDrop(click, drop);
	}
	
	/*--------------------------------------------------------------------------------------------
	 * 		CORE IMAGE RECOGNITION FUNCTION (FOR ABSOLUTE FILE/FOLDER)
	 *--------------------------------------------------------------------------------------------
	 */
	
	/**
	 * Base image search function, throws Exception if image search returns no match
	 * @param imagePath
	 * @param tolerance (Variation: 0 = 0% match  to 100 = 100% match)
	 * @param timeout (in seconds)
	 * @return org.sikuli.Pattern (returns a pattern object, which can be used to perform operations)
	 * @throws Exception
	 */
	public Pattern waitForImage(String imagePath, Integer tolerance, Integer timeout) throws Exception
	{
		lastMatch = null;
		lastPattern = null;
		
		File loc = new File(imagePath);
		Assert.assertTrue(loc.exists(), "Image not found in file system: [" + imagePath + "]");
		Assert.assertTrue(timeout >= 0, "Already timed out");
		
		// getting the float value of tolerance (sikuli takes float values)
		float tol = (float)tolerance/100; // (0.0 - 1.0  =  0% match - 100% match)
		
		/*
		 * If imagePath is a direct .png file
		 */
		if (loc.isFile()) {
			
			// setting the timeout for which image needs to be searched
			regionToSearch.setAutoWaitTimeout(timeout);
			
			try {
				
				// getting a pattern with the required tolerance
				Pattern pattern = new Pattern(loc.getAbsolutePath()).similar(tol);
				
				// waiting for the pattern to appear in the region of search, this function throws FindFailed exception
				lastMatch = regionToSearch.wait(pattern);
				
				// assign it to lastPattern(assuming image is found and comes to this step) 
				lastPattern = pattern;
				
				// return the pattern as it is found
				return pattern;
			} catch (Exception e) {
				
				lastMatch = null;
				lastPattern = null;
				throw new FindFailed("Failed to find image: [" + imagePath + "]");
			}
		}
		
		/*
		 *  If image is not a direct .png image, instead a folder with .png files is specified
		 */
		
		// get the files with .png extension in the directory specified
		ArrayList<File> files = getImagesInDirectory(imagePath);
		
		// create pattern objects from the files that are obtained from the directory
		ArrayList<Pattern> patterns = new ArrayList<Pattern>();
		for(File file:files)
			patterns.add(new Pattern(file.getAbsolutePath()).similar(tol));
		
		// setting the timeout to 0.3, this will make the image to be searched once and proceed to next image
		regionToSearch.setAutoWaitTimeout(0.3);
		
		// note the time when the search starts
		double t1 = System.currentTimeMillis();
		
		// we are iterating through the images for searching (polling fashion, cant seem to get a better method here)
		while ((System.currentTimeMillis() - t1) / 1000 < timeout) {
			
			// iteration over the list of pattern objects
			for (Pattern pattern : patterns) {
				try {
					
					// wait for the particular pattern (will throw FindFailed here if not found)
					lastMatch = regionToSearch.wait(pattern);
					
					// will come here if exception is not thrown and will exit out of function returning the pattern
					lastPattern = pattern;
					return pattern;
				}
				catch (Exception e) {
					// catching the exception to continue search for all images
					lastMatch = null;
					lastPattern = null;
				}
			}
		}
		
		// This statement is reached if after searching all the files and no match is found
		throw new FindFailed("Failed to find image: [" + imagePath + "]");
	}
	
	
	
	/*------------------------------------------------------
	 * 		LOW LEVEL MOUSE EVENTS
	 *------------------------------------------------------ 
	 */
	
	/**
	 * move mouse to the location specified
	 * @param point
	 * @throws FindFailed (I don't know how this will happen)
	 */
	public void mouseMove(Point point) throws FindFailed
	{
		log("In mouseMove: ");
		regionToSearch.mouseMove(new Location(point.x, point.y));
	}
	
	/**
	 * mouse click at the location specified
	 * @param point
	 * @throws Exception (I don't know how this will happen either)
	 */
	public void mouseClick(Point point) throws Exception
	{
		regionToSearch.click(new Location(point.x, point.y));
	}
	public void mouserightClick(Point point) throws Exception
	{
		regionToSearch.rightClick(new Location(point.x, point.y));
	}
	
	/**
	 * mouse double click at the location specified
	 * @param point
	 * @throws Exception (You know the drill)
	 */
	public void mouseDoubleClick(Point point) throws Exception
	{
		regionToSearch.doubleClick(new Location(point.x, point.y));
	}
	
	/**
	 * mouse SCROLL at the location specified
	 * @param point
	 * @throws Exception (You know the drill)
	 */
	public void mouseScroll(Point point, int SCROLLCNT) throws Exception
	{
		regionToSearch.wheel(new Location(point.x, point.y), org.sikuli.script.Button.WHEEL_DOWN, SCROLLCNT);
		
	}
	
	/**
	 * Simple wait for the timeout(in secs) specified
	 * @param timeout (in seconds)
	 */
	public void wait(int timeout)
	{
		try {
			log("Waiting for [" + timeout + "] secs");
			Thread.sleep(timeout*1000);
		} catch (InterruptedException e) {
			logError("wait failed...some one has done something weird");
			e.printStackTrace();
		}
	}
	
	

	/*------------------------------------------------------
	 * 		KEY EVENTS
	 *------------------------------------------------------ 
	 */
	
	/**
	 * Type text or special keys (In case of text pass a string variable, if it is a key pass org.sikuli.Key variable)
	 * @param String/Key
	 * @throws FindFailed
	 */
	public void pressKey(String key) throws FindFailed
	{
		regionToSearch.type(key);
		log("Succesfully entered keys " + key);
	}
	
	public void pressKey(String key, int modifiers) throws FindFailed
	{
		regionToSearch.type(key, modifiers);
		log("Press key called with Modifiers...");
	}


	private void log(String string) {
		System.out.println("" + string);
		
	}
	private void logError(String string) {
		System.out.println("Error " + string);
		
	}

	
	
}
