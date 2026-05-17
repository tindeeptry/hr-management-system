import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { NhanVien } from './entities/nhan-vien.entity';
import { CreateNhanVienDto } from './dto/create-nhan-vien.dto';
import { UpdateNhanVienDto } from './dto/update-nhan-vien.dto';
import { Luong } from '../luong/entities/luong.entity';
import { NguoiDung } from '../nguoi_dung/entities/nguoi-dung.entity';
import { successResponse, errorResponse } from '../common/response.interface';
import * as bcrypt from 'bcrypt';

@Injectable()
export class NhanVienService {
  constructor(
    @InjectRepository(NhanVien)
    private nhanVienRepo: Repository<NhanVien>,
    @InjectRepository(Luong)
    private luongRepo: Repository<Luong>,
    @InjectRepository(NguoiDung)
    private nguoiDungRepo: Repository<NguoiDung>,
  ) {}

  async findAll() {
    const data = await this.nhanVienRepo.find({
      relations: ['phongBan'],
      order: { id: 'ASC' },
    });
    const result = data.map(nv => ({
      ...nv,
      tenPhongBan: nv.phongBan?.tenPb ?? null,
    }));
    return successResponse(result);
  }

  async findOne(id: number) {
    const nv = await this.nhanVienRepo.findOne({
      where: { id },
      relations: ['phongBan'],
    });
    if (!nv) return errorResponse('Không tìm thấy nhân viên!');
    return successResponse({ ...nv, tenPhongBan: nv.phongBan?.tenPb ?? null });
  }

  async findByMa(ma: string) {
    const nv = await this.nhanVienRepo.findOne({
      where: { maNv: ma },
      relations: ['phongBan'],
    });
    if (!nv) return errorResponse('Không tìm thấy nhân viên!');
    return successResponse({ ...nv, tenPhongBan: nv.phongBan?.tenPb ?? null });
  }

  async create(dto: CreateNhanVienDto) {
    const exists = await this.nhanVienRepo.findOne({ where: { maNv: dto.maNv } });
    if (exists) return errorResponse('Mã nhân viên đã tồn tại!');

    const nv = this.nhanVienRepo.create(dto);
    const saved = await this.nhanVienRepo.save(nv);

    // Tạo tài khoản mặc định (mật khẩu: 123456)
    const matKhauHash = await bcrypt.hash('123456', 10);
    const email = `${dto.maNv.toLowerCase()}@hrapp.com`;
    const nguoiDung = this.nguoiDungRepo.create({
      nhanVienId: saved.id,
      email,
      matKhau: matKhauHash,
      vaiTro: 'nhan_vien',
    });
    await this.nguoiDungRepo.save(nguoiDung);

    return successResponse(saved, 'Thêm nhân viên thành công');
  }

  async update(id: number, dto: UpdateNhanVienDto) {
    const nv = await this.nhanVienRepo.findOne({ where: { id } });
    if (!nv) return errorResponse('Không tìm thấy nhân viên!');
    Object.assign(nv, dto);
    const saved = await this.nhanVienRepo.save(nv);
    return successResponse(saved, 'Cập nhật thành công');
  }

  async remove(id: number) {
    // Kiểm tra lương chưa thanh toán
    const luongChuaTT = await this.luongRepo.findOne({
      where: { nhanVienId: id, trangThai: 'chua_thanh_toan' },
    });
    if (luongChuaTT) {
      return errorResponse(
        'Nhân viên còn lương chưa thanh toán, vui lòng thanh toán trước khi xóa!'
      );
    }

    const nv = await this.nhanVienRepo.findOne({ where: { id } });
    if (!nv) return errorResponse('Không tìm thấy nhân viên!');

    await this.nhanVienRepo.remove(nv);
    return successResponse(null, 'Xóa nhân viên thành công');
  }
}