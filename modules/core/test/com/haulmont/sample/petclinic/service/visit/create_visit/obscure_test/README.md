## Obscure Test

> Difficult to understand a test at a glance

The problem with obscure tests is that they threaten the goal of "Test as Documentation" and instead only concentrate on "Verify behavior of the SUT".

Furthermore, it decreases the test maintainability, as it is hard to understand the test case. This results in longer cycle times for fixing bugs through tests.

[X-Unit Test Patterns: Obscure Test](http://xunitpatterns.com/Obscure%20Test.html)

There are several causes of a obscure test:

* Eager Test (tests too much in a single test method)
* Mystery Guest (not able to see the cause -> effect of setup & verification)
* General Fixture


### Eager Test

In case of an eager test, the test case performs multiple functionalities inside of one test method.

The test method `createVisitForToday_createsANewVisit_ifPossible` in [VisitServiceEagerTest](eager_test/problem/VisitServiceEagerTest.java) consists of three behaviors:

* is the newly created visit assigned to the right pet?
* in case the identification number of the pet is correct, does it create a correct kind of visit?
* in case the identification number of the pet is incorrect, will the visit then not be created?

The root cause of this test is oftentimes the patterns that are applied when doing manual testing. In order to reduce the setup overhead, which in manual testing is tedious and error prone and slow. Therefore, multiple things are tested inside one test run. As in automated testing these constraints do not apply.

This smell results in:

* very long tests
* hard to read tests
* tests that fail for more than one reason

The solution to the smell is to create "single condition tests". Test methods that focus on one particular aspect / question of the overall functionality. 

In the test case [VisitServiceNonEagerTest](eager_test/solution/VisitServiceNonEagerTest.java) the test case is split into two distinct test cases:

* createVisitForToday_createsANewVisit_forTheCorrectPet
* createVisitForToday_createsANewVisit_withTheCorrectVisitInformation
* createVisitForToday_createsNoVisit_forAnIncorrectIdentificationNumber