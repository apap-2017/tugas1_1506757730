package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KecamatanModel {
    private String id;
    private String kode_kecamatan;
    private KotaModel kota;
    private String nama_kecamatan;
    private List<KelurahanModel> kelurahanKelurahan;
}
