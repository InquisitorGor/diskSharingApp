package ru.ayubdzhanov.disksharingapp.bootsrtap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ayubdzhanov.disksharingapp.dao.jpa.Dao;
import ru.ayubdzhanov.disksharingapp.domain.Credential;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.TakenItems;
import ru.ayubdzhanov.disksharingapp.domain.User;

import java.util.Collections;

@RestController
@RequestMapping("/loadData")
public class BootstrapController {

    private final Dao dao;
    private final PasswordEncoder passwordEncoder;

    public BootstrapController(Dao dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> loadData(){

        Disk firstDisk = new Disk();
        Disk secondDisk = new Disk();
        Disk thirdDisk = new Disk();
        Disk fourthDisk = new Disk();

        User firstUser = new User();
        User secondUser = new User();

        Credential firstUserCredential = new Credential();
        Credential secondUserCredential = new Credential();

        TakenItems takenItems = new TakenItems();
        TakenItems takenItems1 = new TakenItems();
        TakenItems takenItems2 = new TakenItems();
        TakenItems takenItems3 = new TakenItems();


        takenItems.setCurrentOwner(firstUser); //get rid of it
        takenItems.setDisk(firstDisk);

        takenItems1.setDisk(secondDisk);
        takenItems1.setCurrentOwner(secondUser);

        takenItems2.setDisk(thirdDisk);

        takenItems3.setDisk(fourthDisk);
        takenItems3.setCurrentOwner(secondUser);

        firstUserCredential.setPassword(passwordEncoder.encode("1234"));
        firstUserCredential.setUsername("TheDanger");
        firstUserCredential.setUser(firstUser);

        secondUserCredential.setPassword(passwordEncoder.encode("1234"));
        secondUserCredential.setUsername("TheOneWhoKnocks");
        secondUserCredential.setUser(secondUser);

        firstDisk.setName("Eternal bliss");
        firstDisk.setCurrentUser(firstUser);
        firstDisk.setOriginalOwner(secondUser); //secondUser

        thirdDisk.setName("Unknown hero");
        thirdDisk.setOriginalOwner(secondUser);

        fourthDisk.setName("Emperor's chosen");
        fourthDisk.setOriginalOwner(secondUser);
        fourthDisk.setCurrentUser(secondUser);

        secondDisk.setOriginalOwner(secondUser);
        secondDisk.setName("Seeking truth");
        secondDisk.setCurrentUser(secondUser);

        firstUser.getListDisk().add(firstDisk);
        firstUser.setCredential(firstUserCredential);
        firstUser.setRealName("Jon");

        secondUser.setCredential(secondUserCredential);
        secondUser.setRealName("Egor");
        secondUser.getListDisk().add(secondDisk);
        secondUser.getListDisk().add(thirdDisk);

        dao.add(firstUser);
        dao.add(secondUser);
        dao.add(takenItems);
        dao.add(takenItems1);
        dao.add(takenItems2);
        dao.add(takenItems3);
        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }
}
