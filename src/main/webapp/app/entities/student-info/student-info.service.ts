import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStudentInfo } from 'app/shared/model/student-info.model';

type EntityResponseType = HttpResponse<IStudentInfo>;
type EntityArrayResponseType = HttpResponse<IStudentInfo[]>;

@Injectable({ providedIn: 'root' })
export class StudentInfoService {
  public resourceUrl = SERVER_API_URL + 'api/student-infos';

  constructor(protected http: HttpClient) {}

  create(studentInfo: IStudentInfo): Observable<EntityResponseType> {
    return this.http.post<IStudentInfo>(this.resourceUrl, studentInfo, { observe: 'response' });
  }

  update(studentInfo: IStudentInfo): Observable<EntityResponseType> {
    return this.http.put<IStudentInfo>(this.resourceUrl, studentInfo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStudentInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudentInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
