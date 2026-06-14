package com.example.bank.endpoint;

import java.math.BigDecimal;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import com.example.bank.ws.CreateAccountRequest;
import com.example.bank.ws.CreateAccountResponse;

import com.example.bank.service.BankService;
import com.example.bank.service.BankService.Account;
import com.example.bank.service.UnknownAccountException;
import com.example.bank.ws.AccountType;
import com.example.bank.ws.DepositRequest;
import com.example.bank.ws.DepositResponse;
import com.example.bank.ws.GetAccountRequest;
import com.example.bank.ws.GetAccountResponse;
import com.example.bank.ws.TransferRequest;
import com.example.bank.ws.TransferResponse;
import com.example.bank.ws.WithdrawRequest;
import com.example.bank.ws.WithdrawResponse;
@Endpoint
public class BankEndpoint {

  private static final String NAMESPACE_URI = "http://example.com/bank";
  private final BankService bankService;

  public BankEndpoint(BankService bankService) {
    this.bankService = bankService;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAccountRequest")
  @ResponsePayload
  public GetAccountResponse getAccount(@RequestPayload GetAccountRequest request) {
    Account acc = bankService.getAccount(request.getAccountId());
    if (acc == null) {
      throw new UnknownAccountException("Unknown accountId: " + request.getAccountId());
    }

    AccountType dto = new AccountType();
    dto.setAccountId(acc.accountId);
    dto.setOwner(acc.owner);
    dto.setBalance(acc.balance);
    dto.setCurrency(acc.currency);

    GetAccountResponse resp = new GetAccountResponse();
    resp.setAccount(dto);
    return resp;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DepositRequest")
  @ResponsePayload
  public DepositResponse deposit(@RequestPayload DepositRequest request) {
    BigDecimal newBalance = bankService.deposit(request.getAccountId(), request.getAmount());
    DepositResponse resp = new DepositResponse();
    resp.setNewBalance(newBalance);
    return resp;
  }
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "WithdrawRequest")
  @ResponsePayload
  public WithdrawResponse withdraw(@RequestPayload WithdrawRequest request) {
    BigDecimal newBalance = bankService.withdraw(request.getAccountId(), request.getAmount());
    WithdrawResponse resp = new WithdrawResponse();
    resp.setNewBalance(newBalance);
    return resp;
  }
  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TransferRequest")
@ResponsePayload
public TransferResponse transfer(@RequestPayload TransferRequest request) {

    bankService.transfer(
            request.getFromAccountId(),
            request.getToAccountId(),
            request.getAmount()
    );

    BigDecimal fromBalance =
            bankService.getAccount(request.getFromAccountId()).balance;

    BigDecimal toBalance =
            bankService.getAccount(request.getToAccountId()).balance;

    TransferResponse resp = new TransferResponse();
    resp.setFromNewBalance(fromBalance);
    resp.setToNewBalance(toBalance);

    return resp;
}
@PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateAccountRequest")
@ResponsePayload
public CreateAccountResponse createAccount(
        @RequestPayload CreateAccountRequest request) {

    BankService.Account acc = bankService.createAccount(
            request.getAccountId(),
            request.getOwner(),
            request.getInitialBalance(),
            request.getCurrency()
    );

    AccountType dto = new AccountType();
    dto.setAccountId(acc.accountId);
    dto.setOwner(acc.owner);
    dto.setBalance(acc.balance);
    dto.setCurrency(acc.currency);

    CreateAccountResponse resp = new CreateAccountResponse();
    resp.setAccount(dto);

    return resp;
}



}
