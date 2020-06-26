## Test Code Duplication

> Many tests need a similar "theme" and therefore are vulnerable for code duplication

Duplicate code is a commonly known problem in production code. But oftentimes in test code code duplication occurs even more. 

The reasons might be different, but oftentimes it is based on the fact that tests are not treated as first-class citizens. In particular test data setup and verfication logic are often duplicated. 

More information: [X-Unit Test Patterns: Code Duplication](http://xunitpatterns.com/Test%20Code%20Duplication.html)


### Causes

There are several causes of test code duplication:

* C&P code reuse
* Reinventing the Wheel


#### C&P code reuse

Oftentimes developers focus on the "how" of the test implementation. Sometimes the bigger picture of the overall test-suite is missed. This is an example of a missing refactoring.


The test methods in [VisitServiceCopyAndPasteCodeReuseTest](copy_and_paste_code_reuse/problem/VisitServiceCopyAndPasteCodeReuseTest.java) are created one after another, where the first test case determined the test case structure. The following test cases were copied and only adjusted the verification part. This has the following problems:

* DB Interaction is duplicated across all test cases
* Test data setup is duplicated

In the solution [VisitServiceCopyAndPasteCodeReuseTest](copy_and_paste_code_reuse/solution/VisitServiceCodeReuseTest.java) the first test case created the test case structure by writing the test "outside in". This way helper methods were imagined in a dedicated object ([PetclinicVisitDb](copy_and_paste_code_reuse/solution/PetclinicVisitDb.java)), which afterwards acted as a "test utility method" holder.