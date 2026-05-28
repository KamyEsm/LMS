package com.KamyEsm.AAA.Service.User;

import com.KamyEsm.AAA.Entity.MyUser;

import java.util.List;

public interface UserService {
    MyUser create(MyUser newUser);
    MyUser getById(Long id);
//    MyUser getByUserName(String username);
    void deleteById(Long id);
    MyUser UpdateById(Long id , MyUser user);
    List<MyUser> getAll(int page , int count);
}
