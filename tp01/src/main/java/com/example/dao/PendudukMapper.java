package com.example.dao;

import com.example.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface PendudukMapper {
    @Select("select nik, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, is_wni, id_keluarga, agama, pekerjaan, status_perkawinan," +
            " status_dalam_keluarga, golongan_darah, is_wafat from penduduk where nik = #{nik}")
    @Results(value = {
            @Result(property = "nik", column = "nik"),
            @Result(property = "nama", column = "nama"),
            @Result(property = "tempat_lahir", column = "tempat_lahir"),
            @Result(property = "tanggal_lahir", column = "tanggal_lahir"),
            @Result(property = "jenis_kelamin", column = "jenis_kelamin"),
            @Result(property = "is_wni", column = "is_wni"),
            @Result(property = "agama", column = "agama"),
            @Result(property = "pekerjaan", column = "pekerjaan"),
            @Result(property = "status_perkawinan", column = "status_perkawinan"),
            @Result(property = "status_dalam_keluarga", column = "status_dalam_keluarga"),
            @Result(property = "golongan_darah", column = "golongan_darah"),
            @Result(property = "is_wafat", column = "is_wafat"),
            @Result(property = "keluarga", column = "id_keluarga", javaType = KeluargaModel.class,  one = @One(select = "selectKeluarga"))

    })
    PendudukModel selectPenduduk(@Param("nik") String nik);

    @Select("select id, nomor_kk, concat(rt, '/', rw) as rtrw, alamat, id_kelurahan from keluarga " +
            "where keluarga.id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "nomor_kk", column = "nomor_kk"),
            @Result(property = "alamat", column= "alamat"),
            @Result(property = "rt_rw", column = "rtrw"),
            @Result(property = "kelurahan", column = "id_kelurahan",
                    javaType = KelurahanModel.class,
                    one = @One(select = "selectKelurahan"))
    })
    KeluargaModel selectKeluarga(@Param("id") String id);

    @Select("select id, nama_kelurahan, id_kecamatan, kode_pos, kode_kelurahan from kelurahan " +
            "where kelurahan.id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "nama_kelurahan", column= "nama_kelurahan"),
            @Result(property = "kode_pos", column = "kode_pos"),
            @Result(property = "kecamatan", column = "id_kecamatan", javaType = KecamatanModel.class, one = @One(select = "selectKecamatan")),
            @Result(property = "kode_kelurahan", column = "kode_kelurahan")
    })
    KelurahanModel selectKelurahan(@Param("id") String id);

    @Select("select id, nama_kecamatan, id_kota, kode_kecamatan from kecamatan " +
            "where kecamatan.id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "nama_kecamatan", column= "nama_kecamatan"),
            @Result(property = "kode_kecamatan", column = "kode_kecamatan"),
            @Result(property = "kota", column = "id_kota", javaType = KotaModel.class, one = @One(select = "selectKota")),
            @Result(property = "kelurahanKelurahan", column = "id", javaType = List.class, many = @Many(select = "selectKelurahanKelurahan"))
    })
    KecamatanModel selectKecamatan(@Param("id") String id);

    @Select("select * from kota " +
            "where kota.id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "nama_kota", column = "nama_kota"),
            @Result(property = "kecamatanKecamatan", column = "id", javaType = List.class, many = @Many(select = "selectKecamatanKecamatan"))
    })
    KotaModel selectKota(@Param("id") String id);

    @Select("select *, concat(rt, '/', rw) as rtrw from keluarga where keluarga.nomor_kk = #{nkk}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "nomor_kk", column = "nomor_kk"),
            @Result(property = "alamat", column= "alamat"),
            @Result(property = "rt_rw", column = "rtrw"),
            @Result(property = "anggota_keluarga", column = "id",
                    javaType = List.class,
                    many = @Many(select = "selectAnggotaKeluarga")),
            @Result(property = "kelurahan", column = "id_kelurahan",
                    javaType = KelurahanModel.class,
                    one = @One(select = "selectKelurahan"))
    })
    KeluargaModel selectNKK(@Param("nkk") String nkk);

    @Select("select * from penduduk " +
            "join keluarga on penduduk.id_keluarga = keluarga.id " +
            "where keluarga.id = #{id}")
    List<PendudukModel> selectAnggotaKeluarga(@Param("id") String id);

    @Select("select * from penduduk where nik like #{front}")
    List<PendudukModel> generateNIK(@Param("front") String front);

    @Insert("insert into penduduk(nik, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, is_wni, id_keluarga, " +
            "agama, pekerjaan, status_perkawinan, status_dalam_keluarga, golongan_darah, is_wafat) " +
            "values(#{nik}, #{penduduk.nama}, #{penduduk.tempat_lahir}, #{penduduk.tanggal_lahir}, " +
            "#{penduduk.jenis_kelamin}, #{penduduk.is_wni}, " +
            "#{id_keluarga}, #{penduduk.agama}, #{penduduk.pekerjaan}, #{penduduk.status_perkawinan}, " +
            "#{penduduk.status_dalam_keluarga}, #{penduduk.golongan_darah}, #{penduduk.is_wafat})")
    void addPenduduk(@Param("penduduk") PendudukModel penduduk, @Param("nik") String nik, @Param("id_keluarga") String id_keluarga);

    @Select("SELECT CURDATE()")
    String currentDate();

    @Select("select * from keluarga where nomor_kk like #{front}")
    List<KeluargaModel> generateNKK(@Param("front") String front);

    @Insert("insert into keluarga(nomor_kk, alamat, RT, RW, id_kelurahan, is_tidak_berlaku) " +
            "values(#{nkk}, #{keluarga.alamat}, #{keluarga.rt}, #{keluarga.rw}, #{id_kelurahan}, 0)")
    void addKeluarga(@Param("nkk") String nkk, @Param("keluarga") KeluargaModel keluarga, @Param("id_kelurahan") String id_kelurahan);

    @Update("update penduduk set " +
            "nik = #{nik_baru}, nama = #{penduduk.nama}, tempat_lahir = #{penduduk.tempat_lahir}, " +
            "tanggal_lahir = #{penduduk.tanggal_lahir}, jenis_kelamin = #{penduduk.jenis_kelamin}, " +
            "is_wni = #{penduduk.is_wni}, id_keluarga = #{id_keluarga}, agama = #{penduduk.agama}, pekerjaan = #{penduduk.pekerjaan}, " +
            "status_perkawinan = #{penduduk.status_perkawinan}, status_dalam_keluarga = #{penduduk.status_dalam_keluarga}, " +
            "golongan_darah = #{penduduk.golongan_darah}, is_wafat = #{penduduk.is_wafat} where nik = #{nik_lama}")
    void updatePenduduk(@Param("nik_lama") String nik_lama, @Param("nik_baru") String nik_baru, @Param("penduduk") PendudukModel penduduk, @Param("id_keluarga") String id_keluarga);

    @Update("update keluarga set " +
            "nomor_kk = #{nkk_baru}, alamat = #{keluarga.alamat}, rt = #{keluarga.rt}, " +
            "rw = #{keluarga.rw}, id_kelurahan = #{id_kelurahan} where nomor_kk = #{nkk_lama}")
    void updateKeluarga(@Param("nkk_lama") String nkk_lama, @Param("nkk_baru") String nkk_baru, @Param("keluarga") KeluargaModel keluarga, @Param("id_kelurahan") String id_kelurahan);

    @Update("update penduduk set is_wafat = 1 where nik = #{nik}")
    void nonaktifkanPenduduk(@Param("nik") String nik);

    @Update("update keluarga set is_tidak_berlaku = 1 where nomor_kk = #{nkk}")
    void nonaktifkanKeluarga (@Param("nkk") String nkk);

    @Select("select * from kota")
    List<KotaModel> selectKotakota ();

    @Select("select * from kecamatan where id_kota = #{id_kota}")
    List<KecamatanModel> selectKecamatanKecamatan(@Param("id_kota") String id_kota);

    @Select("select * from kelurahan where id_kecamatan = #{id_kecamatan}")
    List<KelurahanModel> selectKelurahanKelurahan(@Param("id_kecamatan") String id_kecamatan);

    @Select("select * from penduduk p join keluarga k on p.id_keluarga = k.id where k.id_kelurahan = #{id_kelurahan}")
    List<PendudukModel> selectPendudukKelurahan(@Param("id_kelurahan") String id_kelurahan);
}
