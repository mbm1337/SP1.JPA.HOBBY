package org.hobby;

import jakarta.persistence.EntityManagerFactory;
import org.hobby.config.HibernateConfig;
import org.hobby.dao.PersonDAO;
import org.hobby.model.Person;
import org.hobby.model.ZipCode;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        //        Person person2 = new Person("Alice", "Smith", 25, Person.Gender.FEMALE, "987-654-3210", "alice@example.com","uk");
//        PersonDAO personDAO = new PersonDAO();
//        // personDAO.save(person2);
//        PersonDAO dao= new PersonDAO();
//        dao.getAllInfo(2);
//        person2.addAddresstoPerson(new ZipCode(200,"cobenhagen","sydhavn","sluseholmen"));
//        System.out.println(person2);





    }
}