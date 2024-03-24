package com.codefusion.wasbackend.user.controller;

import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/allUser")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<UserDTO>> getUsersByStoreId(@PathVariable Long storeId) {
        return ResponseEntity.ok(userService.getUsersByStoreId(storeId));
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO, @RequestParam MultipartFile file) throws IOException {
        return new ResponseEntity<>(userService.addUser(userDTO, file), HttpStatus.CREATED);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO,
                                              @RequestParam(required = false) MultipartFile file) throws IOException {
        userDTO.setId(id);
        return ResponseEntity.ok(userService.update(id, userDTO, file));
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws IOException {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}