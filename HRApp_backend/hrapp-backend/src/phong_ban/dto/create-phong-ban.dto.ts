import { IsNotEmpty, IsOptional } from 'class-validator';

export class CreatePhongBanDto {
  @IsNotEmpty({ message: 'Mã phòng ban không được để trống' })
  maPb!: string;

  @IsNotEmpty({ message: 'Tên phòng ban không được để trống' })
  tenPb!: string;

  @IsOptional()
  moTa?: string;
}