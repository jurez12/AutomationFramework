package com.framework.library.utils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.framework.core.Global;



public class MiscUtils {
	static final Logger log = Logger.getLogger(MiscUtils.class);
	
	private static String[] unitdo ={"", " ONE", " TWO", " THREE", " FOUR", " FIVE",
        " SIX", " SEVEN", " EIGHT", " NINE", " TEN", " ELEVEN", " TWELVE",
        " THIRTEEN", " FOURTEEN", " FIFTEEN",  " SIXTEEN", " SEVENTEEN", 
        " EIGHTEEN", " NINETEEN"};
	private static  String[] tens =  {"", "TEN", " TWENTY", " THIRTY", " FORTY", " FIFTY",
	        " SIXTY", " SEVENTY", " EIGHTY", " NINETY"};
	private static String[] digit = {"", " HUNDRED", " THOUSAND", " LAKH", " CRORE"};

	
	   public static int screenX;
	    public static int screenY;
	    
	    public static boolean empty(String str) {
	        return str == null || str.trim().isEmpty();
	    }
	    
	    
	    public static void creatImagesByTest(String text) {
	  	    System.out.println("Creating an image!");
	  		
	  		int width = 250;
	  		int height = 250;

	  		//create a BufferedImage for mentioned image types.
	  		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	  		//create a graphics2d object which can be used to draw into the buffered image
	  		Graphics2D g2d = buffImg.createGraphics();
	  		
	  		//fill the rectangle with grey color
	  		g2d.setColor(Color.GRAY);
	  		g2d.fillRect(0, 0, width, height);
	  		
	  		//draw a string
	  		g2d.setColor(Color.yellow);
	  		g2d.setFont(new Font("TimesRoman", Font.BOLD, 40)); 
	  		g2d.drawString(text, 20, 100);
	  		
	  		//disposes of this graphics context and releases any system resources that it is using
	  		g2d.dispose();
	  		
	  		//write the image file
	  		File f = new File(Global.USER_DIR +"/icon.jpg");
	  		try {
	  			ImageIO.write(buffImg, "jpg", f);
	  		} catch (IOException e) {
	  			e.printStackTrace();
	  		}
	  		
	  		System.out.println(f.getAbsolutePath()+" created successfully!");
	    }

	    public boolean captureScreen(String dir, String fileName, int x, int y) throws Exception {
	        log.debug("Capturing Screenshot to [" + fileName + "] in directory [" + dir + "]");
	        log.debug("User action performed at: " + x + ", " + y);
	        File dirFile = new File(dir);
	        dirFile.mkdirs();
	        File file = new File(dirFile, fileName);
	        
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        Rectangle screenRectangle = new Rectangle(screenSize);
	        Robot robot = new Robot();
	        BufferedImage image = robot.createScreenCapture(screenRectangle);
	        
	        if(screenX != 0 && screenY != 0) {
	            Graphics2D g2d = image.createGraphics();
	            //For the black circle
	            Ellipse2D e = new Ellipse2D.Double(x-30, y-30, 60, 60);
	            //For the red circle
	            Ellipse2D e1 = new Ellipse2D.Double(x-20, y-20, 40, 40);
	            //outer black circle
	            g2d.setPaint(Color.black);
	            g2d.setStroke(new BasicStroke(8));
	            g2d.draw(e);
	            //inner red circle
	            g2d.setPaint(Color.red);
	            g2d.setStroke(new BasicStroke(8));
	            g2d.draw(e1);
	            g2d.dispose();
	            
	        }
	        return ImageIO.write(image, "png", file);
	    }
	    
	    public boolean captureScreen(String dir, String fileName) throws Exception {
	        log.debug("Capturing Screenshot to [" + fileName + "] in directory [" + dir + "]");
	        File dirFile = new File(dir);
	        dirFile.mkdirs();
	        File file = new File(dirFile, fileName);
	        
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        Rectangle screenRectangle = new Rectangle(screenSize);
	        Robot robot = new Robot();
	        BufferedImage image = robot.createScreenCapture(screenRectangle);
	        return ImageIO.write(image, "png", file);
	    }
	
