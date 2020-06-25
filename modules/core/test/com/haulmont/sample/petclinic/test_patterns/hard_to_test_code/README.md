## Hard to test code 

> Difficult to understand a test at a glance

The problem with obscure tests is that they threaten the goal of "Test as Documentation" and instead only concentrate on "Verify behavior of the SUT".

Furthermore, it decreases the test maintainability, as it is hard to understand the test case. This results in longer cycle times for fixing bugs through tests.

More information: [X-Unit Test Patterns: Obscure Test](http://xunitpatterns.com/Obscure%20Test.html)


### Causes

There are several causes of hard to test code:

* Async Code


#### Async Code

When testing async code the test case has to invoke the logic and afterwards apply some kind of polling mechanism, because the test is not waiting until the operation is over.

The test method `when_updateVisitStatus_then_invoiceWillBeGenerated` in [VisitCompletedAsyncTest](async_code/problem/VisitCompletedAsyncTest.java) shows the situation with testing asynchronous code:

* DB polling is required, which makes the tests more flaky
* Exceptions in the async execution are not recognized by the tests
* The error message of a failing test is not pointing to the root cause of the problem 

In total there are four test cases that actually just want to check the algorithm, but accidentially
test the transport mechanism as well.

The test method `when_updateVisitStatus_then_invoiceContainsDocument` in [VisitCompletedAsyncTest](async_code/problem/VisitCompletedAsyncTest.java) does not even work, as it is missed to setup the report for generating the document in the DB. As the exception is not shown in the test case execution, this error is very hard to find.


The solution for async code is to separate the test of the transport mechanism from the test of the algorithm. By splitting those two concerns in the test, the 

 
