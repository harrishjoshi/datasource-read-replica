package com.harrish.datasourcereadreplica.dto;

import lombok.Builder;

@Builder
public record UserResponse(Long id, String name, String email, String address) {
}
