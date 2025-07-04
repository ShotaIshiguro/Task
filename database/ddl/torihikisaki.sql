CREATE TABLE torihikisaki (
    torihikisaki_id INT AUTO_INCREMENT PRIMARY KEY,
    torihikisaki_name VARCHAR(255) NOT NULL,
    hojin_kojin_kbn VARCHAR(1) NOT NULL,
    deleted_flag VARCHAR(1) NOT NULL DEFAULT '0',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    seikyu_sofu_date VARCHAR(2),
    UNIQUE KEY torihikisaki_pkey (torihikisaki_id),
    KEY idx_torihikisaki_name (torihikisaki_name),
    KEY idx_torihikisaki_deleted_flag (deleted_flag)
);
