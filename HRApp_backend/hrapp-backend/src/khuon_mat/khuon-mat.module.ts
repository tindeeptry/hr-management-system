import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { KhuonMatController } from './khuon-mat.controller';
import { KhuonMatService } from './khuon-mat.service';
import { KhuonMat } from './entities/khuon-mat.entity';
import { NhanVien } from '../nhan_vien/entities/nhan-vien.entity';

@Module({
  imports: [TypeOrmModule.forFeature([KhuonMat, NhanVien])],
  controllers: [KhuonMatController],
  providers: [KhuonMatService],
  exports: [KhuonMatService],
})
export class KhuonMatModule {}