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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "disk_id")
    private Disk disk;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
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

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        if(currentOwner == null) isFree = true;
        this.disk = disk;
    }

    public User getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(User currentOwner) {
        isFree = (currentOwner == null);
        this.currentOwner = currentOwner;
    }
}
