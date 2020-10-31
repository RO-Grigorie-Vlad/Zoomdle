import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IConsultatie } from 'app/shared/model/consultatie.model';
import { AplicareDTO } from 'app/shared/model/aplicare-dto';

type EntityResponseType = HttpResponse<IConsultatie>;
type EntityArrayResponseType = HttpResponse<IConsultatie[]>;

@Injectable({ providedIn: 'root' })
export class ConsultatieService {
  public resourceUrl = SERVER_API_URL + 'api/consultaties';
  public aplicaResourceUrl = SERVER_API_URL + 'api/consultatie/aplica';
  public verificaAplicariUrl = SERVER_API_URL + 'api/aplicare-consultaties/student';

  constructor(protected http: HttpClient) {}

  create(consultatie: IConsultatie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consultatie);
    return this.http
      .post<IConsultatie>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(consultatie: IConsultatie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consultatie);
    return this.http
      .put<IConsultatie>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IConsultatie>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IConsultatie[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  public aplica(dataToSend: AplicareDTO): Observable<HttpResponse<AplicareDTO>> {
    return this.http.post<AplicareDTO>(this.aplicaResourceUrl, dataToSend, { observe: 'response' });
  }

  verificaAplicari(): Observable<number[]> {
    return this.http.get<number[]>(this.verificaAplicariUrl);
  }

  protected convertDateFromClient(consultatie: IConsultatie): IConsultatie {
    const copy: IConsultatie = Object.assign({}, consultatie, {
      data: consultatie.data && consultatie.data.isValid() ? consultatie.data.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.data = res.body.data ? moment(res.body.data) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((consultatie: IConsultatie) => {
        consultatie.data = consultatie.data ? moment(consultatie.data) : undefined;
      });
    }
    return res;
  }
}
