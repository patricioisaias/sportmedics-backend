package cl.sportmedics.ms_auth.service;

import com.sportmedics.auth.entity.User;
import java.util.List;

public interface IuserService {
    List<User> findAll();
    User save(User user);
    User findById(Long id);
    void delete(Long id);
}