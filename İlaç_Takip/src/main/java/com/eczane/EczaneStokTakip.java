package com.eczane;

import com.eczane.model.Urun;
import com.eczane.service.UrunService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.io.File;
import net.glxn.qrgen.javase.QRCode;
import net.glxn.qrgen.core.image.ImageType;
import java.io.ByteArrayOutputStream;

@SpringBootApplication
public class EczaneStokTakip implements CommandLineRunner {

    private final UrunService urunService;
    private final Scanner scanner = new Scanner(System.in);

    public EczaneStokTakip(UrunService urunService) {
        this.urunService = urunService;
    }

    public static void main(String[] args) {
        SpringApplication.run(EczaneStokTakip.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        boolean devam = true;

        while (devam) {
            System.out.println("1. Yeni İlaç Kaydı");
            System.out.println("2. Tüm İlaçları Görüntüle");
            System.out.println("3. Stok Miktarını Güncelle");
            System.out.println("4. İlaç Arama");
            System.out.println("5. Son Kullanma Tarihi Uyarısı");
            System.out.println("6. Kritik Stokları Listele");
            System.out.println("7. İlaç QR Kodu Oluştur");
            System.out.println("8. İlaç Kaydını Sil");
            System.out.println("9. Programdan Çıkış");
            System.out.print("Seçiminiz: ");

            int secim = -1;
            try {
                secim = scanner.nextInt();
                scanner.nextLine(); // Buffer temizleme
            } catch (InputMismatchException e) {
                System.out.println("Hatalı giriş! Lütfen bir sayı girin.");
                scanner.nextLine(); // Hatalı girdiyi tüket
                continue;
            }

            switch (secim) {
                case 1:
                    urunEkleMenu();
                    break;
                case 2:
                    urunleriListeleMenu();
                    break;
                case 3:
                    stokGuncelleMenu();
                    break;
                case 4:
                    urunAraMenu();
                    break;
                case 5:
                    sonKullanmaTarihiKontrolMenu();
                    break;
                case 6:
                    dusukStokluUrunleriListeleMenu();
                    break;
                case 7:
                    qrKodUretMenu();
                    break;
                case 8:
                    urunSilMenu();
                    break;
                case 9:
                    devam = false;
                    System.out.println("Program sonlandırılıyor...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        }

        scanner.close();
    }

    private void urunEkleMenu() {
        System.out.println("\n=== İlaç Ekle ===");
        
        String ad = "";
        boolean adGecerli = false;
        while (!adGecerli) {
            System.out.print("İlaç Adı: ");
            ad = scanner.nextLine().trim();
            if (ad.isEmpty()) {
                System.out.println("Hata: İlaç adı boş olamaz!");
            } else {
                adGecerli = true;
            }
        }

        String barkod = "";
        boolean barkodGecerli = false;
        while (!barkodGecerli) {
        System.out.print("Barkod: ");
            barkod = scanner.nextLine().trim();
            if (barkod.isEmpty()) {
                System.out.println("Hata: Barkod boş olamaz!");
            } else {
                barkodGecerli = true;
            }
        }

        double fiyat = 0;
        boolean fiyatGecerli = false;
        while (!fiyatGecerli) {
            System.out.print("Fiyat: ");
            try {
                String fiyatStr = scanner.nextLine().trim();
                fiyat = Double.parseDouble(fiyatStr);
                if (fiyat <= 0) {
                    System.out.println("Hata: Fiyat 0'dan büyük olmalıdır!");
                } else {
                    fiyatGecerli = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Hata: Lütfen geçerli bir sayı girin!");
            }
        }

        int stokMiktari = 0;
        boolean stokGecerli = false;
        while (!stokGecerli) {
            System.out.print("Stok Miktarı: ");
            try {
                String stokStr = scanner.nextLine().trim();
                stokMiktari = Integer.parseInt(stokStr);
                if (stokMiktari < 0) {
                    System.out.println("Hata: Stok miktarı negatif olamaz!");
                } else {
                    stokGecerli = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Hata: Lütfen geçerli bir tam sayı girin!");
            }
        }

        String kategori = "";
        boolean kategoriGecerli = false;
        while (!kategoriGecerli) {
            System.out.print("Reçeteli mi? (E/H): ");
            String receteliInput = scanner.nextLine().trim();
            if (receteliInput.equalsIgnoreCase("E")) {
                kategori = "Reçeteli";
                kategoriGecerli = true;
            } else if (receteliInput.equalsIgnoreCase("H")) {
                kategori = "Reçetesiz";
                kategoriGecerli = true;
            } else {
                System.out.println("Hata: Lütfen 'E' (Evet) veya 'H' (Hayır) girin.");
            }
        }

        String tedarikci = "";
        boolean tedarikciGecerli = false;
        while (!tedarikciGecerli) {
        System.out.print("Üretici Firma: ");
            tedarikci = scanner.nextLine().trim();
            if (tedarikci.isEmpty()) {
                System.out.println("Hata: Üretici firma boş olamaz!");
            } else {
                tedarikciGecerli = true;
            }
        }

        String sonKullanmaTarihi = "";
        boolean sonKullanmaTarihiGecerli = false;
        while (!sonKullanmaTarihiGecerli) {
        System.out.print("Son Kullanma Tarihi (YYYY-MM-DD): ");
            sonKullanmaTarihi = scanner.nextLine().trim();
            try {
                LocalDate tarih = LocalDate.parse(sonKullanmaTarihi);
                if (tarih.isBefore(LocalDate.now())) {
                    System.out.println("Hata: Son kullanma tarihi geçmiş bir tarih olamaz!");
                } else {
                    sonKullanmaTarihiGecerli = true;
                }
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Hata: Geçersiz tarih formatı! Lütfen YYYY-MM-DD formatını kullanın.");
            }
        }

        Urun yeniUrun = new Urun(ad, barkod, fiyat, stokMiktari, kategori, tedarikci);
        yeniUrun.setSonKullanmaTarihi(sonKullanmaTarihi);
        urunService.urunEkle(yeniUrun);
    }

    private void urunleriListeleMenu() {
        System.out.println("\n=== İlaç Listesi ===");
        List<Urun> urunler = urunService.tumUrunleriGetir();
        if (urunler.isEmpty()) {
            System.out.println("Henüz ilaç bulunmamaktadır.");
            return;
        }
        urunler.sort(Comparator.comparing(Urun::getAd, Comparator.nullsLast(String::compareTo)));

        urunler.forEach(urun -> {
                    String kategori = urun.getKategori() != null ? urun.getKategori() : "Yok";
                    String tedarikci = urun.getTedarikci() != null ? urun.getTedarikci() : "Yok";
                    String sonKullanma = urun.getSonKullanmaTarihi() != null ? urun.getSonKullanmaTarihi() : "Yok";
                    System.out.printf(
                            "ID: %d, Ad: %s, Barkod: %s, Fiyat: %.2f TL, Stok: %d, Kategori: %s, Üretici: %s, Son Kullanma: %s%n",
                            urun.getId(), urun.getAd(), urun.getBarkod(), urun.getFiyat(),
                            urun.getStokMiktari(), kategori, tedarikci, sonKullanma);
                });
    }

    private void stokGuncelleMenu() {
        System.out.println("\n=== Stok Güncelleme ===");
        System.out.print("Güncellenecek İlaç ID: ");
        Long urunId = -1L;
        boolean idGecerli = false;
        while (!idGecerli) {
            try {
                urunId = scanner.nextLong();
                idGecerli = true;
            } catch (InputMismatchException e) {
                System.out.println("Hatalı giriş! Lütfen geçerli bir İlaç ID'si girin.");
                scanner.nextLine(); // Hatalı girdiyi tüket
            }
        }
        scanner.nextLine(); // Buffer temizleme

        System.out.print("Yeni Stok Miktarı: ");
        int yeniMiktar = -1;
        boolean miktarGecerli = false;
        while (!miktarGecerli) {
            System.out.print("Stok Miktarı: ");
            try {
                yeniMiktar = scanner.nextInt();
                miktarGecerli = true;
            } catch (InputMismatchException e) {
                System.out.println("Hatalı giriş! Lütfen geçerli bir tam sayı girin.");
                scanner.nextLine(); // Hatalı girdiyi tüket
            }
        }
        scanner.nextLine(); // Buffer temizleme

        urunService.stokGuncelle(urunId, yeniMiktar);
    }

    private void urunAraMenu() {
        System.out.println("\n=== İlaç Arama ===");
        System.out.print("Arama Kelimesi (Ad, Barkod veya Kategori): ");
        String aramaKelimesi = scanner.nextLine();

        List<Urun> sonuclar = urunService.urunAra(aramaKelimesi);
        if (sonuclar.isEmpty()) {
            System.out.println("Arama sonucu bulunamadı.");
            return;
        }

        // İlaçları ada göre alfabetik sıralama
        sonuclar.sort(Comparator.comparing(Urun::getAd, Comparator.nullsLast(String::compareTo)));

        System.out.println("\nArama Sonuçları:");
        sonuclar.forEach(urun -> {
            System.out.printf(
                    "ID: %d, Ad: %s, Barkod: %s, Fiyat: %.2f TL, Stok: %d, Kategori: %s, Üretici: %s, Son Kullanma: %s%n",
                    urun.getId(), urun.getAd(), urun.getBarkod(), urun.getFiyat(),
                    urun.getStokMiktari(), urun.getKategori(), urun.getTedarikci(), urun.getSonKullanmaTarihi());
        });
    }

    private void urunSilMenu() {
        System.out.println("\n=== İlaç Silme ===");
        System.out.print("Silinecek İlaç ID: ");
        Long urunId = -1L;
        boolean idGecerli = false;
        while (!idGecerli) {
            try {
                urunId = scanner.nextLong();
                idGecerli = true;
            } catch (InputMismatchException e) {
                System.out.println("Hatalı giriş! Lütfen geçerli bir İlaç ID'si girin.");
                scanner.nextLine(); // Hatalı girdiyi tüket
            }
        }
        scanner.nextLine(); // Buffer temizleme

        System.out.print("Bu ilacı silmek istediğinizden emin misiniz? (E/H): ");
        String onay = scanner.nextLine().toUpperCase();
        
        if (onay.equals("E")) {
            urunService.urunSil(urunId);
        } else {
            System.out.println("İlaç silme işlemi iptal edildi.");
        }
    }

    private void sonKullanmaTarihiKontrolMenu() {
        System.out.println("\n=== Son Kullanma Tarihi Kontrolü ===");
        List<Urun> urunler = urunService.tumUrunleriGetir();
        
        if (urunler.isEmpty()) {
            System.out.println("Henüz ilaç bulunmamaktadır.");
            return;
        }

        LocalDate bugun = LocalDate.now();
        LocalDate birAySonrasi = bugun.plusMonths(1);

        System.out.println("\nSon Kullanma Tarihi Yaklaşan İlaçlar:");
        urunler.stream()
            .filter(urun -> urun.getSonKullanmaTarihi() != null)
            .filter(urun -> {
                LocalDate sonKullanmaTarihi = LocalDate.parse(urun.getSonKullanmaTarihi());
                return !sonKullanmaTarihi.isBefore(bugun) && !sonKullanmaTarihi.isAfter(birAySonrasi);
            })
            .sorted(Comparator.comparing(urun -> LocalDate.parse(urun.getSonKullanmaTarihi())))
            .forEach(urun -> {
                System.out.printf(
                    "ID: %d, Ad: %s, Son Kullanma Tarihi: %s, Stok: %d%n",
                    urun.getId(), urun.getAd(), urun.getSonKullanmaTarihi(), urun.getStokMiktari());
            });

        System.out.println("\nSüresi Geçmiş İlaçlar:");
        urunler.stream()
            .filter(urun -> urun.getSonKullanmaTarihi() != null)
            .filter(urun -> {
                LocalDate sonKullanmaTarihi = LocalDate.parse(urun.getSonKullanmaTarihi());
                return sonKullanmaTarihi.isBefore(bugun);
            })
            .sorted(Comparator.comparing(urun -> LocalDate.parse(urun.getSonKullanmaTarihi())))
            .forEach(urun -> {
                System.out.printf(
                    "ID: %d, Ad: %s, Son Kullanma Tarihi: %s, Stok: %d%n",
                    urun.getId(), urun.getAd(), urun.getSonKullanmaTarihi(), urun.getStokMiktari());
            });
    }

    private void generateQrCodeForUrun(Urun urun) {
        if (urun == null) {
            System.out.println("QR kod oluşturulacak ilaç bulunamadı.");
            return;
        }

        String data = "ID: " + urun.getId() + ", Ad: " + urun.getAd() + ", Barkod: " + urun.getBarkod();
        
        // QR kodları için dizin oluştur
        File qrCodeDir = new File("qr_codes");
        if (!qrCodeDir.exists()) {
            qrCodeDir.mkdirs(); // Dizin yoksa oluştur
        }

        String fileName = qrCodeDir.getAbsolutePath() + File.separator + "qr_code_" + urun.getId() + ".png";

        try {
            ByteArrayOutputStream stream = QRCode.from(data).to(ImageType.PNG).stream();
            File qrFile = new File(fileName);
            stream.writeTo(new java.io.FileOutputStream(qrFile));
            System.out.println("QR kod başarıyla oluşturuldu: " + qrFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("QR kod oluşturulurken bir hata oluştu: " + e.getMessage());
        }
    }

    private void qrKodUretMenu() {
        System.out.println("\n=== QR Kod Oluştur ===");
        System.out.print("QR kodu oluşturulacak İlaç ID: ");
        Long urunId = -1L;
        boolean idGecerli = false;
        while (!idGecerli) {
            try {
                urunId = scanner.nextLong();
                idGecerli = true;
            } catch (InputMismatchException e) {
                System.out.println("Hatalı giriş! Lütfen geçerli bir İlaç ID'si girin.");
                scanner.nextLine(); // Hatalı girdiyi tüket
            }
        }
        scanner.nextLine(); // Buffer temizleme

        java.util.Optional<Urun> urunOptional = urunService.getUrunById(urunId);
        if (urunOptional.isPresent()) {
            generateQrCodeForUrun(urunOptional.get());
        } else {
            System.out.println("Belirtilen ID ile ilaç bulunamadı.");
        }
    }

    private void dusukStokluUrunleriListeleMenu() {
        System.out.println("\n=== Düşük Stoklu İlaçlar ===");
        int dusukStokEsigi = 10; // Düşük stok eşiği
        List<Urun> dusukStokluUrunler = urunService.getDusukStokluUrunler(dusukStokEsigi);

        if (dusukStokluUrunler.isEmpty()) {
            System.out.println("Düşük stoklu ilaç bulunmamaktadır.");
        } else {
            System.out.println("Aşağıdaki ilaçlar düşük stok seviyesindedir (eşik: " + dusukStokEsigi + "):");
            dusukStokluUrunler.forEach(urun -> {
                System.out.printf("ID: %d, Ad: %s, Stok: %d%n", urun.getId(), urun.getAd(), urun.getStokMiktari());
            });
        }
    }
} 