package com.ideaas.ecomm.ecomm.service;
import com.ideaas.ecomm.ecomm.domain.AFIP.Address;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.repository.AddressDao;
import com.ideaas.ecomm.ecomm.repository.PersonDao;
import com.ideaas.ecomm.ecomm.services.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonaServiceTest {    @Mock
private PersonDao personDao;

    @Mock
    private AddressDao addressDao;

    @InjectMocks
    private PersonaService personaService;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setPersonId(12345L);
        person.setName("Juan");
        person.setLastName("Perez");

        Address address = new Address();
        address.setDireccion("Calle falsa 123");
        address.setPersona(person);

        Set<Address> addresses = new HashSet<>();
        addresses.add(address);
        person.setAddresses(addresses);
    }

    @Test
    void testSavePerson() {
        // Configuración del mock para devolver la persona cuando se guarde
        when(personDao.save(any(Person.class))).thenReturn(person);
        doReturn(Collections.emptyList()).when(addressDao).saveAll(any());

        // Ejecutar el método
        Person savedPerson = personaService.save(person);

        // Verificar que se guardó correctamente
        assertNotNull(savedPerson);
        assertEquals("Juan", savedPerson.getName());
        assertEquals("Perez", savedPerson.getLastName());

        // Verificar que personDao.save() fue llamado con cualquier instancia de Person
        verify(personDao, times(1)).save(any(Person.class));

        // Capturar la lista que se pasó a addressDao.saveAll()
        ArgumentCaptor<List<Address>> addressCaptor = ArgumentCaptor.forClass(List.class);
        verify(addressDao, times(1)).saveAll(addressCaptor.capture());

        // Obtener la lista capturada y validar que contiene la dirección esperada
        List<Address> capturedAddresses = addressCaptor.getValue();
        assertEquals(1, capturedAddresses.size());
        assertEquals(person, capturedAddresses.get(0).getPersona()); // Asegura que la relación se asignó bien
    }

    @Test
    void testFindByPersonId() {
        when(personDao.findByPersonId("12345")).thenReturn(person);

        Person foundPerson = personaService.findByPersonId("12345");

        assertNotNull(foundPerson);
        assertEquals("Juan", foundPerson.getName());
        verify(personDao, times(1)).findByPersonId("12345");
    }
}
