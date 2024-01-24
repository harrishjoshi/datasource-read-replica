package com.harrish.datasourcereadreplica.service;

import com.harrish.datasourcereadreplica.config.ReadOnlyRepository;
import com.harrish.datasourcereadreplica.dto.UserDTO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ReadOnlyRepository
public class UserReportService {

    private final EntityManager entityManager;

    public UserReportService(@Qualifier("readOnlyDataSourceManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<UserDTO> findUserReport() {
        var storedProcedure = entityManager.createNamedStoredProcedureQuery("user_report");

        return storedProcedure.getResultList();
    }
}
