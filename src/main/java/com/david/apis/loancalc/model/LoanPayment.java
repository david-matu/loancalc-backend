package com.david.apis.loancalc.model;

public class LoanPayment {
	private int paymentMonth;
	private double fixedPayment;
	private double principal;
	private double interest;
	private double totalInterest;
	private double endingBalance;
	
	public LoanPayment() {
	}
	
	/**
	 * Use this construct to instantly fill the fields with data. will avoid instantiating the class in some case to reduce footprint
	 *  
	 * @param paymentMonth
	 * @param fixedPayment
	 * @param principal
	 * @param interest
	 * @param totalInterest
	 * @param endingBalance
	 */
	public LoanPayment(int paymentMonth, double fixedPayment, double principal, double interest, double totalInterest, double endingBalance) {
		this.paymentMonth = paymentMonth;
		this.fixedPayment = fixedPayment;
		this.principal = principal;
		this.interest = interest;
		this.totalInterest = totalInterest;
		this.endingBalance = endingBalance;
	}

	public int getPaymentMonth() {
		return paymentMonth;
	}

	public void setPaymentMonth(int paymentMonth) {
		this.paymentMonth = paymentMonth;
	}

	public double getFixedPayment() {
		return fixedPayment;
	}

	public void setFixedPayment(double fixedPayment) {
		this.fixedPayment = fixedPayment;
	}

	public double getPrincipal() {
		return principal;
	}

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getTotalInterest() {
		return totalInterest;
	}

	public void setTotalInterest(double totalInterest) {
		this.totalInterest = totalInterest;
	}

	public double getEndingBalance() {
		return endingBalance;
	}

	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}

	@Override
	public String toString() {
		return String.format(
				"LoanPayment [paymentMonth=%s, fixedPayment=%s, principal=%s, interest=%s, totalInterest=%s, endingBalance=%s]",
				paymentMonth, fixedPayment, principal, interest, totalInterest, endingBalance);
	}
}
