package com.eczane;

import com.eczane.model.Urun;
import com.eczane.repository.UrunRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class EczaneStokTakipApplicationTests {

    @Autowired
    private UrunRepository urunRepository;

    @Test
    public void testVeritabaniBaglantisi() {
        // Test verisi oluştur
        Urun testUrun = new Urun(
            "Test İlaç",
            "TEST123",
            10.0,
            100,
            "Reçeteli",
            "Test Üretici"
        );
        testUrun.setSonGuncellemeTarihi(LocalDateTime.now());

        // Veritabanına kaydet
        Urun kaydedilenUrun = urunRepository.save(testUrun);

        // Kaydedilen ürünü kontrol et
        assertNotNull(kaydedilenUrun.getId());
        assertEquals("Test İlaç", kaydedilenUrun.getAd());
        assertEquals("TEST123", kaydedilenUrun.getBarkod());
        assertEquals(10.0, kaydedilenUrun.getFiyat());
        assertEquals(100, kaydedilenUrun.getStokMiktari());
        assertEquals("Reçeteli", kaydedilenUrun.getKategori());
        assertEquals("Test Üretici", kaydedilenUrun.getTedarikci());

        // Veritabanından sil
        urunRepository.delete(kaydedilenUrun);
    }
} 