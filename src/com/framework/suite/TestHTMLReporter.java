package com.framework.suite;


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
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.internal.Utils;
import org.testng.reporters.HtmlHelper;
import com.framework.library.utils.MiscUtils;
import com.framework.core.Global;
import com.framework.helper.DataBase;
import com.framework.helper.EmailFunctions;
import com.framework.shared.Status;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


/**
 * Modified TEstng code accordingly with simple data to understand for management
 */
public class TestHTMLReporter extends TestListenerAdapter  {
    static final Logger log = Logger.getLogger(TestHTMLReporter.class);
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private static final Comparator<ITestResult> NAME_COMPARATOR= new NameComparator();
    private ITestContext m_testContext = null;
	static Set<String> attachFiles;
	static Set<String> testFiles = new HashSet<String>();
	static String customeReportTable = "";
	static String PASSED;
	static String FAILED;
	static String NO_EXECUTED;

    @Override
    public void onStart(ITestContext context) {
		Global.TEST_NAME = context.getName();
		Global.SUITE_NAME= context.getCurrentXmlTest().getSuite().getName();
		System.out.println(" Test Name " + Global.TEST_NAME );
		Global.TEST_PATH =  setPath( "test-output",  Global.SUITE_NAME);
		System.out.println("The Test  Path " + Global.TEST_PATH);
		m_testContext = context;
		
		PASSED = "0";
		FAILED = "0";
		NO_EXECUTED = "0";
    }

