package com.findmeapp.findme.Models.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photos_id_seq")
    @SequenceGenerator(name = "photos_id_seq", sequenceName = "photos_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "countsilhouette")
    private int countsilhouette;

    @Column(name = "format")
    private String format;

    @Column(name = "indentitycode")
    private String indentitycode;

}
