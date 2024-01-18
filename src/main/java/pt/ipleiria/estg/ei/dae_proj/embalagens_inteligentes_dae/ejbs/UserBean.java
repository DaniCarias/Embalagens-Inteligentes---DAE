package pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.ProductManufacturer;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.entities.User;
import pt.ipleiria.estg.ei.dae_proj.embalagens_inteligentes_dae.security.Hasher;

@Stateless
public class UserBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Hasher hasher;
    public User find(String username) {
        return entityManager.find(User.class, username);
    }

    public User findOrFail(String username) {
        var user = entityManager.getReference(User.class, username);
        Hibernate.initialize(user);
        return user;
    }

    public boolean canLogin(String username, String password) {
        var user = find(username);
        return user != null && user.getPassword().equals(hasher.hash(password));
    }


}
