import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Luong } from './entities/luong.entity';
import { NhanVien } from '../nhan_vien/entities/nhan-vien.entity';
import { ChamCong } from '../cham_cong/entities/cham-cong.entity';
import { TamUng } from '../tam_ung/entities/tam-ung.entity';
import { successResponse, errorResponse } from '../common/response.interface';

@Injectable()
export class LuongService {
  constructor(
    @InjectRepository(Luong)
    private luongRepo: Repository<Luong>,
    @InjectRepository(NhanVien)
    private nhanVienRepo: Repository<NhanVien>,
    @InjectRepository(ChamCong)
    private chamCongRepo: Repository<ChamCong>,
    @InjectRepository(TamUng)
    private tamUngRepo: Repository<TamUng>,
  ) {}

  async findAll() {
    const data = await this.luongRepo.find({
      relations: ['nhanVien'],
      order: { nam: 'DESC', thang: 'DESC' },
    });
    return successResponse(data.map(l => ({
      ...l,
      hoTen: l.nhanVien?.hoTen ?? '',
    })));
  }

  async findByNhanVien(nhanVienId: number) {
    const data = await this.luongRepo.find({
      where: { nhanVienId },
      relations: ['nhanVien'],
      order: { nam: 'DESC', thang: 'DESC' },
    });
    return successResponse(data.map(l => ({
      ...l,
      hoTen: l.nhanVien?.hoTen ?? '',
    })));
  }

  async findOne(id: number) {
    const l = await this.luongRepo.findOne({
      where: { id },
      relations: ['nhanVien'],
    });
    if (!l) return errorResponse('Không tìm thấy bản ghi lương!');
    return successResponse({ ...l, hoTen: l.nhanVien?.hoTen ?? '' });
  }

  async tinhLuong(nhanVienId: number) {
    const nv = await this.nhanVienRepo.findOne({ where: { id: nhanVienId } });
    if (!nv) return errorResponse('Không tìm thấy nhân viên!');

    const now = new Date();
    const thang = now.getMonth() + 1;
    const nam = now.getFullYear();

    const existing = await this.luongRepo.findOne({
      where: { nhanVienId, thang, nam },
    });
    if (existing) return errorResponse('Đã tính lương tháng này rồi!');

    const thangStr = String(thang).padStart(2, '0');
    const danhSachCC = await this.chamCongRepo
      .createQueryBuilder('cc')
      .where('cc.nhan_vien_id = :id', { id: nhanVienId })
      .andWhere('cc.ngay LIKE :thang', { thang: `${nam}-${thangStr}-%` })
      .andWhere('cc.gio_vao IS NOT NULL')
      .getMany();

    const soNgayCong = danhSachCC.length;

    const danhSachTamUng = await this.tamUngRepo
      .createQueryBuilder('tu')
      .where('tu.nhan_vien_id = :id', { id: nhanVienId })
      .andWhere('tu.ngay_ung LIKE :thang', { thang: `${nam}-${thangStr}-%` })
      .andWhere('tu.trang_thai = :tt', { tt: 'da_duyet' })
      .getMany();

    const daUng = danhSachTamUng.reduce((sum, t) => sum + Number(t.soTien), 0);

    const luongCoBan = Number(nv.luongCoBan);
    const heSo = Number(nv.heSoLuong);
    const tongLuong = luongCoBan * heSo * soNgayCong;
    const thucLinh = tongLuong - daUng;

    const luong = this.luongRepo.create({
      nhanVienId,
      thang,
      nam,
      soNgayCong,
      luongCoBan,
      heSo,
      tongLuong,
      daUng,
      thucLinh,
      trangThai: 'chua_thanh_toan',
    });

    const saved = await this.luongRepo.save(luong);
    return successResponse(
      { ...saved, hoTen: nv.hoTen },
      'Tính lương thành công'
    );
  }

  async thanhToan(id: number) {
    const luong = await this.luongRepo.findOne({
      where: { id },
      relations: ['nhanVien'],
    });
    if (!luong) return errorResponse('Không tìm thấy bản ghi lương!');
    if (luong.trangThai === 'da_thanh_toan')
      return errorResponse('Lương đã được thanh toán!');

    luong.trangThai = 'da_thanh_toan';
    const saved = await this.luongRepo.save(luong);
    return successResponse(
      { ...saved, hoTen: luong.nhanVien?.hoTen ?? '' },
      'Thanh toán lương thành công'
    );
  }
}