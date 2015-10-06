package com.framework.helper;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.framework.core.Global;


public class Scheduler {

	/**
	 * @Author : Suresh
     * Desc: addScheduler add the scheduler, test name of the test and details is information for this scheduler.
    */
	public void addScheduler(String test, String details) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		
        Document document = documentBuilder.parse(getPath());
        Element root = document.getDocumentElement();

        Element rootElement = document.getDocumentElement();

        Element subRoot = document.createElement("Schedule");
        Calendar cal = Calendar.getInstance(); 
        cal.add(Calendar.MINUTE, Global.SCHEDULER_EXPIRE_TIME_DELAY);
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        int date = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        
        String expireTime =  hour + " " + minute + " " + seconds + " " + date + " " + month + " " + year;
        
        subRoot.setAttribute("id", expireTime);
        rootElement.appendChild(subRoot);
        
        Element expireTimeElement = document.createElement("expireTime");
        expireTimeElement.appendChild(document.createTextNode(expireTime));
        subRoot.appendChild(expireTimeElement);
        
        Element postedElement = document.createElement("test");
        postedElement.appendChild(document.createTextNode(test));
        subRoot.appendChild(postedElement);
        
        Element detailsElement = document.createElement("details");
        detailsElement.appendChild(document.createTextNode(details));
        subRoot.appendChild(detailsElement);
        
        root.appendChild(subRoot);

        DOMSource source = new DOMSource(document);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(getPath());
        transformer.transform(source, result);
	}

	/**
	 * @Author : Suresh
     * Desc: getPath is used to get the scheduler file from project, so this solves path issue.
    */
	private String getPath() throws IOException {
		String parentPath =getClass().getResource("")
			.getPath().replace("/bin/com/slando/webdriver/helper/", "");
        parentPath = parentPath.substring(1, parentPath.length());
        parentPath += Global.SCHEDULER_FILE;
		return URLDecoder.decode(parentPath, "UTF-8");
	}
	
	/**
	 * @Author : Suresh
	 * @throws Exception
	 * @return Return Expired(Matured) schedule Id. 
	 */
	public String checkScheduler() throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(getPath());
        doc.getDocumentElement ().normalize ();
        NodeList listOfSchedule = doc.getElementsByTagName("Schedule");
        int totalPost = listOfSchedule.getLength();
        String scheduleId = null;
        for(int i = 0; i < totalPost; i++) {	
            Node scheduleNode = listOfSchedule.item(i);
            if(scheduleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element firstScheduleElement = (Element) scheduleNode;
                scheduleId = firstScheduleElement.getAttribute("id");
                NodeList expireTimeList = firstScheduleElement.getElementsByTagName("expireTime");
                Element expireTimeElement = (Element)expireTimeList.item(0);
                if (!expireTimeElement.getTextContent().equals("")) {
                	String calenderList[] = expireTimeElement.getTextContent().split(" ");
                	int hour = Integer.parseInt(calenderList[0]);
                	int minute = Integer.parseInt(calenderList[1]);
                	int seconds = Integer.parseInt(calenderList[2]);
                	int date = Integer.parseInt(calenderList[3]);
                	int month = Integer.parseInt(calenderList[4]);
                	int year = Integer.parseInt(calenderList[5]);
                	Calendar expireCal = Calendar.getInstance(); 
                	expireCal.set(Calendar.HOUR, hour);
                    expireCal.set(Calendar.MINUTE, minute);
                    expireCal.set(Calendar.SECOND, seconds);
                    expireCal.set(Calendar.DATE, date);
                    expireCal.set(Calendar.MONTH, month);
                    expireCal.set(Calendar.YEAR, year);
                    Date expireDate = expireCal.getTime();
                    Date currentDate = new Date();
                    if (currentDate.compareTo(expireDate) >= 0) {
                          return scheduleId;
                    }
                }
            }
        }
		return null;
	}
	
	/**
	 * @Author : Suresh
	 * Desc: deleteScheduler is used to delete scheduler based on scheduledId.
	 */
	public void deleteScheduler(String deleteScheduleId) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(getPath());
        doc.getDocumentElement ().normalize ();
        NodeList listOfSchedule = doc.getElementsByTagName("Schedule");
        int totalSchedule = listOfSchedule.getLength();
        for(int i = 0; i < totalSchedule; i++) {	
            Node scheduleNode = listOfSchedule.item(i);
            if(scheduleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element scheduleElement = (Element) scheduleNode;
                String scheduleId = scheduleElement.getAttribute("id");
                if (scheduleId.equals(deleteScheduleId)) {
                	scheduleElement.getParentNode().removeChild(scheduleElement);
                	break;
                }
            }
        }
        DOMSource source = new DOMSource(doc);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(getPath());
        transformer.transform(source, result);
	}
	
	/**
	 * @Author : Suresh
	 * Desc: getDetails is used to get details node.
	 */
	public String getDetails(String searchScheduleId) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(getPath());
        doc.getDocumentElement ().normalize ();
        NodeList listOfSchedule = doc.getElementsByTagName("Schedule");
        int totalSchedule = listOfSchedule.getLength();
        for(int i = 0; i < totalSchedule; i++) {	
            Node scheduleNode = listOfSchedule.item(i);
            if(scheduleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element scheduleElement = (Element) scheduleNode;
                String scheduleId = scheduleElement.getAttribute("id");
                if (scheduleId.equals(searchScheduleId)) {
                    NodeList detailsList = scheduleElement.getElementsByTagName("details");
                    Element detailsElement = (Element)detailsList.item(0);
                    return detailsElement.getTextContent();
                }
            }
        }
        return null;
	}
	
	/**
	 * @Author : Suresh
	 * Desc: getTest is used to get test name.
	 */
	public String getTest(String searchScheduleId) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(getPath());
        doc.getDocumentElement ().normalize ();
        NodeList listOfSchedule = doc.getElementsByTagName("Schedule");
        int totalSchedule = listOfSchedule.getLength();
        for(int i = 0; i < totalSchedule; i++) {	
            Node scheduleNode = listOfSchedule.item(i);
            if(scheduleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element scheduleElement = (Element) scheduleNode;
                String scheduleId = scheduleElement.getAttribute("id");
                if (scheduleId.equals(searchScheduleId)) {
                    NodeList testList = scheduleElement.getElementsByTagName("test");
                    Element testElement = (Element)testList.item(0);
                    return testElement.getTextContent();
                }
            }
        }
        return null;
	}
	
	/**
	 * @Author : Suresh
	 * Desc: getValueFromProperty is used to get test attribute from details node.
	 * property can be Id, Name, PostindDateAndTime, Category etc.
	 */
	public String getValueFromProperty(String details, String property) {
		String searchAttributes[] = details.split(";");
		for (int i = 0; i < searchAttributes.length; i++) {
			String keyAndValue[] = searchAttributes[i].split("=");
			if (keyAndValue[0].equals(property)) {
				return keyAndValue[1];
			}
		}
		return null;
	}
}