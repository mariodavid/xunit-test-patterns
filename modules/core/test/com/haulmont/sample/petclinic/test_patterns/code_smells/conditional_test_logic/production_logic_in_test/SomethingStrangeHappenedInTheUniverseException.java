package com.haulmont.sample.petclinic.test_patterns.code_smells.conditional_test_logic.production_logic_in_test;

public class SomethingStrangeHappenedInTheUniverseException extends RuntimeException {

    public SomethingStrangeHappenedInTheUniverseException() {
        super("Something strange happened here...");
    }
}
