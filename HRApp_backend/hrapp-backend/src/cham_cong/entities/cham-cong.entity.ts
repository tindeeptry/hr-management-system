import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { NhanVien } from '../../nhan_vien/entities/nhan-vien.entity';

@Entity('cham_cong')
export class ChamCong {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ 
    name: 'nhan_vien_id' 
})
  nhanVienId!: number;

  @Column({ 
    type: 'date' 
})
  ngay!: string;

  @Column({ 
    name: 'gio_vao', 
    type: 'time',
     nullable: true 
    })
  gioVao!: string;

  @Column({ 
    name: 'gio_ra', 
    type: 'time', 
    nullable: true 
})
  gioRa!: string;

  @Column({
    name: 'trang_thai',
    type: 'enum',
    enum: ['di_lam', 'nghi_phep', 'nghi_khong_phep', 'nghi_le'],
    default: 'di_lam'
  })
  trangThai!: string;

  @Column({ 
    name: 'ghi_chu', 
    nullable: true 
})
  ghiChu!: string;

  @ManyToOne(() => NhanVien)
  @JoinColumn({ name: 'nhan_vien_id' })
  nhanVien!: NhanVien;
}