package com.framework.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.framework.core.Global;
import com.framework.library.utils.MiscUtils;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class EmailFunctions {
	static final Logger logger = Logger.getLogger(EmailFunctions.class);

	public static void sendMail(String emailMsgTxt, String[] sendTo) throws Exception {
		String emailSubjectTxt = "Result of Test";	
		String emailFromAddress = "email.test@gmail.com";
		sendEmailBySubjectWithMsg(sendTo, emailSubjectTxt,
			emailMsgTxt, emailFromAddress);
		System.out.println("Sucessfully Sent mail to All Users");
	}

	public static void sendEmailBySubjectWithMsg(String recipients[], String subject,
		String message, String from) throws MessagingException {
		String SMTP_HOST_NAME = "smtp.gmail.com";
		String SMTP_PORT = "465";
		String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		boolean debug = true;
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
   		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication("email.test@gmail.com", "gmail");
		}
		});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
		addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain;charset=UTF-8");
		Transport.send(msg);
	}
	
	public static String getMail(String subject) throws Exception{
		System.out.println("Searching for Email with subject:"+subject);
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imaps");
		try {
					store.connect("imap.gmail.com", "email.test@gmail.com", "gmail");
					//store.connect("dev.gmail.com",993, "build", "Ruo7aidu1maNgoovailahngai");
					//System.out.println(store);
					for(int count=0;count<20;count++){
							Folder inbox = store.getFolder("Inbox");
							inbox.open(Folder.READ_WRITE);
							Message messages[] = inbox.getMessages();
							for (int i=0, n=messages.length; i<n; i++) {
								//System.out.print(messages[i].getSubject());
								if(messages[i].getSubject().replace(" ","").contains(subject.replace(" ",""))){
							        //System.out.println(i + ": " + messages[i].getFrom()[0]+ "\t" + messages[i].getSubject());
							        //System.out.println(messages[i].getContent());
									String contentStr = null;
								 	ContentType ct = new ContentType(messages[i].getContentType() );
					            	if(ct.getPrimaryType().equals("multipart")) {
					            		contentStr = getMultiPart(messages[i]);
					            	} else {
					            		contentStr = messages[i].getContent().toString();
					            	}
									
							        return messages[i].getSubject().toString()+ " " + contentStr;
								}
							}
					Thread.sleep(200);
					}
					
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
				return "";
				//System.out.println("Error:");
				//System.exit(1);
			} catch (MessagingException e) {
				e.printStackTrace();
				return "";
				//System.out.println("Error");
				//System.exit(2);
			} catch (Exception e) {
				System.out.println("The error is " + e.toString());
				return "";
			}
			finally{
				store.close();
			}
			
			return "";

		}
	
	/*
	* @Author : Suresh
	* @Desc: getMultiPart is used to get MultiPart content
	*/
	public static String getMultiPart(Message m) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			MimeMultipart content = ( MimeMultipart )m.getContent();
			for( int i = 0; i < content.getCount(); i++ ) {
				BodyPart part = content.getBodyPart( i );
				stringBuffer.append(part.getContent());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
	
	/**
	 * deleteAllEmail is used to delete all the emails.
	 * @return
	 * @throws Exception
	 */
	public static String deleteAllEmail() throws Exception{
		System.out.println("Deleting All Email:");
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		try {
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", "email.test@gmail.com", "gmail");
			Folder inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);
			Message messages[] = inbox.getMessages();
			for (int i=0, n=messages.length; i<n; i++) {
				messages[i].setFlag(Flags.Flag.DELETED, true);
			}
				
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			System.out.println("Error");
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("Error");
		} catch (Exception e) {
			System.out.println("The error is " + e.toString());
			return "";
		}
		return "";
	}
	
	public static String deleteEmail(String subject) throws Exception{
		System.out.println("Deleting Email with subject:"+subject);
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
			try {
					Session session = Session.getDefaultInstance(props, null);
					Store store = session.getStore("imaps");
					store.connect("imap.gmail.com", "email.test@gmail.com", "gmail");
					//store.connect("dev.gmail.com",993, "build", "Ruo7aidu1maNgoovailahngai");
					//System.out.println(store);
					Folder inbox = store.getFolder("Inbox");
					inbox.open(Folder.READ_WRITE);
					Message messages[] = inbox.getMessages();
					for (int i=0, n=messages.length; i<n; i++) {
						if(messages[i].getSubject().replace(" ","").contains(subject.replace(" ",""))){
						       //System.out.println(i + ": " + messages[i].getFrom()[0]+ "\t" + messages[i].getSubject());
						       //System.out.println(messages[i].getContent());
						       messages[i].setFlag(Flags.Flag.DELETED, true);
						}
					}
					
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
				System.out.println("Error");
				//System.exit(1);
			} catch (MessagingException e) {
				e.printStackTrace();
				System.out.println("Error");
				//System.exit(2);
			} catch (Exception e) {
				System.out.println("The error is " + e.toString());
				return "";
			}
			return "";

		}

	public static String checkMail(String subject,String body[]) throws Exception{
	System.out.println("Validating Email:"+subject);
	Properties props = System.getProperties();
	props.setProperty("mail.store.protocol", "imaps");
		try {
				Session session = Session.getDefaultInstance(props, null);
				Store store = session.getStore("imaps");
				store.connect("imap.gmail.com", "email.test@gmail.com", "gmail");
				//store.connect("dev.gmail.com",993, "build", "Ruo7aidu1maNgoovailahngai");
				//System.out.println(store);
				Folder inbox = store.getFolder("Inbox");
				inbox.open(Folder.READ_WRITE);
				Message messages[] = inbox.getMessages();
				for (int i=0, n=messages.length; i<n; i++) {
					if(messages[i].getSubject().contains(subject)){
					       //System.out.println(i + ": " + messages[i].getFrom()[0]+ "\t" + messages[i].getSubject());
					       for (int j=0;j<body.length;j++){
					    	   if(messages[i].getContent().toString().contains(body[j])){
					    	   }
					    	   else{
					    		   System.out.println("ERROR: String Not Found:"+body[j]);
					    	   }
					       }
					    	   
					       //System.out.println(messages[i].getContent());
					       //System.out.println("Do you want to DELETE message? [YES to read/QUIT to end]");
					       BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
					       String line = reader.readLine();
					       if ("YES".equals(line)) {
					    	   messages[i].setFlag(Flags.Flag.DELETED, true);

					       }
					}
				}
				
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			System.out.println("Error");
			System.exit(1);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("Error");
			System.exit(2);
		}
		return "";
	}

	 public static Set<String> attachFiles = new HashSet<String>();
	 static Set<String> testFiles = new HashSet<String>();
	 
	 String customeReportTable = "";
	
		/**
	     * Test sending e-mail with attachments
	     * @throws IOException 
		 * @throws XPathExpressionException 
		 * @throws ParserConfigurationException 
	     */
	   public String storeReport() throws IOException, XPathExpressionException, ParserConfigurationException {
		   String fileSepartor ="\\";
		   if (Global.OS_NAME.contains("Mac")) {
			   fileSepartor = "/";
			} else {
				fileSepartor = "\\";
			}
		   
			 String report =  Global.TEST_PATH + fileSepartor +Global.TEST_NAME+ "_" + Global.REPORT_NAME;
			 String test_Summary_Automation = Global.TEST_PATH + fileSepartor +Global.TEST_NAME+ "Test Summary Report Automation Team.html";
			 String test_Summary_Management = Global.TEST_PATH + fileSepartor +Global.TEST_NAME+ "Test Summary Report Management Team.html";
	        
	       String message =  Files.toString(new File(report), Charsets.UTF_8);
	       FileUtils.writeStringToFile(new File(test_Summary_Management), message);
	       attachFiles.add(test_Summary_Management);
	       String automationDetailsText = customeReportTable;

	       for(String file : testFiles) {
	    		   automationDetailsText += getResultTable(file);
	       }
	       
	       FileUtils.writeStringToFile(new File(test_Summary_Automation), automationDetailsText);
	       attachFiles.add(test_Summary_Automation);
//	        try {
//	            sendEmailWithAttachments(message);
//	            System.out.println("Email sent.");
//	        } catch (Exception ex) {
//	            System.out.println("Could not send email.");
//	            ex.printStackTrace();
//	        }
			return message;
	    }

       public String getResultTable(String file) throws IOException, ParserConfigurationException, XPathExpressionException {
    	  try {
    	   File input = new File(file);
    	   Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
    	   Elements elements = doc.select("table");
    	   String str = ""; 
    	   System.out.println(" the str " + elements.html());
    	   int count = 0;
    	   for (Element table : elements) {
    		   if( count != 0) {
    			   System.out.println("table " + table.outerHtml());
        		   str +=  table.outerHtml();
    			   
    		   }
    		   count++;
    		   
    	   }
    	   return str;
    	   
    	  } catch(Exception e) {
    		  return "";
    	  }
    	   //div.html(strHtml);
    	   //String reportContent =  Files.toString(new File(file), Charsets.UTF_8);
		   //return getXpathStrFromStr(reportContent, xpathStr);
       }
       
       public String getXpathStrFromStr(String content, String xpathStr) throws ParserConfigurationException, XPathExpressionException {
    	   XPath xpath = XPathFactory.newInstance().newXPath();
			TagNode tagNode = new HtmlCleaner().clean(content);
			org.w3c.dom.Document doc = new DomSerializer(
			        new CleanerProperties()).createDOM(tagNode);
			return (String) xpath.evaluate(xpathStr, doc, XPathConstants.STRING);
       }
	   
       public String getReport() throws XPathExpressionException, IOException, ParserConfigurationException {
    	    String message = "";
	        String filePath = "test-output\\"+ Global.SUITE_NAME; 
	        log(Global.TEST_SET.toString());
	        
	        for (String testInfo : Global.TEST_SET) {
	        	if ((!testInfo.contains("null"))) {
	        		message += exportReport(filePath, testInfo);
	        	}
	        }
	  //      message += "<div id='summarytable'>" + message + "</div>";
	       // FileUtils.writeStringToFile(new File(filePath + test_Summary_Management), message);
	      //p[2]
	        customeReportTable = message;
	        return message;
       }
       

       
		private String exportReport(String filePath, String testInfo) throws IOException,
				ParserConfigurationException, XPathExpressionException {
			String test[] = testInfo.split("@");
			String fileSepartor ="\\";
			if (Global.OS_NAME.contains("Mac")) {
				   fileSepartor = "/";
			} else {
					fileSepartor = "\\";
			}
			String fileName = filePath  + fileSepartor + Global.TEST_NAME + ".html";
			testFiles.add(fileName);
			String browerNamAndVersion = createRowTable2Column("Browser Name", test[0]) + createRowTable2Column("Browser Version", test[1]);
			String reportContent =  Files.toString(new File(fileName), Charsets.UTF_8);
			System.out.println(reportContent);
			XPath xpath = XPathFactory.newInstance().newXPath();
			TagNode tagNode = new HtmlCleaner().clean(reportContent);
			org.w3c.dom.Document doc = new DomSerializer(
			        new CleanerProperties()).createDOM(tagNode);
			String resultText = (String) xpath.evaluate("//body/table/tbody/tr/td[1]", doc, XPathConstants.STRING);
			String[] result=resultText.replaceAll(":", "").split("/");
 	       
			String resultNumber = (String) xpath.evaluate("//body/table/tbody/tr/td[2]", doc, XPathConstants.STRING);
			
			String[] result1=resultNumber.trim().split("/");
			int totalCount = 0; 
			for(String str: result1) {
				totalCount += Integer.parseInt(str);
			}
			
			String resultTimeXpath = (String) xpath.evaluate("//body/table/tbody/tr[3]/td[2]", doc, XPathConstants.STRING);
			String min = MiscUtils.convertSecondToHHMMString(Integer.parseInt(resultTimeXpath.split(" ")[0]));
			
			//String startTime = startTimeXpath.split(" ")[3];
			 String cssPath = "resources\\cssTable.txt";
			String resultStr = Files.toString(new File(cssPath), Charsets.UTF_8);

			resultStr += createRowTable2Column("Total Test Count", totalCount +"");
 	        for (int i = 0; i < result.length; i++) {
			   resultStr += createRowTable2Column(result[i], result1[i]);
			   System.out.println(result[i] +":" + result1[i]);
 	        }
 	        resultStr += createRowTable2Column("Duration", min +"") +
 	        		createRowTable2Column("Start Time", Global.START_TIME) +
 	        		createRowTable2Column("End Time",  Global.END_TIME) + browerNamAndVersion;
		    resultStr += "</table>";
		    log("Result"+ resultStr);
		    return "<div id='summarytable'><b>  <h3 align=\"center\"> GoIbibo Smoke Testing Suite(" + Global.TEST_NAME +") - Test Result Summary </h3>" +  resultStr +"</br>  <h3 align=\"center\"> Detailed Report </h3> </b> </div>" ;
		}
 
	   private String createRowTable2Column(String col1, String col2) {
		   return "<tr><td>" + col1 + "</td><td>" + col2 + "</td></tr>";
	   }
	   
	    private void log(String result) {
	    	logger.info("Email log :" + result);
		}

