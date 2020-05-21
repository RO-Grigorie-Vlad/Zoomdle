import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAplicareLicenta } from 'app/shared/model/aplicare-licenta.model';

type EntityResponseType = HttpResponse<IAplicareLicenta>;
type EntityArrayResponseType = HttpResponse<IAplicareLicenta[]>;

@Injectable({ providedIn: 'root' })
export class AplicareLicentaService {
  public resourceUrl = SERVER_API_URL + 'api/aplicare-licentas';
  public customResourceURL = SERVER_API_URL + 'api/aplicare-licentas/raspunde';

  constructor(protected http: HttpClient) {}

  create(aplicareLicenta: IAplicareLicenta): Observable<EntityResponseType> {
    return this.http.post<IAplicareLicenta>(this.resourceUrl, aplicareLicenta, { observe: 'response' });
  }

  update(aplicareLicenta: IAplicareLicenta): Observable<EntityResponseType> {
    return this.http.put<IAplicareLicenta>(this.resourceUrl, aplicareLicenta, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAplicareLicenta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAplicareLicenta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  raspunde(aplicareLicentaID: number, raspuns: boolean): Observable<EntityResponseType> {
    return this.http.post<IAplicareLicenta>(this.customResourceURL, { aplicareLicentaID, raspuns }, { observe: 'response' });
  }
}
