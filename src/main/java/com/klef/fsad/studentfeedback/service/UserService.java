package com.klef.fsad.studentfeedback.service;

import com.klef.fsad.studentfeedback.entity.User;
import java.util.List;

public interface UserService
{
    User login(String email, String password);
    String register(User user);
    List<User> getAllByRole(String role);
    List<User> getRecentlySignedIn(String role);
    User getById(Long id);
}
