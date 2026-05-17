import { Controller, Get, Post, Put, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { PhongBanService } from './phong-ban.service';
import { CreatePhongBanDto } from './dto/create-phong-ban.dto';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';

@Controller('phong-ban')
@UseGuards(JwtAuthGuard)
export class PhongBanController {
  constructor(private readonly phongBanService: PhongBanService) {}

  @Get()
  findAll() { return this.phongBanService.findAll(); }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.phongBanService.findOne(+id);
  }

  @Post()
  create(@Body() dto: CreatePhongBanDto) {
    return this.phongBanService.create(dto);
  }

  @Put(':id')
  update(@Param('id') id: string, @Body() dto: CreatePhongBanDto) {
    return this.phongBanService.update(+id, dto);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.phongBanService.remove(+id);
  }
}