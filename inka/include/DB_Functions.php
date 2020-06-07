<?php
 
class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // koneksi ke database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }


    /**
     * Get user berdasarkan email dan password
     */
    public function getUserByEmailAndPassword($email, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM tb_user WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifikasi password user
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // cek password jika sesuai
            if ($encrypted_password == $hash) {
                // autentikasi user berhasil
                return $user;
            }
        } else {
            return NULL;
        }
    }

    /**
     * Get user berdasarkan id
     */
    public function getUserAccount($id) {
 
        $stmt = $this->conn->prepare("SELECT * FROM tb_user WHERE unique_id = ?");
        $stmt->bind_param("s", $id); 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user; 
        } else {
            return NULL;
        }
    }

    /**
     * Cek User ada atau tidak
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from tb_user WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user telah ada 
            $stmt->close();
            return true;
        } else {
            // user belum ada 
            $stmt->close();
            return false;
        }
    }
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 
    /**
     * Registrasi Akun
     */
    public function simpanUser($nama, $email, $password, $no_telp, $jenis_kegiatan, $level_user, $foto_profile) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        
        $path = "images/$uuid.jpeg";
        // $finalPath = "/inka/".$path;

        $stmt = $this->conn->prepare("INSERT INTO tb_user(unique_id, nama, email, encrypted_password, salt, no_telp, jenis_kegiatan, level_user, foto_profile) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("sssssssss", $uuid, $nama, $email, $encrypted_password, $salt, $no_telp, $jenis_kegiatan, $level_user, $path);
        $result = $stmt->execute();
        $stmt->close();
 
        // cek jika sudah sukses
        if ($result) {
            if (file_put_contents($path, base64_decode($foto_profile))) {
                $stmt = $this->conn->prepare("SELECT * FROM tb_user WHERE email = ?");
                $stmt->bind_param("s", $email);
                $stmt->execute();
                $user = $stmt->get_result()->fetch_assoc();
                $stmt->close();
     
                return $user;
            }else{
                return false;
            }
            
        } else {
            return false;
        }
    }

    /**
     * Insert data pendaftar prakerin
     */

    public function daftarAwalPrakerin($id,$nama,$nis,$raport,$sekolah,$divisi,$tempName1,$tempName2,$tempName3,$tempName4){

        $path = "file_prakerin/";

        $folder1 = $path."scan_surat_pengajuan/";
        $url1 = $folder1.$id.".pdf";

        $folder2 = $path."scan_rapot/";
        $url2 = $folder2.$id.".pdf";

        $folder3 = $path."scan_kartu_asuransi/";
        $url3 = $folder3.$id.".pdf";

        $folder4 = $path."scan_surat_pernyataan/";
        $url4 = $folder4.$id.".pdf";
        
        $x = "belum diisi";

        $stmt = $this->conn->prepare("INSERT INTO tb_pendaftar_prakerin(id_user, nama_lengkap, nis, nilai_raport, nama_sekolah, scan_pengajuan_prakerin, scan_raport, scan_asuransi, scan_pernyataan, scan_surat_tugas, penempatan_divisi) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
        $stmt->bind_param("sssssssssss", $id, $nama, $nis, $raport, $sekolah, $url1, $url2, $url3, $url4, $x, $divisi);
        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            if(move_uploaded_file($tempName1,$url1) && move_uploaded_file( $tempName2,$url2) && move_uploaded_file($tempName3,$url3) && move_uploaded_file($tempName4,$url4)){
                $stmt = $this->conn->prepare("SELECT * FROM tb_pendaftar_prakerin WHERE id_user = ?");
                $stmt->bind_param("s", $id);
                $stmt->execute();
                $user = $stmt->get_result()->fetch_assoc();
                $stmt->close();
                return $user;
            }else{
                return false;
            }
        }
    }

    /**
     * Insert data pendaftar PKL
     */
    public function daftarAwalPkl($id,$nama,$nim,$ipk,$kampus,$divisi,$tempName1,$tempName2,$tempName3,$tempName4,$tempName5){

        $path = "file_pkl/";

        $folder1 = $path."scan_ktp/";
        $url1 = $folder1.$id.".pdf";

        $folder2 = $path."scan_ktm/";
        $url2 = $folder2.$id.".pdf";

        $folder3 = $path."scan_ipk/";
        $url3 = $folder3.$id.".pdf";

        $folder4 = $path."scan_surat_pengajuan/";
        $url4 = $folder4.$id.".pdf";

        $folder5 = $path."scan_proposal/";
        $url5 = $folder5.$id.".pdf";
        
        $x = "belum diisi";

        $stmt = $this->conn->prepare("INSERT INTO tb_pendaftar_pkl(id_user, nama_lengkap, nim, nilai_ipk, perguruan_tinggi, scan_ktp, scan_ktm, scan_nilai_ipk, scan_pengajuan_pkl, scan_proposal_pkl, scan_surat_tugas, penempatan_divisi) VALUES(?, ?,?,?,?,?,?,?,?,?,?,?)");
        $stmt->bind_param("ssssssssssss", $id, $nama, $nim, $ipk, $kampus, $url1, $url2, $url3, $url4, $url5, $x, $divisi);
        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            if(move_uploaded_file($tempName1,$url1) && move_uploaded_file( $tempName2,$url2) && move_uploaded_file($tempName3,$url3) && move_uploaded_file($tempName4,$url4) && move_uploaded_file($tempName5,$url5)){
                $stmt = $this->conn->prepare("SELECT * FROM tb_pendaftar_pkl WHERE id_user = ?");
                $stmt->bind_param("s", $id);
                $stmt->execute();
                $user = $stmt->get_result()->fetch_assoc();
                $stmt->close();

                return $user;
            }else{
                return false;
            }
        }

    }

    /**
     * Get data Prakerin berdasarkan id
     */
    public function getDataPrakerin($id) {
 
        $stmt = $this->conn->prepare("SELECT * FROM tb_pendaftar_prakerin WHERE id_user = ?");
        $stmt->bind_param("s", $id); 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user; 
        } else {
            return NULL;
        }
    }

    /**
     * Get data PKL berdasarkan id
     */
    public function getDataPkl($id) {
 
        $stmt = $this->conn->prepare("SELECT * FROM tb_pendaftar_pkl WHERE id_user = ?");
        $stmt->bind_param("s", $id); 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user; 
        } else {
            return NULL;
        }
    }

    public function daftarUlangPrakerin($id,$tempName){

        $path = "file_prakerin/";

        $folder = $path."scan_surat_penugasan/";
        $url = $folder.$id.".pdf";

        $stmt = $this->conn->prepare("UPDATE tb_pendaftar_prakerin SET scan_surat_tugas = ? WHERE id_user = ?");
        $stmt->bind_param("ss", $url, $id);
        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            if(move_uploaded_file($tempName,$url) ){
                $stmt = $this->conn->prepare("SELECT * FROM tb_pendaftar_prakerin WHERE id_user = ?");
                $stmt->bind_param("s", $id);
                $stmt->execute();
                $user = $stmt->get_result()->fetch_assoc();
                $stmt->close();
                return $user;
            }else{
                return false;
            }
        }
    }

    public function daftarUlangPkl($id,$tempName){

        $path = "file_pkl/";

        $folder = $path."scan_surat_penugasan/";
        $url = $folder.$id.".pdf";

        $stmt = $this->conn->prepare("UPDATE tb_pendaftar_pkl SET scan_surat_tugas = ? WHERE id_user = ?");
        $stmt->bind_param("ss", $url, $id);
        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            if(move_uploaded_file($tempName,$url) ){
                $stmt = $this->conn->prepare("SELECT * FROM tb_pendaftar_pkl WHERE id_user = ?");
                $stmt->bind_param("s", $id);
                $stmt->execute();
                $user = $stmt->get_result()->fetch_assoc();
                $stmt->close();
                return $user;
            }else{
                return false;
            }
        }
    }

    public function editInfoPrakerin($id,$nama,$nis,$raport){

        $stmt = $this->conn->prepare("UPDATE tb_pendaftar_prakerin SET nama_lengkap = ?, nis = ?, nilai_raport = ? WHERE id_user = ?");
        $stmt->bind_param("ssss", $nama, $nis, $raport, $id);
        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM tb_pendaftar_prakerin WHERE id_user = ?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        }else{
            return false;
        }
        
    }

    public function editInfoPkl($id,$nama,$nim,$ipk){

        $stmt = $this->conn->prepare("UPDATE tb_pendaftar_pkl SET nama_lengkap = ?, nim = ?, nilai_ipk = ? WHERE id_user = ?");
        $stmt->bind_param("ssss", $nama, $nim, $ipk, $id);
        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM tb_pendaftar_pkl WHERE id_user = ?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        }else{
            return false;
        }
        
    }


    /**
     * Cek User ada atau tidak
     */
    public function prakerinExisted($id) {
        $stmt = $this->conn->prepare("SELECT * from tb_pendaftar_prakerin WHERE id_user = ?");
        $stmt->bind_param("s", $id);
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user; 
        } else {
            return false;
        }
    }


    /**
     * Cek User ada atau tidak
     */
    public function pklExisted($id) {
        $stmt = $this->conn->prepare("SELECT * from tb_pendaftar_pkl WHERE id_user = ?");
        $stmt->bind_param("s", $id);
       if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user; 
        } else {
            return false;
        }
    }

    public function getTahun($id) {
 
        $stmt = $this->conn->prepare("SELECT * FROM tb_periode WHERE id_periode = ?");
        $stmt->bind_param("s", $id); 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }

    public function getKuotaPrakerin($id_jur,$id_periode) {
 
        $stmt = $this->conn->prepare("SELECT * FROM tb_kuota_prakerin WHERE id_jur_smk = ? AND id_periode = ? ");
        $stmt->bind_param("ss", $id_jur,$id_periode); 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user; 
        } else {
            return NULL;
        }
    }

    public function getKuotaPkl($id_jur,$id_periode) {
 
        $stmt = $this->conn->prepare("SELECT * FROM tb_kuota_pkl WHERE id_jur_pt = ? AND id_periode = ? ");
        $stmt->bind_param("ss", $id_jur,$id_periode); 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user; 
        } else {
            return NULL;
        }
    }

 
}
 
?>