import { Moment } from 'moment';
import { IAplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';
import { IStudentInfo } from 'app/shared/model/student-info.model';
import { IProfesorInfo } from 'app/shared/model/profesor-info.model';

export interface IConsultatie {
  id?: number;
  data?: Moment;
  rezolvata?: boolean;
  acceptata?: boolean;
  aplicareConsultaties?: IAplicareConsultatie[];
  student?: IStudentInfo;
  profesor?: IProfesorInfo;
}

export class Consultatie implements IConsultatie {
  constructor(
    public id?: number,
    public data?: Moment,
    public rezolvata?: boolean,
    public acceptata?: boolean,
    public aplicareConsultaties?: IAplicareConsultatie[],
    public student?: IStudentInfo,
    public profesor?: IProfesorInfo
  ) {
    this.rezolvata = this.rezolvata || false;
    this.acceptata = this.acceptata || false;
  }
}
