CREATE TABLE IF NOT EXISTS urunler (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ad VARCHAR(255) NOT NULL,
    barkod VARCHAR(255) UNIQUE,
    fiyat DOUBLE NOT NULL,
    stok_miktari INTEGER NOT NULL,
    kategori VARCHAR(255),
    son_guncelleme_tarihi DATETIME,
    tedarikci VARCHAR(255),
    son_kullanma_tarihi DATE
); 