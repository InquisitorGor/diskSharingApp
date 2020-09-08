package ru.ayubdzhanov.disksharingapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ayubdzhanov.disksharingapp.dao.CredentialRepository;
import ru.ayubdzhanov.disksharingapp.dao.UserRepository;
import ru.ayubdzhanov.disksharingapp.domain.Credential;
import ru.ayubdzhanov.disksharingapp.domain.Disk;

import java.util.List;

@RestController
//"/user/get"
@RequestMapping("/user")
public class MainController {

    private Long currentUserId;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAllDisks")
    public ResponseEntity<List<Disk>> getAllUserDisks(){
        specifyCurrentUserId();
        List<Disk> disks = userRepository.findById(currentUserId).get().getListDisk();
        return ResponseEntity.ok(disks);
    }
//    @GetMapping("getAllFreeDisks")
//    public ResponseEntity<List<Disk>> getAllFreeDisks(){
//        specifyCurrentUserId();
//        //List<Disk> freeDisks =
//    }




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


