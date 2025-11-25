-- 创建供应商表
CREATE TABLE IF NOT EXISTS supplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '供应商ID',
    name VARCHAR(100) NOT NULL COMMENT '供应商名称',
    contact_person VARCHAR(50) NOT NULL COMMENT '联系人',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(500) COMMENT '地址',
    credit_code VARCHAR(20) COMMENT '统一社会信用代码',
    remark VARCHAR(1000) COMMENT '备注',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-活跃，INACTIVE-非活跃',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_supplier_contact_phone (contact_phone),
    UNIQUE KEY uk_supplier_credit_code (credit_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- 创建商机表
CREATE TABLE IF NOT EXISTS business_opportunity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商机ID',
    name VARCHAR(200) NOT NULL COMMENT '商机名称',
    description TEXT NOT NULL COMMENT '商机描述',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    status VARCHAR(20) DEFAULT 'OPEN' COMMENT '状态：OPEN-开放，IN_PROGRESS-进行中，CLOSED_WON-赢单，CLOSED_LOST-输单',
    estimated_amount DECIMAL(15,2) COMMENT '预估金额',
    expected_close_time DATETIME COMMENT '预计关闭时间',
    remark VARCHAR(1000) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY fk_business_opportunity_supplier (supplier_id) REFERENCES supplier (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商机表';
