package com.klu.Repository;

import com.klu.Interfaces.IUserRepository;
import com.klu.Models.Users;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepository implements IUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Get user by ID safely using Optional
    @Override
    public Optional<Users> getUserById(int id) {
        return Optional.ofNullable(entityManager.find(Users.class, id));
    }

    // Get all users
    @Override
    public List<Users> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM Users u", Users.class)
                .getResultList();
    }

    // Add a new user
    @Override
    public void addUser(Users user) {
        entityManager.persist(user);
    }

    // Update existing user
    @Override
    public void updateUser(Users user) {
        entityManager.merge(user);
    }

    // Delete user by ID
    @Override
    public void deleteUser(int id) {
        Users user = entityManager.find(Users.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    // Authenticate user safely (compatible with all JPA versions)
    @Override
    public Optional authenticate(String email, String password) {
        List<Users> users = entityManager.createQuery(
                        "SELECT u FROM Users u WHERE u.email = :email AND u.password = :password", Users.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultList();  // returns a List<Users>

        return users.isEmpty() ? null : Optional.empty(); // return first match or null
    }

    @Override
    public Users findByEmail(String email) {
        List<Users> users = entityManager.createQuery(
                "SELECT u FROM Users u WHERE u.email = :email", Users.class)
                .setParameter("email", email)
                .getResultList();

        return users.isEmpty() ? null : users.get(0);
    }
}
