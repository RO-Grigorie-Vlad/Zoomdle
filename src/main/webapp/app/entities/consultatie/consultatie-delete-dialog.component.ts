import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConsultatie } from 'app/shared/model/consultatie.model';
import { ConsultatieService } from './consultatie.service';

@Component({
  templateUrl: './consultatie-delete-dialog.component.html'
})
export class ConsultatieDeleteDialogComponent {
  consultatie?: IConsultatie;

  constructor(
    protected consultatieService: ConsultatieService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.consultatieService.delete(id).subscribe(() => {
      this.eventManager.broadcast('consultatieListModification');
      this.activeModal.close();
    });
  }
}
