package com.harrish.datasourcereadreplica.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "user_report", procedureName = "user_report", resultClasses = {
                UserDTO.class})
})
@Entity
@Data
public class UserDTO implements Serializable {

    @Id
    private Long id;

    private String name;

    private String email;

    private String address;

    private String fullName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
