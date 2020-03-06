import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILicenta } from 'app/shared/model/licenta.model';
import { LicentaService } from './licenta.service';

@Component({
  templateUrl: './licenta-delete-dialog.component.html'
})
export class LicentaDeleteDialogComponent {
  licenta?: ILicenta;

  constructor(protected licentaService: LicentaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.licentaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('licentaListModification');
      this.activeModal.close();
    });
  }
}
