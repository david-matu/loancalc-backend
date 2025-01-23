package com.david.apis.loancalc.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.david.apis.loancalc.model.AppRuntimeException;
import com.david.apis.loancalc.model.ArmotizationSchedule;
import com.david.apis.loancalc.model.LoanParams;
import com.david.apis.loancalc.service.LoanCalcService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@Tag(name = "Loan Calculator Service", description = "This API will handle calculation for loans")
//@CrossOrigin(origins = {"http://localhost:3000", "https://loancalc-dave.netlify.app"}, allowedHeaders = "*")
@RestController
public class LoanCalcAPI {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoanCalcAPI.class);

	private final LoanCalcService loanCalcService;
	
	public LoanCalcAPI(LoanCalcService loanCalcService) {
		this.loanCalcService = loanCalcService; 
	}
	
	@Operation(
			summary = "Generate an Armotization Schedule",
			description = "This resource receives loan details (loan amount, interest rate and loan period). Returns loan Summary and Armotization Schedule for the loan")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Request served successfully"),
			@ApiResponse(responseCode = "400", description = "The request body was malformed"),
			@ApiResponse(responseCode = "404", description = "No API found on given path to serve the request. Ensure you are calling the correct API endpoint"),
			@ApiResponse(responseCode = "500", description = "An error occurred while serving request. Internal team will check and debig issue.")
	})
	@PostMapping(value = "/calc/loan-armotization", consumes = "application/json", produces = "application/json")
	public Mono<ArmotizationSchedule> getLoanArmotizationSchedule(@RequestBody LoanParams loanParam) {
		LOG.info("Received loan armotization request: {}", loanParam.toString());
		
		ArmotizationSchedule sched = this.loanCalcService.getArmotizationSchedule(loanParam.getLoanAmount(), loanParam.getInterestRate(), loanParam.getLoanPeriod());
		
		if(sched != null) {
			return Mono.just(sched);
		} else {
			throw new AppRuntimeException("An exception occurred processing load armotization for: " + loanParam.toString());
		}
	}
}
