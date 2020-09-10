package ru.ayubdzhanov.disksharingapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ayubdzhanov.disksharingapp.dao.jpa.Dao;
import ru.ayubdzhanov.disksharingapp.dao.spring.data.CredentialRepository;
import ru.ayubdzhanov.disksharingapp.dao.spring.data.DiskRepository;
import ru.ayubdzhanov.disksharingapp.dao.spring.data.TakenItemRepository;
import ru.ayubdzhanov.disksharingapp.dao.spring.data.UserRepository;
import ru.ayubdzhanov.disksharingapp.domain.Credential;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.TakenItems;
import ru.ayubdzhanov.disksharingapp.domain.User;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
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
    private TakenItemRepository takenItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiskRepository diskRepository;

    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        specifyCurrentUserId();
        User user = userRepository.findById(currentUserId).get();
        return ResponseEntity.ok("Добро пожаловать " + user.getRealName());
    }

    @GetMapping("/getAllDisks")
    public ResponseEntity<List<Disk>> getAllUserDisks() {
        List<Disk> disks = dao.getAllDisks(currentUserId);
        return ResponseEntity.ok(disks);
    }

    @GetMapping("/getAllFreeDisks")
    public ResponseEntity<List<Disk>> getAllFreeDisks() {
        List<Disk> freeDisks = dao.getAllFreeDisks();
        return ResponseEntity.ok(freeDisks);
    }

    @GetMapping("/getAllTakenDisks")
    public ResponseEntity<List<Disk>> getAllDisksTakenByUser() {
        List<Disk> takenDisks = dao.getAllDisksTakenByUser(currentUserId);
        return ResponseEntity.ok(takenDisks);
    }

    @GetMapping("/getAllDiskWhichWasTaken")
    public ResponseEntity<List<ControllerSupport>> getAllDisksWhichWasTaken() {
        List<Disk> takenDisks = dao.getAllDisksWhichWasTaken(currentUserId);
        List<ControllerSupport> takenDiskWithUser = new LinkedList<>();

        takenDisks.forEach(disk -> {
            ControllerSupport controllerSupport =
                    new ControllerSupport(disk.getId(),
                            disk.getName(),
                            disk.getCurrentUser().getRealName());
            takenDiskWithUser.add(controllerSupport);
        });
        return ResponseEntity.ok(takenDiskWithUser);
    }

    static class ControllerSupport {
        private Long id;
        private String name;
        private String realName;

        public ControllerSupport(Long id, String name, String realName) {
            this.id = id;
            this.name = name;
            this.realName = realName;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }
    }

    @GetMapping("/giveBackDisk/{id}")
    public ResponseEntity<?> giveBackDisk(@PathVariable("id") Long id) {
        Disk borrowedDisk = dao.getDisk(id);
        if (borrowedDisk.getCurrentUser() == null) {
            return ResponseEntity.badRequest().body("This disk is free");
        }
        if(!borrowedDisk.getCurrentUser().getId().equals(currentUserId)) {
            return ResponseEntity.badRequest().body("This disk belongs to another user");
        }

        TakenItems takenItems = takenItemRepository.findByDiskId(borrowedDisk.getId());

        borrowedDisk.setCurrentUser(null);
        takenItems.setCurrentOwner(null);

        takenItemRepository.save(takenItems);
        diskRepository.save(borrowedDisk);

        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }

    @GetMapping("/getFreeDisk/{id}")
    public ResponseEntity<?> getFreeDisk(@PathVariable("id") Long id) {
        Disk freeDisk = dao.findFreeDisk(id);

        if(freeDisk == null) {
            return ResponseEntity.badRequest().body("This disk is not free");
        }

        User user = userRepository.findById(currentUserId).get();

        TakenItems takenItems = takenItemRepository.findByDiskId(freeDisk.getId());

        freeDisk.setCurrentUser(user);
        takenItems.setCurrentOwner(user);

        dao.add(takenItems);
        dao.add(freeDisk);

        return ResponseEntity.ok(Collections.EMPTY_LIST);

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


