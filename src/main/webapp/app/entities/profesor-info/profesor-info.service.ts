import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProfesorInfo } from 'app/shared/model/profesor-info.model';

type EntityResponseType = HttpResponse<IProfesorInfo>;
type EntityArrayResponseType = HttpResponse<IProfesorInfo[]>;

@Injectable({ providedIn: 'root' })
export class ProfesorInfoService {
  public resourceUrl = SERVER_API_URL + 'api/profesor-infos';

  constructor(protected http: HttpClient) {}

  create(profesorInfo: IProfesorInfo): Observable<EntityResponseType> {
    return this.http.post<IProfesorInfo>(this.resourceUrl, profesorInfo, { observe: 'response' });
  }

  update(profesorInfo: IProfesorInfo): Observable<EntityResponseType> {
    return this.http.put<IProfesorInfo>(this.resourceUrl, profesorInfo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfesorInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfesorInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
