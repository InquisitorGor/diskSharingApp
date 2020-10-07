package ru.ayubdzhanov.disksharingapp.domain;

import javax.persistence.*;

@Entity
@Table(name = "taken_items")
public class TakenItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_free")
    private boolean isFree;

    @OneToOne
    @JoinColumn(name = "disk_id")
    private Disk disk;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "original_owner_id")
    private User originalOwner;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_owner_id")
    private User currentOwner;


    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public Long getId() {
        return id;
    }

    public User getOriginalOwner() {
        return originalOwner;
    }

    public void setOriginalOwner(User originalOwner) {
        this.originalOwner = originalOwner;
    }

    public User getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(User currentOwner) {
        this.currentOwner = currentOwner;
    }

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }
}
