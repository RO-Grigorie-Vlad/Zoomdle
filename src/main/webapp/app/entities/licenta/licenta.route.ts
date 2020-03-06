import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILicenta, Licenta } from 'app/shared/model/licenta.model';
import { LicentaService } from './licenta.service';
import { LicentaComponent } from './licenta.component';
import { LicentaDetailComponent } from './licenta-detail.component';
import { LicentaUpdateComponent } from './licenta-update.component';

@Injectable({ providedIn: 'root' })
export class LicentaResolve implements Resolve<ILicenta> {
  constructor(private service: LicentaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILicenta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((licenta: HttpResponse<Licenta>) => {
          if (licenta.body) {
            return of(licenta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Licenta());
  }
}

export const licentaRoute: Routes = [
  {
    path: '',
    component: LicentaComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'licentaApp.licenta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LicentaDetailComponent,
    resolve: {
      licenta: LicentaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'licentaApp.licenta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LicentaUpdateComponent,
    resolve: {
      licenta: LicentaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'licentaApp.licenta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LicentaUpdateComponent,
    resolve: {
      licenta: LicentaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'licentaApp.licenta.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
