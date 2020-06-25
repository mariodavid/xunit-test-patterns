package com.haulmont.sample.petclinic.test_patterns.hard_to_test_code.async_code.problem;

import java.util.Optional;
import java.util.function.Supplier;

public class PollingUtils {

    public static Boolean retryBoolean(Supplier<Boolean> condition, int attemptQuantity, int sleepTimeInMilliseconds) {
        for (int i = 0; i < attemptQuantity; i++) {

            if (condition.get()) {
                return true;
            } else {
                try {
                    Thread.sleep(sleepTimeInMilliseconds);
                } catch (InterruptedException ignore) {}
            }
        }
        return false;
    }

    public static <T> T untilPresent(Supplier<Optional<T>> condition) {
        return untilPresent(condition, 3, 5000);
    }

    public static <T> T untilPresent(Supplier<Optional<T>> condition, int attemptQuantity, int sleepTimeInMilliseconds) {
        return retry(condition, attemptQuantity, sleepTimeInMilliseconds)
                .orElseThrow(() -> new RuntimeException("Condition not met within the timeout"));
    }

    public static <T> Optional<T> retry(Supplier<Optional<T>> condition, int attemptQuantity, int sleepTimeInMilliseconds) {
        for (int i = 0; i < attemptQuantity; i++) {
            Optional<T> result = condition.get();
            if (result.isPresent()) {
                return result;
            }

            try {
                Thread.sleep(sleepTimeInMilliseconds);
            } catch (InterruptedException ignore) {
            }
        }
        return Optional.empty();
    }
}
