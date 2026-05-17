import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';

@Entity('phong_ban')
export class PhongBan {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ name: 'ma_pb', unique: true })
  maPb!: string;

  @Column({ name: 'ten_pb' })
  tenPb!: string;

  @Column({ name: 'mo_ta', nullable: true })
  moTa!: string;

  @Column({ name: 'trang_thai', default: 1 })
  trangThai!: number;

  @Column({
    name: 'tao_luc',
    type: 'timestamp',
    default: () => 'CURRENT_TIMESTAMP'
  })
  taoLuc!: Date;
}