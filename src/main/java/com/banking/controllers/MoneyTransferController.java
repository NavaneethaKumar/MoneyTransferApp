package com.banking.controllers;

import com.banking.config.DIConfig;
import com.banking.domain.Account;
import com.banking.dto.AccountDetailsDTO;
import com.banking.exception.BankingException;
import com.banking.services.BankingServiceImpl;
import com.banking.services.IBankingService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/** Controller which exposes all required apis which handles all request and responses. */
public class MoneyTransferController extends AbstractVerticle {

  public static final String CONTENT_TYPE = "content-type";
  public static final String APPLICATION_JSON = "application/json";
  public static final String ADD_ACCOUNT = "addAccount";
  public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
  private static final Logger logger = LoggerFactory.getLogger(MoneyTransferController.class);

  @Inject
  private IBankingService bankingService;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    bankingService = Guice
      .createInjector(new DIConfig())
      .getInstance(IBankingService.class);
  }

  @Override
  public void start(Future<Void> fut) {
    // Create a router object.
    Router router = Router.router(vertx);
    router
        .route("/")
        .handler(
            routingContext -> {
              HttpServerResponse response = routingContext.response();
              response.putHeader(CONTENT_TYPE, "text/html").end("<h1>Core Banking APP</h1>");
            });
    router
        .route()
        .handler(
            ctx -> {
              ctx.response().putHeader("Cache-Control", "no-store, no-cache");
              ctx.next();
            });

    // Api -Routes for CRUD
    router.get("/api/banking/accounts/").handler(this::getAllAccounts);
    router.get("/api/banking/accounts/:id").handler(this::getAccount);
    router.route("/api/banking/accounts*").handler(BodyHandler.create());
    router
        .post("/api/banking/accounts/")
        .produces(APPLICATION_JSON)
        .consumes(APPLICATION_JSON)
        .handler(this::addAccount);
    router.delete("/api/banking/accounts/:id").handler(this::deleteAccount);
    router.put("/api/banking/accounts/:id/:amount").handler(this::updateAccount);
    router.route("/api/banking/transaction*").handler(BodyHandler.create());
    router.post("/api/banking/transaction/:from/:to/:amount").handler(this::moneyTransfer);

    ConfigRetriever retriever = ConfigRetriever.create(vertx);
    retriever.getConfig(
        config -> {
          if (config.failed()) {
            fut.fail(config.cause());
          } else {
            // Create the HTTP server and pass the "accept" method to the request handler.
            vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                    config.result().getInteger("HTTP_PORT", 8088),
                    result -> {
                      if (result.succeeded()) {
                        fut.complete();
                      } else {
                        fut.fail(result.cause());
                      }
                    });
          }
        });
  }

  /**
   * Returns AllAccounts
   *
   * @param routingContext
   */
  private void getAllAccounts(RoutingContext routingContext) {
    logger.info("getAllAccounts");
    try {
      routingContext
          .response()
          .putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
          .end(Json.encodePrettily(bankingService.getAllAccountDetails()));
    } catch (BankingException ex) {
      String message =
          MessageFormat.format("getAllAccounts {0} {1}", ex.getHttpStatusCode(), ex.getMessage());
      logger.error(message);
      routingContext.response().setStatusCode(400).end();
    }
  }

  /**
   * AddAccount
   *
   * @param routingContext
   */
  private void addAccount(RoutingContext routingContext) {
    logger.info(ADD_ACCOUNT);
    AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
    try {
      Account account = routingContext.getBodyAsJson().mapTo(Account.class);
      BeanUtils.copyProperties(accountDetailsDTO, account);
      bankingService.register(accountDetailsDTO);
      routingContext
          .response()
          .setStatusCode(201)
          .putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
          .end(Json.encodePrettily(accountDetailsDTO));

    } catch (Exception ex) {
      logger.error(ADD_ACCOUNT, ex.getMessage());
      routingContext.response().setStatusCode(400).end();
    }
  }

  /**
   * DeleteAccount
   *
   * @param routingContext
   */
  private void deleteAccount(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    logger.info("deleteAccount");
    try {
      bankingService.deleteAccount(id);
      routingContext.response().setStatusCode(204).end();
    } catch (BankingException ex) {
      String message =
          MessageFormat.format("deleteAccount {0} {1}", ex.getHttpStatusCode(), ex.getMessage());
      logger.error(message);
      routingContext.response().setStatusCode(400).end();
    }
  }

  /**
   * GetAccount Details
   *
   * @param routingContext
   */
  private void getAccount(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    logger.info("getAccount");
    try {
      AccountDetailsDTO accountDetails = bankingService.getAccountDetails(id);
      routingContext
          .response()
          .setStatusCode(200)
          .putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
          .end(Json.encodePrettily(accountDetails));

    } catch (BankingException ex) {
      String message =
          MessageFormat.format("getAccount {0} {1}", ex.getHttpStatusCode(), ex.getMessage());
      logger.error(message);
      routingContext.response().setStatusCode(404).end();
    }
  }

  /**
   * Update Account Details
   *
   * @param routingContext
   */
  private void updateAccount(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    String amount = routingContext.request().getParam("amount");
    logger.info("updateAccount");
    try {
      bankingService.deposit(id, amount != null ? Double.parseDouble(amount) : null);
      routingContext
          .response()
          .setStatusCode(204)
          .putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
          .end();
    } catch (BankingException ex) {
      String message =
          MessageFormat.format("updateAccount {0} {1}", ex.getHttpStatusCode(), ex.getMessage());
      logger.error(message);
      routingContext.response().setStatusCode(400).end();
    }
  }

  /** Transfer Funds
   * * @param routingContext */
  private void moneyTransfer(RoutingContext routingContext) {

    String amount = routingContext.request().getParam("amount");
    String fromAccount = routingContext.request().getParam("from");
    String toAccount = routingContext.request().getParam("to");
    logger.info("moneyTransfer");
    try {
      AccountDetailsDTO accountDetailsDTO =
          bankingService.transfer(
              fromAccount, toAccount, amount != null ? Double.parseDouble(amount) : null);
      routingContext
          .response()
          .setStatusCode(201)
          .putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
          .end(Json.encodePrettily(accountDetailsDTO));
    } catch (BankingException ex) {
      String message =
          MessageFormat.format("moneyTransfer {0} {1}", ex.getHttpStatusCode(), ex.getMessage());
      logger.error(message);
      routingContext.response().setStatusCode(400).setStatusMessage(ex.getMessage()).end();
    }
  }
}
