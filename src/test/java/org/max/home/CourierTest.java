package org.max.home;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourierTest extends AbstractTest {
    @Test
    @Order(1)
    void testNumberOfCouriers() {
        //given
        String sql = "FROM CourierInfoEntity";
        //when
        final Query query = getSession().createQuery(sql);
        //then
        Assertions.assertEquals(4, query.list().size());
    }

    @Test
    @Order(2)
    void testAddNewCourier() {
        //given
        Session session = getSession();
        //when
        final Query query01 = session.createSQLQuery("SELECT MAX(courier_id) FROM courier_info");
        int cId = (int) query01.uniqueResult() + 1;

        CourierInfoEntity newCourier = new CourierInfoEntity();
        newCourier.setCourierId((short) cId);
        newCourier.setFirstName("Ivan");
        newCourier.setLastName("Petrov");
        newCourier.setPhoneNumber("+79009609999");
        newCourier.setDeliveryType("car");

        session.beginTransaction();
        session.persist(newCourier);
        session.getTransaction().commit();

        final Query query02 = session.createSQLQuery("SELECT * FROM courier_info WHERE courier_id=" + cId)
                .addEntity(CourierInfoEntity.class);
        CourierInfoEntity addCourier = (CourierInfoEntity) query02.uniqueResult();
        //then
        Assertions.assertNotNull(addCourier);
        Assertions.assertEquals("Ivan", addCourier.getFirstName());
    }

    @Test
    @Order(3)
    void testDeleteCourier() {
        //given
        Session session = getSession();
        //when
        final Query query01 = session.createSQLQuery("SELECT * FROM courier_info WHERE last_name='Petrov'")
                .addEntity(CourierInfoEntity.class);
        CourierInfoEntity courierDelete = (CourierInfoEntity) query01.uniqueResult();
        Assumptions.assumeTrue(courierDelete != null);

        session.beginTransaction();
        session.delete(courierDelete);
        session.getTransaction().commit();

        final Query query02 = session.createSQLQuery("SELECT * FROM courier_info WHERE last_name='Petrov'")
                .addEntity(CourierInfoEntity.class);
        CourierInfoEntity courierAfterDelete = (CourierInfoEntity) query02.uniqueResult();
        //then
        Assertions.assertNull(courierAfterDelete);
    }
}