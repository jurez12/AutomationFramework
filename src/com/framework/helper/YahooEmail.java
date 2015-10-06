package com.framework.helper;

import java.util.*;

import javax.mail.*; 
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.apache.log4j.Logger;

import com.framework.library.utils.MiscUtils;
import com.mysql.jdbc.Driver;

public class YahooEmail { 
	
	static final Logger logger = Logger.getLogger(YahooEmail.class);
    
//    public static void main(String args[]) throws Exception {
///*			sentFromYahooEmail();
//* 
//*/
//    	String subject ="Activate your account on passbrains";
//	 	String content = readBySubject(subject);
//	   System.out.println("Content " + content);
//	 	//String activationCode = getActivationCode(content);  // returns "BAR"
//	  //  System.out.println("The str " + activationCode);
//	    
//		//getActivationLink(content);
//	 	
//	 	
//	 	/*
//		deleteAllYahooEmail("");
//		
//		deleteAllYahooEmail("Activate your account on passbrains");
//		*/
//    }
	
	 static void showUnreadMails(Folder inbox) {       
        try {
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message msg[] = inbox.search(ft);
            System.out.println("MAILS: "+msg.length);
            for(Message message:msg) {
                try {
                    System.out.println("DATE: "+message.getSentDate().toString());
                    System.out.println("FROM: "+message.getFrom()[0].toString());           
                    System.out.println("SUBJECT: "+message.getSubject().toString());
                    System.out.println("CONTENT: "+message.getContent().toString());
                    System.out.println("-------------------------------------------");
                } catch (Exception e) {
                    System.out.println("No Information");
                }
            }
        } catch (MessagingException e) {
            System.out.println(e.toString());
        }
     }
	 
	 static void deleteAllMails(Folder inbox, String subject) {       
	        try {
	           // FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
	            Message msg[] =inbox.getMessages();
	            System.out.println("MAILS: "+msg.length);
	            for(Message message:msg) {
	            	
                try {
	                	System.out.println("The test is used to delete " + message);
	                	if (message.getSubject().toString().contains(subject) || 
	                			message.getSubject().toString().contains("")) {
	                		System.out.println("Deleted email subject :" + message.getSubject().toString());
	                		message.setFlag(Flags.Flag.DELETED, true);
	                		
	                	}

                } catch (Exception e) {
	                    System.out.println("No Information");
	                }
	            }
	            inbox.close(true);
	        } catch (MessagingException e) {
	            System.out.println(e.toString());
	        }
	        
	        
	     }

    static void showAllMails(Folder inbox){
	        try {
	            Message msg[] = inbox.getMessages();
	            System.out.println("MAILS: "+msg.length);
	            for(Message message:msg) {
	                try {
	                    System.out.println("DATE: " +message.getSentDate().toString());
	                    System.out.println("FROM: " +message.getFrom()[0].toString());           
	                    System.out.println("SUBJECT: " +message.getSubject().toString());
	                    String contentStr = null;
					 	ContentType ct = new ContentType(message.getContentType());
		            	
					 	if(ct.getPrimaryType().equals("multipart")) {
		            		contentStr = getMultiPart(message);
		            	} else {
		            		contentStr = message.getContent().toString();
		            	}
	                    System.out.println("CONTENT: " +contentStr);
	                    System.out.println("------------------------------------------");
	                } catch (Exception e) {
	                    System.out.println("No Information");
	                }
	            }
	        } catch (MessagingException e) {
	            System.out.println(e.toString());
	        }
        }
    
