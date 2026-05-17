import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { NhanVien } from '../../nhan_vien/entities/nhan-vien.entity';

@Entity('khuon_mat')
export class KhuonMat {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ name: 'nhan_vien_id', unique: true })
  nhanVienId!: number;

  @Column({ type: 'blob' })
  embedding!: Buffer;

  @Column({
    name: 'cap_nhat_luc',
    type: 'timestamp',
    default: () => 'CURRENT_TIMESTAMP',
    onUpdate: 'CURRENT_TIMESTAMP'
  })
  capNhatLuc!: Date;

  @ManyToOne(() => NhanVien)
  @JoinColumn({ name: 'nhan_vien_id' })
  nhanVien!: NhanVien;
}