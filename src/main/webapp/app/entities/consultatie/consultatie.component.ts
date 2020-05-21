import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConsultatie } from 'app/shared/model/consultatie.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ConsultatieService } from './consultatie.service';
import { ConsultatieDeleteDialogComponent } from './consultatie-delete-dialog.component';
import { LicentaDTO } from 'app/shared/model/licenta-dto';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-consultatie',
  templateUrl: './consultatie.component.html'
})
export class ConsultatieComponent implements OnInit, OnDestroy {
  consultaties?: IConsultatie[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  currentUser!: Account | null;
  currentUserLogin!: string;
  licentaID!: number;
  succes!: string;
  dataToSend2!: LicentaDTO;

  isStudent!: Boolean;
  isProfesor!: Boolean;
  isAdmin!: Boolean;
  curentProfesorID!: number;
  areLicenta!: Boolean;

  listaConsultatiiDejaAplicate: number[] = [];

  constructor(
    protected consultatieService: ConsultatieService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected accountService: AccountService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected alertService: JhiAlertService
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.accountService
      .getAuthenticationState()
      .subscribe(account => (account == null ? (this.currentUserLogin = '') : (this.currentUserLogin = account.login)));
    this.isStudent = this.accountService.hasAnyAuthority('ROLE_STUDENT');
    if (this.isStudent === true) {
      this.accountService.verificaLicenta(this.currentUserLogin).subscribe(response => (this.areLicenta = response));
    }

    this.isProfesor = this.accountService.hasAnyAuthority('ROLE_PROFESOR');
    if (this.isProfesor === true) {
      this.accountService.getCurrentProfesorId(this.currentUserLogin).subscribe(response => (this.curentProfesorID = response));
    }

    this.isAdmin = this.accountService.hasAnyAuthority('ROLE_ADMIN');
    if (this.isStudent === true) {
      this.consultatieService.verificaAplicari().subscribe(response => (this.listaConsultatiiDejaAplicate = response));
    }

    this.consultatieService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IConsultatie[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInConsultaties();
  }

  aplica(consultatieID: number): void {
    this.dataToSend2 = new LicentaDTO(this.currentUserLogin, consultatieID);

    this.consultatieService.aplica(this.dataToSend2).subscribe(() => this.loadPage());
    // alerta de success
    this.alertService.get().push(
      this.alertService.addAlert(
        {
          type: 'success',
          msg: 'licentaApp.consultatie.aplica',
          timeout: 3000,
          toast: false,
          scoped: true
        },
        this.alertService.get()
      )
    );
    // window.location.reload();
    // this.licentaService.aplica2(this.currentUserLogin,licentaID2);
  }

  checkIncludes(consultatieID: number): Boolean {
    if (this.listaConsultatiiDejaAplicate.includes(consultatieID)) {
      return true;
    } else {
      return false;
    }
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IConsultatie): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInConsultaties(): void {
    this.eventSubscriber = this.eventManager.subscribe('consultatieListModification', () => this.loadPage());
  }

  delete(consultatie: IConsultatie): void {
    const modalRef = this.modalService.open(ConsultatieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.consultatie = consultatie;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IConsultatie[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/consultatie'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.consultaties = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
