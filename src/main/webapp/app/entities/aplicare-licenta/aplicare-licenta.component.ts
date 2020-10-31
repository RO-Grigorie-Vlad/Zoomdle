import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAplicareLicenta } from 'app/shared/model/aplicare-licenta.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AplicareLicentaService } from './aplicare-licenta.service';
import { AplicareLicentaDeleteDialogComponent } from './aplicare-licenta-delete-dialog.component';
import { AccountService } from 'app/core/auth/account.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

import { Account } from 'app/core/user/account.model';

@Component({
  selector: 'jhi-aplicare-licenta',
  templateUrl: './aplicare-licenta.component.html'
})
export class AplicareLicentaComponent implements OnInit, OnDestroy {
  aplicareLicentas?: IAplicareLicenta[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  currentUser!: Account | null;
  currentUserLogin!: string;
  user!: IUser;

  constructor(
    protected aplicareLicentaService: AplicareLicentaService,
    protected accountService: AccountService,
    private userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected alertService: JhiAlertService
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        this.currentUser = account;
        // this.userService.find(account.login).subscribe(
        // user => (this.user = user) );
      }
      account == null ? (this.currentUserLogin = '') : (this.currentUserLogin = account.login);
    });

    this.aplicareLicentaService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IAplicareLicenta[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInAplicareLicentas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  raspundeLaAplicatie(aplicareID: number, raspuns: boolean): void {
    this.aplicareLicentaService.raspunde(aplicareID, raspuns).subscribe(() => this.loadPage());
    if (raspuns === true) {
      this.alertService.get().push(
        this.alertService.addAlert(
          {
            type: 'success',
            msg: 'licentaApp.aplicareConsultatie.accepta',
            timeout: 3000,
            toast: false,
            scoped: true
          },
          this.alertService.get()
        )
      );
    } else {
      this.alertService.get().push(
        this.alertService.addAlert(
          {
            type: 'danger',
            msg: 'licentaApp.aplicareConsultatie.respinge',
            timeout: 3000,
            toast: false,
            scoped: true
          },
          this.alertService.get()
        )
      );
    }
  }

  trackId(index: number, item: IAplicareLicenta): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAplicareLicentas(): void {
    this.eventSubscriber = this.eventManager.subscribe('aplicareLicentaListModification', () => this.loadPage());
  }

  delete(aplicareLicenta: IAplicareLicenta): void {
    const modalRef = this.modalService.open(AplicareLicentaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aplicareLicenta = aplicareLicenta;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IAplicareLicenta[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/aplicare-licenta'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.aplicareLicentas = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
