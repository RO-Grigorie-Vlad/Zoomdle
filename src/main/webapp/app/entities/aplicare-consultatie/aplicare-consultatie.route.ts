import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAplicareConsultatie, AplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';
import { AplicareConsultatieService } from './aplicare-consultatie.service';
import { AplicareConsultatieComponent } from './aplicare-consultatie.component';
import { AplicareConsultatieDetailComponent } from './aplicare-consultatie-detail.component';
import { AplicareConsultatieUpdateComponent } from './aplicare-consultatie-update.component';

@Injectable({ providedIn: 'root' })
export class AplicareConsultatieResolve implements Resolve<IAplicareConsultatie> {
  constructor(private service: AplicareConsultatieService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAplicareConsultatie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((aplicareConsultatie: HttpResponse<AplicareConsultatie>) => {
          if (aplicareConsultatie.body) {
            return of(aplicareConsultatie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AplicareConsultatie());
  }
}

export const aplicareConsultatieRoute: Routes = [
  {
    path: '',
    component: AplicareConsultatieComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_STUDENT', 'ROLE_ADMIN', 'ROLE_PROFESOR'],
      defaultSort: 'id,asc',
      pageTitle: 'licentaApp.aplicareConsultatie.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AplicareConsultatieDetailComponent,
    resolve: {
      aplicareConsultatie: AplicareConsultatieResolve
    },
    data: {
      authorities: ['ROLE_STUDENT', 'ROLE_ADMIN', 'ROLE_PROFESOR'],
      pageTitle: 'licentaApp.aplicareConsultatie.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AplicareConsultatieUpdateComponent,
    resolve: {
      aplicareConsultatie: AplicareConsultatieResolve
    },
    data: {
      authorities: ['ROLE_STUDENT', 'ROLE_ADMIN'],
      pageTitle: 'licentaApp.aplicareConsultatie.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AplicareConsultatieUpdateComponent,
    resolve: {
      aplicareConsultatie: AplicareConsultatieResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'licentaApp.aplicareConsultatie.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
