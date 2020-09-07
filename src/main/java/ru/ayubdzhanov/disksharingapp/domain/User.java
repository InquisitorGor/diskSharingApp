package ru.ayubdzhanov.disksharingapp.domain;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credential_id")
    private Credential credential;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "currentUser",
            fetch = FetchType.EAGER)
    private List<Disk> listDisk;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public List<Disk> getListDisk() {
        if(listDisk == null) listDisk = new LinkedList<>();
        return listDisk;
    }


    public void setListDisk(List<Disk> listDisk) {
        this.listDisk = listDisk;
    }
}
