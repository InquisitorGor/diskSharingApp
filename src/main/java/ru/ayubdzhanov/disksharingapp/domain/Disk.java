package ru.ayubdzhanov.disksharingapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "disks")
public class Disk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "current_user_id")
    private User currentUser;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "original_owner_id")
    private User originalOwner;


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

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getOriginalOwner() {
        return originalOwner;
    }

    public void setOriginalOwner(User originalOwner) {
        this.originalOwner = originalOwner;
    }

}
