# Eczane Stok Takip Sistemi

Bu proje, eczanelerin ilaç stoklarını etkin bir şekilde yönetmelerini sağlayan modern bir stok takip sistemidir. Java ve Spring Boot teknolojileri kullanılarak geliştirilmiştir.

## 📋 İçindekiler

- [Özellikler](#özellikler)
- [Teknolojiler](#teknolojiler)
- [Kurulum](#kurulum)
- [Kullanım](#kullanım)
- [API Dokümantasyonu](#api-dokümantasyonu)
- [Veritabanı Yapısı](#veritabanı-yapısı)
- [Geliştirme Ortamı](#geliştirme-ortamı)
- [Katkıda Bulunma](#katkıda-bulunma)
- [Lisans](#lisans)

## ✨ Özellikler

- 📦 İlaç stok yönetimi
- 🔍 Gelişmiş arama ve filtreleme
- 📊 Stok raporlama
- 🏷️ QR kod desteği
- 📱 Kullanıcı dostu arayüz
- 🔐 Güvenli veri yönetimi
- 📈 Stok takibi ve uyarı sistemi
- 📋 Detaylı raporlama özellikleri

## 🛠️ Teknolojiler

- **Backend:**
  - Java 8
  - Spring Boot 2.7.0
  - Spring Data JPA
  - Spring Security
  - Maven

- **Veritabanı:**
  - SQLite
  - JPA/Hibernate

- **Araçlar ve Kütüphaneler:**
  - Lombok
  - QRGen
  - Spring Boot Starter
  - Spring Boot JDBC
  - Spring Boot Test

## 💻 Kurulum

1. Projeyi klonlayın:
   ```bash
   git clone https://github.com/kullanici/eczane-stok-takip.git
   ```

2. Proje dizinine gidin:
   ```bash
   cd eczane-stok-takip
   ```

3. Maven bağımlılıklarını yükleyin:
   ```bash
   mvn clean install
   ```

4. Uygulamayı çalıştırın:
   ```bash
   mvn spring-boot:run
   ```

## 🚀 Kullanım

Uygulama başlatıldığında aşağıdaki özellikleri kullanabilirsiniz:

1. **İlaç Yönetimi**
   - Yeni ilaç ekleme
   - İlaç bilgilerini güncelleme
   - İlaç silme
   - İlaç arama

2. **Stok İşlemleri**
   - Stok girişi
   - Stok çıkışı
   - Stok güncelleme
   - Stok raporlama

3. **QR Kod İşlemleri**
   - QR kod oluşturma
   - QR kod okuma
   - QR kod ile hızlı stok kontrolü

## 📚 API Dokümantasyonu

API endpoint'leri aşağıdaki gibidir:

- `GET /api/ilaclar` - Tüm ilaçları listeler
- `POST /api/ilaclar` - Yeni ilaç ekler
- `PUT /api/ilaclar/{id}` - İlaç bilgilerini günceller
- `DELETE /api/ilaclar/{id}` - İlaç siler
- `GET /api/ilaclar/{id}` - İlaç detaylarını gösterir

## 💾 Veritabanı Yapısı

Proje SQLite veritabanı kullanmaktadır. Veritabanı şeması aşağıdaki tabloları içerir:

- `ilaclar` - İlaç bilgileri
- `stok_hareketleri` - Stok giriş/çıkış kayıtları
- `kullanicilar` - Sistem kullanıcıları

## 🔧 Geliştirme Ortamı

Geliştirme için gerekli araçlar:

- JDK 8 veya üzeri
- Maven 3.6 veya üzeri
- IDE (IntelliJ IDEA, Eclipse vb.)
- Git