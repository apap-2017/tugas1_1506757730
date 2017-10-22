package com.example.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.model.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.service.PendudukService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.websocket.server.PathParam;

import java.util.List;

import static java.lang.Integer.parseInt;

@Controller
public class PendudukController{

    @Autowired
    PendudukService pendudukDAO;

    @RequestMapping("/")
    public String index (){
        return "form";
    }

    @RequestMapping("/penduduk")
    public String viewPenduduk(Model model, @RequestParam(value = "nik", required = false) String nik){
        PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
        if(penduduk != null) {
            model.addAttribute("penduduk", penduduk);
            return "view-penduduk";
        }
        else{
            model.addAttribute("nik", nik);
            model.addAttribute("cause", "nik-not-found");
            return "not-found";
        }
    }

    @RequestMapping("/keluarga")
    public String viewAnggotaKeluarga(Model model, @RequestParam(value = "nkk", required = false) String nkk){
        KeluargaModel keluarga = pendudukDAO.selectNKK(nkk);
        model.addAttribute("keluarga", keluarga);
        return "view-anggota-keluarga";
    }

    @RequestMapping("/penduduk/tambah")
    public String tambahPenduduk(Model model){
        model.addAttribute("penduduk", new PendudukModel());
        return "form-tambah-penduduk";
    }

    @PostMapping ("/penduduk/tambah")
    public String tambahPendudukSubmit(
            Model model,
            PendudukModel penduduk,
            @RequestParam(value = "tanggal_lahir") String tanggal_lahir,
            @RequestParam(value = "id_keluarga") String id_keluarga)
    {
        String[] tokens = tanggal_lahir.split("-");
        String tanggal = tokens[2];
        if(penduduk.getJenis_kelamin() == 0){
           int tambah = parseInt(tokens[2].substring(0,1)) + 4;
           tanggal = Integer.toString(tambah) + tokens[2].substring(1,2);
        }
        String tanggallahir = tanggal +  tokens[1]  + tokens[0].substring(2);
        KeluargaModel keluarga = pendudukDAO.selectKeluarga(id_keluarga);
        String kode = keluarga.getKelurahan().getKecamatan().getKode_kecamatan().substring(0,6);
        String front = kode + tanggallahir;
        String nik = pendudukDAO.generateNIK(front, penduduk, id_keluarga);
        pendudukDAO.addPenduduk(penduduk, nik, id_keluarga);
        model.addAttribute("condition", "tambah-penduduk");
        model.addAttribute("nik", nik);
        return "success";
    }

    @RequestMapping ("/keluarga/tambah")
    public String tambahKeluarga(Model model)
    {
        model.addAttribute("keluarga", new KeluargaModel());
        return "form-tambah-keluarga";
    }
    @PostMapping("/keluarga/tambah")
    public String tambahKeluargaSubmit(Model model, KeluargaModel keluarga,
                                       @RequestParam(value = "id_kelurahan") String id_kelurahan)
    {
        KelurahanModel kelurahan = pendudukDAO.selectKelurahan(id_kelurahan);
        String kodeLokasi = kelurahan.getKecamatan().getKode_kecamatan().substring(0,6);
        String[] today = pendudukDAO.currentDate().split("-");
        String front = kodeLokasi + today[2] + today[1] + today[0].substring(2);
        String nkk = pendudukDAO.generateNKK(front, keluarga, id_kelurahan);
        model.addAttribute("condition", "tambah-keluarga");
        model.addAttribute("nkk", nkk);
        return "success";
    }

    @RequestMapping("/penduduk/ubah/{NIK}")
    public String ubahPenduduk(Model model, @PathVariable("NIK") String nik){
        PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
        if(penduduk.getGolongan_darah().substring(1).equals("−")){
            penduduk.setGolongan_darah(penduduk.getGolongan_darah().substring(0,1)+"-");
        }
        model.addAttribute("penduduk", penduduk);
        return "form-ubah-penduduk";
    }

    @PostMapping("/penduduk/ubah/{NIK}")
    public String ubahPendudukSubmit(Model model, PendudukModel penduduk,
                                     @RequestParam(value = "tanggal_lahir") String tanggal_lahir,
                                     @RequestParam(value = "id_keluarga") String id_keluarga)
    {
        PendudukModel penduduk_lama = pendudukDAO.selectPenduduk(penduduk.getNik());
        if(!penduduk_lama.getTanggal_lahir().equals(penduduk.getTanggal_lahir())
                || !penduduk_lama.getKeluarga().getId().equals(id_keluarga)){

            // kondisi harus membuat NIK baru

            String[] tokens = tanggal_lahir.split("-");
            String tanggal = tokens[2];
            if(penduduk.getJenis_kelamin() == 0){
                int tambah = parseInt(tokens[2].substring(0,1)) + 4;
                tanggal = Integer.toString(tambah) + tokens[2].substring(1,2);
            }
            String tanggallahir = tanggal +  tokens[1]  + tokens[0].substring(2);
            KeluargaModel keluarga = pendudukDAO.selectKeluarga(id_keluarga);
            String kode = keluarga.getKelurahan().getKecamatan().getKode_kecamatan().substring(0,6);
            String front = kode + tanggallahir;
            String nik_baru = pendudukDAO.generateNIK(front, penduduk, id_keluarga);
            pendudukDAO.ubahPenduduk(penduduk, penduduk_lama.getNik(), nik_baru, id_keluarga);
        }
        else{
            pendudukDAO.ubahPenduduk(penduduk, penduduk_lama.getNik(), penduduk_lama.getNik(), id_keluarga);
        }
        model.addAttribute("condition", "ubah-penduduk");
        model.addAttribute("nik", penduduk_lama.getNik());

        return "success";
    }

