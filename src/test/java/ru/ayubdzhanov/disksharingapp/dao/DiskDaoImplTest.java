package ru.ayubdzhanov.disksharingapp.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.User;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DiskDaoImplTest {


    @Autowired
    private DiskDao diskDao;

    private Long firstUserId = 1L;
    private Long secondUserId = 2L;

    @TestConfiguration
    static class DaoImplTestConfiguration {
        @Bean
        public DiskDao diskDao() {
            return new DiskDaoImpl();
        }
    }

    @Before
    public void setUp() throws Exception {
        assertNotNull(diskDao);
    }

    @Test
    public void getDisksTest(){
        List<Disk> firstUserDiskList = diskDao.getDisks(firstUserId);
        List<Disk> secondUserDiskList = diskDao.getDisks(secondUserId);

        assertThat(firstUserDiskList.size()).isEqualTo(2);
        Optional<Disk> firstDiskOfFirstUser = firstUserDiskList.stream()
                .filter(
                        x -> x.getName().equals("Emperor's chosen")
                ).findFirst();
        firstDiskOfFirstUser.ifPresent(disk -> assertThat(disk
                .getName())
                .isEqualTo("Emperor's chosen"));
        Optional<Disk> secondDiskOfFirstUser = firstUserDiskList.stream()
                .filter(
                        x -> x.getName().equals("Eternal bliss")
                ).findFirst();
        secondDiskOfFirstUser.ifPresent(disk -> assertThat(disk
                .getName())
                .isEqualTo("Eternal bliss"));


        assertThat(secondUserDiskList.size()).isEqualTo(2);
        Optional<Disk> firstDiskOfSecondUser = secondUserDiskList.stream()
                .filter(
                        x -> x.getName().equals("Seeking truth")
                ).findFirst();
        firstDiskOfSecondUser.ifPresent(disk -> assertThat(disk
                .getName())
                .isEqualTo("Seeking truth"));
        Optional<Disk> secondDiskOfSecondUser = secondUserDiskList.stream()
                .filter(
                        x -> x.getName().equals("Unknown hero")
                ).findFirst();
        secondDiskOfSecondUser.ifPresent(disk -> assertThat(disk
                .getName())
                .isEqualTo("Unknown hero"));
    }

    @Test
    public void getFreeDisksTest() {
        List<Disk> freeDisks = diskDao.getFreeDisks();

        assertThat(freeDisks.size()).isEqualTo(2);
        freeDisks.forEach(disk -> {
            assertThat(disk.getTakenItems().getCurrentOwner()).isEqualTo(null);
        });
    }

    @Test
    public void getDisksTakenByCurrentUserTest() {

        List<Disk> firstUserDiskList = diskDao.getDisksTakenByCurrentUser(firstUserId);
        List<Disk> secondUserDiskList = diskDao.getDisksTakenByCurrentUser(secondUserId);

        assertThat(firstUserDiskList.size()).isEqualTo(1);
        Optional<Disk> firstDiskOfFirstUser = firstUserDiskList.stream()
                .filter(
                        x -> x.getName().equals("Unknown hero")
                ).findFirst();
        firstDiskOfFirstUser.ifPresent(disk -> assertThat(disk
                .getName())
                .isEqualTo("Unknown hero"));
        firstUserDiskList.forEach(disk -> {
            assertThat(disk.getTakenItems().getCurrentOwner().getId()).isEqualTo(firstUserId);
            assertThat(disk.getTakenItems().getOriginalOwner().getId()).isNotEqualTo(firstUserId);
        });

        assertThat(secondUserDiskList.size()).isEqualTo(1);
        Optional<Disk> firstDiskOfSecondUser = secondUserDiskList.stream()
                .filter(
                        x -> x.getName().equals("Eternal bliss")
                ).findFirst();
        firstDiskOfSecondUser.ifPresent(disk -> assertThat(disk
                .getName())
                .isEqualTo("Eternal bliss"));
        secondUserDiskList.forEach(disk -> {
            assertThat(disk.getTakenItems().getCurrentOwner().getId()).isEqualTo(secondUserId);
            assertThat(disk.getTakenItems().getOriginalOwner().getId()).isNotEqualTo(secondUserId);
        });
    }

    @Test
    public void getDisksWhichWasTakenFromUserTest() {
        List<Disk> firstUserDiskList = diskDao.getDisksWhichWasTakenFromUser(firstUserId);
        List<Disk> secondUserDiskList = diskDao.getDisksWhichWasTakenFromUser(secondUserId);


        assertThat(firstUserDiskList.size()).isEqualTo(1);

        Optional<Disk> secondDiskOfFirstUser = firstUserDiskList.stream()
                .filter(
                        x -> x.getName().equals("Eternal bliss")
                ).findFirst();
        secondDiskOfFirstUser.ifPresent(disk -> assertThat(disk
                .getName())
                .isEqualTo("Eternal bliss"));
        firstUserDiskList.forEach(disk -> {
            assertThat(disk.getTakenItems().getCurrentOwner().getId()).isNotEqualTo(firstUserId);
            assertThat(disk.getTakenItems().getOriginalOwner().getId()).isEqualTo(firstUserId);
        });

        assertThat(secondUserDiskList.size()).isEqualTo(1);

        Optional<Disk> secondDiskOfSecondUser = secondUserDiskList.stream()
                .filter(
                        x -> x.getName().equals("Unknown hero")
                ).findFirst();
        secondDiskOfSecondUser.ifPresent(disk -> assertThat(disk
                .getName())
                .isEqualTo("Unknown hero"));
        secondUserDiskList.forEach(disk -> {
            assertThat(disk.getTakenItems().getCurrentOwner().getId()).isNotEqualTo(secondUserId);
            assertThat(disk.getTakenItems().getOriginalOwner().getId()).isEqualTo(secondUserId);
        });
    }
}
