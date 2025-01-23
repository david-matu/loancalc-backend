package com.david.apis.loancalc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.david.apis.loancalc.model.ArmotizationSchedule;
import com.david.apis.loancalc.model.LoanPayment;

@Service
public class LoanCalcService {

	public LoanCalcService() {
		super();
	}
	
	/**
	 * Generate an Armotization Schedule based on the following parameters:
	 * 
	 * @param loanAmount
	 * @param interestRate
	 * @param loanPeriodMonths
	 * @return
	 */
	public ArmotizationSchedule getArmotizationSchedule(double loanAmount, double interestRate, int loanPeriodMonths) {
		
		double monthlyInterestRate = ((interestRate / 100)  / 12);
		
		// using annuity formula
		double monthlyPayment = formatAmount((monthlyInterestRate * loanAmount) / (1 - Math.pow(1 + monthlyInterestRate, -loanPeriodMonths)));
		
		// System.out.println("Monthly payment: " + monthlyPayment);
		
		// set initial remaining balance
		double remainingBalance = loanAmount;
		
		// set initial interest amount
		double totalInterest = 0;
		
		System.out.printf("%-6s%-12s%-12s%-12s%-15s%n", "Month", "Payment", "Interest", "Principal", "Balance");
		
		List<LoanPayment> repayments = new ArrayList<>();
		for(int month = 1; month <= loanPeriodMonths; month++) {
			double interestPayment = remainingBalance * monthlyInterestRate; // get the interest amount for the month
			double principalPayment = monthlyPayment - interestPayment; 	// calculate repayment amount
			remainingBalance -= principalPayment;
			
			// add up total interest
			totalInterest += interestPayment;
			
			// using the one-time constructor to create the loan object and feed it directly to the list (without ever allocating a memory/variable) 
			repayments.add(new LoanPayment(month, formatAmount(monthlyPayment), formatAmount(principalPayment), formatAmount(interestPayment), formatAmount(totalInterest), formatAmount(remainingBalance)));
			
			//System.out.printf("%-6d%-12.2f%-12.2f%-12.2f%-15.2f%n",
              //      month, monthlyPayment, interestPayment, principalPayment, remainingBalance);
			System.out.printf("Remaining balance: %-15.2f%n", remainingBalance);
		}
		
		ArmotizationSchedule sched = new ArmotizationSchedule(loanAmount, interestRate, loanPeriodMonths);
		
		sched.setMonthlyPayment(formatAmount(monthlyPayment));
		sched.setTotalInterest(formatAmount(totalInterest));
		sched.setTotalPayment(formatAmount(monthlyPayment * loanPeriodMonths));
		
		sched.setLoanPayments(repayments);
		
		return sched;
	}
	
	private double formatAmount(double amount) {
        return Double.valueOf(String.format("%.2f", amount)); // Format to 2 decimal places
    }
}
