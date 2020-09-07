package ru.ayubdzhanov.disksharingapp.bootsrtap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ayubdzhanov.disksharingapp.dao.Dao;
import ru.ayubdzhanov.disksharingapp.domain.Credential;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.TakenItems;
import ru.ayubdzhanov.disksharingapp.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

        User firstUser = new User();

        Credential firstUserCredential = new Credential();



        TakenItems takenItems = new TakenItems();
        takenItems.setCurrentOwner(firstUser);
        takenItems.setDisk(firstDisk);

        firstUserCredential.setPassword(passwordEncoder.encode("1234"));
        firstUserCredential.setUsername("egor");
        firstUserCredential.setUser(firstUser);

        firstDisk.setName("JoJo");
        firstDisk.setCurrentUser(firstUser);
        firstDisk.setOriginalOwner(firstUser);

        firstUser.getListDisk().add(firstDisk);
        firstUser.setCredential(firstUserCredential);
        firstUser.setUsername("Jon");

        dao.add(firstUser);
        dao.add(takenItems);
        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }
}
