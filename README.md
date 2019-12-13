# Aggregator for Power of Attorney Service
This WS provides REST API for accessing aggregated power of attorney information of a user
  - list of aggregated power of attorneys for a given user (/power-of-attorneys?user=name).

**To build and run:**
  - import project into your IDE;
  - build it;
  - and run nl.rabobank.powerofattorney.stub.JsonStub + com.ooo.poa.aggregator.ws.app.AggregatorApplication.

**Power of Attorney app runs on:** http://localhost:8080

**Aggregator app runs on:** http://localhost:8082

**Get aggregated power of attorneys:**
  - send HTTP GET request to http://localhost:8082/aggregator/power-of-attorneys?user=Super%20duper%20employee
  - value of "user" query param has to match grantor or grantee from one of power of attorneys.

**SOAP UI project:**
  there is SOAP UI project available in poa-aggregator-ws/src/test/resources/POA-soapui-project.xml
  which you can use for testing.

# Changes in original Power of Attorney Service
  - changed "owner" field of account\123123123 from "uper duper employee" to "Super duper employee" as looked incorrect;
  - changed direction in poa\0004 as it looked incorrect ("Super duper employee" got poa for his own account);
  - added "number" and "iban" fields to account entity;
  - changed comment in nl.rabobank.powerofattorney.stub.JsonStub -> line #81 as it looked like copy-paste error;
  - changed summary, description and operationId in apidef.yaml -> paths/power-of-attorneys as they looked incorrect;
  - changed operationId in apidef.yaml -> paths/credit-cards/{id} as it looked incorrect;
  - added paths/accounts/{number} to apidef.yaml as it was missing;
  - added definitions/Account to apidef.yaml as it was missing;
  - added "status" field to apidef.yaml -> definitions/DebitCard and apidef.yaml -> definitions/CreditCard as it was missing;
  - added definitions/Status to apidef.yaml as it was missing.

# TODO
  - handle more exceptions and error cases;
  - add HTTPS.