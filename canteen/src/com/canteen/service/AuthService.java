package com.canteen.service;

public class AuthService {

    // Simple, static credentials
    private static final String STUDENT_USER = "student";
    private static final String STUDENT_PASS = "pass";

    private static final String CANTEEN_USER = "admin";
    private static final String CANTEEN_PASS = "canteen123";

    public enum UserRole {
        STUDENT,
        CANTEEN_ADMIN,
        INVALID
    }

    /**
     * Authenticates the user and returns their role.
     */
    public UserRole authenticate(String username, String password) {
        if (username.equals(STUDENT_USER) && password.equals(STUDENT_PASS)) {
            return UserRole.STUDENT;
        } else if (username.equals(CANTEEN_USER) && password.equals(CANTEEN_PASS)) {
            return UserRole.CANTEEN_ADMIN;
        } else {
            return UserRole.INVALID;
        }
    }
}