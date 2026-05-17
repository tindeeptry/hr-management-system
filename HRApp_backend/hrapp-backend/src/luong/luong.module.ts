import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { LuongController } from './luong.controller';
import { LuongService } from './luong.service';
import { Luong } from './entities/luong.entity';
import { NhanVien } from '../nhan_vien/entities/nhan-vien.entity';
import { ChamCong } from '../cham_cong/entities/cham-cong.entity';
import { TamUng } from '../tam_ung/entities/tam-ung.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Luong, NhanVien, ChamCong, TamUng])],
  controllers: [LuongController],
  providers: [LuongService],
  exports: [LuongService],
})
export class LuongModule {}