    @RequestMapping("/keluarga/ubah/{nkk}")
    public String ubahKeluarga(Model model, @PathVariable("nkk") String nkk){
        KeluargaModel keluarga = pendudukDAO.selectNKK(nkk);
        model.addAttribute("keluarga", keluarga);
        return "form-ubah-keluarga";
    }

    @PostMapping("/keluarga/ubah/{nkk}")
    public String ubahKeluargaSubmit(Model model, KeluargaModel keluarga,
                                     @RequestParam(value = "id_kelurahan") String id_kelurahan)
    {
        KeluargaModel keluarga_lama = pendudukDAO.selectNKK(keluarga.getNomor_kk());
        System.out.println(keluarga.getNomor_kk() + " kk lama");
        if(!keluarga_lama.getKelurahan().getId().equals(id_kelurahan))
        {
            // kondisi ketika NKK harus diganti

            KelurahanModel kelurahan = pendudukDAO.selectKelurahan(id_kelurahan);
            String kodeLokasi = kelurahan.getKecamatan().getKode_kecamatan().substring(0,6);
            String[] today = pendudukDAO.currentDate().split("-");
            String front = kodeLokasi + today[2] + today[1] + today[0].substring(2);
            String nkk_baru = pendudukDAO.generateNKK(front, keluarga, id_kelurahan);
            pendudukDAO.ubahKeluarga(keluarga_lama.getNomor_kk(), nkk_baru, keluarga, id_kelurahan);
        }
        else{
            pendudukDAO.ubahKeluarga(keluarga_lama.getNomor_kk(), keluarga_lama.getNomor_kk(), keluarga, id_kelurahan);
        }
        model.addAttribute("condition", "ubah-keluarga");
        model.addAttribute("nkk", keluarga_lama.getNomor_kk());

        return "success";
    }

    @PostMapping("/penduduk/mati")
    public String nonaktifkanPenduduk(Model model,
                                      @RequestParam(value = "nik") String nik,
                                      @RequestParam(value = "nomor_kk") String nomor_kk){
        pendudukDAO.nonaktifkanPenduduk(nik);
        PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
        KeluargaModel keluarga = pendudukDAO.selectNKK(nomor_kk);
        int jumlah_keluarga = keluarga.getAnggota_keluarga().size();
        int i, is_wafat = 0;
        for(i = 0; i < jumlah_keluarga; i++){
            if(keluarga.getAnggota_keluarga().get(i).getIs_wafat() == 1){
                is_wafat=is_wafat + 1;
            }
        }
        System.out.println(is_wafat);
        if(jumlah_keluarga == is_wafat){
            pendudukDAO.nonaktifkanKeluarga(nomor_kk);
        }
        model.addAttribute("penduduk", penduduk);
        return "view-penduduk";
    }

    @GetMapping(value = {"/penduduk/cari"})
    public String cariPenduduk(Model model,
                               @RequestParam(value = "id_kota", required = false) String id_kota,
                               @RequestParam(value = "id_kecamatan", required = false) String id_kecamatan,
                               @RequestParam(value = "id_kelurahan", required = false) String id_kelurahan)
    {
        if(!(id_kota == null)) {
            KotaModel kota = pendudukDAO.selectKota(id_kota);
            model.addAttribute("kecamatan_kecamatan", kota.getKecamatanKecamatan());
            model.addAttribute("id_kota", id_kota);
            model.addAttribute("nama_kota", kota.getNama_kota());
            if(!(id_kecamatan == null)){
                KecamatanModel kecamatan = pendudukDAO.selectKecamatan(id_kecamatan);
                model.addAttribute("kelurahan_kelurahan", kecamatan.getKelurahanKelurahan());
                model.addAttribute("id_kecamatan", id_kecamatan);
                model.addAttribute("nama_kecamatan", kecamatan.getNama_kecamatan());
                if(!(id_kelurahan == null)){
                    System.out.print("masuk");
                   model.addAttribute("id_kelurahan", id_kelurahan);
                   List<PendudukModel> penduduk_penduduk = pendudukDAO.selectPendudukKelurahan(id_kelurahan);
                   model.addAttribute("penduduk_penduduk", penduduk_penduduk);
                   return "view-penduduk-kelurahan";
                }
                return "cari-kelurahan";
            }
            return "cari-kecamatan";
        }

       else{
            List<KotaModel> kota_kota = pendudukDAO.cariKotakota();
            model.addAttribute("kota_kota", kota_kota);
            return "cari-kota";
        }




    }
}
