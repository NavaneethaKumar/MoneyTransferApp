package com.banking.controllers;

import com.banking.domain.Account;
import com.banking.dto.AccountDetailsDTO;
import com.banking.exception.BankingException;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

@RunWith(VertxUnitRunner.class)
public class MoneyTransferControllerTest {

  private Vertx vertx;
  private int port = 8081;

  @Before
  public void setUp(TestContext context) throws IOException {

    vertx = Vertx.vertx();
    ServerSocket socket = new ServerSocket(0);
    port = socket.getLocalPort();
    socket.close();

    DeploymentOptions options =
        new DeploymentOptions().setConfig(new JsonObject().put("HTTP_PORT", port));
    vertx.deployVerticle(
        MoneyTransferController.class.getName(), options, context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void smokeTestMyApp(TestContext context) {
    final Async async = context.async();

    vertx
        .createHttpClient()
        .getNow(
            port,
            "localhost",
            "/",
            response ->
                response.handler(
                    body -> {
                      context.assertTrue(body.toString().contains("<h1>Core Banking APP</h1>"));
                      async.complete();
                    }));
  }

  @Test
  public void test_AddAccount(TestContext context) throws BankingException {
    final Async async = context.async();
    Account account = generateMockAccount();
    final String json = Json.encodePrettily(account);
    final String length = Integer.toString(json.length());
    vertx
        .createHttpClient()
        .post(port, "localhost", "/api/banking/accounts/")
        .putHeader("content-type", "application/json")
        .putHeader("content-length", length)
        .handler(
            response -> {
              context.assertEquals(response.statusCode(), 201);
              context.assertTrue(
                  response.headers().get("content-type").contains("application/json"));
              response.bodyHandler(
                  body -> {
                    final AccountDetailsDTO accountResponse =
                        Json.decodeValue(body.toString(), AccountDetailsDTO.class);
                    context.assertEquals(accountResponse.getName(), "navaneeth");
                    async.complete();
                  });
            })
        .write(json)
        .end();
  }

  @Test
  public void test_AddAccount_Failed(TestContext context) throws BankingException {
    final Async async = context.async();

    final String json =
        "{\\\"accountNumber\\\" : \\\"$$HDFC1234&\\\",\\\"name\\\" : \\\"navaneeth\\\",\\\"country\\\" : \\\"INDIA\\\",\\\"amount\\\" : 200.0,\\\"currency\\\" : \\\"INR\\\"}";
    final String length = Integer.toString(json.length());
    vertx
        .createHttpClient()
        .post(port, "localhost", "/api/banking/accounts/")
        .putHeader("content-type", "application/json")
        .putHeader("content-length", length)
        .handler(
            response -> {
              context.assertEquals(response.statusCode(), 400);
              async.complete();
            })
        .write(json)
        .end();
  }

  @Test
  public void test_getAccount_Invalid(TestContext context) throws BankingException {
    final Async async = context.async();
    Account account = generateMockAccount();
    final String json = Json.encodePrettily(account);
    final String length = Integer.toString(json.length());
    vertx
        .createHttpClient()
        .post(port, "localhost", "/api/banking/accounts/")
        .putHeader("content-type", "application/json")
        .putHeader("content-length", length)
        .handler(
            response -> {
              async.complete();
            })
        .write(json)
        .end();

    vertx
        .createHttpClient()
        .getNow(
            port,
            "localhost",
            "/api/banking/accounts/1234",
            response ->
                response.handler(
                    body -> {
                      context.assertEquals(response.statusCode(), 400);
                      async.complete();
                    }));
  }

  @Test
  public void test_getAccount(TestContext context) throws BankingException {
    final Async async = context.async();
    Account account = generateMockAccount();
    final String json = Json.encodePrettily(account);
    final String length = Integer.toString(json.length());
    vertx
        .createHttpClient()
        .post(port, "localhost", "/api/banking/accounts/")
        .putHeader("content-type", "application/json")
        .putHeader("content-length", length)
        .handler(
            response -> {
              async.complete();
            })
        .write(json)
        .end();

    vertx
        .createHttpClient()
        .getNow(
            port,
            "localhost",
            "/api/banking/accounts/HDFC1234",
            response ->
                response.handler(
                    body -> {
                      final AccountDetailsDTO accountResponse =
                          Json.decodeValue(body.toString(), AccountDetailsDTO.class);
                      context.assertEquals(response.statusCode(), 200);
                      context.assertEquals(accountResponse.getName(), "navaneeth");
                      async.complete();
                    }));
  }

  @Test
  public void test_getAllAccounts(TestContext context) throws BankingException {
    final Async async = context.async();
    Account account = generateMockAccount();
    final String json = Json.encodePrettily(account);
    final String length = Integer.toString(json.length());
    vertx
        .createHttpClient()
        .post(port, "localhost", "/api/banking/accounts/")
        .putHeader("content-type", "application/json")
        .putHeader("content-length", length)
        .handler(
            response -> {
              async.complete();
            })
        .write(json)
        .end();

    vertx
        .createHttpClient()
        .getNow(
            port,
            "localhost",
            "/api/banking/accounts/",
            response ->
                response.handler(
                    body -> {
                      final List<AccountDetailsDTO> accountResponse =
                          Json.decodeValue(body.toString(), List.class);
                      context.assertEquals(response.statusCode(), 200);
                      context.assertEquals(accountResponse.size(), 1);
                      async.complete();
                    }));
  }

  @Test
  public void test_updateAccount_Failed(TestContext context) throws BankingException {
    final Async async = context.async();
    Account account = generateMockAccount();
    final String json = Json.encodePrettily(account);
    final String length = Integer.toString(json.length());
    vertx
        .createHttpClient()
        .post(port, "localhost", "/api/banking/accounts/")
        .putHeader("content-type", "application/json")
        .putHeader("content-length", length)
        .handler(
            response -> {
              async.complete();
            })
        .write(json)
        .end();

    vertx
        .createHttpClient()
        .put(
            port,
            "localhost",
            "/api/banking/accounts/HDFC12345/200",
            response ->
                response.handler(
                    body -> {
                      context.assertEquals(response.statusCode(), 400);
                      async.complete();
                    }))
                    .end();
  }

  @Test
  public void test_updateAccount(TestContext context) throws BankingException {
    final Async async = context.async();
    Account account = generateMockAccount();
    final String json = Json.encodePrettily(account);
    final String length = Integer.toString(json.length());
    vertx
        .createHttpClient()
        .post(port, "localhost", "/api/banking/accounts/")
        .putHeader("content-type", "application/json")
        .putHeader("content-length", length)
        .handler(
            response -> {
              async.complete();
            })
        .write(json)
        .end();

    vertx
        .createHttpClient()
        .put(
            port,
            "localhost",
            "/api/banking/accounts/HDFC1234/200",
            response ->
                response.handler(
                    body -> {
                      context.assertEquals(response.statusCode(), 204);
                      async.complete();
                    }))
                    .end();
  }

  @Test
  public void test_DeleteAccount_Failed(TestContext context) throws BankingException {
    final Async async = context.async();
    Account account = generateMockAccount();
    final String json = Json.encodePrettily(account);
    final String length = Integer.toString(json.length());
    vertx
        .createHttpClient()
        .post(port, "localhost", "/api/banking/accounts/")
        .putHeader("content-type", "application/json")
        .putHeader("content-length", length)
        .handler(
            response -> {
              async.complete();
            })
        .write(json)
        .end();

    vertx
        .createHttpClient()
        .delete(
            port,
            "localhost",
            "/api/banking/accounts/HDFC1234/ICICI123/100",
            response ->
                response.handler(
                    body -> {
                      context.assertEquals(response.statusCode(), 400);
                      async.complete();
                    }));
  }

  @Test
  public void test_DeleteAccount(TestContext context) throws BankingException {
    final Async async = context.async();
    Account account = generateMockAccount();
    final String json = Json.encodePrettily(account);
    final String length = Integer.toString(json.length());
    vertx
        .createHttpClient()
        .post(port, "localhost", "/api/banking/accounts/")
        .putHeader("content-type", "application/json")
        .putHeader("content-length", length)
        .handler(
            response -> {
              async.complete();
            })
        .write(json)
        .end();

    vertx
        .createHttpClient()
        .delete(
            port,
            "localhost",
            "/api/banking/accounts/HDFC1234",
            response ->
                response.handler(
                    body -> {
                      context.assertEquals(response.statusCode(), 204);
                      async.complete();
                    }))
                    .end();
  }

  @Test
  public void test_Transfer_Account(TestContext context) throws BankingException {
    final Async async = context.async();
    Account account = generateMockAccount();
    Account account1 = generateMockAccount();
    account1.setAccountNumber("ICICI123");
    account1.setAmount(500);
    final String json = Json.encodePrettily(account);
    final String length = Integer.toString(json.length());
    final String json1 = Json.encodePrettily(account1);
    final String length1 = Integer.toString(json.length());


    vertx
      .createHttpClient()
      .post(port, "localhost", "/api/banking/accounts/")
      .putHeader("content-type", "application/json")
      .putHeader("content-length", length)
      .handler(
        response -> {
          async.complete();
        })
      .write(json)
      .end();
    vertx
      .createHttpClient()
      .post(port, "localhost", "/api/banking/accounts/")
      .putHeader("content-type", "application/json")
      .putHeader("content-length", length1)
      .handler(
        response -> {
          async.complete();
        })
      .write(json1)
      .end();


    vertx
        .createHttpClient()
        .post(
            port,
            "localhost",
            "/api/banking/transaction/HDFC1234/ICICI123/200",
            response ->
                response.handler(
                    body -> {
                      context.assertEquals(response.statusCode(), 201);
                      async.complete();
                    }))
                    .end();
  }

  @Test
  public void test_Transfer_Account_failedWithInsufficientFunds(TestContext context) throws BankingException {
    final Async async = context.async();
    Account account = generateMockAccount();
    Account account1 = generateMockAccount();
    account1.setAccountNumber("ICICI123");
    account1.setAmount(500);
    final String json = Json.encodePrettily(account);
    final String length = Integer.toString(json.length());
    final String json1 = Json.encodePrettily(account1);
    final String length1 = Integer.toString(json1.length());

    vertx
      .createHttpClient()
      .post(port, "localhost", "/api/banking/accounts/")
      .putHeader("content-type", "application/json")
      .putHeader("content-length", length)
      .handler(
        response -> {
          async.complete();
        })
      .write(json)
      .end();
    vertx
      .createHttpClient()
      .post(port, "localhost", "/api/banking/accounts/")
      .putHeader("content-type", "application/json")
      .putHeader("content-length", length1)
      .handler(
        response -> {
          async.complete();
        })
      .write(json1)
      .end();

    vertx
      .createHttpClient()
      .post(
        port,
        "localhost",
        "/api/banking/transaction/HDFC1234/ICICI123/1000",
        response ->
          response.handler(
            body -> {
              context.assertEquals(response.statusCode(), 400);
              async.complete();
            }))
         .end();
  }

  private Account generateMockAccount() throws BankingException {
    Account account = new Account();
    account.setAccountNumber("HDFC1234");
    account.setAmount(500);
    account.setCountry("INDIA");
    account.setCurrency("INR");
    account.setName("navaneeth");
    return account;
  }
}
