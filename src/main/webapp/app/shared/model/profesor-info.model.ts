import { IUser } from 'app/core/user/user.model';
import { IStudentInfo } from 'app/shared/model/student-info.model';
import { ILicenta } from 'app/shared/model/licenta.model';
import { IConsultatie } from 'app/shared/model/consultatie.model';

export interface IProfesorInfo {
  id?: number;
  user?: IUser;
  studentCoordonats?: IStudentInfo[];
  licentes?: ILicenta[];
  consultaties?: IConsultatie[];
}

export class ProfesorInfo implements IProfesorInfo {
  constructor(
    public id?: number,
    public user?: IUser,
    public studentCoordonats?: IStudentInfo[],
    public licentes?: ILicenta[],
    public consultaties?: IConsultatie[]
  ) {}
}
