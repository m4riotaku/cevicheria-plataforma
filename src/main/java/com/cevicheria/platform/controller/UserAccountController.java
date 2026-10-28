package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.UserAccountCreateRequest;
import com.cevicheria.platform.dto.UserAccountResponse;
import com.cevicheria.platform.dto.UserAccountUpdateRequest;
import com.cevicheria.platform.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserAccountController
{
    private final UserAccountService userService;


    @GetMapping
    public List<UserAccountResponse> listAllUsers()
    {
        return userService.listAllUsers();
    }


    @GetMapping
    public List<UserAccountResponse> searchUsers(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean active)
    {
        return userService.searchUsers(userName, email, active);
    }

    @GetMapping("/{id}")
    public UserAccountResponse findById(@PathVariable Long id)
    {
        return userService.findUserById(id);
    }

    @GetMapping("/filter/{email}")
    public UserAccountResponse findByEmail(@PathVariable String email)
    {
        return userService.findUserByEmail(email);
    }

    @PatchMapping("/{id}")
    public UserAccountResponse updateUser(@PathVariable Long id, @RequestBody UserAccountUpdateRequest request)
    {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<UserAccountResponse> createUser(@RequestBody UserAccountCreateRequest request)
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }
}
