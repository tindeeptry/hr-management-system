import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { NhanVien } from '../../nhan_vien/entities/nhan-vien.entity';

@Entity('tam_ung')
export class TamUng {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ 
    name: 'nhan_vien_id' 
})
  nhanVienId!: number;

  @Column({ 
    name: 'luong_id', 
    nullable: true 
})
  luongId!: number;

  @Column({ 
    name: 'so_tien', 
    type: 'decimal', 
    precision: 15, 
    scale: 2 
})
  soTien!: number;

  @Column({ 
    name: 'ly_do', 
    nullable: true 
})
  lyDo!: string;

  @Column({
    name: 'trang_thai',
    type: 'enum',
    enum: ['cho_duyet', 'da_duyet', 'tu_choi'],
    default: 'cho_duyet'
  })
  trangThai!: string;

  @Column({ 
    name: 'ngay_ung',
     type: 'date' 
    })
  ngayUng!: string;

  @Column({ 
    name: 'tao_luc', 
    type: 'timestamp', 
    default: () => 'CURRENT_TIMESTAMP' 
})
  taoLuc!: Date;

  @ManyToOne(() => NhanVien)
  @JoinColumn({ name: 'nhan_vien_id' })
  nhanVien!: NhanVien;
}