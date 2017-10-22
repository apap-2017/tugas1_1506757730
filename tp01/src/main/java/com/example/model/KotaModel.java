package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class KotaModel {
    public String id;
    private String kode_kota;
    private String nama_kota;
    private List<KecamatanModel> kecamatanKecamatan;
}
