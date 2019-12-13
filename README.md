# Aggregator for Power of Attorney Service
This WS provides REST API for accessing aggregated power of attorney information of a user
  - List of aggregated power of attorneys for a given user (/power-of-attorneys?user=name)

**To build and run:** use `mvn compile exec:java`
**Application runs on:** http://localhost:8081

# Changes in original Power of Attorney Service
  - changed owner of account\123123123 from "uper duper employee" to "Super duper employee" as looked incorrect
  - changed direction in poa\0004 as it looked incorrect ("Super duper employee" got poa for his own account)
  - added "number" and "iban" fields to account entity
  - changed comment in nl.rabobank.powerofattorney.stub.JsonStub -> line #81 as it looked like copy-paste error
  - changed summary, description and operationId in apidef.yaml -> paths/power-of-attorneys as they looked incorrect
  - changed operationId in apidef.yaml -> paths//credit-cards/{id} as it looked incorrect
  - added paths/accounts/{number} to apidef.yaml as it was missing
  - added definitions/Account to apidef.yaml as it was missing
  - added status field to apidef.yaml -> definitions/DebitCard and apidef.yaml -> definitions/CreditCard as it was missing
  - added definitions/Status to apidef.yaml as it was missing
 
# TODO
  - finish unit tests
  - handle more exceptions and error cases
  - We look at the quality and readability of code that has been delivered more than if the functionality matches our expectations