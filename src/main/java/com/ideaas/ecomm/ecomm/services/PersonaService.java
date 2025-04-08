package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.repository.AddressDao;
import com.ideaas.ecomm.ecomm.repository.PersonDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class PersonaService implements IPersonaService {

    private PersonDao dao;
    private AddressDao addressDao;

    @Autowired
    public PersonaService(final PersonDao dao, final AddressDao addressDao) {
        this.dao = dao;
        this.addressDao = addressDao;
    }

    @Override
    public Person save(final Person person) {
        Person personSaved = dao.save(person);
        if (person.getAddresses() != null) {
            personSaved.getAddresses().forEach(address -> address.setPersona(person));
            addressDao.saveAll(new ArrayList<>(personSaved.getAddresses())); // Convertir Set en List
        }
        return personSaved;
    }

    @Override
    public Person findByPersonId(final String id) {
        return dao.findByPersonId(id);
    }

}
