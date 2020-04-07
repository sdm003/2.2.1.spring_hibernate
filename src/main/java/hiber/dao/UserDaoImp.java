package hiber.dao;

import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      Session session = sessionFactory.openSession();
      Transaction tx = session.beginTransaction();
      session.save(user);
      if (tx.getStatus().equals(TransactionStatus.ACTIVE)) {
         tx.commit();
      }
      session.close();

   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      Session session = sessionFactory.openSession();
      TypedQuery<User> query=session.createQuery("from User");
      List<User> users  = query.getResultList();
      session.close();
      return users;
   }

   @Override
   public User getUserByCarSeriesAndName(String carName, Integer series) {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("SELECT us FROM User us JOIN us.car car WHERE car.series = :series AND car.name = :name");
      query.setParameter("series", series);
      query.setParameter("name", carName);
      return query.getSingleResult();
   }
}
