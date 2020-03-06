import { IStudentInfo } from 'app/shared/model/student-info.model';
import { IAplicareLicenta } from 'app/shared/model/aplicare-licenta.model';
import { IProfesorInfo } from 'app/shared/model/profesor-info.model';

export interface ILicenta {
  id?: number;
  denumire?: string;
  descriere?: string;
  atribuita?: boolean;
  studentInfo?: IStudentInfo;
  aplicareLicentas?: IAplicareLicenta[];
  profesor?: IProfesorInfo;
}

export class Licenta implements ILicenta {
  constructor(
    public id?: number,
    public denumire?: string,
    public descriere?: string,
    public atribuita?: boolean,
    public studentInfo?: IStudentInfo,
    public aplicareLicentas?: IAplicareLicenta[],
    public profesor?: IProfesorInfo
  ) {
    this.atribuita = this.atribuita || false;
  }
}
