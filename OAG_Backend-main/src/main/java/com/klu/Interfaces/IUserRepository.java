package com.klu.Interfaces;

import com.klu.Models.Users;
import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    Optional<Users> getUserById(int id);

    List<Users> getAllUsers();

    void addUser(Users user);

    void updateUser(Users user);

    void deleteUser(int id);

    Optional<Users> authenticate(String email, String password);

	Users findByEmail(String email);
}