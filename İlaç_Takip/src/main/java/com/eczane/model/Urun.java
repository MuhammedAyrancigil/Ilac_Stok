package com.eczane.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@Entity
@Table(name = "urunler")
public class Urun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ad;

    @Column(unique = true)
    private String barkod;

    @Column(nullable = false)
    private double fiyat;

    @Column(name = "stok_miktari", nullable = false)
    private int stokMiktari;

    @Column(name = "kategori")
    private String kategori;

    @Column(name = "son_guncelleme_tarihi")
    private LocalDateTime sonGuncellemeTarihi;

    @Column(name = "tedarikci")
    private String tedarikci;

    @Column(name = "son_kullanma_tarihi")
    private LocalDate sonKullanmaTarihi;

    public Urun() {
        // sonGuncellemeTarihi burada başlatılmıyor, veritabanından gelecek veya
        // eklenirken atanacak
    }

    public Urun(String ad, String barkod, double fiyat, int stokMiktari, String kategori, String tedarikci) {
        this.ad = ad;
        this.barkod = barkod;
        this.fiyat = fiyat;
        this.stokMiktari = stokMiktari;
        this.kategori = kategori;
        this.tedarikci = tedarikci;
        this.sonGuncellemeTarihi = LocalDateTime.now(); // Yeni ürün oluşturulurken ayarlanıyor
    }

    public void setSonKullanmaTarihi(String tarihStr) {
        if (tarihStr != null && !tarihStr.trim().isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                this.sonKullanmaTarihi = LocalDate.parse(tarihStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Hata: Geçersiz tarih formatı. Lütfen YYYY-MM-DD formatında girin.");
                this.sonKullanmaTarihi = null;
            }
        }
    }

    public String getSonKullanmaTarihi() {
        if (sonKullanmaTarihi != null) {
            return sonKullanmaTarihi.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }
} 