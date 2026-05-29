package com.KamyEsm.AAA.Service.Role;

import com.KamyEsm.AAA.Entity.Role;
import com.KamyEsm.AAA.ExceptionHandling.DuplicateNameException;
import com.KamyEsm.AAA.ExceptionHandling.NotFoundException;
import com.KamyEsm.AAA.Mapper.RoleMapper;
import com.KamyEsm.AAA.Repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    @Override
    public Role create(Role newRole) {
        if(newRole == null) return null;
        if(repository.existsByName(newRole.getName()))
            throw new DuplicateNameException("role name is already exist");
        if(!newRole.getName().startsWith("ROLE_")) newRole.setName("ROLE_" + newRole.getName());
        return repository.save(newRole);
    }

    @Override
    public Role getById(Long id) {
        Optional<Role> optional = repository.findById(id);
        if(optional.isPresent())
            return optional.get();
        else throw new NotFoundException("role not found");
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Role UpdateById(Long id, Role role) {
        if(role == null) return null;

        if(repository.existsByName(role.getName()))
            throw new DuplicateNameException("this role name is already exist");


        Optional<Role> Optional = repository.findById(id);
        Role oldRole = null;
        if(Optional.isPresent())
            oldRole = Optional.get();
        else throw new NotFoundException("failed to find Role by id:" + id);

        mapper.updateRoleFromDto(role,oldRole);
        return repository.save(oldRole);
    }

    @Override
    public List<Role> getAll(int page, int count) {
        if (page < 0) page = 0;
        if (count <= 0) count = 10;
        if (count > 100) count = 100;

        Pageable pageable = PageRequest.of(page, count, Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(pageable).getContent();
    }

    @Override
    public Role getByName(String name) {
        Optional<Role> optional = repository.findByName(name);
        if(optional.isPresent())
            return optional.get();
        else throw new NotFoundException("role not found");
    }
}
