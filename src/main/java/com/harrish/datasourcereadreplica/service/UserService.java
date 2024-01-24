package com.harrish.datasourcereadreplica.service;

import com.harrish.datasourcereadreplica.dto.UserRequest;
import com.harrish.datasourcereadreplica.dto.UserResponse;
import com.harrish.datasourcereadreplica.model.User;
import com.harrish.datasourcereadreplica.repository.UserReadOnlyRepository;
import com.harrish.datasourcereadreplica.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserReadOnlyRepository userReadOnlyRepository;

    public UserService(UserRepository userRepository, UserReadOnlyRepository userReadOnlyRepository) {
        this.userRepository = userRepository;
        this.userReadOnlyRepository = userReadOnlyRepository;
    }

    public void addUser(UserRequest userRequest) {
        var user = new User();
        BeanUtils.copyProperties(userRequest, user);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public List<UserResponse> finaAllUser() {
        return userReadOnlyRepository.findAll()
                .stream().map(this::mapToUserResponse)
                .toList();
    }

    public List<User> findAllByVersion(String version) {
        return switch (ObjectUtils.isEmpty(version) ? "" : version) {
            case "v1" -> userReadOnlyRepository.findAllV1();
            case "v2" -> userReadOnlyRepository.findAllV2();
            case "v3" -> userReadOnlyRepository.findAllV3();
            case "v4" -> userReadOnlyRepository.findAllV4();
            case "v5" -> userReadOnlyRepository.findAllV5();
            default -> userReadOnlyRepository.findAll();
        };
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .address(user.getAddress())
                .email(user.getEmail())
                .build();
    }
}
