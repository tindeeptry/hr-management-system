import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { TamUng } from './entities/tam-ung.entity';
import { NhanVien } from '../nhan_vien/entities/nhan-vien.entity';
import { CreateTamUngDto } from './dto/create-tam-ung.dto';
import { successResponse, errorResponse } from '../common/response.interface';

@Injectable()
export class TamUngService {
  constructor(
    @InjectRepository(TamUng)
    private tamUngRepo: Repository<TamUng>,
    @InjectRepository(NhanVien)
    private nhanVienRepo: Repository<NhanVien>,
  ) {}

  async findAll() {
    const data = await this.tamUngRepo.find({
      relations: ['nhanVien'],
      order: { taoLuc: 'DESC' },
    });
    return successResponse(data.map(t => ({
      ...t,
      hoTen: t.nhanVien?.hoTen ?? '',
    })));
  }

  async findByNhanVien(nhanVienId: number) {
    const data = await this.tamUngRepo.find({
      where: { nhanVienId },
      relations: ['nhanVien'],
      order: { taoLuc: 'DESC' },
    });
    return successResponse(data.map(t => ({
      ...t,
      hoTen: t.nhanVien?.hoTen ?? '',
    })));
  }

  async create(dto: CreateTamUngDto) {
    const nv = await this.nhanVienRepo.findOne({
      where: { id: dto.nhanVienId }
    });
    if (!nv) return errorResponse('Không tìm thấy nhân viên!');

    const ngayUng = new Date().toISOString().split('T')[0];
    const tamUng = this.tamUngRepo.create({
      ...dto,
      ngayUng,
      trangThai: 'cho_duyet',
    });
    const saved = await this.tamUngRepo.save(tamUng);
    return successResponse(
      { ...saved, hoTen: nv.hoTen },
      'Yêu cầu tạm ứng đã được gửi'
    );
  }

  async duyet(id: number) {
    const tamUng = await this.tamUngRepo.findOne({
      where: { id },
      relations: ['nhanVien'],
    });
    if (!tamUng) return errorResponse('Không tìm thấy yêu cầu tạm ứng!');
    if (tamUng.trangThai !== 'cho_duyet')
      return errorResponse('Yêu cầu này đã được xử lý!');

    tamUng.trangThai = 'da_duyet';
    const saved = await this.tamUngRepo.save(tamUng);
    return successResponse(
      { ...saved, hoTen: tamUng.nhanVien?.hoTen ?? '' },
      'Duyệt tạm ứng thành công'
    );
  }

  async tuChoi(id: number) {
    const tamUng = await this.tamUngRepo.findOne({
        where: { id },
        relations: ['nhanVien'],
    });
    if (!tamUng) return errorResponse('Không tìm thấy yêu cầu tạm ứng!');
    if (tamUng.trangThai !== 'cho_duyet')
        return errorResponse('Yêu cầu này đã được xử lý!');

    tamUng.trangThai = 'tu_choi';
    const saved = await this.tamUngRepo.save(tamUng);
    return successResponse(
        { ...saved, hoTen: tamUng.nhanVien?.hoTen ?? '' },
        'Từ chối tạm ứng thành công'
    );
}
}