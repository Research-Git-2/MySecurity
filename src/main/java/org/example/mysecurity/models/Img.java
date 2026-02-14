package org.example.mysecurity.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "img")
@Data
public class Img {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_img;

    @Column(name = "path")
    private String path;

    @ManyToOne()
    @JoinColumn(name = "id_person")
    private Person person_img;
}
