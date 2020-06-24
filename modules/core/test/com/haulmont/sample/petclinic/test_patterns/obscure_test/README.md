## Obscure Test

> Difficult to understand a test at a glance

The problem with obscure tests is that they threaten the goal of "Test as Documentation" and instead only concentrate on "Verify behavior of the SUT".

Furthermore, it decreases the test maintainability, as it is hard to understand the test case. This results in longer cycle times for fixing bugs through tests.

More information: [X-Unit Test Patterns: Obscure Test](http://xunitpatterns.com/Obscure%20Test.html)


### Reasons

There are several causes of an obscure test:

* Eager Test (tests too much in a single test method)
* Mystery Guest (not able to see the cause -> effect of setup & verification)
* General Fixture


#### Eager Test

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

In the test class [VisitServiceNonEagerTest](eager_test/solution/VisitServiceNonEagerTest.java) the test case is split into three distinct test cases:

* `createVisitForToday_createsANewVisit_forTheCorrectPet`
* `createVisitForToday_createsANewVisit_withTheCorrectVisitInformation`
* `createVisitForToday_createsNoVisit_forAnIncorrectIdentificationNumber`


#### Mystery Guest

A mystery guest test has the problem that it is hard to understand how the system looks like only by looking at the test case. This happens e.g. when "general fixture" is used.   

The test method `createVisitForToday_createsANewVisit_withMysteryGuest` in [VisitServiceMysteryGuestTest](mystery_guest/problem/VisitServiceMysteryGuestTest.java#L51) has these problems:

 * where does "pikachu" come from? what attributes does it have?
 * which pet is associated with the identification number "25"?
 * how do we know how many visits are available for pikachu?
 * what happens if another test also relies on pikachu and creates a visit for this test case?

This smell results in:

* hard to get the full picture of the test
* dependencies between test cases


In the test class [VisitServiceNonMysteryGuestViaFreshFixtureTest](mystery_guest/solution/VisitServiceNonMysteryGuestViaFreshFixtureTest.java#L56) the test case creates the test data as a fresh fixture. It creates a Pet that is only used in this test case. 

As an alternative in case switching to "fresh fixture" is not possible, one alternative approach is still keep the general & shared fixture (see [VisitServiceNonMysteryGuestViaGuardClauseTest](mystery_guest/solution/VisitServiceNonMysteryGuestViaGuardClauseTest.java#L58). In order to mitigate the situation that other tests can influence the behavior of the test by changing the test data, a "guard-clause" is used in the "arrange" part of the test case.


#### General fixture

A general fixture is a test data fixture that is broad and can be used for various test cases.

More information: [X-Unit Test Patterns: General Fixture](http://xunitpatterns.com/Obscure%20Test.html#General%20Fixture)

In this example, the general fixture lives in the init scripts of the database [db/init/hsqldb](https://github.com/mariodavid/xunit-test-patterns/blob/master/modules/core/db/init/hsql/30.create-db.sql), which contains certain test / seed data.

The idea is to support many test cases, but with that it also has to be in a central place.
This creates problems when reading the test case, because it becomes an obscure test when looking at it. It also reduces the goal of "test as documentation".

Example on how to solve this: [VisitServiceNonMysteryGuestViaFreshFixtureTest](mystery_guest/solution/VisitServiceNonMysteryGuestViaFreshFixtureTest.java#L56).

