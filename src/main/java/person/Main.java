package person;

import com.github.javafaker.Faker;
import legoset.model.LegoSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;

public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");
    private static Faker faker = new Faker();


    private static Person randomPerson(){
        Address adr = Address.builder()
                .city(faker.address().city())
                .country(faker.address().country())
                .state(faker.address().state())
                .zip(faker.address().zipCode())
                .streetAddress(faker.address().streetAddress())
                .build();
        Person person = Person.builder()
                .name(faker.name().fullName())
                .dob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .email(faker.internet().emailAddress())
                .gender(faker.options().option(Person.Gender.class))
                .profession(faker.company().profession())
                .address(adr)
                .build();
        return person;
    }

    public static void main(String[] args) {
        int n = 1000;
        if(args.length>1){
            n=Integer.parseInt(args[1]);
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (int i = 0; i < n; i++) {
                em.persist(randomPerson());
            }
            em.getTransaction().commit();

            //Checking
            em.createQuery("SELECT p FROM Person p ORDER BY p.id", Person.class).getResultList().forEach(System.out::println);


        } finally {
            em.close();
        }


    }
}
