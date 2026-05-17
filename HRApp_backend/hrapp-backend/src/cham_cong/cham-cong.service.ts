import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { ChamCong } from './entities/cham-cong.entity';
import { NhanVien } from '../nhan_vien/entities/nhan-vien.entity';
import { successResponse, errorResponse } from '../common/response.interface';

@Injectable()
export class ChamCongService {
  constructor(
    @InjectRepository(ChamCong)
    private chamCongRepo: Repository<ChamCong>,
    @InjectRepository(NhanVien)
    private nhanVienRepo: Repository<NhanVien>,
  ) {}

  async findAll() {
    const data = await this.chamCongRepo.find({
      relations: ['nhanVien'],
      order: { ngay: 'DESC' },
    });
    return successResponse(data.map(cc => ({
      ...cc,
      hoTen: cc.nhanVien?.hoTen ?? '',
    })));
  }

  async findByNhanVien(nhanVienId: number) {
    const data = await this.chamCongRepo.find({
      where: { nhanVienId },
      relations: ['nhanVien'],
      order: { ngay: 'DESC' },
    });
    return successResponse(data.map(cc => ({
      ...cc,
      hoTen: cc.nhanVien?.hoTen ?? '',
    })));
  }

  async findByNgay(ngay: string) {
    const data = await this.chamCongRepo.find({
      where: { ngay },
      relations: ['nhanVien'],
    });
    return successResponse(data.map(cc => ({
      ...cc,
      hoTen: cc.nhanVien?.hoTen ?? '',
    })));
  }

  async checkIn(nhanVienId: number) {
    const homNay = new Date().toISOString().split('T')[0];
    const gioVao = new Date().toTimeString().split(' ')[0];

    const existing = await this.chamCongRepo.findOne({
      where: { nhanVienId, ngay: homNay },
    });
    if (existing) return errorResponse('Nhân viên đã check-in hôm nay!');

    const nv = await this.nhanVienRepo.findOne({ where: { id: nhanVienId } });
    if (!nv) return errorResponse('Không tìm thấy nhân viên!');

    const cc = this.chamCongRepo.create({
      nhanVienId,
      ngay: homNay,
      gioVao,
      trangThai: 'di_lam',
    });
    const saved = await this.chamCongRepo.save(cc);
    return successResponse(
      { ...saved, hoTen: nv.hoTen },
      'Check-in thành công'
    );
  }

  async checkOut(id: number) {
    const cc = await this.chamCongRepo.findOne({
      where: { id },
      relations: ['nhanVien'],
    });
    if (!cc) return errorResponse('Không tìm thấy bản ghi chấm công!');
    if (cc.gioRa) return errorResponse('Nhân viên đã check-out!');

    cc.gioRa = new Date().toTimeString().split(' ')[0];
    const saved = await this.chamCongRepo.save(cc);
    return successResponse(
      { ...saved, hoTen: cc.nhanVien?.hoTen ?? '' },
      'Check-out thành công'
    );
  }
}