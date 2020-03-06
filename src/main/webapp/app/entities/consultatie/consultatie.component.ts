import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConsultatie } from 'app/shared/model/consultatie.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ConsultatieService } from './consultatie.service';
import { ConsultatieDeleteDialogComponent } from './consultatie-delete-dialog.component';

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

  constructor(
    protected consultatieService: ConsultatieService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

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
