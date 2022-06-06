package com.ns.managebars.repo;

import com.ns.managebars.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {


    @Query(value = "select * from user u where u.actual = 1",nativeQuery = true)
    List<User> getAllUsers();

    @Transactional
    @Modifying
    @Query("update User u set u.name = :newName where u = :user")
    void updateUserName(@Param("user") User user, 
                        @Param("newName") String newName);

    @Transactional
    @Modifying
    @Query(value = "update User u set u.actual = 0 where u.id = :id",nativeQuery = true)
    void removeUser(@Param("id") long id);
}
