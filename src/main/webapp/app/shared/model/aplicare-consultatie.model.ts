import { IStudentInfo } from 'app/shared/model/student-info.model';
import { IConsultatie } from 'app/shared/model/consultatie.model';

export interface IAplicareConsultatie {
  id?: number;
  rezolvata?: boolean;
  acceptata?: boolean;
  student?: IStudentInfo;
  consultatie?: IConsultatie;
}

export class AplicareConsultatie implements IAplicareConsultatie {
  constructor(
    public id?: number,
    public rezolvata?: boolean,
    public acceptata?: boolean,
    public student?: IStudentInfo,
    public consultatie?: IConsultatie
  ) {
    this.rezolvata = this.rezolvata || false;
    this.acceptata = this.acceptata || false;
  }
}
