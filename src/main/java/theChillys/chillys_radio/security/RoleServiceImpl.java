package theChillys.chillys_radio.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService{

    private final IRoleRepository repository;

    @Override
    public Role getRoleByTitle(String title) {
        return repository.findRoleByTitle(title);
    }

}
