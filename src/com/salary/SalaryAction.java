package com.salary;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class SalaryAction {

	
	
	public static void main(String args[]) {	
		SalaryForm salaryData = new SalaryForm();
		List<SalaryForm> salaryList = new ArrayList<SalaryForm>();
		Scanner input = new Scanner(System.in);			
		String incrementTerm;
		String deductionTerm;		
		 
		/* User input details */
		System.out.println("The Starting Salary : ");
		salaryData.setSalary(input.nextDouble());
		
		/* for validation check */
		if (salaryData.getSalary() < 1) {
			System.out.println("Negative number not allowed");
			return;
		}
		
		System.out.println("Increment to be received in percent : ");
		salaryData.setIncrementPercent(input.nextDouble());
		
		/* for validation check */
		if (salaryData.getIncrementPercent() < 1) {
			System.out.println("Negative number not allowed");
			return;
		}

		System.out.println("How frequently is increment received (quarterly, half-yearly, annually) : ");
		incrementTerm = input.next();
		salaryData.setIncrementTerm(getTermCalc(incrementTerm));
		
		/* for validation check */
		if (salaryData.getIncrementTerm() < 1) {
			System.out.println("Increment Term(" +incrementTerm +") - is not properly entered");
			return;
		}
		
		
		System.out.println("Deductions on Income in Percent : ");
		salaryData.setDeductionPercent(input.nextDouble());
		
		/* for validation check */
		if (salaryData.getDeductionPercent() < 1) {
			System.out.println("Negative number not allowed");
			return;
		}
		
		System.out.println("How frequently are deductions done (quarterly, half-yearly, annually) : ");
		deductionTerm = input.next();
		salaryData.setDeductionTerm(getTermCalc(deductionTerm));
		
		/* for validation check */
		if (salaryData.getDeductionTerm() < 1) {
			System.out.println("Deduction Term(" +deductionTerm +") - is not properly entered");
			return;
		}
		
		System.out.println("Prediction for (years) : ");
		salaryData.setYear(input.nextInt());
		
		/* for validation check */
		if (salaryData.getYear() < 1) {
			System.out.println("Year(" +deductionTerm +") - is not properly entered");
			return;
		}
		
		System.out.println("Increment Report");
		System.out.println("---------------------------------------------------------------------------------------------------------------------");
		System.out.println("Year                "+ "Starting Salary         "+"No of Increments    "+"Increment %           "+"Increment Amount        ");
		System.out.println("---------------------------------------------------------------------------------------------------------------------");
		
		/* calling method for increment Report and applied Lambda expressions */
		getIncrementReport(salaryData, salaryList);

		System.out.println("Deduction Report");
		System.out.println("---------------------------------------------------------------------------------------------------------------------");
		System.out.println("Year                "+ "Starting Salary         "+"No of Deductions    "+"Deduction %             "+"Deduction Amount       ");
		System.out.println("---------------------------------------------------------------------------------------------------------------------");
		
		/* calling method for Deduction Report and applied Lambda expressions */
		getDeductionReport(salaryData, salaryList);
		
		System.out.println("Prediction");
		System.out.println("---------------------------------------------------------------------------------------------------------------------");
		System.out.println("Year                "+ "Starting Salary         "+"Increment Amount    "+"Deduction Amount        "+"Salary Growth         ");
		System.out.println("---------------------------------------------------------------------------------------------------------------------");
		
		/* calling method for Salary Prediction Report and applied Lambda expressions */
		getPredictionReport(salaryList);
	}
	
	
	/* Lambda expression is using for getting the increment report*/
	private static void getIncrementReport(SalaryForm salaryData, List<SalaryForm> salaryList) {		
		IntStream.rangeClosed(1, salaryData.getYear()).forEach(i -> {
			SalaryForm salaryForm = new SalaryForm();
			salaryForm.setSalary(salaryData.getSalary());
			
			incrementCalc(salaryForm, salaryData.getIncrementTerm(), salaryData.getIncrementPercent());
			deductionCalc(salaryForm, salaryData.getDeductionTerm(), salaryData.getDeductionPercent());
			
			System.out.println(i +"                   "+salaryForm.getSalary()+"                     "+salaryData.getIncrementTerm()+"                   "+salaryData.getIncrementPercent()+"                   "+salaryForm.getAmountReceived());
			salaryList.add(salaryForm);			
		
			salaryData.setSalary(getSalaryGrowth(salaryForm));
		});
	}
	
	/* Lambda expression is using for getting the deduction report*/
	private static void getDeductionReport(SalaryForm salaryData, List<SalaryForm> salaryList) {
		AtomicInteger atomicCount = new AtomicInteger(0);
		salaryList.forEach(salaryDet -> {
			atomicCount.incrementAndGet();
			System.out.println(atomicCount.get() +"                   "+salaryDet.getSalary()+"                   "+salaryData.getDeductionTerm()+"                   "+salaryData.getDeductionPercent()+"                   "+salaryDet.getDeductionAmount());
		});		
	}
	
	/* Lambda expression is using for getting the prediction report*/
	private static void getPredictionReport(List<SalaryForm> salaryList) {
		AtomicInteger atomic = new AtomicInteger(0);	
		salaryList.forEach(salaryDet -> {
			atomic.incrementAndGet();			
			Double salaryGrowth = getSalaryGrowth(salaryDet);	
			salaryGrowth = salaryGrowth - salaryDet.getSalary();
			System.out.println(atomic.get() +"                   "+salaryDet.getSalary()+"                   "+salaryDet.getAmountReceived()+"                   "+salaryDet.getDeductionAmount()+"           "+ Math.round(salaryGrowth));
		});
	}
	
	/* Salary Growth calculation applied */
	private static Double getSalaryGrowth (SalaryForm salaryForm) {
		Double salaryhike = salaryForm.getSalary() + salaryForm.getAmountReceived(); 
		Double salaryGrowth = salaryhike - salaryForm.getDeductionAmount();
		return (double) Math.round(salaryGrowth);
	}
	
	/* Salary Increment Calculation is applied */ 
	private static void incrementCalc(SalaryForm salaryForm, int noOfIncrement, double incrementPercent) {
		Double amount = (salaryForm.getSalary()*incrementPercent)/100;
		salaryForm.setAmountReceived(Math.round(amount)*noOfIncrement);	
	}
	
	/* Salary Decrement Calculation is applied */ 
	private static void deductionCalc(SalaryForm salaryForm, int noOfDeduction, double decrementPercent) {
		Double amount = (salaryForm.getSalary()*decrementPercent)/100;
		salaryForm.setDeductionAmount(Math.round(amount)*noOfDeduction);		
	}
	
	/* Increment and Decrement term logic is applied */ 
	private static int getTermCalc(String incrementPeriod) {
		int incrementCount = 0;
		if(incrementPeriod.equalsIgnoreCase("quarterly")) {
			incrementCount = 4;
		} else if(incrementPeriod.equalsIgnoreCase("half-yearly")) {
			incrementCount = 3;
		} else if(incrementPeriod.equalsIgnoreCase("annually")) {
			incrementCount = 1;
		}
		return incrementCount;
	}		
}
