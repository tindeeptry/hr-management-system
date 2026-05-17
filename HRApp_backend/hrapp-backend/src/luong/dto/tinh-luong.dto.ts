import { IsNotEmpty } from 'class-validator';

export class TinhLuongDto {
  @IsNotEmpty()
  nhanVienId!: number;

  @IsNotEmpty()
  thang!: number;

  @IsNotEmpty()
  nam!: number;
}