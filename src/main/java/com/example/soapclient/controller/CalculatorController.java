package com.example.soapclient.controller;

import com.example.soapclient.client.CalculatorClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final CalculatorClient calculatorClient;

    public CalculatorController(CalculatorClient calculatorClient) {
        this.calculatorClient = calculatorClient;
    }

    @GetMapping("/add")
    public CalculationResult add(@RequestParam int a, @RequestParam int b) {
        return new CalculationResult("add", a, b, calculatorClient.add(a, b));
    }

    @GetMapping("/subtract")
    public CalculationResult subtract(@RequestParam int a, @RequestParam int b) {
        return new CalculationResult("subtract", a, b, calculatorClient.subtract(a, b));
    }

    @GetMapping("/multiply")
    public CalculationResult multiply(@RequestParam int a, @RequestParam int b) {
        return new CalculationResult("multiply", a, b, calculatorClient.multiply(a, b));
    }

    @GetMapping("/divide")
    public CalculationResult divide(@RequestParam int a, @RequestParam int b) {
        return new CalculationResult("divide", a, b, calculatorClient.divide(a, b));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    public record CalculationResult(String operation, int a, int b, int result) {}
}
