import { Controller, Get, Post, Put, Param, Body, UseGuards } from '@nestjs/common';
import { ChamCongService } from './cham-cong.service';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';

@Controller('cham-cong')
@UseGuards(JwtAuthGuard)
export class ChamCongController {
  constructor(private readonly chamCongService: ChamCongService) {}

  @Get()
  findAll() { return this.chamCongService.findAll(); }

  @Get('nhan-vien/:id')
  findByNhanVien(@Param('id') id: string) {
    return this.chamCongService.findByNhanVien(+id);
  }

  @Get('ngay/:ngay')
  findByNgay(@Param('ngay') ngay: string) {
    return this.chamCongService.findByNgay(ngay);
  }

  @Post('check-in')
  checkIn(@Body() body: { nhanVienId: number }) {
    return this.chamCongService.checkIn(body.nhanVienId);
  }

  @Put('check-out/:id')
  checkOut(@Param('id') id: string) {
    return this.chamCongService.checkOut(+id);
  }
}