package ru.ayubdzhanov.disksharingapp.services;

import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;
import ru.ayubdzhanov.disksharingapp.controllers.DiskOwnerException;
import ru.ayubdzhanov.disksharingapp.controllers.MainController;
import ru.ayubdzhanov.disksharingapp.dao.CredentialDao;
import ru.ayubdzhanov.disksharingapp.dao.DiskDao;
import ru.ayubdzhanov.disksharingapp.dao.TakenItemsDao;
import ru.ayubdzhanov.disksharingapp.dao.UserDao;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.TakenItems;
import ru.ayubdzhanov.disksharingapp.domain.User;

import java.util.LinkedList;
import java.util.List;

@Service
public class MainServiceImpl implements MainService {

    private final DiskDao diskDao;

    private final UserDao userDao;

    private final TakenItemsDao takenItemsDao;

    private final CredentialDao credentialDao;


    public MainServiceImpl(DiskDao diskDao, UserDao userDao, TakenItemsDao takenItemsDao, CredentialDao credentialDao) {
        this.diskDao = diskDao;
        this.userDao = userDao;
        this.takenItemsDao = takenItemsDao;
        this.credentialDao = credentialDao;
    }

    @Override
    @Transactional
    public User getUserById() {
        return userDao.findById(getCurrentUserId());
    }

    @Override
    @Transactional
    public List<Disk> getAllUserDisks() {
        return diskDao.getDisks(getCurrentUserId());
    }

    @Override
    @Transactional
    public List<Disk> getAllFreeDisks() {
        return diskDao.getFreeDisks();
    }

    @Override
    @Transactional
    public List<Disk> getAllDisksTakenByUser() {
        return diskDao.getDisksTakenByCurrentUser(getCurrentUserId());
    }

    @Override
    @Transactional
    public List<ControllerSupport> getAllDisksWhichWasTaken() {

        List<Disk> takenDisks = diskDao.getDisksWhichWasTakenFromUser(getCurrentUserId());
        List<ControllerSupport> takenDiskWithUser = new LinkedList<>();

        takenDisks.forEach(disk -> {
            ControllerSupport controllerSupport =
                    new ControllerSupport(disk.getId(),
                            disk.getName(),
                            disk.getTakenItems().getCurrentOwner().getRealName());
            takenDiskWithUser.add(controllerSupport);
        });

        return takenDiskWithUser;
    }

    @Override
    @Transactional
    public void freeDiskFromCurrentOwner(Long id) throws Exception{
        Disk borrowedDisk = diskDao.getDisk(id);
        if (borrowedDisk.getTakenItems().getCurrentOwner() == null) {
            throw new DiskOwnerException("This disk is free");
        }
        if(!borrowedDisk.getTakenItems().getCurrentOwner().getId().equals(getCurrentUserId())) {
            throw new DiskOwnerException("This disk was borrowed by another user");
        }

        TakenItems takenItems = takenItemsDao.findByDiskId(borrowedDisk.getId());

        takenItems.setCurrentOwner(null);

        takenItemsDao.save(takenItems);
    }

    @Override
    @Transactional
    public void setDiskCurrentOwner(Long id) throws Exception{
        Disk freeDisk = diskDao.findFreeDisk(id);

        if(freeDisk == null) {
            throw new DiskOwnerException("Hold ur horses. This disk is not free");
        }

        User user = userDao.findById(getCurrentUserId());

        if(freeDisk.getTakenItems().getOriginalOwner().getId().equals(user.getId())) {
            throw new DiskOwnerException("Bro, chill. This disk belongs to you");
        }

        TakenItems takenItems = takenItemsDao.findByDiskId(freeDisk.getId());

        takenItems.setCurrentOwner(user);

        takenItemsDao.save(takenItems);
    }



    public Long getCurrentUserId() {
        UserDetails credential = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String userName;
        Long currentUserId = null;
        if (credential != null) {

            userName = credential.getUsername();
            try {
                currentUserId = credentialDao.findByUsername(userName)
                        .getId();
            } catch (CannotCreateTransactionException ignored) {
                System.out.println("Something went wrong");
            }
        }
        return currentUserId;
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
}
