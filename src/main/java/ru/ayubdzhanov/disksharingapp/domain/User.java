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

    @Column(name = "real_name")
    private String realName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credential_id")
    private Credential credential;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "originalOwner",
            fetch = FetchType.EAGER)
    private List<TakenItems> listOfCurrentDisks;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "currentOwner",
            fetch = FetchType.EAGER)
    private List<TakenItems> listOfTakenDisks;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<TakenItems> getListOfCurrentDisks() {
        return listOfCurrentDisks;
    }

    public void setListOfCurrentDisks(List<TakenItems> listOfCurrentDisks) {
        this.listOfCurrentDisks = listOfCurrentDisks;
    }

    public List<TakenItems> getListOfTakenDisks() {
        return listOfTakenDisks;
    }

    public void setListOfTakenDisks(List<TakenItems> listOfTakenDisks) {
        this.listOfTakenDisks = listOfTakenDisks;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
        this.credential.setUser(this);
    }

}