    static String showAllMails(Folder inbox, String subject){
    	System.out.println("Search in in box, Subject : " + subject);
        try {
            Message msg[] = inbox.getMessages();
            System.out.println("MAILS: "+msg.length);
            for(Message message:msg) {
                try {
                	if (message.getSubject().toString().equals(subject)) {
	                    System.out.println("DATE: " +message.getSentDate().toString());
	                    System.out.println("FROM: " +message.getFrom()[0].toString());    
	                    
	                    System.out.println("SUBJECT: " +message.getSubject().toString());
	                    String contentStr = null;
					 	ContentType ct = new ContentType(message.getContentType());
		            	
					 	if(ct.getPrimaryType().equals("multipart")) {
		            		contentStr = getMultiPart(message);
		            	} else {
		            		contentStr = message.getContent().toString();
		            	}
	                 //   System.out.println("CONTENT: " +contentStr);
	                    System.out.println("------------------------------------------");
	                    return contentStr;
                	}
                } catch (Exception e) {
                    System.out.println("No Information");
                }
            }
        } catch (MessagingException e) {
            System.out.println(e.toString());
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


		public static String getActivationCode(String content) {
			System.out.println("Activation code searching....");
			return MiscUtils.get_match(content, "Password: <b>(.*?)\\</b>");
		}

		public static String getActivationLink(String content) {
			System.out.println("Activation conetent is :" + content);
			List<String> links = MiscUtils.getLinksFromContent(content);
		 	for (String link : links) {
		 		
		 		if(link.contains("activate")) {
		 			return link;
		 		}
		 	}
			return "";
		}
	    
	    public static String readBySubject(String subject) {
			Properties props = System.getProperties();
	        props.setProperty("mail.store.protocol", "imaps");
	        for (int i = 0; i< 5; i++) {
		        try {
		                Session session = Session.getDefaultInstance(props, null);
		                Store store = session.getStore("imaps");
		                store.connect("imap.mail.yahoo.com", "automation_demo_live@yahoo.com", "Auto@1234");
		                System.out.println(store);
		                Folder inbox = store.getFolder("Inbox");
		                inbox.open(Folder.READ_WRITE);
		              return showAllMails(inbox, subject);
		        } catch (NoSuchProviderException e) {
		           // System.out.println("No such provider exeption " + e.toString());
		            logger.info("No such provider exeption " + e.toString());
		            //System.exit(1);
		        } catch (MessagingException e) {
		           // System.out.println("Message is not executed " + e.toString());
		            logger.info("Message is not executed " + e.toString());
		           // System.exit(2);
		        } catch (Exception e) {
		        	logger.info("General Exception" + e.toString());
		        	 System.out.println("General Exception " + e);
		        }
	        }
			return "";
	    }

	    private static void readAllYahooEmail() {
			Properties props = System.getProperties();
	        props.setProperty("mail.store.protocol", "imaps");
	        try {
	                Session session = Session.getDefaultInstance(props, null);
	                Store store = session.getStore("imaps");
	                store.connect("imap.mail.yahoo.com", "automation_demo_live@yahoo.com", "Auto@1234");
	                System.out.println(store);
	                Folder inbox = store.getFolder("Inbox");
	                inbox.open(Folder.READ_WRITE);
	                showAllMails(inbox);
	        } catch (NoSuchProviderException e) {
	            System.out.println(e.toString());
	            System.exit(1);
	        } catch (MessagingException e) {
	            System.out.println(e.toString());
	            System.exit(2);
	        }
	    }

	    public static void deleteAllYahooEmail(String subject) {
			Properties props = System.getProperties();
	        props.setProperty("mail.store.protocol", "imaps");
	        try {
	                Session session = Session.getDefaultInstance(props, null);
	                Store store = session.getStore("imaps");
	                store.connect("imap.mail.yahoo.com", "automation_demo_live@yahoo.com", "Auto@1234");
	                System.out.println(store);
	                Folder inbox = store.getFolder("Inbox");
	                inbox.open(Folder.READ_WRITE);
	                deleteAllMails(inbox, subject);
	        } catch (NoSuchProviderException e) {
	            System.out.println(e.toString());
	            logger.info("Message is not executed " + e.toString());
	            //System.exit(1);
	        } catch (MessagingException e) {
	            System.out.println(e.toString());
	            logger.info("General Exception" + e.toString());
	            //System.exit(2);
	        }
	    }

	    private static void sentFromYahooEmail() {
		   String from = "automation_demo_live@yahoo.com";
		   String pass ="Auto@1234";
		   String to = "automation_demo_live@yahoo.com";
		   String host = "smtp.mail.yahoo.com";
	
		   Properties properties = System.getProperties();
		   properties.put("mail.smtp.starttls.enable", "true");
		   properties.put("mail.smtp.host", host);
		   properties.put("mail.smtp.user", from);
		   properties.put("mail.smtp.password", pass);
		   properties.put("mail.smtp.port", "587");
		   properties.put("mail.smtp.auth", "true");
		   Session session = Session.getDefaultInstance(properties);
	
		   try {
		      MimeMessage message = new MimeMessage(session);
		      message.setFrom(new InternetAddress(from));
		      message.addRecipient(Message.RecipientType.TO,
		                               new InternetAddress(to));
		      message.setSubject("This is the Subject Line!");
		      message.setText("This is actual message");
		      Transport transport = session.getTransport("smtp");
		      transport.connect(host, from, pass);
		      transport.sendMessage(message, message.getAllRecipients());
		      transport.close();
		      System.out.println("Sent message successfully....");
		   } catch (MessagingException mex) {
		      mex.printStackTrace();
		   }
	    }
 
}