import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AuthModule } from './auth/auth.module';
import { NhanVienModule } from './nhan_vien/nhan-vien.module';
import { PhongBanModule } from './phong_ban/phong-ban.module';
import { ChamCongModule } from './cham_cong/cham-cong.module';
import { LuongModule } from './luong/luong.module';
import { TamUngModule } from './tam_ung/tam-ung.module';
import { KhuonMatModule } from './khuon_mat/khuon-mat.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    TypeOrmModule.forRoot({
      type: 'mysql',
      host: process.env.DB_HOST || 'localhost',
      port: parseInt(process.env.DB_PORT || '3306'),
      username: process.env.DB_USERNAME || 'root',
      password: process.env.DB_PASSWORD || 'Tien0610',
      database: process.env.DB_DATABASE || 'hrapp',
      entities: [__dirname + '/**/*.entity{.ts,.js}'],
      synchronize: false,
    }),
    AuthModule,
    NhanVienModule,
    PhongBanModule,
    ChamCongModule,
    LuongModule,
    TamUngModule,
    KhuonMatModule,
  ],
  controllers: [],
  providers: [],
})
export class AppModule {}