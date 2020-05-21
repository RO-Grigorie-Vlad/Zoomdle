import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AplicareConsultatieService } from './aplicare-consultatie.service';
import { AplicareConsultatieDeleteDialogComponent } from './aplicare-consultatie-delete-dialog.component';

@Component({
  selector: 'jhi-aplicare-consultatie',
  templateUrl: './aplicare-consultatie.component.html'
})
export class AplicareConsultatieComponent implements OnInit, OnDestroy {
  aplicareConsultaties?: IAplicareConsultatie[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected aplicareConsultatieService: AplicareConsultatieService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected alertService: JhiAlertService
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.aplicareConsultatieService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IAplicareConsultatie[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInAplicareConsultaties();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  raspundeLaAplicatie(aplicareConsultatieID: number, raspuns: boolean): void {
    this.aplicareConsultatieService.raspunde(aplicareConsultatieID, raspuns).subscribe(() => this.loadPage());
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

  trackId(index: number, item: IAplicareConsultatie): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAplicareConsultaties(): void {
    this.eventSubscriber = this.eventManager.subscribe('aplicareConsultatieListModification', () => this.loadPage());
  }

  delete(aplicareConsultatie: IAplicareConsultatie): void {
    const modalRef = this.modalService.open(AplicareConsultatieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aplicareConsultatie = aplicareConsultatie;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IAplicareConsultatie[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/aplicare-consultatie'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.aplicareConsultaties = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
