package com.pbo.konterfaiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

class App {
    public static void main(String[] args) {
        // Koneksi ke database
        Connection connection = null;
        // exception handling
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:4306/konter_faiz", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // collection framework
        ArrayList<Paket> paketList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        // Proses login
        boolean isLoggedIn = login(scanner);
        if (isLoggedIn) {
            // Memanggil menu utama jika login berhasil
            menuUtama(paketList, connection, scanner);
        } else {
            System.out.println("Login gagal!");
        }
    }

    // Fungsi untuk login
    public static boolean login(Scanner scanner) {
        System.out.println("Masukkan username:");
        String username = scanner.nextLine();
        System.out.println("Masukkan password:");
        String password = scanner.nextLine();

        return username.equals("admin") && password.equals("admin123"); // Contoh sederhana
    }

    // Fungsi untuk menu utama
    public static void menuUtama(ArrayList<Paket> paketList, Connection connection, Scanner scanner) {
        boolean isRunning = true;
        // Perulangan
        while (isRunning) {
            System.out.println("\nMenu Utama:");
            System.out.println("1. Tampilkan Paket");
            System.out.println("2. Tambah Paket");
            System.out.println("3. Edit Paket");
            System.out.println("4. Hapus Paket");
            System.out.println("5. Pesan Paket");
            System.out.println("6. Keluar");
            System.out.println("Pilih menu (1-6):");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Membuang new line

            // Percabangan
            switch (choice) {
                case 1:
                    tampilkanPaket(paketList, connection);
                    break;
                case 2:
                    tambahPaket(connection, scanner);
                    break;
                case 3:
                    editPaket(connection, scanner);
                    break;
                case 4:
                    hapusPaket(connection, scanner);
                    break;
                case 5:
                    pesanPaket(paketList, connection, scanner);
                    break;
                case 6:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }
        }
    }

