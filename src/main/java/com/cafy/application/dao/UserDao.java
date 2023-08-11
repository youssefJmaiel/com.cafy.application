package com.cafy.application.dao;

import com.cafy.application.entity.User;
import com.cafy.application.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao extends JpaRepository<User,Integer> , JpaSpecificationExecutor<User> {

    User findByEmailId(@Param("email") String email);

    List<UserWrapper> getAllUser();

    List<String> getAllAdmin();
    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status,@Param("id") Integer id);

    User findByEmail(String email);
    @Query("Select h from User h where h.id=:id")
    User findByIds(Integer id);
}
