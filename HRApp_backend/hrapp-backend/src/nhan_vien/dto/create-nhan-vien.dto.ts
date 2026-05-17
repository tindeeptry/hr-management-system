import { IsNotEmpty, IsOptional, IsNumber } from 'class-validator';

export class CreateNhanVienDto {
  @IsNotEmpty({ message: 'Mã nhân viên không được để trống' })
  maNv!: string;

  @IsNotEmpty({ message: 'Họ tên không được để trống' })
  hoTen!: string;

  @IsOptional()
  soDienThoai?: string;

  @IsOptional()
  diaChi?: string;

  @IsOptional()
  ngaySinh?: string;

  @IsOptional()
  gioiTinh?: string;

  @IsOptional()
  phongBanId?: number;

  @IsNotEmpty({ message: 'Lương cơ bản không được để trống' })
  luongCoBan!: number;

  @IsNotEmpty({ message: 'Hệ số lương không được để trống' })
  heSoLuong!: number;

  @IsOptional()
  ngayVaoLam?: string;
}