## Hard to test code 

> Difficult to understand a test at a glance

The problem with obscure tests is that they threaten the goal of "Test as Documentation" and instead only concentrate on "Verify behavior of the SUT".

Furthermore, it decreases the test maintainability, as it is hard to understand the test case. This results in longer cycle times for fixing bugs through tests.

More information: [X-Unit Test Patterns: Obscure Test](http://xunitpatterns.com/Obscure%20Test.html)


### Causes

There are several causes of an obscure test:

* Eager Test (tests too much in a single test method)
* Mystery Guest (not able to see the cause -> effect of setup & verification)
* General Fixture
* ...


#### Eager Test

In case of an eager test, the test case performs multiple functionalities inside of one test method.
