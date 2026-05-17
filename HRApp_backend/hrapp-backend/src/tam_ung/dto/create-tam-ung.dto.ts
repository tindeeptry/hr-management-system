import { IsNotEmpty, IsOptional } from 'class-validator';

export class CreateTamUngDto {
  @IsNotEmpty()
  nhanVienId!: number;

  @IsNotEmpty()
  soTien!: number;

  @IsOptional()
  lyDo?: string;
}