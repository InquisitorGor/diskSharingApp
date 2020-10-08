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

    @OneToOne(mappedBy = "disk")
    @JsonIgnore
    private TakenItems takenItems;

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

    public TakenItems getTakenItems() {
        return takenItems;
    }

    public void setTakenItems(TakenItems takenItems) {
        this.takenItems = takenItems;
    }

    @Override
    public String toString() {
        return "Disk{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
