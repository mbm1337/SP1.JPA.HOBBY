package org.hobby.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.hobby.config.HibernateConfig;
import org.hobby.model.Person;
import org.hobby.model.PostnummerDTO;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;

public class DAO <T> {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();


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

    public Person getPersonByPhoneNumber(String phoneNumber) {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT p FROM Person p WHERE p.phone = :phoneNumber";
        TypedQuery<Person> query = em.createQuery(jpql, Person.class);
        query.setParameter("phoneNumber", phoneNumber);
        List<Person> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    public static class PostnummerDAO {

        private final String API_URL = "https://api.dataforsyningen.dk/postnumre/";

        public PostnummerDTO getPostnummer(String nr) throws IOException {
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
                return gson.fromJson(response.toString(), PostnummerDTO.class);
            } else {
                System.out.println("Fejl ved forespørgsel til API. Statuskode: " + responseCode);
                return null;
            }
        }

    }

}
