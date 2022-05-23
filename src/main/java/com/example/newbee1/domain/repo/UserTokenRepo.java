package com.example.newbee1.domain.repo;

import com.example.newbee1.domain.UserToken;
import org.springframework.data.repository.CrudRepository;

public interface UserTokenRepo extends CrudRepository<UserToken, Long> {

    UserToken findUserTokenByUserId(Long id);

    UserToken save(UserToken userToken);

}
