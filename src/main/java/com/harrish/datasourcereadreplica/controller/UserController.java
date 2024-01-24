package com.harrish.datasourcereadreplica.controller;

import com.harrish.datasourcereadreplica.dto.UserDTO;
import com.harrish.datasourcereadreplica.dto.UserRequest;
import com.harrish.datasourcereadreplica.dto.UserResponse;
import com.harrish.datasourcereadreplica.model.User;
import com.harrish.datasourcereadreplica.service.UserReportService;
import com.harrish.datasourcereadreplica.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    private final UserReportService userReportService;

    public UserController(UserService userService, UserReportService userReportService) {
        this.userService = userService;
        this.userReportService = userReportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void addUser(@RequestBody UserRequest userRequest) {
        userService.addUser(userRequest);
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> finaAllUsers() {
        return ResponseEntity.ok(userService.finaAllUser());
    }

    @GetMapping("{version}")
    ResponseEntity<List<User>> finaAllByVersion(@PathVariable String version) {
        return ResponseEntity.ok(userService.findAllByVersion(version));
    }

    @GetMapping("report")
    ResponseEntity<List<UserDTO>> findUserReport() {
        return ResponseEntity.ok(userReportService.findUserReport());
    }
}
