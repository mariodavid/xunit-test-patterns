package com.haulmont.sample.petclinic.test_patterns.code_smells.conditional_test_logic.production_logic_in_test;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.sample.petclinic.config.PetclinicTestdataConfig;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PetclinicMocking {


    private DataManager dataManager;
    private PetclinicTestdataConfig petclinicTestdataConfig;

    public PetclinicMocking(
        DataManager dataManager,
        PetclinicTestdataConfig petclinicTestdataConfig
    ) {
        this.dataManager = dataManager;
        this.petclinicTestdataConfig = petclinicTestdataConfig;
    }


    public void possiblePets(Pet... pets) {
        FluentLoader petLoaderMock = mock(FluentLoader.class);
        when(dataManager.load(Pet.class))
            .thenReturn(petLoaderMock);

        when(petLoaderMock.list())
            .thenReturn(asList(
                pets
            ));
    }


    public void possibleNurses(List<User> nurses) {

        FluentLoader fluentLoader = mock(FluentLoader.class);

        lenient().when(dataManager.load(Role.class))
            .thenReturn(fluentLoader);


        final List<UserRole> nursesUserRoles = nurses.stream()
            .map(user -> {
                final UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRoleName("Nurse");
                return userRole;
            })
            .collect(Collectors.toList());

        mockList(UserRole.class, nursesUserRoles);
    }

    <E extends Entity> void mockList(Class<E> entityClass, List<E> entityList) {
        FluentLoader fluentLoader = mock(FluentLoader.class);

        lenient().when(dataManager.load(entityClass))
            .thenReturn(fluentLoader);

        lenient().when(fluentLoader.view(any(Consumer.class)))
            .thenReturn(fluentLoader);

        lenient().when(fluentLoader.list())
            .thenReturn(entityList);
    }

    public void daysInPastToGenerateFor(int days) {
        when(petclinicTestdataConfig.getTestdataVisitStartAmountPastDays())
            .thenReturn(days);
    }

    public void daysInFutureToGenerateFor(int days) {
        when(petclinicTestdataConfig.getTestdataVisitStartAmountFutureDays())
            .thenReturn(days);
    }

    public void visitAmountPerDay(int amount) {
        when(petclinicTestdataConfig.getTestdataVisitAmountPerDay())
            .thenReturn(amount);
    }

    public void possibleDescriptions(String descriptions) {
        when(petclinicTestdataConfig.getTestdataVisitDescriptionOptions())
            .thenReturn(descriptions);
    }

}
