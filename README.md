### MoneyTransferApp

Builded with Vertx

###1. Technologies
Vertx 3.7.0
Maven 3.7.0
Java 8
###2. To Run this project locally
mvn clean package vertx:run
###3. Money Tansfer Apis
      POST:
      http://localhost:8088/api/banking/accounts
      {
        "accountNumber":"HDFC",
        "name":"John",
        "country":"INDIA",
        "currency":"INR",
        "amount":"100"
      }
      GET :
      http://localhost:8088/api/banking/accounts/HDFC
      GET all:
      http://localhost:8088/api/banking/accounts
      DELETE :
      http://localhost:8088/api/banking/accounts/HDFC
      UPDATE :
      http://localhost:8088/api/banking/accounts/HDFC/500
      POST Transaction :For Transaction need to add atleast two accounts 
      http://localhost:8088/api/banking/transaction/HDFC/ICICI/1000
      
###4 : Apis has been exposed which scales,handles concurrency, no worries about thread block all builded with vertx.
###5 : Clean Up,Logging,Unit tested,Integration tested.
###6 : Code Coverage > 90%