	public static Date getDateWithoutTimeComponent(Date date) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+530"));
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date dateWithOutTimeComponent = cal.getTime();
		return dateWithOutTimeComponent;
	}
	
	
	public static String getAmountFormatedString(double amount) {
		MessageFormat mf = new MessageFormat("{0, number,#,##0.00}");
		Object[] objs = {new Double(amount)};
		String result = mf.format(objs);
		return result;
	}
	
	//Count the number of digits in the input number
	private static int numberCount(int num) {
		int cnt = 0;
		while (num > 0) {
			cnt++;
			num = num / 10;
		}
		return cnt;
	}
	
	//Function for Conversion of two digit
	private static String twonum(int numq) {
		int numr, nq;
		String word = "";
		nq = numq / 10;
		numr = numq % 10;
		if (numq > 19) {
			word=word + tens[nq] + unitdo[numr];
		}
		else {
			word = word + unitdo[numq];
		}
		return word;
	}
	//Function for Conversion of three digit
	private static String threenum(int numq) {
		int numr, nq;
		String word = "";
		nq = numq / 100;
		numr = numq % 100;
		if (numr == 0) {
			word = word + unitdo[nq] + digit[1];
		}
		else {
			word = word + unitdo[nq] + digit[1] + " AND" + twonum(numr);
		}
		return word;
	}
	
	public static String getNumberToWordString(double amount) {
		int len, q = 0, r = 0;
		String word = " ";
		String numberToWordStr = "RUPEES";
		String paise [] = getAmountFormatedString(amount).split("[.]");
		   
		int num = (int) amount;
		if (num <= 0) return "";
		
		while (num > 0) {
			len = numberCount(num);
			//Take the length of the number and do letter conversion
			switch (len) {
				case 8:
					q = num / 10000000;
					r = num % 10000000;
					word = twonum(q);
					numberToWordStr = numberToWordStr + word + digit[4];
					num = r;
					break;
				
				case 7:
				case 6:
					q = num / 100000;
					r=num % 100000;
					word = twonum(q);
					numberToWordStr = numberToWordStr + word + digit[3];
					num = r;
					break;
				
				case 5:
				case 4:
					q = num / 1000;
					r = num % 1000;
					word = twonum(q);
					numberToWordStr= numberToWordStr + word + digit[2];
					num = r;
					break;
				
				case 3:
					if (len == 3) {
					r = num;
					}
					word = threenum(r);
					numberToWordStr = numberToWordStr + word;
					num = 0;
					break;
				
				case 2:
					word = twonum(num);
					numberToWordStr = numberToWordStr + word;
					num=0;
					break;
				
				case 1:
					numberToWordStr = numberToWordStr + unitdo[num];
					num=0;
					break;
					
				default:
					num=0;
					numberToWordStr = "";
					log.error("Exceeding Crore....No conversion");
			}
			if (num == 0) {
				if (paise[1].equals("00")) {
					numberToWordStr = numberToWordStr + " ONLY";
				} else {
					numberToWordStr = numberToWordStr + " AND " +  paise[1] + " PAISA ONLY";
				}
				return numberToWordStr;
			}
		}
		return numberToWordStr;
	}
	
	/**
	 * for a given word it will change the case of the first character to other case
	 * i.e, upperCase to lowerCase or viceversa.
	 * 
	 * @return
	 * <li> for ex: if the given word is "abc", then it will returns "Abc"
	 * <li> for ex: if the given word is "Abc", then it will returns "abc"
	 */
	public static String getWordStartingWithOtherCase(String word) {
		if (word.isEmpty()) {
			return word;
		}

		Character firstChar = word.charAt(0);
		if ((firstChar >= 'a' && firstChar <= 'z') || (firstChar >= 'A' && firstChar <= 'Z')) {

			if (Character.isLowerCase(firstChar)) {
				word = Character.toUpperCase(firstChar) + word.substring(1, word.length());
			} else if (Character.isUpperCase(firstChar)) {
				word = Character.toLowerCase(firstChar) + word.substring(1, word.length());
			}
		}
		return word;
	}

	/**
	 * for a given word it will returns the word with lowerCase
	 * 
	 * @return
	 * <li> for ex: if the given word is "abc", then it will returns "abc"
	 * <li> for ex: if the given word is "ABC", then it will returns "abc"
	 */
	public static String getWordWithLowerCase(String word) {
		if (word.isEmpty()) {
			return word;
		}

		Character firstChar = word.charAt(0);
		if ((firstChar >= 'a' && firstChar <= 'z') || (firstChar >= 'A' && firstChar <= 'Z')) {

			String newWord = word.toLowerCase();
			return newWord;
		}
		return word;
	}

	/**
	 * for a given word it will returns the word with upperCase
	 * 
	 * @return
	 * <li> for ex: if the given word is "abc", then it will returns "ABC"
	 * <li> for ex: if the given word is "ABC", then it will returns "ABC"
	 */
	public static String getWordWithUpperCase(String word) {
		if (word.isEmpty()) {
			return word;
		}

		Character firstChar = word.charAt(0);
		if ((firstChar >= 'a' && firstChar <= 'z') || (firstChar >= 'A' && firstChar <= 'Z')) {

			String newWord = word.toUpperCase();
			return newWord;
		}
		return word;
	}

	public static Date getDateWith24hrTimeComponent(Date date) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+530"));
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		Date dateWith24hrTimeComponent = cal.getTime();
		return dateWith24hrTimeComponent;
	}
	
   public boolean validateDate(Date date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.setLenient(false);
			String dateString =  sdf.format(date);
			sdf.parse(dateString);
		} catch (ParseException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	public boolean validateDate(Date fromDate, Date thruDate){
		if(fromDate != null && thruDate != null){
			if(validateDate(fromDate) && validateDate(thruDate)){
				return fromDate.before(thruDate);
			}
			return false;
		} 
		
		if(fromDate != null)
			return validateDate(fromDate);
		return false;
	}
	
    public boolean validateEmail(String partyEmail) {
		final String EMAIL_PATTERN = "^[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*@gmail.com$";
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(partyEmail);
		return matcher.matches();
	}	
    
	public static String convertSecondToHHMMString(int secondtTime) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(tz);
		String time = df.format(new Date(secondtTime*1000L));
		return time;
	}
	
	public static String arrayToString(String[] array) {
		StringBuffer result = new StringBuffer("");
		for (String element : array) {
			result.append(element).append(" ");
		}
		return result.toString();
	}
	
	public static String capitalizeFirstLetter(String original){
	    if(original.length() == 0)
	        return original;
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
	
	
	public static String getString(String str) {
		if(str.length() == 0 ) {
			return str;
		}  else if (str.length() < 100){ 
			return str;
		} else {
			return (String) str.subSequence(0, 100);
		}
	}
	public static List<String> getLinksFromContent(String content) {
	     List<String> resultList = new ArrayList<String>();
	        Pattern pattern = Pattern.compile(
	            "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" + 
	            "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" + 
	            "|mil|biz|info|mobi|name|aero|jobs|museum" + 
	            "|travel|[a-z]{2}))(:[\\d]{1,5})?" + 
	            "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" + 
	            "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + 
	            "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" + 
	            "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" + 
	            "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" + 
	            "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");

	        Matcher matcher = pattern.matcher(content);
	        while (matcher.find()) {
	            resultList.add(matcher.group());
	        }
		return resultList;
	}
	
	public String getGALinks(String trackResults){
		   Pattern  p = Pattern.compile ("http.*://www.google-analytics.com.*\\S");
		   Matcher m = p.matcher (trackResults);
		   String gaResults=" ";
		     while (m.find ())
		      {
		    	 gaResults= gaResults + m.group (); 
		      } 
		      return gaResults;
	}

	public List<String> getGALinkList(String trackResults) {
	     List<String> resultList = new ArrayList<String>();
	     Pattern  p = Pattern.compile ("http.*://www.google-analytics.com.*\\S");
		   Matcher m = p.matcher (trackResults);
		      while (m.find ())
		      {
		    	  resultList.add(m.group());
		      }
	     return resultList; 
	}

	
	public static String get_match(String s, String p) {
	    // returns first match of p in s for first group in regular expression 
	    Matcher m = Pattern.compile(p).matcher(s);
	    return m.find() ? m.group(1) : "";
	}

	

	public static List<String> get_matches(String s, String p) {
	    // returns all matches of p in s for first group in regular expression 
	    List<String> matches = new ArrayList<String>();
	    Matcher m = Pattern.compile(p).matcher(s);
	    while(m.find()) {
	        matches.add(m.group(1));
	    }
	    return matches;
	}
}
