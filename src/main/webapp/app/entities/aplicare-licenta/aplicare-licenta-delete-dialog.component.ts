import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAplicareLicenta } from 'app/shared/model/aplicare-licenta.model';
import { AplicareLicentaService } from './aplicare-licenta.service';

@Component({
  templateUrl: './aplicare-licenta-delete-dialog.component.html'
})
export class AplicareLicentaDeleteDialogComponent {
  aplicareLicenta?: IAplicareLicenta;

  constructor(
    protected aplicareLicentaService: AplicareLicentaService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aplicareLicentaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('aplicareLicentaListModification');
      this.activeModal.close();
    });
  }
}
