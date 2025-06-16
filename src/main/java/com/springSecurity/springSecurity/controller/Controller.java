package com.springSecurity.springSecurity.controller;

import com.springSecurity.springSecurity.model.User;
import com.springSecurity.springSecurity.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
public class Controller {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String goHome() {
        return "This is publicly accessible without needing authentication.";
    }

    @PostMapping("/user/save")
    public ResponseEntity<Object> saveUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User result = userRepo.save(user);
        if (result.getId() > 0) {
            return ResponseEntity.ok("User was saved.");
        }
        return ResponseEntity.status(404).body("Error, user not saved.");
    }

//    @GetMapping("/product/all")
//    public ResponseEntity<Object> getAllProducts() {
//        return ResponseEntity.ok(productRepo.findAll());
//    }

    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllusers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

//    @GetMapping("/users/single")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
//    public ResponseEntity<Object> getMyDetails() {
//        return ResponseEntity.ok(userRepo.findByEmail(getLoggedInUserDetails().getUsername()));
//    }

    /**
     * USER: Endpoint para um usuário visualizar seu próprio perfil.
     */
    @GetMapping("/users/me")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> getMyProfile() {
        String userEmail = getLoggedInUserDetails().getUsername();
        return ResponseEntity.ok(userRepo.findByEmail(userEmail));
    }

    /**
     * USER: Endpoint para um usuário editar seu próprio perfil (nome e senha).
     */
    @PutMapping("/users/me")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> updateMyProfile(@RequestBody User updatedUser) {
        String userEmail = getLoggedInUserDetails().getUsername();
        Optional<User> optionalUser = userRepo.findByEmail(userEmail);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        User currentUser = optionalUser.get();
        // Atualiza o nome se for fornecido
        if (updatedUser.getName() != null) {
            currentUser.setName(updatedUser.getName());
        }
        // Atualiza a senha se for fornecida, e a re-criptografa
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            currentUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepo.save(currentUser);
        return ResponseEntity.ok("Your profile has been updated.");
    }

    /**
     * ADMIN: Endpoint para um admin visualizar qualquer usuário pelo ID.
     */
    @GetMapping("/admin/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(404).body("User with ID " + id + " not found.");
    }

    /**
     * ADMIN: Endpoint para um admin editar qualquer usuário pelo ID.
     */
    @PutMapping("/admin/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> updateUserById(@PathVariable Integer id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("User with ID " + id + " not found.");
        }

        User userToUpdate = optionalUser.get();
        // Atualiza os campos fornecidos
        if (updatedUser.getName() != null) {
            userToUpdate.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            userToUpdate.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getRoles() != null) {
            userToUpdate.setRoles(updatedUser.getRoles());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepo.save(userToUpdate);
        return ResponseEntity.ok("User with ID " + id + " has been updated.");
    }

    /**
     * ADMIN: Endpoint para um admin deletar qualquer usuário pelo ID.
     */
    @DeleteMapping("/admin/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> deleteUserById(@PathVariable Integer id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return ResponseEntity.ok("User with ID " + id + " has been deleted.");
        }
        return ResponseEntity.status(404).body("User with ID " + id + " not found.");
    }

    public UserDetails getLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

}
