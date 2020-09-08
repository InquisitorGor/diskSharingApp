package ru.ayubdzhanov.disksharingapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ayubdzhanov.disksharingapp.dao.jpa.Dao;
import ru.ayubdzhanov.disksharingapp.dao.spring.data.CredentialRepository;
import ru.ayubdzhanov.disksharingapp.dao.spring.data.UserRepository;
import ru.ayubdzhanov.disksharingapp.domain.Credential;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.User;

import java.util.List;

@RestController
@RequestMapping("/user")
public class MainController {

    private Long currentUserId;

    @Autowired
    private Dao dao;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/welcome")
    public ResponseEntity<?> welcome(){
        specifyCurrentUserId();
        User user = userRepository.findById(currentUserId).get();
        return ResponseEntity.ok("Добро пожаловать " + user.getRealName());
    }

    @GetMapping("/getAllDisks")
    public ResponseEntity<List<Disk>> getAllUserDisks(){
        List<Disk> disks = userRepository.findById(currentUserId).get().getListDisk();
        return ResponseEntity.ok(disks);
    }
    @GetMapping("/getAllFreeDisks")
    public ResponseEntity<List<Disk>> getAllFreeDisks(){
        List<Disk> freeDisks = dao.getAllFreeDisks();
        return ResponseEntity.ok(freeDisks);
    }

    @GetMapping("/getAllTakenDisks")
    public ResponseEntity<List<Disk>> getAllDisksTakenByUser(){
        List<Disk> takenDisks = dao.getAllDisksTakenByUser(currentUserId);
        return ResponseEntity.ok(takenDisks);
    }

    @GetMapping("/getAllDiskWhichWasTaken")
    public ResponseEntity<List<Disk>> getAllDisksWhichWasTaken(){
        List<Disk> takenDisks = dao.getAllDisksWhichWasTaken(currentUserId);
        return ResponseEntity.ok(takenDisks);
    }




    private void specifyCurrentUserId() {
        Credential credential = ((Credential) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String userName = null;

        if (credential != null) {

            userName = credential.getUsername();

            try {
                this.currentUserId = credentialRepository.findByUsername(userName)
                        .getId();
            } catch (CannotCreateTransactionException ignored) {
                System.out.println("Something went wrong");
            }
        }
    }
}


