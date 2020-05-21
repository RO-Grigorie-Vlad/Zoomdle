import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILicenta } from 'app/shared/model/licenta.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { LicentaService } from './licenta.service';
import { LicentaDeleteDialogComponent } from './licenta-delete-dialog.component';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { LicentaDTO } from 'app/shared/model/licenta-dto';

@Component({
  selector: 'jhi-licenta',
  templateUrl: './licenta.component.html'
})
export class LicentaComponent implements OnInit, OnDestroy {
  licentas?: ILicenta[];
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
  areLicenta!: Boolean;
  curentProfesorID!: number;

  listaLicenteDejaAplicate: number[] = [];

  constructor(
    protected licentaService: LicentaService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
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
      this.licentaService.verificaAplicari().subscribe(response => (this.listaLicenteDejaAplicate = response));
    }

    this.licentaService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ILicenta[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  /** goToEdit(profesorOfThisLicenta: number, licentaID: number): void {
     if(this.isProfesor){
       if(this.curentProfesorID === profesorOfThisLicenta){
        this.router.navigate(['/licenta/' + licentaID +'/edit'], { queryParams: { page: licentaID } });
       }
       else{
        this.router.navigate(['/accessdenied/']);
       }
     }
     else if(this.isAdmin){
        this.router.navigate(['/licenta/' + licentaID +'/edit'], { queryParams: { page: licentaID } });
     }
     else{
      this.router.navigate(['/accessdenied/']);
     }
    
    //
  } **/

  reloadPageProfesor(page?: number): void {
    const pageToLoad: number = page || this.page;
    this.licentaService
      .getAllOfAProfesor({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ILicenta[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInLicentas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILicenta): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLicentas(): void {
    this.eventSubscriber = this.eventManager.subscribe('licentaListModification', () => this.loadPage());
  }

  delete(licenta: ILicenta): void {
    const modalRef = this.modalService.open(LicentaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.licenta = licenta;
  }

  aplicaLaLicenta(licentaID2: number): void {
    this.dataToSend2 = new LicentaDTO(this.currentUserLogin, licentaID2);
    this.licentaService.aplica(this.dataToSend2).subscribe(() => this.loadPage());
    this.alertService.get().push(
      this.alertService.addAlert(
        {
          type: 'success',
          msg: 'licentaApp.licenta.aplica',
          timeout: 3000,
          toast: false,
          scoped: true
        },
        this.alertService.get()
      )
    );
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: ILicenta[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/licenta'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.licentas = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
