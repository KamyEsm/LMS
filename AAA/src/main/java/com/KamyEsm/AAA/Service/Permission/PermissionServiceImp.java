package com.KamyEsm.AAA.Service.Permission;

import com.KamyEsm.AAA.Entity.Permission;
import com.KamyEsm.AAA.ExceptionHandling.DuplicateNameException;
import com.KamyEsm.AAA.ExceptionHandling.NotFoundException;
import com.KamyEsm.AAA.Mapper.PermissionMapper;
import com.KamyEsm.AAA.Repository.PermissionsRepository;
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
public class PermissionServiceImp implements PermissionService {

    private final PermissionsRepository repository;
    private final PermissionMapper mapper;

    @Override
    public Permission create(Permission newPermission) {
        if (newPermission == null) return null;
        if(repository.existsByName(newPermission.getName()))
            throw new DuplicateNameException("role name is already exist");
        return repository.save(newPermission);
    }

    @Override
    public Permission getById(Long id) {
        Optional<Permission> optional = repository.findById(id);
        if(optional.isPresent())
            return optional.get();
        else throw new NotFoundException("Permission not found");
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Permission UpdateById(Long id, Permission permission) {
        if(permission == null) return null;

        if(repository.existsByName(permission.getName()))
            throw new DuplicateNameException("this Permission name is already exist");


        Optional<Permission> Optional = repository.findById(id);
        Permission oldPermission = null;
        if(Optional.isPresent())
            oldPermission = Optional.get();
        else throw new NotFoundException("failed to find Permission by id:" + id);

        mapper.updatePermissionFromDto(permission,oldPermission);
        return repository.save(oldPermission);
    }

    @Override
    public List<Permission> getAll(int page, int count) {
        if (page < 0) page = 0;
        if (count <= 0) count = 10;
        if (count > 100) count = 100;

        Pageable pageable = PageRequest.of(page, count, Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(pageable).getContent();
    }
}
