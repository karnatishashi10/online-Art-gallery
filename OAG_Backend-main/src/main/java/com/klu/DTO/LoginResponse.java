package com.klu.DTO;

public class LoginResponse {
    private String message;
    private UserData user;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String message, UserData user) {
        this.message = message;
        this.user = user;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getUser() {
        return user;
    }
    public void setUser(UserData user) {
        this.user = user;
    }

    // Nested class for UserData
    public static class UserData {
        private int id;
        private String name;
        private String email;
        private String phone;

        // Constructors
        public UserData() {}

        public UserData(int id, String name, String email, String phone) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        // Getters and Setters
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}