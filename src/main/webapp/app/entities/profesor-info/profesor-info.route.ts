import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProfesorInfo, ProfesorInfo } from 'app/shared/model/profesor-info.model';
import { ProfesorInfoService } from './profesor-info.service';
import { ProfesorInfoComponent } from './profesor-info.component';
import { ProfesorInfoDetailComponent } from './profesor-info-detail.component';
import { ProfesorInfoUpdateComponent } from './profesor-info-update.component';

@Injectable({ providedIn: 'root' })
export class ProfesorInfoResolve implements Resolve<IProfesorInfo> {
  constructor(private service: ProfesorInfoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProfesorInfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((profesorInfo: HttpResponse<ProfesorInfo>) => {
          if (profesorInfo.body) {
            return of(profesorInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProfesorInfo());
  }
}

export const profesorInfoRoute: Routes = [
  {
    path: '',
    component: ProfesorInfoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'licentaApp.profesorInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProfesorInfoDetailComponent,
    resolve: {
      profesorInfo: ProfesorInfoResolve
    },
    data: {
      authorities: ['ROLE_USER', 'ROLE_STUDENT', 'ROLE_ADMIN', 'ROLE_PROFESOR'],
      pageTitle: 'licentaApp.profesorInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProfesorInfoUpdateComponent,
    resolve: {
      profesorInfo: ProfesorInfoResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'licentaApp.profesorInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProfesorInfoUpdateComponent,
    resolve: {
      profesorInfo: ProfesorInfoResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'licentaApp.profesorInfo.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
