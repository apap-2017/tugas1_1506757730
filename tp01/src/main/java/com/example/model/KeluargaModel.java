package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class KeluargaModel {
    private String id;
    private String nomor_kk;
    private List<PendudukModel> anggota_keluarga;
    private String alamat;
    private String rt_rw;
    private String rt;
    private String rw;
    private KelurahanModel kelurahan;
    private int is_tidak_berlaku;
}
