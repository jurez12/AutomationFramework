package com.framework.suite;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

public class RetryListener extends TestListenerAdapter {

    private int count = 0;

    @Override
    public void onTestFailure(ITestResult result) {
        if (result.getMethod().getRetryAnalyzer() != null) {
            Reporter.setCurrentTestResult(result);

            if(result.getMethod().getRetryAnalyzer().retry(result)) {
                count++;
                result.setStatus(ITestResult.SKIP);
                System.out.println("Error in " + result.getName() + " with status "
                        + result.getStatus()+ " Retrying " + count + " of 3 times");
                System.out.println("Setting test run attempt status to Skipped");
            } else {
                count = 0;
                System.out.println("Retry limit exceeded for " + result.getName());
            }

            Reporter.setCurrentTestResult(null);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        count = 0;
    }
}