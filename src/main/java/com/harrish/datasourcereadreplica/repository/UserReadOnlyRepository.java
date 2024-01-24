package com.harrish.datasourcereadreplica.repository;

import com.harrish.datasourcereadreplica.config.ReadOnlyRepository;
import com.harrish.datasourcereadreplica.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;


@ReadOnlyRepository
public interface UserReadOnlyRepository extends Repository<User, Long> {

    List<User> findAll();

    @Query("""
             SELECT u FROM User u WHERE u.createdAt > CURRENT_TIMESTAMP
            """)
    List<User> findAllV1();

    @Query("""
             SELECT u.id, u.name, u.email, u.address, u.createdAt FROM User u WHERE u.createdAt > CURRENT_TIMESTAMP
            """)
    List<User> findAllV2();

    @Query(value = """
             SELECT u.id, u.name, u.email, u.address, u.created_at FROM tbl_user u WHERE u.created_at > CURRENT_TIMESTAMP
            """, nativeQuery = true)
    List<User> findAllV3();

    @Query(value = """
             SELECT * FROM tbl_user WHERE created_at > CURRENT_TIMESTAMP
            """, nativeQuery = true)
    List<User> findAllV4();

    @Query(value = """
             SELECT u.* FROM tbl_user u WHERE u.created_at > CURRENT_TIMESTAMP
            """, nativeQuery = true)
    List<User> findAllV5();
}
