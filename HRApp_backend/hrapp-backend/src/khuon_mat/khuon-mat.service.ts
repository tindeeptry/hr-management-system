import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { KhuonMat } from './entities/khuon-mat.entity';
import { NhanVien } from '../nhan_vien/entities/nhan-vien.entity';
import { CreateKhuonMatDto } from './dto/create-khuon-mat.dto';
import { successResponse, errorResponse } from '../common/response.interface';

@Injectable()
export class KhuonMatService {
  constructor(
    @InjectRepository(KhuonMat)
    private khuonMatRepo: Repository<KhuonMat>,
    @InjectRepository(NhanVien)
    private nhanVienRepo: Repository<NhanVien>,
  ) {}

  async dangKy(dto: CreateKhuonMatDto) {
    const nv = await this.nhanVienRepo.findOne({
      where: { id: dto.nhanVienId }
    });
    if (!nv) return errorResponse('Không tìm thấy nhân viên!');

    const buffer = this.floatArrayToBuffer(dto.embedding);

    const existing = await this.khuonMatRepo.findOne({
      where: { nhanVienId: dto.nhanVienId }
    });

    if (existing) {
      existing.embedding = buffer;
      await this.khuonMatRepo.save(existing);
      return successResponse(
        { nhanVienId: dto.nhanVienId, hoTen: nv.hoTen },
        'Cập nhật khuôn mặt thành công'
      );
    }

    const khuonMat = this.khuonMatRepo.create({
      nhanVienId: dto.nhanVienId,
      embedding: buffer,
    });
    await this.khuonMatRepo.save(khuonMat);

    return successResponse(
      { nhanVienId: dto.nhanVienId, hoTen: nv.hoTen },
      'Đăng ký khuôn mặt thành công'
    );
  }

  async layTatCaEmbedding() {
    const danhSach = await this.khuonMatRepo.find({
      relations: ['nhanVien'],
    });

    const result = danhSach.map(km => ({
      nhanVienId: km.nhanVienId,
      hoTen: km.nhanVien?.hoTen ?? '',
      embedding: this.bufferToFloatArray(km.embedding),
    }));

    return successResponse(result);
  }

  async xacThuc(dto: { nhanVienId: number; embedding: number[] }) {
    const khuonMat = await this.khuonMatRepo.findOne({
      where: { nhanVienId: dto.nhanVienId },
      relations: ['nhanVien'],
    });

    if (!khuonMat) {
      return errorResponse('Nhân viên chưa đăng ký khuôn mặt!');
    }

    const embeddingLuu = this.bufferToFloatArray(khuonMat.embedding);
    const doTuongDong = this.tinhCosineSimilarity(dto.embedding, embeddingLuu);

    const NGUONG = 0.75;

    if (doTuongDong >= NGUONG) {
      return successResponse({
        khopNhat: true,
        doTuongDong,
        nhanVienId: khuonMat.nhanVienId,
        hoTen: khuonMat.nhanVien?.hoTen ?? '',
      }, 'Xác thực thành công');
    } else {
      return successResponse({
        khopNhat: false,
        doTuongDong,
        nhanVienId: null,
        hoTen: '',
      }, 'Không khớp khuôn mặt');
    }
  }

  async nhanDien(embedding: number[]) {
    const danhSach = await this.khuonMatRepo.find({
      relations: ['nhanVien'],
    });

    if (danhSach.length === 0) {
      return errorResponse('Chưa có dữ liệu khuôn mặt nào!');
    }

    let nhanVienKhopNhat: KhuonMat | null = null; 
    let doTuongDongCaoNhat = 0;
    const NGUONG = 0.75;

    for (const km of danhSach) {
      const embeddingLuu = this.bufferToFloatArray(km.embedding);
      const doTuongDong = this.tinhCosineSimilarity(embedding, embeddingLuu);

      if (doTuongDong > doTuongDongCaoNhat) {
        doTuongDongCaoNhat = doTuongDong;
        nhanVienKhopNhat = km;
      }
    }

    if (doTuongDongCaoNhat >= NGUONG && nhanVienKhopNhat !== null) {  
      return successResponse({
        khopNhat: true,
        doTuongDong: doTuongDongCaoNhat,
        nhanVienId: nhanVienKhopNhat.nhanVienId,
        hoTen: nhanVienKhopNhat.nhanVien?.hoTen ?? '',
      }, 'Nhận diện thành công');
    }

    return successResponse({
      khopNhat: false,
      doTuongDong: doTuongDongCaoNhat,
      nhanVienId: null,
      hoTen: '',
    }, 'Không nhận diện được khuôn mặt');
  }

  async kiemTra(nhanVienId: number) {
    const km = await this.khuonMatRepo.findOne({
      where: { nhanVienId }
    });
    return successResponse({
      daDangKy: !!km,
      nhanVienId,
    });
  }

  private floatArrayToBuffer(arr: number[]): Buffer {
    const buffer = Buffer.allocUnsafe(arr.length * 4);
    for (let i = 0; i < arr.length; i++) {
      buffer.writeFloatLE(arr[i], i * 4);
    }
    return buffer;
  }

  private bufferToFloatArray(buffer: Buffer): number[] {
    const arr: number[] = [];
    for (let i = 0; i < buffer.length; i += 4) {
      arr.push(buffer.readFloatLE(i));
    }
    return arr;
  }

  private tinhCosineSimilarity(a: number[], b: number[]): number {
    if (a.length !== b.length) return 0;
    let dotProduct = 0;
    let normA = 0;
    let normB = 0;
    for (let i = 0; i < a.length; i++) {
      dotProduct += a[i] * b[i];
      normA += a[i] * a[i];
      normB += b[i] * b[i];
    }
    if (normA === 0 || normB === 0) return 0;
    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
  }
}