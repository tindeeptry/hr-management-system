import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ChamCongController } from './cham-cong.controller';
import { ChamCongService } from './cham-cong.service';
import { ChamCong } from './entities/cham-cong.entity';
import { NhanVien } from '../nhan_vien/entities/nhan-vien.entity';

@Module({
  imports: [TypeOrmModule.forFeature([ChamCong, NhanVien])],
  controllers: [ChamCongController],
  providers: [ChamCongService],
  exports: [ChamCongService],
})
export class ChamCongModule {}