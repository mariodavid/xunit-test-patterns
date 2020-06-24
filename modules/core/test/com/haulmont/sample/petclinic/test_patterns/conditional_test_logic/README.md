## Conditional Test Logic

> A test contains code that may or may not be executed

The goal of an automated test is go gain trust in our buggy implementation. But how do we trust our tests? Two options are available:

1. test your tests
2. apply a simpler programming pattern do increase the chances of not doing something wrong

In case of conditional test logic, we basically actively violating the second idea.

Additionally, it actively works against the goal of "Test as documentation"

More information: [X-Unit Test Patterns: Obscure Test](http://xunitpatterns.com/Conditional%20Test%20Logic.html)


### Causes

* Flexible Tests
* Conditional Verification Logic
* Production Logic in Tests
* ...


#### Production Logic in Tests

In case of a production logic mirror in a test case, the test can handle multiple situations depending on how the test data requires it.

Example of this can be found in [ProductionLogicInTestTest](production_logic_in_test/problem/ProductionLogicInTestTest.java#L98). In this test case there are conditions in the verification block that check the results depending on the test data. The test method name `when_generateVisits_thenTreatmentStatusShouldBeDifferent` already indicates that it is not exactly clear what is actually tested here.

There are two solutions that can be found in the solutions package:

* [test_your_tests](production_logic_in_test/solution/test_your_tests)
* [unfold_complexity](production_logic_in_test/solution/unfold_complexity)

In the "test your tests" solution, the heavy if else construct has been extracted to a test utility method: [RelativeDateTreatmentStatusMapping](production_logic_in_test/solution/test_your_tests/RelativeDateTreatmentStatusMapping.java). With that the complexity in the test is gone. Unfortunately the complexity goes not go away by moving code from one class to the other. This is why it is actually just hidden, not gone. But with putting it to a dedicated class, it is possible to write a test for the test utility method: [RelativeDateTreatmentStatusMappingTest](production_logic_in_test/solution/test_your_tests/RelativeDateTreatmentStatusMappingTest.java).

In the case of "unfold complexity", the test is restructured to use a method that is easier to directly interact with.
Furthermore, it uses one concrete example for each test case. With that the complexity was unfolded into different test
cases. Each of which became simple once again. See [NoProductionLogicInTestByUnfoldingTest](production_logic_in_test/solution/unfold_complexity/NoProductionLogicInTestByUnfoldingTest.java)
