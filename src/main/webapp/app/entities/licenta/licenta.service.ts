import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILicenta } from 'app/shared/model/licenta.model';

type EntityResponseType = HttpResponse<ILicenta>;
type EntityArrayResponseType = HttpResponse<ILicenta[]>;

@Injectable({ providedIn: 'root' })
export class LicentaService {
  public resourceUrl = SERVER_API_URL + 'api/licentas';

  constructor(protected http: HttpClient) {}

  create(licenta: ILicenta): Observable<EntityResponseType> {
    return this.http.post<ILicenta>(this.resourceUrl, licenta, { observe: 'response' });
  }

  update(licenta: ILicenta): Observable<EntityResponseType> {
    return this.http.put<ILicenta>(this.resourceUrl, licenta, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILicenta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILicenta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
