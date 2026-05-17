import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { PhongBan } from '../../phong_ban/entities/phong-ban.entity';

@Entity('nhan_vien')
export class NhanVien {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ 
    name: 'ma_nv', 
    unique: true 
})
  maNv!: string;

  @Column({ 
    name: 'ho_ten' 
})
  hoTen!: string;

  @Column({ 
    name: 'so_dien_thoai', 
    nullable: true 
})
  soDienThoai!: string;

  @Column({ 
    name: 'dia_chi', 
    nullable: true 
})
  diaChi!: string;

  @Column({ 
    name: 'ngay_sinh', 
    type: 'date', 
    nullable: true 
})
  ngaySinh!: string;

  @Column({
    name: 'gioi_tinh',
    type: 'enum',
    enum: ['nam', 'nu', 'khac'],
    default: 'nam'
  })
  gioiTinh!: string;

  @Column({ 
    name: 'phong_ban_id', 
    nullable: true 
})
  phongBanId!: number;

  @Column({ 
    name: 'luong_co_ban', 
    type: 'decimal', 
    precision: 15, 
    scale: 2, 
    default: 0 
})
  luongCoBan!: number;

  @Column({ 
    name: 'he_so_luong', 
    type: 'decimal', 
    precision: 5, 
    scale: 2, 
    default: 1.0 
})
  heSoLuong!: number;

  @Column({ 
    name: 'ngay_vao_lam', 
    type: 'date', 
    nullable: true 
})
  ngayVaoLam!: string;

  @Column({ 
    name: 'trang_thai', 
    default: 1 
})
  trangThai!: number;

  @ManyToOne(() => PhongBan)
  @JoinColumn({ name: 'phong_ban_id' })
  phongBan!: PhongBan;
}