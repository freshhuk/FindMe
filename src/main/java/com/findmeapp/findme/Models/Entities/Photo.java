package com.findmeapp.findme.Models.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private int id;
    @Column(name = "fileName")
    private String fileName;

    @Column(name = "countSilhouette")
    private int countSilhouette;

    @Column(name = "format")
    private String format;

    @Column(name = "indentityCode")
    private String identityCode;
}
