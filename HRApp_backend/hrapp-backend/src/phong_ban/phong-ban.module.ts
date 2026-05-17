import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PhongBanController } from './phong-ban.controller';
import { PhongBanService } from './phong-ban.service';
import { PhongBan } from './entities/phong-ban.entity';

@Module({
  imports: [TypeOrmModule.forFeature([PhongBan])],
  controllers: [PhongBanController],
  providers: [PhongBanService],
  exports: [PhongBanService],
})
export class PhongBanModule {}