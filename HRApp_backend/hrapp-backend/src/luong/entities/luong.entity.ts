import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { NhanVien } from '../../nhan_vien/entities/nhan-vien.entity';

@Entity('luong')
export class Luong {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ name: 'nhan_vien_id' })
  nhanVienId!: number;

  @Column({ type: 'tinyint' })
  thang!: number;

  @Column({ type: 'year' })
  nam!: number;

  @Column({ name: 'so_ngay_cong', default: 0 })
  soNgayCong!: number;

  @Column({ name: 'luong_co_ban', type: 'decimal', precision: 15, scale: 2 })
  luongCoBan!: number;

  @Column({ name: 'he_so', type: 'decimal', precision: 5, scale: 2 })
  heSo!: number;

  @Column({ name: 'tong_luong', type: 'decimal', precision: 15, scale: 2 })
  tongLuong!: number;

  @Column({ name: 'da_ung', type: 'decimal', precision: 15, scale: 2, default: 0 })
  daUng!: number;

  @Column({ name: 'thuc_linh', type: 'decimal', precision: 15, scale: 2 })
  thucLinh!: number;

  @Column({
    name: 'trang_thai',
    type: 'enum',
    enum: ['chua_thanh_toan', 'da_thanh_toan'],
    default: 'chua_thanh_toan'
  })
  trangThai!: string;

  @Column({ name: 'tao_luc', type: 'timestamp', default: () => 'CURRENT_TIMESTAMP' })
  taoLuc!: Date;

  @ManyToOne(() => NhanVien)
  @JoinColumn({ name: 'nhan_vien_id' })
  nhanVien!: NhanVien;
}