    // Fungsi untuk menampilkan paket (Read)
    public static void tampilkanPaket(ArrayList<Paket> paketList, Connection connection) {
        // exception handling
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM paket");

            // Header tabel
            System.out.printf("%-5s%-20s%-10s%-10s%-15s%-10s%n", "ID", "Nama Paket", "Harga", "Kuota (GB)",
                    "Masa Berlaku", "Jumlah");
            System.out.println("---------------------------------------------------------------------");

            // Perulangan
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nama = resultSet.getString("nama");
                int harga = resultSet.getInt("harga");
                int kuota = resultSet.getInt("kuota");
                int masaBerlaku = resultSet.getInt("masa_berlaku");
                String jenis = resultSet.getString("jenis");
                String tambahan = "";

                // Mengambil informasi tambahan berdasarkan jenis paket
                if (jenis.equals("mingguan")) {
                    int minggu = resultSet.getInt("jumlah_minggu");
                    tambahan = minggu + " minggu";
                } else if (jenis.equals("bulanan")) {
                    int bulan = resultSet.getInt("jumlah_bulan");
                    tambahan = bulan + " bulan";
                } else if (jenis.equals("harian")) {
                    int hari = resultSet.getInt("jumlah_hari");
                    tambahan = hari + " hari";
                }

                // Menampilkan dalam format tabel
                System.out.printf("%-5d%-20s%-10d%-10d%-15d%-10s%n", id, nama, harga, kuota, masaBerlaku, tambahan);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Fungsi untuk menambahkan paket (Create)
    public static void tambahPaket(Connection connection, Scanner scanner) {
        System.out.println("Masukkan jenis paket (mingguan/bulanan/harian):");
        String jenis = scanner.nextLine();
        System.out.println("Masukkan nama paket:");
        String nama = scanner.nextLine();
        System.out.println("Masukkan harga:");
        int harga = scanner.nextInt();
        System.out.println("Masukkan kuota (dalam GB):");
        int kuota = scanner.nextInt();
        System.out.println("Masukkan masa berlaku (dalam hari):");
        int masaBerlaku = scanner.nextInt();

        // exception handling
        try {
            PreparedStatement preparedStatement = null;
            if (jenis.equals("mingguan")) {
                System.out.println("Masukkan jumlah minggu:");
                int minggu = scanner.nextInt();
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO paket (jenis, nama, harga, kuota, masa_berlaku, jumlah_minggu) VALUES (?, ?, ?, ?, ?, ?)");
                preparedStatement.setInt(6, minggu);
            } else if (jenis.equals("bulanan")) {
                System.out.println("Masukkan jumlah bulan:");
                int bulan = scanner.nextInt();
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO paket (jenis, nama, harga, kuota, masa_berlaku, jumlah_bulan) VALUES (?, ?, ?, ?, ?, ?)");
                preparedStatement.setInt(6, bulan);
            } else if (jenis.equals("harian")) {
                System.out.println("Masukkan jumlah hari:");
                int hari = scanner.nextInt();
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO paket (jenis, nama, harga, kuota, masa_berlaku, jumlah_hari) VALUES (?, ?, ?, ?, ?, ?)");
                preparedStatement.setInt(6, hari);
            }

            preparedStatement.setString(1, jenis);
            preparedStatement.setString(2, nama);
            preparedStatement.setInt(3, harga);
            preparedStatement.setInt(4, kuota);
            preparedStatement.setInt(5, masaBerlaku);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Paket berhasil ditambahkan.");
            } else {
                System.out.println("Gagal menambahkan paket.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Fungsi untuk mengedit paket(Update)
    public static void editPaket(Connection connection, Scanner scanner) {
        tampilkanPaket(null, connection);
        System.out.println("Pilih ID paket yang akan diedit:");
        int idPaket = scanner.nextInt();
        scanner.nextLine(); // Membuang new line

        // Memeriksa apakah ID paket ada di database sebelum melanjutkan edit
        if (!isPaketExist(connection, idPaket)) {
            System.out.println("Paket dengan ID tersebut tidak ditemukan.");
            return; // Menghentikan eksekusi jika ID paket tidak ditemukan
        }

        // Sisanya dari fungsi editPaket seperti yang telah diperbaharui sebelumnya
        System.out.println("Masukkan nama baru:");
        String nama = scanner.nextLine();
        System.out.println("Masukkan harga baru:");
        int harga = scanner.nextInt();
        System.out.println("Masukkan kuota baru (dalam GB):");
        int kuota = scanner.nextInt();
        System.out.println("Masukkan masa berlaku baru (dalam hari):");
        int masaBerlaku = scanner.nextInt();
        scanner.nextLine(); // Membuang new line

        // exception handling
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE paket SET nama=?, harga=?, kuota=?, masa_berlaku=? WHERE id=?");
            preparedStatement.setString(1, nama);
            preparedStatement.setInt(2, harga);
            preparedStatement.setInt(3, kuota);
            preparedStatement.setInt(4, masaBerlaku);
            preparedStatement.setInt(5, idPaket);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Paket berhasil diupdate.");
            } else {
                System.out.println("Gagal mengupdate paket.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Fungsi untuk menghapus paket (Delete)
    public static void hapusPaket(Connection connection, Scanner scanner) {
        tampilkanPaket(null, connection);
        System.out.println("Pilih ID paket yang akan dihapus:");
        int idPaket = scanner.nextInt();
        scanner.nextLine(); // Membuang new line

        // Memeriksa apakah ID paket ada di database sebelum melanjutkan hapus
        if (!isPaketExist(connection, idPaket)) {
            System.out.println("Paket dengan ID tersebut tidak ditemukan.");
            return; // Menghentikan eksekusi jika ID paket tidak ditemukan
        }

        // exception handling
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM paket WHERE id=?");
            preparedStatement.setInt(1, idPaket);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Paket berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus paket.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Fungsi untuk memeriksa keberadaan paket berdasarkan ID
    public static boolean isPaketExist(Connection connection, int idPaket) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM paket WHERE id=?");
            preparedStatement.setInt(1, idPaket);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Jika terdapat hasil dari query, maka ID paket ditemukan
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false; // Defaultnya, mengembalikan false jika terjadi kesalahan atau tidak ada hasil
    }

    // Fungsi untuk memesan paket
    public static void pesanPaket(ArrayList<Paket> paketList, Connection connection, Scanner scanner) {
        tampilkanPaket(paketList, connection);
        System.out.println("Pilih ID paket yang ingin dibeli:");
        int idPaket = scanner.nextInt();
        scanner.nextLine(); // Membuang new line

        // exception handling
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM paket WHERE id=?");
            preparedStatement.setInt(1, idPaket);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nama = resultSet.getString("nama");
                int harga = resultSet.getInt("harga");
                int kuota = resultSet.getInt("kuota");
                int masaBerlaku = resultSet.getInt("masa_berlaku");
                String jenis = resultSet.getString("jenis");

                // Menampilkan informasi paket yang dipilih
                System.out.println("Anda memilih paket:");
                System.out.println("Nama Paket: " + nama);
                System.out.println("Harga: Rp. " + harga);

                // Menerima input nomor HP
                System.out.println("Masukkan nomor HP:");
                String nomorHP = scanner.nextLine();

                // Menampilkan nomor HP yang dimasukkan pengguna
                System.out.println("Nomor HP: " + nomorHP);

                // Menerima input nominal pembayaran
                System.out.println("Masukkan nominal pembayaran:");
                int nominalPembayaran = scanner.nextInt();

                // Perhitungan Matematika
                int totalHarga = harga;
                int kembalian = nominalPembayaran - totalHarga;

                // Membuat transaksi baru jika pembayaran sudah cukup
                if (kembalian >= 0) {
                    // Menyimpan transaksi ke database
                    simpanTransaksi(connection, idPaket, nominalPembayaran);

                    // Menambahkan paket yang dipilih ke dalam paketList
                    if (jenis.equals("mingguan")) {
                        int minggu = resultSet.getInt("jumlah_minggu");
                        PaketMingguan paket = new PaketMingguan(id, nama, harga, kuota, masaBerlaku, minggu);
                        paketList.add(paket);
                    } else if (jenis.equals("bulanan")) {
                        int bulan = resultSet.getInt("jumlah_bulan");
                        PaketBulanan paket = new PaketBulanan(id, nama, harga, kuota, masaBerlaku, bulan);
                        paketList.add(paket);
                    } else if (jenis.equals("harian")) {
                        int hari = resultSet.getInt("jumlah_hari");
                        PaketHarian paket = new PaketHarian(id, nama, harga, kuota, masaBerlaku, hari);
                        paketList.add(paket);
                    }

                    // String and Date
                    Date tanggalPesan = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    System.out.println("Tanggal pemesanan: " + formatter.format(tanggalPesan));

                    System.out.println("Pembelian berhasil! Kembalian Anda: Rp. " + kembalian);
                } else {
                    System.out.println("Pembayaran kurang! Mohon masukkan nominal yang cukup.");
                }
            } else {
                System.out.println("Paket tidak ditemukan.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Fungsi untuk menyimpan transaksi ke dalam database
    public static void simpanTransaksi(Connection connection, int idPaket, int nominalPembayaran) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO transaksi (id_paket, jumlah_pembayaran, tanggal_transaksi) VALUES (?, ?, CURDATE())");
            preparedStatement.setInt(1, idPaket);
            preparedStatement.setInt(2, nominalPembayaran);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaksi berhasil disimpan.");
            } else {
                System.out.println("Gagal menyimpan transaksi.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
