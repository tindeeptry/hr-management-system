import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { NhanVienService } from './nhan-vien.service';
import { CreateNhanVienDto } from './dto/create-nhan-vien.dto';
import { UpdateNhanVienDto } from './dto/update-nhan-vien.dto';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';

@Controller('nhan-vien')
@UseGuards(JwtAuthGuard)
export class NhanVienController {
  constructor(private readonly nhanVienService: NhanVienService) {}

  @Get()
  findAll() { return this.nhanVienService.findAll(); }

  @Get('ma/:ma')
  findByMa(@Param('ma') ma: string) {
    return this.nhanVienService.findByMa(ma);
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.nhanVienService.findOne(+id);
  }

  @Post()
  create(@Body() dto: CreateNhanVienDto) {
    return this.nhanVienService.create(dto);
  }

  @Put(':id')
  update(@Param('id') id: string, @Body() dto: UpdateNhanVienDto) {
    return this.nhanVienService.update(+id, dto);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.nhanVienService.remove(+id);
  }
}