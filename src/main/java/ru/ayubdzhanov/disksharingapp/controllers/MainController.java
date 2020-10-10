package ru.ayubdzhanov.disksharingapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ayubdzhanov.disksharingapp.domain.Disk;
import ru.ayubdzhanov.disksharingapp.services.MainService;
import ru.ayubdzhanov.disksharingapp.services.MainServiceImpl;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user")
public class MainController {

    private final MainService service;

    public MainController(MainService service) {
        this.service = service;
    }


    @GetMapping("/disks")
    public ResponseEntity<List<Disk>> getAllUserDisks() {
        return ResponseEntity.ok(service.getAllUserDisks());
    }

    @GetMapping("/disks/free")
    public ResponseEntity<List<Disk>> getAllFreeDisks() {
        return ResponseEntity.ok(service.getAllFreeDisks());
    }

    @GetMapping("/disks/taken-by-user")
    public ResponseEntity<List<Disk>> getAllDisksTakenByUser() {
        return ResponseEntity.ok(service.getAllDisksTakenByUser());
    }

    @GetMapping("/disks/taken-from-user")
    public ResponseEntity<List<MainServiceImpl.ControllerSupport>> getAllDisksWhichWasTaken() {
        return ResponseEntity.ok(service.getAllDisksWhichWasTaken());
    }

    @PutMapping("/disk/return/{id}")
    public ResponseEntity<?> giveBackDisk(@PathVariable("id") Long id) throws Exception {

        service.freeDiskFromCurrentOwner(id);
        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }

    @PutMapping("/disk/take/{id}")
    public ResponseEntity<?> takeDisk(@PathVariable("id") Long id) throws Exception {

        service.setDiskCurrentOwner(id);
        return ResponseEntity.ok(Collections.EMPTY_LIST);

    }

    @ExceptionHandler(DiskOwnerException.class)
    public ResponseEntity<?> handleDiskOwnerException(DiskOwnerException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}


