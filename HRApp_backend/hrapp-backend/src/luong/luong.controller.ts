import { Controller, Get, Post, Put, Param, UseGuards } from '@nestjs/common';
import { LuongService } from './luong.service';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';

@Controller('luong')
@UseGuards(JwtAuthGuard)
export class LuongController {
  constructor(private readonly luongService: LuongService) {}

  @Get()
  findAll() { return this.luongService.findAll(); }

  @Get('nhan-vien/:id')
  findByNhanVien(@Param('id') id: string) {
    return this.luongService.findByNhanVien(+id);
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.luongService.findOne(+id);
  }

  @Post('tinh/:nhanVienId')
  tinhLuong(@Param('nhanVienId') id: string) {
    return this.luongService.tinhLuong(+id);
  }

  @Put('thanh-toan/:id')
  thanhToan(@Param('id') id: string) {
    return this.luongService.thanhToan(+id);
  }
}