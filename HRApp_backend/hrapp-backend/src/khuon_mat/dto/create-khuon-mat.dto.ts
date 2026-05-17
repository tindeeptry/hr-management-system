import { IsNotEmpty } from 'class-validator';

export class CreateKhuonMatDto {
  @IsNotEmpty()
  nhanVienId!: number;

  @IsNotEmpty()
  embedding!: number[];
}