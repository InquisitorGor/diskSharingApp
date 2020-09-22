package ru.ayubdzhanov.disksharingapp.dao.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ayubdzhanov.disksharingapp.domain.Credential;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.TakenItems;
import ru.ayubdzhanov.disksharingapp.domain.User;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class DaoImplTest {

    @Autowired
    private Dao dao;

    @Autowired
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long firstUserId;
    private Long secondUserId;


    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public Dao dao() {
            return new DaoImpl();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @BeforeEach // bad choice (((((
    void setUp() {
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


        takenItems.setCurrentOwner(firstUser);
        takenItems.setDisk(firstDisk);

        takenItems1.setDisk(secondDisk);
        takenItems1.setCurrentOwner(secondUser);

        takenItems2.setDisk(thirdDisk);

        takenItems3.setDisk(fourthDisk);

        firstUserCredential.setPassword(passwordEncoder.encode("1234"));
        firstUserCredential.setUsername("TheDanger");

        secondUserCredential.setPassword(passwordEncoder.encode("1234"));
        secondUserCredential.setUsername("TheOneWhoKnocks");

        firstDisk.setName("Eternal bliss");
        firstDisk.setCurrentUser(secondUser);

        thirdDisk.setName("Unknown hero");

        fourthDisk.setName("Emperor's chosen");

        secondDisk.setName("Seeking truth");
        secondDisk.setCurrentUser(firstUser);


        firstUser.addDisk(fourthDisk);
        firstUser.addDisk(firstDisk);
        firstUser.setCredential(firstUserCredential);
        firstUser.setRealName("Jon");

        secondUser.setCredential(secondUserCredential);
        secondUser.setRealName("Egor");
        secondUser.addDisk(secondDisk);
        secondUser.addDisk(thirdDisk);

        dao.add(firstUser);
        dao.add(secondUser);
        dao.add(takenItems);
        dao.add(takenItems1);
        dao.add(takenItems2);
        dao.add(takenItems3);

        firstUserId = firstUser.getId();
        secondUserId = secondUser.getId();
    }



    @Test
    void addTest() {
        User user = new User();
        user.setRealName("Jon");

        dao.add(user);
        User userFromDB = em.find(User.class, user.getId());

        assertThat(userFromDB.getRealName())
                .isEqualTo(user.getRealName());

        em.remove(user);
    }

    @Test
    void getAllDisksTest() {

        List<Disk> firstUserDiskList = dao.getAllDisks(firstUserId);
        List<Disk> secondUserDiskList = dao.getAllDisks(secondUserId);

        assertThat(firstUserDiskList.size()).isEqualTo(2);
        assertThat(firstUserDiskList.get(0).getName()).isEqualTo("Emperor's chosen");
        assertThat(firstUserDiskList.get(1).getName()).isEqualTo("Eternal bliss");

        assertThat(secondUserDiskList.size()).isEqualTo(2);
        assertThat(secondUserDiskList.get(0).getName()).isEqualTo("Seeking truth");
        assertThat(secondUserDiskList.get(1).getName()).isEqualTo("Unknown hero");

    }

    @Test
    void getAllFreeDisksTest() {
        List<Disk> freeDisks = dao.getAllFreeDisks();

        assertThat(freeDisks.size()).isEqualTo(2);
        freeDisks.forEach(disk -> {
            assertThat(disk.getCurrentUser()).isEqualTo(null);
        });

    }

    @Test
    void getAllDisksTakenByUserTest() {

        List<Disk> firstUserDiskList = dao.getAllDisksTakenByUser(firstUserId);
        List<Disk> secondUserDiskList = dao.getAllDisksTakenByUser(secondUserId);

        assertThat(firstUserDiskList.size()).isEqualTo(1);
        assertThat(firstUserDiskList.get(0).getName()).isEqualTo("Seeking truth");
        firstUserDiskList.forEach(disk -> {
            assertThat(disk.getCurrentUser().getId()).isEqualTo(firstUserId);
            assertThat(disk.getOriginalOwner().getId()).isNotEqualTo(firstUserId);
        });

        assertThat(secondUserDiskList.size()).isEqualTo(1);
        assertThat(secondUserDiskList.get(0).getName()).isEqualTo("Eternal bliss");
        secondUserDiskList.forEach(disk -> {
            assertThat(disk.getCurrentUser().getId()).isEqualTo(secondUserId);
            assertThat(disk.getOriginalOwner().getId()).isNotEqualTo(secondUserId);
        });
    }

    @Test
    void getAllDisksWhichWasTakenTest() {
        List<Disk> firstUserDiskList = dao.getAllDisksWhichWasTaken(firstUserId);
        List<Disk> secondUserDiskList = dao.getAllDisksWhichWasTaken(secondUserId);

        assertThat(firstUserDiskList.size()).isEqualTo(1);
        assertThat(firstUserDiskList.get(0).getName()).isEqualTo("Eternal bliss");
        firstUserDiskList.forEach(disk -> {
            assertThat(disk.getCurrentUser().getId()).isNotEqualTo(firstUserId);
            assertThat(disk.getOriginalOwner().getId()).isEqualTo(firstUserId);
        });

        assertThat(secondUserDiskList.size()).isEqualTo(1);
        assertThat(secondUserDiskList.get(0).getName()).isEqualTo("Seeking truth");
        secondUserDiskList.forEach(disk -> {
            assertThat(disk.getCurrentUser().getId()).isNotEqualTo(secondUserId);
            assertThat(disk.getOriginalOwner().getId()).isEqualTo(secondUserId);
        });
    }

    @Test
    void findFreeDiskTest() {
        List<Disk> freeDisks = dao.getAllFreeDisks();
        List<Disk> unfreeDisks = dao.getAllDisksTakenByUser(firstUserId);

        Disk randomFreeDisk = dao.findFreeDisk(freeDisks.get(0).getId());
        Disk randomUnfreeDisk = dao.findFreeDisk(unfreeDisks.get(0).getId());

        assertThat(randomFreeDisk).isNotNull();
        assertThat(randomUnfreeDisk).isNull();

    }

}