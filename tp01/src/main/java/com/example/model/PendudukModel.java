package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PendudukModel {
    private String nik;
    private String nama;
    private String tempat_lahir;
    private String tanggal_lahir;
    private int jenis_kelamin;
    private int is_wni;
    private KeluargaModel keluarga;
    private String agama;
    private String pekerjaan;
    private String status_perkawinan;
    private String status_dalam_keluarga;
    private String golongan_darah;
    private int is_wafat;
}
