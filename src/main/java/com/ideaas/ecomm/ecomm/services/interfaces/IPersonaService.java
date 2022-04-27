package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.AFIP.Person;

public interface IPersonaService {
    Person save(Person person);

    Person findByPersonId(String id);
}
