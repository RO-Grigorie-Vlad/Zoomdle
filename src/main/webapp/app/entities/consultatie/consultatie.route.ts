import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IConsultatie, Consultatie } from 'app/shared/model/consultatie.model';
import { ConsultatieService } from './consultatie.service';
import { ConsultatieComponent } from './consultatie.component';
import { ConsultatieDetailComponent } from './consultatie-detail.component';
import { ConsultatieUpdateComponent } from './consultatie-update.component';

@Injectable({ providedIn: 'root' })
export class ConsultatieResolve implements Resolve<IConsultatie> {
  constructor(private service: ConsultatieService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConsultatie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((consultatie: HttpResponse<Consultatie>) => {
          if (consultatie.body) {
            return of(consultatie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Consultatie());
  }
}

export const consultatieRoute: Routes = [
  {
    path: '',
    component: ConsultatieComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'licentaApp.consultatie.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ConsultatieDetailComponent,
    resolve: {
      consultatie: ConsultatieResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'licentaApp.consultatie.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ConsultatieUpdateComponent,
    resolve: {
      consultatie: ConsultatieResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'licentaApp.consultatie.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ConsultatieUpdateComponent,
    resolve: {
      consultatie: ConsultatieResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'licentaApp.consultatie.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
