package com.david.apis.loancalc.model;

import java.util.List;

public class ArmotizationSchedule {
	
	// input group
	private double loanAmount;
	private double interestRate;
	private int loanPeriod;
	
	// computed loan info group
	private double monthlyPayment;
	private double totalInterest;
	private double totalPayment;
	
	// Will hold a list of monthly armotizations
	private List<LoanPayment> loanPayments;

	public ArmotizationSchedule() {
		super();
	}
	
	/**
	 * Make it possible to create this schedule object with pre-assigned loan information
	 * 
	 * @param loanAmount
	 * @param interestRate
	 * @param loanPeriod
	 */
	public ArmotizationSchedule(double loanAmount, double interestRate, int loanPeriod) {
		this.loanAmount = loanAmount;
		this.interestRate = interestRate;
		this.loanPeriod = loanPeriod;
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public int getLoanPeriod() {
		return loanPeriod;
	}

	public void setLoanPeriod(int loanPeriod) {
		this.loanPeriod = loanPeriod;
	}

	public double getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(double monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	public double getTotalInterest() {
		return totalInterest;
	}

	public void setTotalInterest(double totalInterest) {
		this.totalInterest = totalInterest;
	}

	public double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(double totalPayment) {
		this.totalPayment = totalPayment;
	}

	public List<LoanPayment> getLoanPayments() {
		return loanPayments;
	}

	public void setLoanPayments(List<LoanPayment> loanPayments) {
		this.loanPayments = loanPayments;
	}

	@Override
	public String toString() {
		return String.format(
				"ArmotizationSchedule [loanAmount=%s, interestRate=%s, loanPeriod=%s, monthlyPayment=%s, totalInterest=%s, totalPayment=%s, loanPayments=%s]",
				loanAmount, interestRate, loanPeriod, monthlyPayment, totalInterest, totalPayment, loanPayments);
	}
}
