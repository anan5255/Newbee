package com.example.newbee1.domain.repo;

import com.example.newbee1.domain.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepo extends CrudRepository<User, Long> {
    User  findUserByLoginName(String loginName);

        User save(User user);

        User findUserByLoginNameAndPassword(String loginName, String password);


}
