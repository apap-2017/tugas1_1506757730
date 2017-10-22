package com.example.service;

import com.example.model.*;

import java.util.List;

public interface PendudukService {
      PendudukModel selectPenduduk(String nik);

      KeluargaModel selectNKK (String nkk);

      KeluargaModel selectKeluarga(String id_keluarga);

      KelurahanModel selectKelurahan(String id_kelurahan);

      String generateNIK(String front, PendudukModel penduduk, String id_keluarga);

      String generateNKK(String front, KeluargaModel keluarga, String id_kelurahan);

      String currentDate();

      void addPenduduk(PendudukModel penduduk, String nik, String id_keluarga);

      void ubahPenduduk(PendudukModel penduduk, String nik_lama, String baru, String id_keluarga);

      void ubahKeluarga(String nkk_lama, String nkk_baru, KeluargaModel keluarga, String id_kelurahan);

      void nonaktifkanPenduduk(String nik);

      void nonaktifkanKeluarga(String nkk);

      List<KotaModel> cariKotakota();

      KotaModel selectKota(String id_kota);

      KecamatanModel selectKecamatan(String id_kecamatan);

      List<PendudukModel> selectPendudukKelurahan(String id_kelurahan);
}
