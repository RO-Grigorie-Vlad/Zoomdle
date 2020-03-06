import { IUser } from 'app/core/user/user.model';
import { IAplicareLicenta } from 'app/shared/model/aplicare-licenta.model';
import { IConsultatie } from 'app/shared/model/consultatie.model';
import { IAplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';
import { ILicenta } from 'app/shared/model/licenta.model';
import { IProfesorInfo } from 'app/shared/model/profesor-info.model';

export interface IStudentInfo {
  id?: number;
  user?: IUser;
  aplicareLics?: IAplicareLicenta[];
  consults?: IConsultatie[];
  aplicareConsults?: IAplicareConsultatie[];
  licenta?: ILicenta;
  profesor?: IProfesorInfo;
}

export class StudentInfo implements IStudentInfo {
  constructor(
    public id?: number,
    public user?: IUser,
    public aplicareLics?: IAplicareLicenta[],
    public consults?: IConsultatie[],
    public aplicareConsults?: IAplicareConsultatie[],
    public licenta?: ILicenta,
    public profesor?: IProfesorInfo
  ) {}
}
