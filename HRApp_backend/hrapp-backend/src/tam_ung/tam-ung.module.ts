import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { TamUngController } from './tam-ung.controller';
import { TamUngService } from './tam-ung.service';
import { TamUng } from './entities/tam-ung.entity';
import { NhanVien } from '../nhan_vien/entities/nhan-vien.entity';

@Module({
  imports: [TypeOrmModule.forFeature([TamUng, NhanVien])],
  controllers: [TamUngController],
  providers: [TamUngService],
  exports: [TamUngService],
})
export class TamUngModule {}