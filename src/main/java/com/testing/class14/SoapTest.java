package com.testing.class14;

import com.testing.common.AutoLogger;
import com.testing.inter.InterKeyWord;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoapTest {
    public static void main(String[] args) {
        InterKeyWord soap=new InterKeyWord();
        soap.doPost("http://www.testingedu.com.cn:8081/inter/SOAP?wsdl","<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.testingedu.com/\"><soapenv:Header/><soapenv:Body><soap:auth></soap:auth></soapenv:Body></soapenv:Envelope>","text");
        soap.parseSOAP("<return>(.*?)</return>");
        soap.saveParam("token","$.token");
        soap.useHeader("{\"token\":\"{token}\"}");
        soap.doPost("http://www.testingedu.com.cn:8081/inter/SOAP?wsdl","<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.testingedu.com/\"><soapenv:Header/><soapenv:Body><soap:login><arg0>roy91</arg0><arg1>123456</arg1></soap:login></soapenv:Body></soapenv:Envelope>","text");
        soap.parseSOAP("<return>(.*?)</return>");
        soap.saveParam("id","$.userid");
        soap.doPost("http://www.testingedu.com.cn:8081/inter/SOAP?wsdl","<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.testingedu.com/\"><soapenv:Header/><soapenv:Body><soap:getUserInfo><arg0>{id}</arg0></soap:getUserInfo></soapenv:Body></soapenv:Envelope>","text");
        soap.parseSOAP("<return>(.*?)</return>");
        soap.doPost("http://www.testingedu.com.cn:8081/inter/SOAP?wsdl","<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.testingedu.com/\"><soapenv:Header/><soapenv:Body><soap:logout></soap:logout></soapenv:Body></soapenv:Envelope>","text");
        soap.parseSOAP("<return>(.*?)</return>");
    }

}
