import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { NhanVienController } from './nhan-vien.controller';
import { NhanVienService } from './nhan-vien.service';
import { NhanVien } from './entities/nhan-vien.entity';
import { Luong } from '../luong/entities/luong.entity';
import { NguoiDung } from '../nguoi_dung/entities/nguoi-dung.entity';

@Module({
  imports: [TypeOrmModule.forFeature([NhanVien, Luong, NguoiDung])],
  controllers: [NhanVienController],
  providers: [NhanVienService],
  exports: [NhanVienService],
})
export class NhanVienModule {}