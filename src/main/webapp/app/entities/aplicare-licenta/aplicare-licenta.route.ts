import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAplicareLicenta, AplicareLicenta } from 'app/shared/model/aplicare-licenta.model';
import { AplicareLicentaService } from './aplicare-licenta.service';
import { AplicareLicentaComponent } from './aplicare-licenta.component';
import { AplicareLicentaDetailComponent } from './aplicare-licenta-detail.component';
import { AplicareLicentaUpdateComponent } from './aplicare-licenta-update.component';

@Injectable({ providedIn: 'root' })
export class AplicareLicentaResolve implements Resolve<IAplicareLicenta> {
  constructor(private service: AplicareLicentaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAplicareLicenta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((aplicareLicenta: HttpResponse<AplicareLicenta>) => {
          if (aplicareLicenta.body) {
            return of(aplicareLicenta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AplicareLicenta());
  }
}

export const aplicareLicentaRoute: Routes = [
  {
    path: '',
    component: AplicareLicentaComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_PROFESOR'],
      defaultSort: 'id,asc',
      pageTitle: 'licentaApp.aplicareLicenta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AplicareLicentaDetailComponent,
    resolve: {
      aplicareLicenta: AplicareLicentaResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_PROFESOR'],
      pageTitle: 'licentaApp.aplicareLicenta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AplicareLicentaUpdateComponent,
    resolve: {
      aplicareLicenta: AplicareLicentaResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_STUDENT'],
      pageTitle: 'licentaApp.aplicareLicenta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AplicareLicentaUpdateComponent,
    resolve: {
      aplicareLicenta: AplicareLicentaResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'licentaApp.aplicareLicenta.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
