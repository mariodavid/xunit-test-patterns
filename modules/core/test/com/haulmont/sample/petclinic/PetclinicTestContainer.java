package com.haulmont.sample.petclinic;

import com.haulmont.cuba.testsupport.TestContainer;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PetclinicTestContainer extends TestContainer {

    static {
        System.setProperty(
            "logback.configurationFile",
            "com/haulmont/sample/petclinic/petclinic-test-logback.xml"
        );
    }

    public PetclinicTestContainer() {
        super();
        //noinspection ArraysAsListWithZeroOrOneArgument
        appComponents = new ArrayList<>(Arrays.asList(
            // list add-ons here: "com.haulmont.reports", "com.haulmont.addon.bproc", etc.
            "com.haulmont.cuba",
            "com.haulmont.reports",
            "com.haulmont.addon.admintools"
        ));
        appPropertiesFiles = Arrays.asList(
                // List the files defined in your web.xml
                // in appPropertiesConfig context parameter of the core module
            "com/haulmont/sample/petclinic/app.properties",
                // Add this file which is located in CUBA and defines some properties
                // specifically for test environment. You can replace it with your own
                // or add another one in the end.
                "com/haulmont/sample/petclinic/test-app.properties");
        autoConfigureDataSource();

    }

    public static class Common extends PetclinicTestContainer {

        public static final PetclinicTestContainer.Common INSTANCE = new PetclinicTestContainer.Common();

        private static volatile boolean initialized;

        private Common() {
        }

        @Override
        public void beforeAll(ExtensionContext extensionContext) throws Exception {
            if (!initialized) {
                super.beforeAll(extensionContext);
                initialized = true;
            }
            setupContext();
        }


        @SuppressWarnings("RedundantThrows")
        @Override
        public void afterAll(ExtensionContext extensionContext) throws Exception {
            cleanupContext();
            // never stops - do not call super
        }

    }
}