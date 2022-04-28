package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDao extends JpaRepository<Person, Long> {

    Person findByPersonId(String id);
}
