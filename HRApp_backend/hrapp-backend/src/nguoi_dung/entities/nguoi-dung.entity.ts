import { Entity, PrimaryGeneratedColumn, Column, OneToOne, JoinColumn } from 'typeorm';

@Entity('nguoi_dung')
export class NguoiDung {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ 
    name: 'nhan_vien_id', 
    unique: true 
})
  nhanVienId!: number;

  @Column({ 
    unique: true 
})
  email!: string;

  @Column({ 
    name: 'mat_khau' 
})
  matKhau!: string;

  @Column({
    name: 'vai_tro',
    type: 'enum',
    enum: ['admin', 'nhan_vien'],
    default: 'nhan_vien'
  })
  vaiTro!: string;

  @Column({ 
    name: 'trang_thai', 
    default: 1 
})
  trangThai!: number;
}