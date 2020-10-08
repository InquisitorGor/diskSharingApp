package ru.ayubdzhanov.disksharingapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ayubdzhanov.disksharingapp.dao.CredentialDao;
import ru.ayubdzhanov.disksharingapp.dao.DiskDao;
import ru.ayubdzhanov.disksharingapp.dao.TakenItemsDao;
import ru.ayubdzhanov.disksharingapp.dao.UserDao;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.TakenItems;
import ru.ayubdzhanov.disksharingapp.domain.User;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class MainController {

    private Long currentUserId;

    private final DiskDao diskDao;

    private final UserDao userDao;

    private final TakenItemsDao takenItemsDao;

    private final CredentialDao credentialDao;

    public MainController(DiskDao diskDao, UserDao userDao, TakenItemsDao takenItemsDao, CredentialDao credentialDao) {
        this.diskDao = diskDao;
        this.userDao = userDao;
        this.takenItemsDao = takenItemsDao;
        this.credentialDao = credentialDao;
    }


    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        specifyCurrentUserId();
        User user = userDao.findById(currentUserId);
        return ResponseEntity.ok("Добро пожаловать " + user.getRealName());
    }

    @GetMapping("/getAllDisks")
    public ResponseEntity<List<Disk>> getAllUserDisks() {
        List<Disk> disks = diskDao.getDisks(currentUserId);
        return ResponseEntity.ok(disks);
    }

    @GetMapping("/getAllFreeDisks")
    public ResponseEntity<List<Disk>> getAllFreeDisks() {
        List<Disk> freeDisks = diskDao.getFreeDisks();
        return ResponseEntity.ok(freeDisks);
    }

    @GetMapping("/getAllTakenDisks")
    public ResponseEntity<List<Disk>> getAllDisksTakenByUser() {
        List<Disk> takenDisks = diskDao.getDisksTakenByCurrentUser(currentUserId);
        return ResponseEntity.ok(takenDisks);
    }

    @GetMapping("/getAllDiskWhichWasTaken")
    public ResponseEntity<List<ControllerSupport>> getAllDisksWhichWasTaken() {
        List<Disk> takenDisks = diskDao.getDisksTakenByCurrentUser(currentUserId);
        List<ControllerSupport> takenDiskWithUser = new LinkedList<>();

        takenDisks.forEach(disk -> {
            ControllerSupport controllerSupport =
                    new ControllerSupport(disk.getId(),
                            disk.getName(),
                            disk.getTakenItems().getCurrentOwner().getRealName());
            takenDiskWithUser.add(controllerSupport);
        });
        return ResponseEntity.ok(takenDiskWithUser);
    }

    public static class ControllerSupport {
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
        Disk borrowedDisk = diskDao.getDisk(id);
        if (borrowedDisk.getTakenItems().getCurrentOwner() == null) {
            return ResponseEntity.badRequest().body("This disk is free");
        }
        if(!borrowedDisk.getTakenItems().getCurrentOwner().getId().equals(currentUserId)) {
            return ResponseEntity.badRequest().body("This disk belongs to another user");
        }

        TakenItems takenItems = takenItemsDao.findByDiskId(borrowedDisk.getId());

        takenItems.setCurrentOwner(null);

        takenItemsDao.save(takenItems);

        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }

    @GetMapping("/getFreeDisk/{id}")
    public ResponseEntity<?> getFreeDisk(@PathVariable("id") Long id) {
        Disk freeDisk = diskDao.findFreeDisk(id);

        if(freeDisk == null) {
            return ResponseEntity.badRequest().body("Hold ur horses. This disk is not free");
        }

        User user = userDao.findById(currentUserId);

        if(freeDisk.getTakenItems().getCurrentOwner().getId().equals(user.getId())) {
            return ResponseEntity.badRequest().body("Bro, chill. This disk is yours");
        }

        TakenItems takenItems = takenItemsDao.findByDiskId(freeDisk.getId());

        takenItems.setCurrentOwner(user);

        takenItemsDao.save(takenItems);

        return ResponseEntity.ok(Collections.EMPTY_LIST);

    }


    private void specifyCurrentUserId() {
        UserDetails credential = ((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String userName = null;

        if (credential != null) {

            userName = credential.getUsername();

            try {
                this.currentUserId = credentialDao.findByUsername(userName)
                        .getId();
            } catch (CannotCreateTransactionException ignored) {
                System.out.println("Something went wrong");
            }
        }
    }
}


