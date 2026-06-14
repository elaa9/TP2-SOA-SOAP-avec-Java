package com.example.bank.service;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT)
public class UnknownAccountException extends RuntimeException {
  public UnknownAccountException(String message) {
    super(message);
  }
}
