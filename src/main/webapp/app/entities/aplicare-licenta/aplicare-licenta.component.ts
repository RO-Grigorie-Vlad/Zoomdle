import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAplicareLicenta } from 'app/shared/model/aplicare-licenta.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AplicareLicentaService } from './aplicare-licenta.service';
import { AplicareLicentaDeleteDialogComponent } from './aplicare-licenta-delete-dialog.component';

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

  constructor(
    protected aplicareLicentaService: AplicareLicentaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

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