//		/**
//	     * Test sending e-mail with attachments
//	     * @throws IOException 
//	     */
//	   public void sendEmailWithAttachments() throws IOException {
//	        String message = Files.toString(new File(attachFiles.get(0)), Charsets.UTF_8);
//	        try {
//	            sendEmailWithAttachments(message);
//	            System.out.println("Email sent.");
//	        } catch (Exception ex) {
//	            System.out.println("Could not send email.");
//	            ex.printStackTrace();
//	        }
//	    }
	   
	   public static void sendEmailWithAttachments(String message)
	            throws AddressException, MessagingException, UnsupportedEncodingException {
	        Properties properties = new Properties();
	        properties.put("mail.smtp.host", Global.HOST);
	        properties.put("mail.smtp.port", Global.PORT);
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.user",  Global.FROM_USER);
	        properties.put("mail.password", Global.PASSWORD);
	 
	        // creates a new session with an authenticator
	        Authenticator auth = new Authenticator() {
	            public PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication( Global.FROM_USER, Global.PASSWORD);
	            }
	        };
	        Session session = Session.getInstance(properties, auth);
	 
	        // creates a new e-mail message
	        Message msg = new MimeMessage(session);
	 
	        msg.setFrom(new InternetAddress(Global.FROM_USER, "Automation Demo"));
	        InternetAddress[] toAddresses = { new InternetAddress(Global.TO_USER) };
	        msg.setRecipients(Message.RecipientType.TO, toAddresses);
//	        
//	        msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(
//	               Global.FROM_USER_CC));
//	        msg.addRecipient(Message.RecipientType.CC, new InternetAddress(
//	               Global.FROM_USER_BCC));
	        Global.SUBJECT= "Automation Report - " + MiscUtils.capitalizeFirstLetter( Global.SUITE_NAME) + " " +  MiscUtils.capitalizeFirstLetter(Global.TEST_NAME) + " Test Suite";   
	        msg.setSubject( Global.SUBJECT);
	        msg.setSentDate(new Date());
	 
	        // creates message part
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setContent(message, "text/html");
	 
	        // creates multi-part
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(messageBodyPart);
	 
	            for (String filePath : attachFiles) {
	                MimeBodyPart attachPart = new MimeBodyPart();
	 
	                try {
	                    attachPart.attachFile(filePath);
	                } catch (IOException ex) {
	                    ex.printStackTrace();
	                }
	 
	                multipart.addBodyPart(attachPart);
	            }
	 
	        // sets the multi-part as e-mail's content
	        msg.setContent(multipart);
	        // sends the e-mail
	        try {
	        Transport.send(msg);
	        }  catch(Exception e) {
	        	System.out.println("Email not send " + e);
	        }
	    }
}