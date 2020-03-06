import { IStudentInfo } from 'app/shared/model/student-info.model';
import { ILicenta } from 'app/shared/model/licenta.model';

export interface IAplicareLicenta {
  id?: number;
  rezolvata?: boolean;
  acceptata?: boolean;
  student?: IStudentInfo;
  licenta?: ILicenta;
}

export class AplicareLicenta implements IAplicareLicenta {
  constructor(
    public id?: number,
    public rezolvata?: boolean,
    public acceptata?: boolean,
    public student?: IStudentInfo,
    public licenta?: ILicenta
  ) {
    this.rezolvata = this.rezolvata || false;
    this.acceptata = this.acceptata || false;
  }
}
