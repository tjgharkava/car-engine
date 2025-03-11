package ge.temo.carengine.cars.user;

import ge.temo.carengine.cars.error.NotFoundException;
import ge.temo.carengine.cars.user.persistance.Role;
import ge.temo.carengine.cars.user.persistance.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Role with id " + id + " not found"));
    }
}
