package ru.ayubdzhanov.disksharingapp.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ayubdzhanov.disksharingapp.dao.Dao;
import ru.ayubdzhanov.disksharingapp.dao.DaoImpl;
import ru.ayubdzhanov.disksharingapp.domain.Disk;

import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DaoImplTest {


    @Autowired
    private Dao dao;

    private Long firstUserId = 1L;
    private Long secondUserId = 2L;

    @TestConfiguration
    static class DaoImplTestConfiguration {
        @Bean
        public Dao dao() {
            return new DaoImpl();
        }
    }

    @Test
    public void getDisksTest(){
        List<Disk> firstUserDiskList = dao.getDisks(firstUserId);
        List<Disk> secondUserDiskList = dao.getDisks(secondUserId);

        assertThat(firstUserDiskList.size()).isEqualTo(2);
        assertThat(firstUserDiskList.get(0).getName()).isEqualTo("Emperor's chosen");
        assertThat(firstUserDiskList.get(1).getName()).isEqualTo("Eternal bliss");

        assertThat(secondUserDiskList.size()).isEqualTo(2);
        assertThat(secondUserDiskList.get(0).getName()).isEqualTo("Seeking truth");
        assertThat(secondUserDiskList.get(1).getName()).isEqualTo("Unknown hero");
    }

    @Test
    public void getFreeDisksTest() {
        List<Disk> freeDisks = dao.getFreeDisks();

        assertThat(freeDisks.size()).isEqualTo(2);
        freeDisks.forEach(disk -> {
            assertThat(disk.getTakenItems().getCurrentOwner()).isEqualTo(null);
        });
    }

    @Test
    public void getDisksTakenByCurrentUserTest() {

        List<Disk> firstUserDiskList = dao.getDisksTakenByCurrentUser(firstUserId);
        List<Disk> secondUserDiskList = dao.getDisksTakenByCurrentUser(secondUserId);

        assertThat(firstUserDiskList.size()).isEqualTo(1);
        assertThat(firstUserDiskList.get(0).getName()).isEqualTo("Unknown hero");
        firstUserDiskList.forEach(disk -> {
            assertThat(disk.getTakenItems().getCurrentOwner().getId()).isEqualTo(firstUserId);
            assertThat(disk.getTakenItems().getOriginalOwner().getId()).isNotEqualTo(firstUserId);
        });

        assertThat(secondUserDiskList.size()).isEqualTo(1);
        assertThat(secondUserDiskList.get(0).getName()).isEqualTo("Eternal bliss");
        secondUserDiskList.forEach(disk -> {
            assertThat(disk.getTakenItems().getCurrentOwner().getId()).isEqualTo(secondUserId);
            assertThat(disk.getTakenItems().getOriginalOwner().getId()).isNotEqualTo(secondUserId);
        });
    }

    @Test
    public void getDisksWhichWasTakenFromUserTest() {
        List<Disk> firstUserDiskList = dao.getDisksWhichWasTakenFromUser(firstUserId);
        List<Disk> secondUserDiskList = dao.getDisksWhichWasTakenFromUser(secondUserId);


        assertThat(firstUserDiskList.size()).isEqualTo(1);
        assertThat(firstUserDiskList.get(0).getName()).isEqualTo("Eternal bliss");
        firstUserDiskList.forEach(disk -> {
            assertThat(disk.getTakenItems().getCurrentOwner().getId()).isNotEqualTo(firstUserId);
            assertThat(disk.getTakenItems().getOriginalOwner().getId()).isEqualTo(firstUserId);
        });

        assertThat(secondUserDiskList.size()).isEqualTo(1);
        assertThat(secondUserDiskList.get(0).getName()).isEqualTo("Unknown hero");
        secondUserDiskList.forEach(disk -> {
            assertThat(disk.getTakenItems().getCurrentOwner().getId()).isNotEqualTo(secondUserId);
            assertThat(disk.getTakenItems().getOriginalOwner().getId()).isEqualTo(secondUserId);
        });
    }


}