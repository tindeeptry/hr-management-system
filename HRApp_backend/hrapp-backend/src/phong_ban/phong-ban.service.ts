import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { PhongBan } from './entities/phong-ban.entity';
import { CreatePhongBanDto } from './dto/create-phong-ban.dto';
import { successResponse, errorResponse } from '../common/response.interface';

@Injectable()
export class PhongBanService {
  constructor(
    @InjectRepository(PhongBan)
    private phongBanRepo: Repository<PhongBan>,
  ) {}

  async findAll() {
    const data = await this.phongBanRepo.find({
      order: { id: 'ASC' }
    });
    return successResponse(data);
  }

  async findOne(id: number) {
    const data = await this.phongBanRepo.findOne({ where: { id } });
    if (!data) return errorResponse('Không tìm thấy phòng ban!');
    return successResponse(data);
  }

  async create(dto: CreatePhongBanDto) {
    const exists = await this.phongBanRepo.findOne({ where: { maPb: dto.maPb } });
    if (exists) return errorResponse('Mã phòng ban đã tồn tại!');
    const phongBan = this.phongBanRepo.create(dto);
    const saved = await this.phongBanRepo.save(phongBan);
    return successResponse(saved, 'Thêm phòng ban thành công');
  }

  async update(id: number, dto: CreatePhongBanDto) {
    const phongBan = await this.phongBanRepo.findOne({ where: { id } });
    if (!phongBan) return errorResponse('Không tìm thấy phòng ban!');
    Object.assign(phongBan, dto);
    const saved = await this.phongBanRepo.save(phongBan);
    return successResponse(saved, 'Cập nhật thành công');
  }

  async remove(id: number) {
    const phongBan = await this.phongBanRepo.findOne({ where: { id } });
    if (!phongBan) return errorResponse('Không tìm thấy phòng ban!');
    await this.phongBanRepo.remove(phongBan);
    return successResponse(null, 'Xóa phòng ban thành công');
  }
}