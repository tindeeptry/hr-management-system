import { Controller, Get, Post, Body, Param, UseGuards } from '@nestjs/common';
import { KhuonMatService } from './khuon-mat.service';
import { CreateKhuonMatDto } from './dto/create-khuon-mat.dto';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';

@Controller('khuon-mat')
@UseGuards(JwtAuthGuard)
export class KhuonMatController {
  constructor(private readonly khuonMatService: KhuonMatService) {}

  @Post('dang-ky')
  dangKy(@Body() dto: CreateKhuonMatDto) {
    return this.khuonMatService.dangKy(dto);
  }

  @Get('embedding')
  layTatCaEmbedding() {
    return this.khuonMatService.layTatCaEmbedding();
  }

  @Post('nhan-dien')
  nhanDien(@Body() body: { embedding: number[] }) {
    return this.khuonMatService.nhanDien(body.embedding);
  }

  @Post('xac-thuc')
  xacThuc(@Body() body: { nhanVienId: number; embedding: number[] }) {
    return this.khuonMatService.xacThuc(body);
  }

  @Get('kiem-tra/:nhanVienId')
  kiemTra(@Param('nhanVienId') id: string) {
    return this.khuonMatService.kiemTra(+id);
  }
}