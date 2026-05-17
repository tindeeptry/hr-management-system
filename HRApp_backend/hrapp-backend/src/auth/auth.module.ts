import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { PassportModule } from '@nestjs/passport';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AuthService } from './auth.service';
import { AuthController } from './auth.controller';
import { JwtStrategy } from './jwt.strategy';
import { JwtAuthGuard } from './jwt-auth.guard';
import { NguoiDung } from '../nguoi_dung/entities/nguoi-dung.entity';
import { NhanVien } from '../nhan_vien/entities/nhan-vien.entity';

@Module({
  imports: [
    TypeOrmModule.forFeature([NguoiDung, NhanVien]),
    PassportModule.register({ defaultStrategy: 'jwt' }),
    JwtModule.register({
      secret: 'hrapp_secret_key_2025',
      signOptions: { expiresIn: '7d' },
    }),
  ],
  controllers: [AuthController],
  providers: [
    AuthService,
    JwtStrategy,
    JwtAuthGuard
  ],
  exports: [
    AuthService,
    JwtAuthGuard,
    JwtModule
  ],
})
export class AuthModule {}