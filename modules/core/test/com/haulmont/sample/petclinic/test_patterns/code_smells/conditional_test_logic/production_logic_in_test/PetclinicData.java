package com.haulmont.sample.petclinic.test_patterns.code_smells.conditional_test_logic.production_logic_in_test;

import com.haulmont.cuba.security.entity.User;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.pet.PetType;


/**
 * PetclinicData represents an API abstraction for creating different entities for test purposes
 */
public class PetclinicData {


  public Pet pet(String name) {
    Pet pet = new Pet();
    pet.setName(name);
    return pet;
  }

  public PetType electricType() {
    PetType type = new PetType();
    type.setName("Electric");
    return type;
  }
  public PetType waterType() {
    PetType type = new PetType();
    type.setName("Water");
    return type;
  }

  public PetType fireType() {
    PetType type = new PetType();
    type.setName("Fire");
    return type;
  }

  public User nurse(String name) {
    User nurse = new User();
    nurse.setName(name);
    return nurse;
  }
}