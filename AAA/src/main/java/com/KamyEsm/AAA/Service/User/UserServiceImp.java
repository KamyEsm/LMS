package com.KamyEsm.AAA.Service.User;

import com.KamyEsm.AAA.Entity.MyUser;
import com.KamyEsm.AAA.ExceptionHandling.DuplicateNameException;
import com.KamyEsm.AAA.ExceptionHandling.PasswordSecureException;
import com.KamyEsm.AAA.ExceptionHandling.NotFoundException;
import com.KamyEsm.AAA.Mapper.UserMapper;
import com.KamyEsm.AAA.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{

    private final UserRepository repository;
    private final CompromisedPasswordChecker passwordChecker;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    @Override
    public MyUser create(MyUser newUser) {
        if(newUser == null) return null;
        if(repository.existsByUsername(newUser.getUsername()))
            throw new DuplicateNameException("this username is already exist");
        if(newUser.getPassword().isBlank() || passwordChecker.check(newUser.getPassword()).isCompromised()){
            throw new PasswordSecureException("Password is not secure.");
        }
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        return repository.save(newUser);
    }

    @Override
    public MyUser getById(Long id) {
        Optional<MyUser> userOptional = repository.findById(id);
        if(userOptional.isPresent())
            return userOptional.get();
        else throw new NotFoundException("failed to find user by id:" + id);
    }

//    @Override
//    public MyUser getByUserName(String username) {
//        Optional<MyUser> userOptional = repository.findByUsername(username);
//        if(userOptional.isPresent())
//            return userOptional.get();
//        else throw new UserNotFoundException("failed to find user by username:" + username);
//    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public MyUser UpdateById(Long id, MyUser user) {
        if(user == null) return null;


        if(repository.existsByUsername(user.getUsername()))
            throw new DuplicateNameException("this username is already exist");


        Optional<MyUser> userOptional = repository.findById(id);
        MyUser oldUser = null;
        if(userOptional.isPresent())
            oldUser = userOptional.get();
        else throw new NotFoundException("failed to find user by id:" + id);

        if(!user.getPassword().isBlank() && !encoder.matches(user.getPassword() , oldUser.getPassword())){
            if(passwordChecker.check(user.getPassword()).isCompromised())
                throw new PasswordSecureException("Password is not secure.");
            else oldUser.setPassword(encoder.encode(user.getPassword()));
        }

        userMapper.updateUserFromDto(user,oldUser);
        return repository.save(oldUser);

    }

    public List<MyUser> getAll(int page , int count){
        if (page < 0) page = 0;
        if (count <= 0) count = 10;
        if (count > 100) count = 100;

        Pageable pageable = PageRequest.of(page, count, Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(pageable).getContent();
    }
}
