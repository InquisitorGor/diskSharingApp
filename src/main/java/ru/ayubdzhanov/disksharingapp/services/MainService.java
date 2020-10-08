package ru.ayubdzhanov.disksharingapp.services;

import ru.ayubdzhanov.disksharingapp.controllers.MainController;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.domain.User;

import java.util.List;

public interface MainService {

    User getUserById();

    List<Disk> getAllUserDisks();

    List<Disk> getAllFreeDisks();

    List<Disk> getAllDisksTakenByUser();

    List<MainServiceImpl.ControllerSupport> getAllDisksWhichWasTaken();

    void freeDiskFromCurrentOwner(Long id) throws Exception;

    void setDiskCurrentOwner(Long id) throws Exception;

    void specifyCurrentUserId();




}
