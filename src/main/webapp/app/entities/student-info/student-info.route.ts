import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IStudentInfo, StudentInfo } from 'app/shared/model/student-info.model';
import { StudentInfoService } from './student-info.service';
import { StudentInfoComponent } from './student-info.component';
import { StudentInfoDetailComponent } from './student-info-detail.component';
import { StudentInfoUpdateComponent } from './student-info-update.component';

@Injectable({ providedIn: 'root' })
export class StudentInfoResolve implements Resolve<IStudentInfo> {
  constructor(private service: StudentInfoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentInfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((studentInfo: HttpResponse<StudentInfo>) => {
          if (studentInfo.body) {
            return of(studentInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentInfo());
  }
}

export const studentInfoRoute: Routes = [
  {
    path: '',
    component: StudentInfoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'licentaApp.studentInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StudentInfoDetailComponent,
    resolve: {
      studentInfo: StudentInfoResolve
    },
    data: {
      authorities: ['ROLE_STUDENT', 'ROLE_PROFESOR', 'ROLE_ADMIN'],
      pageTitle: 'licentaApp.studentInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StudentInfoUpdateComponent,
    resolve: {
      studentInfo: StudentInfoResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'licentaApp.studentInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StudentInfoUpdateComponent,
    resolve: {
      studentInfo: StudentInfoResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'licentaApp.studentInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
