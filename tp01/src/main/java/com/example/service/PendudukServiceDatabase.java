package com.example.service;

import com.example.dao.PendudukMapper;

import com.example.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PendudukServiceDatabase implements PendudukService{
    @Autowired
    private PendudukMapper pendudukMapper;

    @Override
    public PendudukModel selectPenduduk(String nik){
        log.info("berhasil melakukan select penduduk");
        return pendudukMapper.selectPenduduk(nik);
    }

    @Override
    public KeluargaModel selectNKK(String nkk){
        log.info ("berhasil melakukan select keluarga");
        return pendudukMapper.selectNKK(nkk);

    }

    @Override
    public KeluargaModel selectKeluarga(String id_keluarga){
        log.info("generate NIK part 1");
        return pendudukMapper.selectKeluarga(id_keluarga);
    }

    @Override
    public String generateNIK(String front, PendudukModel penduduk, String id_keluarga){
        log.info("generate NIK part 2");
        List<PendudukModel> back = pendudukMapper.generateNIK(front+'%');
        log.info("mendapatkan back");
        String nik;
        if(back.size() >= 1000){
            log.info("diatas 1000");
            nik = front + (back.size() + 1);
        }else if(back.size() >= 100){
            log.info("diatas 100");
            nik = front + "0" + (back.size() + 1);
        }else if(back.size() >= 10){
            log.info("diatas 10");
            nik = front + "00" + (back.size() + 1);
        }else {
            log.info("diatas 0");
            nik = front + "000" + (back.size() + 1);
        }
        return nik;
    }

    @Override
    public void addPenduduk(PendudukModel penduduk, String nik, String id_keluarga){
        pendudukMapper.addPenduduk(penduduk, nik, id_keluarga);
        log.info("penduduk masuk");
    }

    @Override
    public String currentDate(){
        return pendudukMapper.currentDate();
    }

    @Override
    public KelurahanModel selectKelurahan(String id_kelurahan){
        return pendudukMapper.selectKelurahan(id_kelurahan);
    }

    @Override
    public String generateNKK(String front, KeluargaModel keluarga, String id_kelurahan){
        log.info("generateNKK");
        List<KeluargaModel> back = pendudukMapper.generateNKK(front+'%');
        String nkk;
        if(back.size() >= 1000){
            log.info("diatas 1000");
            nkk = front + (back.size() + 1);
        }else if(back.size() >= 100){
            log.info("diatas 100");
            nkk = front + "0" + (back.size() + 1);
        }else if(back.size() >= 10){
            log.info("diatas 10");
            nkk = front + "00" + (back.size() + 1);
        }else {
            log.info("diatas 0");
            nkk = front + "000" + (back.size() + 1);
        }
        pendudukMapper.addKeluarga(nkk, keluarga, id_kelurahan);
        log.info("berhasil menambah keluarga" );
        return nkk;
    }

    @Override
    public void ubahPenduduk(PendudukModel penduduk, String nik_lama, String nik_baru, String id_keluarga){
        pendudukMapper.updatePenduduk(nik_lama, nik_baru, penduduk, id_keluarga);
        log.info("berhasil update nik menjadi = " + nik_baru);
    }

    @Override
    public void ubahKeluarga(String nkk_lama, String nkk_baru, KeluargaModel keluarga, String id_kelurahan){
        pendudukMapper.updateKeluarga(nkk_lama, nkk_baru, keluarga, id_kelurahan);
        log.info("berhasil mengupdate keluarga");
    }

    @Override
    public void nonaktifkanPenduduk(String nik){
        pendudukMapper.nonaktifkanPenduduk(nik);
        log.info("penduduk nonaktif");
    }

    @Override
    public void nonaktifkanKeluarga(String nkk){
        pendudukMapper.nonaktifkanKeluarga(nkk);
        log.info("non aktifkan keluarga");
    }

    @Override
    public List<PendudukModel> selectPendudukKelurahan(String id_kelurahan){
        return pendudukMapper.selectPendudukKelurahan(id_kelurahan);
    }

    @Override
    public List<KotaModel> cariKotakota(){
        return pendudukMapper.selectKotakota();
    }

    @Override
    public KotaModel selectKota(String id_kota){
        return pendudukMapper.selectKota(id_kota);
    }

    @Override
    public KecamatanModel selectKecamatan(String id_kecamatan){
        return pendudukMapper.selectKecamatan(id_kecamatan);
    }


}
