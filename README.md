## Loancalc API

> Crafted in __Spring Boot__

___

This API serves the [frontend React client](https://github.com/david-matu/react-loancalc), by receiving loan information (loan amount, interest rate and loan period in months).


The API returns  a summary of the computed loan, and an armotization schedule for the loan repayment.


![high-level architecture overview](./LoanCalc-arch.drawio.png "Architecture overview")


Take a look at the following request and response:

__request:__
```json
{
  "loanAmount": 5000,
  "interestRate": 12.38,
  "loanPeriod": 12
}
```

__response:__
```json
{
    "loanAmount": 5000.0,
    "interestRate": 12.38,
    "loanPeriod": 12,
    "monthlyPayment": 445.13,
    "totalInterest": 341.6,
    "totalPayment": 5341.56,
    "loanPayments": [
        {
            "paymentMonth": 1,
            "fixedPayment": 445.13,
            "principal": 393.55,
            "interest": 51.58,
            "totalInterest": 51.58,
            "endingBalance": 4606.45
        },
        {
            "paymentMonth": 2,
            "fixedPayment": 445.13,
            "principal": 397.61,
            "interest": 47.52,
            "totalInterest": 99.11,
            "endingBalance": 4208.85
        },
        {
            "paymentMonth": 3,
            "fixedPayment": 445.13,
            "principal": 401.71,
            "interest": 43.42,
            "totalInterest": 142.53,
            "endingBalance": 3807.14
        },
        {
            "paymentMonth": 4,
            "fixedPayment": 445.13,
            "principal": 405.85,
            "interest": 39.28,
            "totalInterest": 181.8,
            "endingBalance": 3401.28
        },
        {
            "paymentMonth": 5,
            "fixedPayment": 445.13,
            "principal": 410.04,
            "interest": 35.09,
            "totalInterest": 216.89,
            "endingBalance": 2991.24
        },
        {
            "paymentMonth": 6,
            "fixedPayment": 445.13,
            "principal": 414.27,
            "interest": 30.86,
            "totalInterest": 247.75,
            "endingBalance": 2576.97
        },
        {
            "paymentMonth": 7,
            "fixedPayment": 445.13,
            "principal": 418.54,
            "interest": 26.59,
            "totalInterest": 274.34,
            "endingBalance": 2158.43
        },
        {
            "paymentMonth": 8,
            "fixedPayment": 445.13,
            "principal": 422.86,
            "interest": 22.27,
            "totalInterest": 296.61,
            "endingBalance": 1735.57
        },
        {
            "paymentMonth": 9,
            "fixedPayment": 445.13,
            "principal": 427.22,
            "interest": 17.91,
            "totalInterest": 314.51,
            "endingBalance": 1308.34
        },
        {
            "paymentMonth": 10,
            "fixedPayment": 445.13,
            "principal": 431.63,
            "interest": 13.5,
            "totalInterest": 328.01,
            "endingBalance": 876.71
        },
        {
            "paymentMonth": 11,
            "fixedPayment": 445.13,
            "principal": 436.09,
            "interest": 9.04,
            "totalInterest": 337.06,
            "endingBalance": 440.63
        },
        {
            "paymentMonth": 12,
            "fixedPayment": 445.13,
            "principal": 440.58,
            "interest": 4.55,
            "totalInterest": 341.6,
            "endingBalance": 0.04
        }
    ]
}
```
The API is protected with basic auth (equivalent of client credentials scheme). The client passes these in the request header:
```cmd
curl --location 'http://localhost:8080/calc/loan-armotization' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic cmVzaWxpZW50OnB3ZDEyMw==' \
--data '{
  "loanAmount": 5000,
  "interestRate": 12.38,
  "loanPeriod": 12
}'
```


### Architecture
____

at the heart of this API, lies the business function: __LoanCalcService__ 
which is a Spring Service. The bean will be injectable at any place of need, such as the controller as we'll see.

```java
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
		
		// set initial remaining balance
		double remainingBalance = loanAmount;
		
		// set initial interest amount
		double totalInterest = 0;
		
		List<LoanPayment> repayments = new ArrayList<>();
		for(int month = 1; month <= loanPeriodMonths; month++) {
			double interestPayment = remainingBalance * monthlyInterestRate; // get the interest amount for the month
			double principalPayment = monthlyPayment - interestPayment; 	// calculate repayment amount
			remainingBalance -= principalPayment;
			
			// add up total interest
			totalInterest += interestPayment;
			
			// using the one-time constructor to create the loan object and feed it directly to the list (without ever allocating a memory/variable) 
			repayments.add(new LoanPayment(month, formatAmount(monthlyPayment), formatAmount(principalPayment), formatAmount(interestPayment), formatAmount(totalInterest), formatAmount(remainingBalance)));
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

```

Here's how the __LoanCalcService__ has been applied:

__LoanCalcAPI:__
```java
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
@CrossOrigin
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

```

Above is the REST controller that will serve requests to the client.


Additionally, the API is secured with Spring Security's basic auth so that only our client, [React frontend][https://github.com/david-matu/react-loancalc], should access this API

This follows a suite of setups:

1. __SecurityConfig__
```java
package com.david.apis.loancalc.configs;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.GET;

import static org.springframework.security.config.Customizer.withDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.http.HttpMethod.OPTIONS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig implements WebFluxConfigurer {
	private static Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);
	
	private String username;
	private String password;
	
	
	public SecurityConfig(@Value("${app.username}") String username, @Value("${app.password}") String password) {
		this.username = username;
		this.password = password;
		
		LOG.info("Client Credentials loaded from props file: {}:{}", username, password);
	}
	
	
	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
			.csrf(c -> c.disable())
			.cors(c -> c.disable())
			.authorizeExchange((exchanges) ->
            	exchanges
            		.pathMatchers("/openapi/**").permitAll()
            		.pathMatchers("/webjars/**").permitAll()
            		.pathMatchers("/swagger-ui/**").permitAll()
            		.pathMatchers(OPTIONS).permitAll()
	            	// any URL that starts with /calc/ requires the role "ROLE_USER"
	            	.pathMatchers(POST, "/calc/**").hasRole("USER")
            )
			.httpBasic(withDefaults());
		
		return http.build();
	}
	
	@Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
          .allowedOrigins("http://localhost:3000", "https://loancalc-dave.netlify.app")
          .allowedMethods("*")
          .maxAge(3600);
    }
	
	@Bean
	CorsWebFilter corsFilter() {
		CorsConfiguration conf = new CorsConfiguration();
		
		conf.setAllowCredentials(true);
		conf.addAllowedOrigin("http://localhost:3000");
		conf.addAllowedOrigin("https://loancalc-dave.netlify.app");
		conf.addAllowedHeader("*");
		conf.addAllowedMethod("*");
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", conf);
		
		return new CorsWebFilter(source);
	}

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder()
			.username(username)
			.password(password)
			.roles("USER")
			.build();
		return new MapReactiveUserDetailsService(user);
	}
}

```
The class setups an in-memory user with details passed from the properties file (username, password): `userDetailsService()`.  

The `springSecurityFilterChain()` method setups request filters that will be applied based on what request.


To access Swagger documentation: [http://localhost:8080/openapi/webjars/swagger-ui/index.html](http://localhost:8080/openapi/webjars/swagger-ui/index.html)