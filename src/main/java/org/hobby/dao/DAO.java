package org.hobby.dao;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Hobby;
import org.hobby.model.Person;
import org.hobby.model.ZipDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DAO <T> {

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

    EntityManager em = emf.createEntityManager();


    public void save(T t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        if (!em.contains(t)) {
            t = em.merge(t);
        }
        em.persist(t);
        em.getTransaction().commit();
        em.close();
    }

    public T findById(int id, Class<T> t) {
        EntityManager em = emf.createEntityManager();
        T foundT = em.find(t, id);
        em.close();
        return foundT;
    }

    public void delete(T t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(t);
        em.getTransaction().commit();
        em.close();
    }

    public void update(T t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(t);
        em.getTransaction().commit();
        em.close();
    }

    public int getNumberOfPeopleWithHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        int numberOfPeople = em.createQuery("SELECT p FROM Person p WHERE :hobby MEMBER OF p.hobbies", Hobby.class).setParameter("hobby", hobby).getResultList().size();
        em.getTransaction().commit();
        em.close();
        return numberOfPeople;
    }

    public List<Object[]> getAllPostcodesAndCities() {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT DISTINCT z.zip, z.city FROM ZipCode z";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        return query.getResultList();
    }
    public List<Person> getPersonsByCity(String zip) {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT p FROM Person p WHERE p.ZipCode = :zip";
        TypedQuery<Person> query = em.createQuery(jpql, Person.class);
        query.setParameter("zip", zip);
        return query.getResultList();
    }

    public Map<String, Integer> countHobbiesPerPersonOnAddress(String address) {
        return em.createQuery(
                        "SELECT p FROM Person p WHERE p.address = :address", Person.class)
                .setParameter("address", address)
                .getResultStream()
                .collect(Collectors.toMap(
                        person -> person.getFirstName() + " " + person.getLastName(),
                        person -> person.getHobbies().size()
                ));

    }

    public ZipDTO getZip(String nr) throws IOException {
        String API_URL = "https://api.dataforsyningen.dk/postnumre/";

        String apiUrl = API_URL + nr;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            Gson gson = new Gson();
            return gson.fromJson(response.toString(), ZipDTO.class);
        } else {
            System.out.println("Fejl ved forespørgsel til API. Statuskode: " + responseCode);
            return null;
        }
    }

}
