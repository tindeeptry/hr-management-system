import { Injectable } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { NguoiDung } from '../nguoi_dung/entities/nguoi-dung.entity';
import { NhanVien } from '../nhan_vien/entities/nhan-vien.entity';
import { LoginDto } from './dto/login.dto';
import { successResponse, errorResponse } from '../common/response.interface';

@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(NguoiDung)
    private nguoiDungRepo: Repository<NguoiDung>,
    @InjectRepository(NhanVien)
    private nhanVienRepo: Repository<NhanVien>,
    private jwtService: JwtService,
  ) {}

  async login(loginDto: LoginDto) {
    const { email, matKhau } = loginDto;

    const nguoiDung = await this.nguoiDungRepo.findOne({
      where: { email, trangThai: 1 },
    });

    if (!nguoiDung) {
      return errorResponse('Email hoặc mật khẩu không đúng!');
    }

    const matKhauDung = await bcrypt.compare(matKhau, nguoiDung.matKhau);
    if (!matKhauDung) {
      return errorResponse('Email hoặc mật khẩu không đúng!');
    }

    const nhanVien = await this.nhanVienRepo.findOne({
      where: { id: nguoiDung.nhanVienId },
    });

    const payload = {
      sub: nguoiDung.id,
      email: nguoiDung.email,
      vaiTro: nguoiDung.vaiTro,
    };

    const token = this.jwtService.sign(payload);

    return successResponse({
      token,
      vaiTro: nguoiDung.vaiTro,
      nhanVienId: nguoiDung.nhanVienId,
      hoTen: nhanVien?.hoTen ?? '',
    }, 'Đăng nhập thành công');
  }
}