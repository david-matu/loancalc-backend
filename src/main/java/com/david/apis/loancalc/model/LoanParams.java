package com.david.apis.loancalc.model;

public class LoanParams {
	private double loanAmount;
	private double interestRate;
	private int loanPeriod;
	
	public LoanParams() {
		super();
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

	@Override
	public String toString() {
		return String.format("LoanParams [loanAmount=%s, interestRate=%s, loanPeriod=%s]", loanAmount, interestRate,
				loanPeriod);
	} 
}