    @Override
    public void onFinish(ITestContext context) {
		  customeReportTable = "";
		  Global.START_TIME = sdf.format(context.getStartDate().getTime());
		  Global.END_TIME =  sdf.format(context.getEndDate().getTime());
		  log.info("Suite Name :"+   Global.SUITE_NAME);
		  try {
			generateLog(m_testContext,
		            null /* host */,
		            m_testContext.getOutputDirectory(),
		            getConfigurationFailures(),
		            getConfigurationSkips(),
		            getPassedTests(),
		            getFailedTests(),
		            getSkippedTests(),
		            getFailedButWithinSuccessPercentageTests());	
		  } catch (XPathExpressionException | IOException
				| ParserConfigurationException e) {
			// TODO Auto-generated catch block
			log.error( e);
		} try {
			storeReport();
		} catch (XPathExpressionException | IOException
				| ParserConfigurationException e) {
			log.error("Email Sent Error: "+ e);
			System.out.println("Email Sent Error: "+ e);
		}
		
		try {
			DataBase.insertIntoTestResult(Integer.parseInt(PASSED),
					 Integer.parseInt(FAILED), Integer.parseInt(NO_EXECUTED));
		} catch (NumberFormatException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DataBase.updateStatusForScheduler(Global.RUN_ID, Status.completed);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private static String getOutputFile(ITestContext context) {
		System.out.println("o/p "  + Global.TEST_NAME+ "_" + Global.REPORT_NAME);
		String outputFileDir =  Global.TEST_NAME+ "_" + Global.REPORT_NAME;
		System.out.println("The output dir " + outputFileDir);
	    return  outputFileDir;
    }
  
    public static void generateTableMe(StringBuffer sb, String title,
	      Collection<ITestResult> tests, String cssClass, Comparator<ITestResult> comparator, String status) {
  		sb.append("<table width='100%' border='1' class='invocation-").append(cssClass).append("'>\n")
	      .append("<tr><td colspan='4' align='center'><b>").append(title).append("</b></td></tr>\n")
	      .append("<tr>")
	      .append("<td align='center'><b>Test Script Discription</b></td>\n")
	      .append("<td  align='center'><b>Method Name</b></td>\n")
	      .append("<td align='center'><b>Duration	</b></td>\n")
	      .append("<td align='center'><b>Status</b></td>\n")
	      .append("</tr>\n");

	    if (tests instanceof List) {
	      Collections.sort((List<ITestResult>) tests, comparator);
	    }

	    for (ITestResult tr : tests) {
	      sb.append("<tr>\n");

	      // Test method
	      ITestNGMethod method = tr.getMethod();

	      String name = method.getMethodName();
	      sb.append("<td  align='center' title='").append(tr.getTestClass().getName()).append(".")
	        .append(name)
	        .append("()'>");
	      // Method description
	      if (! Utils.isStringEmpty(method.getDescription())) {
	        sb.append("<br>").append(method.getDescription());
	      }

	      Object[] parameters = tr.getParameters();
	      if (parameters != null && parameters.length > 0) {
	        sb.append("<br>Parameters: ");
	        for (int j = 0; j < parameters.length; j++) {
	          if (j > 0) {
	            sb.append(", ");
	          }
	          sb.append(parameters[j] == null ? "null" : parameters[j].toString());
	        }
	      }

	      sb.append("</td>\n");
	      sb.append("<td  align='center'>");
	      sb.append(name);
	      sb.append("</td>\n");

	      // Time
	      long time = (tr.getEndMillis() - tr.getStartMillis()) / 1000;
	      sb.append("<td  align='center'>").append(MiscUtils.convertSecondToHHMMString(Ints.checkedCast(time))).append("</td>\n");

	      // Status
	      sb.append("<td  align='center'>").append(status).append("</td>");
	      sb.append("</tr>\n");
	    }
	    sb.append("</table><p>\n");

	}

    private static String HEAD =
    "\n<style type=\"text/css\">\n" +
    ".log { display: none;} \n" +
    ".stack-trace { display: none;} \n" +
    "</style>\n" +
    "<script type=\"text/javascript\">\n" +
      "<!--\n" +
      "function flip(e) {\n" +
      "  current = e.style.display;\n" +
      "  if (current == 'block') {\n" +
      "    e.style.display = 'none';\n" +
      "    return 0;\n" +
      "  }\n" +
      "  else {\n" +
      "    e.style.display = 'block';\n" +
      "    return 1;\n" +
      "  }\n" +
      "}\n" +
      "\n" +
      "function toggleBox(szDivId, elem, msg1, msg2)\n" +
      "{\n" +
      "  var res = -1;" +
      "  if (document.getElementById) {\n" +
      "    res = flip(document.getElementById(szDivId));\n" +
      "  }\n" +
      "  else if (document.all) {\n" +
      "    // this is the way old msie versions work\n" +
      "    res = flip(document.all[szDivId]);\n" +
      "  }\n" +
      "  if(elem) {\n" +
      "    if(res == 0) elem.innerHTML = msg1; else elem.innerHTML = msg2;\n" +
      "  }\n" +
      "\n" +
      "}\n" +
      "\n" +
      "function toggleAllBoxes() {\n" +
      "  if (document.getElementsByTagName) {\n" +
      "    d = document.getElementsByTagName('div');\n" +
      "    for (i = 0; i < d.length; i++) {\n" +
      "      if (d[i].className == 'log') {\n" +
      "        flip(d[i]);\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}\n" +
      "\n" +
      "// -->\n" +
      "</script>\n" +
      "\n";

  public static void generateLog(ITestContext testContext,
      String host,
      String outputDirectory,
      Collection<ITestResult> failedConfs,
      Collection<ITestResult> skippedConfs,
      Collection<ITestResult> passedTests,
      Collection<ITestResult> failedTests,
      Collection<ITestResult> skippedTests,
      Collection<ITestResult> percentageTests) throws XPathExpressionException, IOException, ParserConfigurationException {
    StringBuffer sb = new StringBuffer();
    sb.append("<html>\n<head>\n")
      .append("<title>TestNG:  ").append(testContext.getName()).append("</title>\n")
      .append(HtmlHelper.getCssString())
      .append(HEAD)
      .append("</head>\n")
      .append("<body>\n");
   
    sb.append(getReport());

    if (passedTests.size() > 0) {
      generateTableMe(sb, "PASSED TESTS", passedTests, "passed", NAME_COMPARATOR, "Passed");
    }
    if (failedTests.size() > 0) {
        generateTableMe(sb, "FAILED TESTS", failedTests, "failed", NAME_COMPARATOR, "Failed");
    }
     
    sb.append("</body>\n</html>");
    Utils.writeFile(outputDirectory, getOutputFile(testContext), sb.toString());
  }

  private static class NameComparator implements Comparator<ITestResult>, Serializable {
    private static final long serialVersionUID = 381775815838366907L;
    public int compare(ITestResult o1, ITestResult o2) {
      String c1 = o1.getMethod().getMethodName();
      String c2 = o2.getMethod().getMethodName();
      return c1.compareTo(c2);
    }
  }

	public static String getReport() throws XPathExpressionException, IOException, ParserConfigurationException {
		String message = "";
		for (String testInfo : Global.TEST_SET) {
			if ((!testInfo.contains("null"))) {
				message += exportReport( testInfo);
			}
		}
		customeReportTable = message;
		return message;
	}
 
	private static String exportReport( String testInfo) throws IOException,
			ParserConfigurationException, XPathExpressionException {
		String test[] = testInfo.split("@");
		String fileSepartor ="\\";
		if (Global.OS_NAME.contains("Mac")) {
			   fileSepartor = "/";
		} else {
				fileSepartor = "\\";
		}
		String fileName = Global.TEST_PATH  + fileSepartor + Global.TEST_NAME + ".html";
		System.out.println(" The O/P " + fileName);
		String browserName = MiscUtils.capitalizeFirstLetter(test[0]);
		String browerNamAndVersion = createRowTable2Column("Browser Name & Version", browserName + " " + test[1]);
		String reportContent =  Files.toString(new File(fileName), Charsets.UTF_8);
		XPath xpath = XPathFactory.newInstance().newXPath();
		TagNode tagNode = new HtmlCleaner().clean(reportContent);
		org.w3c.dom.Document doc = new DomSerializer(
		        new CleanerProperties()).createDOM(tagNode);
      
		String resultNumber = (String) xpath.evaluate("//body/table/tbody/tr/td[2]", doc, XPathConstants.STRING);
		
		String[] result1=resultNumber.trim().split("/");
		int totalCount = 0; 
		for(String str: result1) {
			totalCount += Integer.parseInt(str);
		}
		PASSED = result1[0];
		FAILED = result1[1];
		NO_EXECUTED = result1[2];
		
		String resultTimeXpath = (String) xpath.evaluate("//body/table/tbody/tr[3]/td[2]", doc, XPathConstants.STRING);
		String min = MiscUtils.convertSecondToHHMMString(Integer.parseInt(resultTimeXpath.split(" ")[0]));
		
		 String cssPath = "resources"+ fileSepartor + "cssTable.txt";
		String resultStr = Files.toString(new File(cssPath), Charsets.UTF_8);
        
		resultStr += createRowTable2Column("Total Number of Test Scripts", totalCount +"");
		resultStr += createRowTable2Column("Number of Test Scripts Passed", PASSED);
		resultStr += createRowTable2Column("Number of Test Scripts Failed", FAILED);
		resultStr += createRowTable2Column("Number of Test Scripts Not Executed", NO_EXECUTED);
        resultStr += createRowTable2Column("Start Time", Global.START_TIME) +
       		createRowTable2Column("End Time",  Global.END_TIME) +  createRowTable2Column("Duration", min +"") + browerNamAndVersion;
	    resultStr += "</table>";
	    Global.TEST_SET.remove(testInfo);
	    String projectStr = "";
	   // if(!Global.SUITE_NAME.contains("")) {
	    	projectStr = MiscUtils.capitalizeFirstLetter(Global.SUITE_NAME) + " " + MiscUtils.capitalizeFirstLetter(Global.TEST_NAME) + " Testing Suite <br><br>";
	    //} 
	    return "<div id='summarytable'><b>  <h3 align=\"center\"> " + projectStr + "<br><div align='center'> Test Result Summary </div> </h3>" +  resultStr +"</br>  <h3 align=\"center\"> Detailed Report </h3> </b> </div>" ;
	}

	private static String createRowTable2Column(String col1, String col2) {
		if (col2.contains("Mobile Hybrid")){
			return "";
		}
		return "<tr><td>" + col1 + "</td><td>" + col2 + "</td></tr>";
	}
	
	public String storeReport() throws IOException, XPathExpressionException, ParserConfigurationException {
		attachFiles = new HashSet<String>();
		String report;
		
		String fileSepartor ="\\";
		if (Global.OS_NAME.contains("Mac")) {
			   fileSepartor = "/";
		} else {
				fileSepartor = "\\";
		}
		
		report =  Global.TEST_PATH + fileSepartor + Global.TEST_NAME+ "_" + Global.REPORT_NAME;
		System.out.println("The Report  " + report);
		String test_Summary_Automation = Global.TEST_PATH + fileSepartor + "Test Summary Report Automation Team (" +Global.TEST_NAME + ").html";
		String test_Summary_Management = Global.TEST_PATH + fileSepartor + "Test Summary Report Management Team (" +Global.TEST_NAME + ").html";
		System.out.println("The Automation   " + test_Summary_Automation);
		System.out.println("The Management   " + test_Summary_Management);
		String mangementDetailsText =  Files.toString(new File(report), Charsets.UTF_8);
		FileUtils.writeStringToFile(new File(test_Summary_Management), mangementDetailsText);
		Global.MANAGEMENT_REPORT = mangementDetailsText;
		attachFiles.add(test_Summary_Management);
		String automationDetailsText = customeReportTable;
		automationDetailsText += getResultTable( Global.TEST_PATH  + fileSepartor + Global.TEST_NAME + ".html");
		FileUtils.writeStringToFile(new File(test_Summary_Automation), automationDetailsText);
		Global.AUTOMATION_REPORT = automationDetailsText;
		attachFiles.add(test_Summary_Automation);
		EmailFunctions.attachFiles = attachFiles;
		try {
			EmailFunctions.sendEmailWithAttachments(mangementDetailsText);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mangementDetailsText;
	}
 
	 public String setPath(String... strings) {
		 String workingDir = System.getProperty("user.dir");
		 String filePath = "";
		 for(String path : strings){
			 filePath +=  File.separator + path;
		 }
		 String fullPath = workingDir  + filePath;
		 System.out.println("Path is "+ fullPath);
		 return fullPath;
	 }
 
 public String getResultTable(String file) throws IOException, ParserConfigurationException, XPathExpressionException {
	  try {
	   File input = new File(file);
	   Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
	   Elements elements = doc.select("table");
	   String str = ""; 
	   //System.out.println(" the str " + elements.html());
	   int count = 0;
	   for (Element table : elements) {
		   if( count != 0) {
			   str +=  table.outerHtml();
		   }
		   count++;
	   }
	   return str;
	  } catch(Exception e) {
		  return "";
	  }
  }
}