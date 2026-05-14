package cl.sportmedics.ms_auth.service;

import com.sportmedics.auth.entity.User;
import com.sportmedics.auth.repository.UserRepository;
import com.sportmedics.auth.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements IuserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        log.info("Obteniendo lista de todos los usuarios");
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        log.info("Guardando nuevo usuario: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("Usuario con ID {} no encontrado", id);
            return new RuntimeException("Usuario no encontrado");
        });
    }

    @Override
    public void delete(Long id) {
        log.warn("Eliminando usuario con ID: {}", id);
        userRepository.deleteById(id);
    }
}
