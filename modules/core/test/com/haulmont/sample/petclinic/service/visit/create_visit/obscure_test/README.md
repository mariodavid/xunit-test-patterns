## Obscure Test

> Difficult to understand a test at a glance

The problem with obscure tests is that they threaten the goal of "Test as Documentation" and instead only concentrate on "Verify behavior of the SUT".

Furthermore, it decreases the test maintainability, as it is hard to understand the test case. This results in longer cycle times for fixing bugs through tests.

There are several causes of a obscure test:

* Eager Test (tests too much in a single test method)
* Mystery Guest (not able to see the cause -> effect of setup & verification)
* General Fixture

http://xunitpatterns.com/Obscure%20Test.html

### Eager Test

In case of an eager test, the test case performs multiple functionalities inside of one test method.

The test method `createVisitForToday_createsANewVisit_ifPossible` in [VisitServiceEagerTest](eager_test/problem/VisitServiceEagerTest.java) consists of three behaviors:

* is the newly created visit assigned to the right pet?
* in case the identification number of the pet is correct, does it create a correct kind of visit?
* in case the identification number of the pet is incorrect, will the visit then not be created?

