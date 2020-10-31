import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';

type EntityResponseType = HttpResponse<IAplicareConsultatie>;
type EntityArrayResponseType = HttpResponse<IAplicareConsultatie[]>;

@Injectable({ providedIn: 'root' })
export class AplicareConsultatieService {
  public resourceUrl = SERVER_API_URL + 'api/aplicare-consultaties';
  public raspundeLaAplicatieUrl = SERVER_API_URL + 'api/aplicare-consultaties/raspunde';

  constructor(protected http: HttpClient) {}

  create(aplicareConsultatie: IAplicareConsultatie): Observable<EntityResponseType> {
    return this.http.post<IAplicareConsultatie>(this.resourceUrl, aplicareConsultatie, { observe: 'response' });
  }

  update(aplicareConsultatie: IAplicareConsultatie): Observable<EntityResponseType> {
    return this.http.put<IAplicareConsultatie>(this.resourceUrl, aplicareConsultatie, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAplicareConsultatie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAplicareConsultatie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  raspunde(aplicareID: number, raspuns: boolean): Observable<EntityResponseType> {
    return this.http.post<IAplicareConsultatie>(this.raspundeLaAplicatieUrl, { aplicareID, raspuns }, { observe: 'response' });
  }
}
