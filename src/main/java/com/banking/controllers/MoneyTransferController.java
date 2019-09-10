package com.banking.controllers;


import com.banking.domain.Account;
import com.banking.dto.AccountDetailsDTO;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.beanutils.BeanUtils;

public class MoneyTransferController extends AbstractVerticle  {

    @Override
    public void start(Future<Void> future) {

        Router router = Router.router(vertx);
        router.get("/api/baeldung/articles/article/:id")
                .handler(this::getArticles);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("http.port", 8080), result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });
    }


    private void getArticles(RoutingContext routingContext) {
        String articleId = routingContext.request()
                .getParam("id");

        //BeanUtils.copyProperties(new AccountDetailsDTO(),new Account());

        /*routingContext.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(200)
                .end(Json.encodePrettily(article));*/
    }


}
