package org.hobby.dao;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.hobby.config.HibernateConfig;
import org.hobby.model.ZipCode;
import org.hobby.model.ZipDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ZipCodeDAO implements IDAO<ZipCode> {

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

    @Override
    public void create(ZipCode zipCode) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        if (!em.contains(zipCode)) {
            zipCode = em.merge(zipCode);
        }
        em.persist(zipCode);
        em.getTransaction().commit();
        em.close();

    }

    @Override
    public ZipCode read(int id) {
        EntityManager em = emf.createEntityManager();
        ZipCode foundZipCode = em.find(ZipCode.class, id);
        em.close();
        return foundZipCode;

    }

    @Override
    public void update(ZipCode zipCode) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(zipCode);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(ZipCode zipCode) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(zipCode);
        em.getTransaction().commit();
        em.close();
    }

    public List<Object[]> getAllPostcodesAndCities() {
        EntityManager em = emf.createEntityManager();
        String jpql = "SELECT DISTINCT z.zip, z.city FROM ZipCode z";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        return query.getResultList();
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
