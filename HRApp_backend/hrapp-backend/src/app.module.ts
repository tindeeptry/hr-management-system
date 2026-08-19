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
      host: process.env.DB_HOST || 'gateway01.ap-southeast-1.prod.aws.tidbcloud.com',
      port: parseInt(process.env.DB_PORT || '4000'),
      username: process.env.DB_USERNAME || '3jcoUEX7E6ee4VB.root',
      password: process.env.DB_PASSWORD || 'yi6nPGdAEla8QgWp',
      database: process.env.DB_DATABASE || 'test', 
      entities: [__dirname + '/**/*.entity{.ts,.js}'],
      synchronize: true,

      ssl: {
        rejectUnauthorized: true,
        minVersion: 'TLSv1.2',
      },
      
      // 🔥 THÊM ĐOẠN NÀY ĐỂ FIX ECONNRESET KHÔNG LO SẬP KẾT NỐI:
      extra: {
        connectionLimit: 5,           // Giới hạn kết nối cho gói free
        waitForConnections: true,
        queueLimit: 0,
        keepAliveInitialDelay: 10000, // Tự động gửi gói tin giữ kết nối sau 10s
        enableKeepAlive: true,
        ssl: {
          rejectUnauthorized: true,
          minVersion: 'TLSv1.2',
        },
      },
      connectTimeout: 20000,          // Tăng thời gian chờ kết nối lên 20s
      retryAttempts: 5,               // Tự động thử lại 5 lần nếu mất kết nối
      retryDelay: 3000,               // Mỗi lần thử lại cách nhau 3 giây
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