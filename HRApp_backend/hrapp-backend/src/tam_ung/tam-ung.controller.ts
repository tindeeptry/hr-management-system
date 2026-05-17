import { Controller, Get, Post, Put, Param, Body, UseGuards } from '@nestjs/common';
import { TamUngService } from './tam-ung.service';
import { CreateTamUngDto } from './dto/create-tam-ung.dto';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';

@Controller('tam-ung')
@UseGuards(JwtAuthGuard)
export class TamUngController {
  constructor(private readonly tamUngService: TamUngService) {}

  @Get()
  findAll() { return this.tamUngService.findAll(); }

  @Get('nhan-vien/:id')
  findByNhanVien(@Param('id') id: string) {
    return this.tamUngService.findByNhanVien(+id);
  }

  @Post()
  create(@Body() dto: CreateTamUngDto) {
    return this.tamUngService.create(dto);
  }

  @Put('duyet/:id')
  duyet(@Param('id') id: string) {
    return this.tamUngService.duyet(+id);
  }

  @Put('tu-choi/:id')
  tuChoi(@Param('id') id: string) {
    return this.tamUngService.tuChoi(+id);
  }
}