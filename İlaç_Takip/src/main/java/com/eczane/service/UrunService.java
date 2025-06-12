package com.eczane.service;

import com.eczane.model.Urun;
import com.eczane.repository.UrunRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrunService {
    private final UrunRepository urunRepository;

    public UrunService(UrunRepository urunRepository) {
        this.urunRepository = urunRepository;
    }

    public void urunEkle(Urun urun) {
        urunRepository.save(urun);
        System.out.println("İlaç başarıyla eklendi.");
    }

    public List<Urun> tumUrunleriGetir() {
        return urunRepository.findAll();
    }

    public void stokGuncelle(Long urunId, int yeniMiktar) {
        Urun urun = urunRepository.findById(urunId)
                .orElseThrow(() -> new RuntimeException("İlaç bulunamadı: " + urunId));
        
        urun.setStokMiktari(yeniMiktar);
        urunRepository.save(urun);
        System.out.println("Stok miktarı güncellendi.");
    }

    public java.util.Optional<Urun> getUrunById(Long urunId) {
        return urunRepository.findById(urunId);
    }

    public List<Urun> urunAra(String aramaKelimesi) {
        return urunRepository.findAll().stream()
                .filter(urun -> 
                    urun.getAd().toLowerCase().contains(aramaKelimesi.toLowerCase()) ||
                    urun.getBarkod().toLowerCase().contains(aramaKelimesi.toLowerCase()) ||
                    (urun.getKategori() != null && urun.getKategori().toLowerCase().contains(aramaKelimesi.toLowerCase()))
                )
                .collect(Collectors.toList());
    }

    public void urunSil(Long urunId) {
        if (urunRepository.existsById(urunId)) {
            urunRepository.deleteById(urunId);
            System.out.println("İlaç başarıyla silindi.");
        } else {
            System.out.println("İlaç bulunamadı.");
        }
    }

    public List<Urun> getDusukStokluUrunler(int esikDegeri) {
        return urunRepository.findAll().stream()
                .filter(urun -> urun.getStokMiktari() < esikDegeri)
                .collect(Collectors.toList());
    }
} 