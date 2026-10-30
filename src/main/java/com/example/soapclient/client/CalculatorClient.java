package com.example.soapclient.client;

import com.example.sample.calculator.wsdl.Add;
import com.example.sample.calculator.wsdl.AddResponse;
import com.example.sample.calculator.wsdl.Divide;
import com.example.sample.calculator.wsdl.DivideResponse;
import com.example.sample.calculator.wsdl.Multiply;
import com.example.sample.calculator.wsdl.MultiplyResponse;
import com.example.sample.calculator.wsdl.Subtract;
import com.example.sample.calculator.wsdl.SubtractResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.util.Objects;

@Component
public class CalculatorClient {

    private static final String SOAP_ACTION_ADD = "http://tempuri.org/Add";
    private static final String SOAP_ACTION_SUBTRACT = "http://tempuri.org/Subtract";
    private static final String SOAP_ACTION_MULTIPLY = "http://tempuri.org/Multiply";
    private static final String SOAP_ACTION_DIVIDE = "http://tempuri.org/Divide";

    private final WebServiceTemplate calculatorTemplate;

    public CalculatorClient(@Qualifier("calculatorTemplate") WebServiceTemplate calculatorTemplate) {
        this.calculatorTemplate = calculatorTemplate;
    }

    public int add(int a, int b) {
        Add request = new Add();
        request.setIntA(a);
        request.setIntB(b);

        AddResponse response = (AddResponse) calculatorTemplate.marshalSendAndReceive(request, new SoapActionCallback(SOAP_ACTION_ADD));
        return Objects.requireNonNull(response).getAddResult();
    }

    public int subtract(int a, int b) {
        Subtract request = new Subtract();
        request.setIntA(a);
        request.setIntB(b);

        SubtractResponse response = (SubtractResponse) calculatorTemplate.marshalSendAndReceive(request, new SoapActionCallback(SOAP_ACTION_SUBTRACT));
        return Objects.requireNonNull(response).getSubtractResult();
    }

    public int multiply(int a, int b) {
        Multiply request = new Multiply();
        request.setIntA(a);
        request.setIntB(b);

        MultiplyResponse response = (MultiplyResponse) calculatorTemplate.marshalSendAndReceive(request, new SoapActionCallback(SOAP_ACTION_MULTIPLY));
        return Objects.requireNonNull(response).getMultiplyResult();
    }

    /**
     * 서버는 결과를 버림이 아니라 반올림한다(7 / 2 = 4). 0으로 나누면 SOAP Fault를 돌려주므로 호출 전에 막는다.
     */
    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("0으로 나눌 수 없습니다.");
        }

        Divide request = new Divide();
        request.setIntA(a);
        request.setIntB(b);

        DivideResponse response = (DivideResponse) calculatorTemplate.marshalSendAndReceive(request, new SoapActionCallback(SOAP_ACTION_DIVIDE));
        return Objects.requireNonNull(response).getDivideResult();
    }
}
