package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class KelurahanModel {
    private String id;
    private String kode_kelurahan;
    private String nama_kelurahan;
    private KecamatanModel kecamatan;
    private String kode_pos;
}
