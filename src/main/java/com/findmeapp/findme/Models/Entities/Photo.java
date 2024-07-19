package com.findmeapp.findme.Models.Entities;


import lombok.Data;

@Data
public class Photo {

    private String fileName;
    private int countSilhouette;
    private String format;
    private long size;
}
