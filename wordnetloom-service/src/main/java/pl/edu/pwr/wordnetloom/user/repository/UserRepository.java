package pl.edu.pwr.wordnetloom.user.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.common.utils.PasswordUtils;
import pl.edu.pwr.wordnetloom.user.model.User;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class UserRepository extends GenericRepository<User> {

    @Inject
    EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveOrUpdate(User user) {
        if (null != user.getId()) {
            em.merge(user);
        } else {
            em.persist(user);
        }
    }

    public User findUserByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User changeUserPassword(String email, String password){
        User u = findUserByEmail(email);
        u.setPassword(PasswordUtils.encryptPassword(password));
        saveOrUpdate(u);
        return u;
    }

    @Override
    protected Class<User> getPersistentClass() {
        return User.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}